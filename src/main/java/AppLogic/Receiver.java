package AppLogic;

import DatabaseLogic.BaseEntityDAO;
import DatabaseLogic.Session;
import DatabaseLogic.Session_DAO;
import GoogleCalendarApi.Quickstart;
import com.rabbitmq.client.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

import static GoogleCalendarApi.Quickstart.getCalendarService;

public class Receiver {

    public static void main(String[] argv) throws Exception {

        // Set your source type here: this will determine what queue you will receive messages from!
        Helper.SourceType yourSourceType = Helper.SourceType.Monitor;

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] _______________________IPGA-JAVA-RECEIVER-v.1______________________ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- [ooo]");


        //uncomment for sending pingmessage in a different thread

        // # Send pingmessage every 'timeBetweenPings' milliseconds
        int timeBetweenPings = 5000;

        // ## make new pingSender object
        PingSender pingSender = new PingSender(0, yourSourceType, timeBetweenPings);

        // ## setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // ## start new pingSender thread
        pingThread.start();

        //# Setup connection
        ConnectionFactory factory = new ConnectionFactory();

        //https://www.rabbitmq.com/api-guide.html
        String username = ""+yourSourceType;
        String password = (""+yourSourceType).toLowerCase();
        String virtualHost = "/";
        String TASK_QUEUE_NAME = (""+yourSourceType).toLowerCase()+"-queue";

        //for localhost
        //factory.setHost("localhost");
        String hostName = "10.3.50.38";
        int portNumber = 5672;

        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //ONLY ex/quDECLARE(...) WHEN exchange/queue DOESN'T EXIST on server yet

        //channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

        channel.queueBind(TASK_QUEUE_NAME, Helper.EXCHANGE_NAME, "");

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println(" ------ [...] Waiting with queue for messages. To exit press CTRL+C [...] ------");
        System.out.println(" -------------------------------------------------------------------------------\n");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                //System.out.println("\n [x] Start");
                //System.out.println("\n [x] MessageStart:\n" + message);
                //System.out.println("\n [x] MessageEnd\n");
                try {
                    doWork(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    System.out.println(" -------------------------------------------------------------------------------");
                    System.out.println(" ------ [...] Waiting with queue for messages. To exit press CTRL+C [...] ------");
                    System.out.println(" -------------------------------------------------------------------------------");
                }
            }
        };
        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);

    }

    private static void doWork(String task) throws JAXBException, IOException, ParserConfigurationException, SAXException, Exception {

        //change to true to show full XML message in receiver console when it's received
        boolean showFullXMLMessage = false;

        // XML -> Data
        // get messagetype from XML (set in sender)
        String messageType = null, xmlTotalMessage = "";
        try {

            messageType = getPropertyFromXml(task, "messageType");
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No messageType found in XML: " + e);
        }
        // get Source from XML (set in sender)
        String messageSource = null;
        try {

            messageSource = getPropertyFromXml(task, "source");
            System.out.println(" [.i.] Message received from: " + messageSource);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No messageType found in XML: " + e);
        }

        // Check XML for message

        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println(" [.i.] [NEW '"+messageType+"'] FROM '"+messageSource+"' @ '\" + Helper.getCurrentDateTimeStamp() + \"'  || message length: '\" + task.length() + \"' characters [.i.]");
        System.out.println(" -------------------------------------------------------------------------------");

        System.out.println(" -------------------------------------------------------------------------------");

        String userUUID = "", sessionUUID = "", reservationUUID = "", eventUUID = "", UUID = "";
        int entity_version = 1;
        boolean allGood = true;

        //toLowercase just for catching CaPitAliZatIOn errors...
        switch (messageType.toLowerCase()) {

            case "reservationmessage":

                reservationUUID = getSafeXmlProperty(task, "reservationUUID");
                if(reservationUUID=="false")
                {
                    reservationUUID = getPropertyFromXml(task, "UUID");
                    if(reservationUUID=="false"){

                        System.out.println(" [!!!] ERROR: No reservationUUID or UUID found in XML... Looking for UUID...");

                    }

                    allGood=false;
                }

                userUUID = getSafeXmlProperty(task, "userUUID");
                if(userUUID=="false"){

                    System.out.println(" [!!!] ERROR: No userUuid found in XML");
                    allGood = false;
                }

                sessionUUID = getSafeXmlProperty(task, "sessionUUID");
                if(sessionUUID=="false") {

                    eventUUID = getSafeXmlProperty(task, "eventUUID");
                    if(eventUUID=="false") {
                        System.out.println(" [!!!] ERROR: No sessionUUID or eventUUID found in XML: ");
                        allGood = false;
                    }
                }
                String Status = getSafeXmlProperty(task,"status");

                String sessionName = getSafeXmlProperty(task,"sessionName");
                String maxAttendees = getSafeXmlProperty(task,"maxAttendees");
                String dateTimeStart = getSafeXmlProperty(task,"dateTimeStart");
                String dateTimeEnd = getSafeXmlProperty(task,"dateTimeEnd");
                String speaker = getSafeXmlProperty(task,"speaker");
                String local= getSafeXmlProperty(task,"local");
                String type= getSafeXmlProperty(task,"type");

                int MaxAttendees=0;
                if(sessionName  == "false"|| maxAttendees == "false"|| dateTimeStart == "false" || dateTimeEnd  == "false"|| speaker  == "false"|| local  == "false"|| type == "false")
                {
                    allGood = false;
                }else{
                    MaxAttendees = Integer.parseInt(maxAttendees);
                }

                System.out.println(" [.i.] " + messageType + ": userUUID:" + userUUID);
                if (sessionUUID != "") {

                    System.out.println(" [.i.] " + messageType + ": sessionUUID:" + sessionUUID);
                } else {

                    System.out.println(" [.i.] " + messageType + ": eventUUID:" + eventUUID);
                }

                //save to local database

                System.out.println("\nDATABASE TEST!\n");

                //check if UUID exists in local db

                boolean uuidExists = false;
                try {
                    uuidExists = new BaseEntityDAO().doesUUIDExist("reservation_event",reservationUUID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(uuidExists)
                {
                    // uuid exists locally:
                    // # update record locally with given info




                    // Prepare new session object for sending to database
                    Session case2NewSession = new Session(0, entity_version, Status, Helper.getCurrentDateTimeStamp(), sessionUUID, eventUUID, sessionName, MaxAttendees, dateTimeStart, dateTimeEnd, speaker, local, type);

                    int case2test = 0;
                    try {
                        case2test = new Session_DAO().insertIntoSession(case2NewSession);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("End of database INSERT... response: "+case2test);



                    // # send update to uuid manager


                }else{

                    // uuid doesn't exit locally yet
                    // # insert record locally with given info



                    // # send insert to uuid manager
                }


                //String headerDescriptionReservation = "Standard header description for reservation set in receiver";
                // 2. create xml message
                //xmlTotalMessage = Helper.getXmlForNewSession(messageType, headerDescriptionReservation, Helper.SourceType.Planning, sessionUUID, eventUUID, sessionName, maxAttendees, dateTimeStart, dateTimeEnd, speaker, local, type, 1);


/*

                //save to UUID
                try {
                    sessionUUID = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }
*/

                System.out.println("\nNew Session made with UUID: " + UUID);

                /*
                System.out.println(" [.!.] START OF TASK:\n\n" + task + " [.!.] \nEND OF TASK\n\n");
                */
                break;

            case "sessionmessage":

                String fullSession = "";

                /*
                try {

                    sessionUUID = getPropertyFromXml(task, "uuid");
                    //System.out.println(" [i] UUID: " + UUID);
                    fullSession = "\n [.i.] UUID: '" + sessionUUID + "' || sessionName: '" + getPropertyFromXml(task, "sessionName");
                    fullSession += "' || dateTimeStart: '" + getPropertyFromXml(task, "dateTimeStart") + "' || dateTimeEnd '" + getPropertyFromXml(task, "dateTimeEnd");
                    fullSession += "' || speaker: '" + getPropertyFromXml(task, "speaker") + "' || local: '" + getPropertyFromXml(task, "local");
                    fullSession += "' || type: '" + getPropertyFromXml(task, "type") + "' || status: '" + getPropertyFromXml(task, "status");
                    fullSession += "' || timestamp: '" + getPropertyFromXml(task, "timestamp") + "'";

                } catch (ParserConfigurationException | SAXException | IOException e) {

                    e.printStackTrace();
                    System.out.println(" [!!!] ERROR: No some xml not found: " + e);
                }
                */

                System.out.println(" [.i.] Full processed sessionmessage: \n" + fullSession + "\n\n [.i.] END of sessionmessage");


                break;

            case "eventmessage":

                UUID = getSafeXmlProperty(task, "userUUID");

                System.out.println(" [" + messageType + "] for UUID: " + UUID);/*
                /*
                MessageMessage sessionMessage = MessageMessage.generateObject(task);
                try {
                    System.out.println("UUID from XML: " + sessionMessage.getDatastructure().getUuid());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }*/

                //System.out.println(" [.!.] "+messageType+":\nUUID:"+messageMessage.getHeader().getMessageTypeFromXml());

                break;
            case "UpdateLocalMessage":

                System.out.println(" [" + messageType + "] for UUID: " + UUID);/*
/*

                MessageMessage updateLocalMessage = MessageMessage.generateObject(task);

                try {
                    System.out.println("UUID from XML: " + updateLocalMessage.getDatastructure().getUuid());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
*/

                break;
            case "UpdateEntityVersionMessage":

                System.out.println(" [" + messageType + "] for UUID: " + UUID);/*
/*
                MessageMessage updateEntityVersionMessage = MessageMessage.generateObject(task);

                try {
                    System.out.println("UUID from XML: " + updateEntityVersionMessage.getDatastructure().getUuid());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }*/
                break;

            case "TestMessage":

                System.out.println(" [" + messageType + "] for UUID: " + UUID);/*
/*

                MessageMessage messageMessage = MessageMessage.generateObject(task);

                try {
                    System.out.println("UUID from XML: " + messageMessage.getDatastructure().getUuid());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
*/

                /*
                System.out.println(" [.!.] TestMessage:\n"+reservationMessage.getHeader().getMessageTypeFromXml());
*/
                break;

            case "pingmessage":

                System.out.println(" [" + messageType + "] Received from "+getPropertyFromXml(task, "source") );

                break;

            case "ListEventsMessage":

                //for listing your upcoming events

                System.out.println(" [" + messageType + "] Trying to list events... ");

                try {
                    com.google.api.services.calendar.Calendar service = getCalendarService();

                    Quickstart.listEvents(service);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "GetAllUUIDs":

                String allRecords = Helper.httpGetAllRecords(50);

                System.out.println(" [.i.] GetAllUUIDs:\n" + allRecords);

            case "":
            default:

                System.out.println(" [.!.] ERROR: Message type NOT recognized: '" + messageType + "' ...");

                break;

        }

        if (showFullXMLMessage) {
            System.out.println("\n [.i.] Full received message:\n\n -*- START OF TASK -*-\n");
            System.out.println(task);
            System.out.println("\n -*- END OF TASK -*-\n");
        } else {

            System.out.println("\n [.!.] XML not shown... Change boolean 'showFullXMLMessage in receiver to show this.\n");
        }

        //System.out.println(" [.i.] END OF WORK [************************************************************************]");
    }

    private static String getSafeXmlProperty(String xml, String property)
    {
        String messageType ="";
        try {

            messageType = getPropertyFromXml(xml, property);
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            messageType="ERROR: No messageType found in XML: " + e;
        }
        return messageType;
    }

    public static String getPropertyFromXml(String xml, String property) throws
            ParserConfigurationException, SAXException, IOException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);


        NodeList nodes = doc.getElementsByTagName(property);

        String thisMessageType = null;
        try {
            thisMessageType = nodes.item(0).getTextContent();
        } catch (DOMException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return thisMessageType;

        //END of getPropertyFromXml();
    }

    //END of Receiver class
}
