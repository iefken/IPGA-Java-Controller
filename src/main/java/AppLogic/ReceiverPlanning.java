package AppLogic;

import DatabaseLogic.Reservation_Session;
import DatabaseLogic.Reservation_Session_DAO;
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
import java.util.concurrent.TimeoutException;

import static GoogleCalendarApi.Quickstart.getCalendarService;

public class ReceiverPlanning {

    //change task queue name to crm-queue, facturatie-queue, frontend-queue, kassa-queue, monitor-queue, planning-queue

    public static void main(String[] argv) throws Exception {

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] _______________________IPGA-JAVA-RECEIVER-v.1______________________ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- [ooo]");


        // # Send pingmessage every 'timeBetweenPings' milliseconds
        int timeBetweenPings = 5000;

        // ## make new pingSender object
        PingSender pingSender = new PingSender(0, Helper.SourceType.Planning, timeBetweenPings);

        // ## setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // ## start new pingSender thread
        pingThread.start();


        ConnectionFactory factory = new ConnectionFactory();

        //for localhost
        //factory.setHost("localhost");

        //for our external server (ipv4 =  10.3.50.38)

        //https://www.rabbitmq.com/api-guide.html
        String username = "Planning";
        String password = "planning";
        String virtualHost = "/";
        String TASK_QUEUE_NAME = "planning-queue";

        // https://ultratoools.com/tools/ipv4toipv6

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


        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println(" [.i.][**************************] NEW MESSAGE [**************************][.i.]");
        System.out.println(" -------------------------------------------------------------------------------");

        System.out.println(" [.i.][*****]@ '" + Helper.getCurrentDateTimeStamp() + "' || message length: '" + task.length() + "' characters [*****]");
        System.out.println(" -------------------------------------------------------------------------------");
        //change to true to show full XML message in receiver console when it's received
        boolean showFullXMLMessage = false;

        // XML -> Data
        // get messagetype from XML (set in sender)
        String messageType = null, xmlTotalMessage="";
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

        String userUUID = "", sessionUUID = "", reservationUUID = "",eventUUID="", UUID = "";

        //toLowercase just for catching CaPitAliZatIOn errors...
        switch (messageType.toLowerCase()) {

            case "reservationmessage":

                try {

                    reservationUUID = getPropertyFromXml(task, "reservationUUID");

                } catch (ParserConfigurationException | SAXException | IOException e) {

                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML... Looking for UUID...");
                    try {

                        reservationUUID = getPropertyFromXml(task, "UUID");
                    }catch (Exception ee)
                    {
                        e.printStackTrace();
                        System.out.println(" [!!!] ERROR: No reservationUUID or UUID found in XML: " + e);

                    }

                    e.printStackTrace();
                    System.out.println(" [!!!] ERROR: No userUuid found in XML: " + e);
                }
                try {

                    userUUID = getPropertyFromXml(task, "userUUID");

                } catch (ParserConfigurationException | SAXException | IOException e) {

                    e.printStackTrace();
                    System.out.println(" [!!!] ERROR: No userUuid found in XML: " + e);
                }


                try {

                    sessionUUID = getPropertyFromXml(task, "sessionUUID");
                    //System.out.println(" [i] UUID: " + UUID);

                } catch (ParserConfigurationException | SAXException | IOException e) {

                    System.out.println(" [!!!] ERROR: No sessionUUID found in XML... Looking for eventUUID");
                    try {
                        eventUUID = getPropertyFromXml(task, "eventUUID");
                    } catch (Exception ee) {

                        ee.printStackTrace();
                        System.out.println(" [!!!] ERROR: No eventUUID or sessionUUID found in XML: " + ee);

                    }
                    e.printStackTrace();
                }

                System.out.println(" [.i.] " + messageType + ": userUUID:" + userUUID);

                if(sessionUUID!="")
                {

                    System.out.println(" [.i.] " + messageType + ": sessionUUID:" + sessionUUID);
                }else{

                    System.out.println(" [.i.] " + messageType + ": eventUUID:" + eventUUID);
                }

                //save to local database

                System.out.println("\nDATABASE TEST!\n");

                String headerDescriptionReservation = "Standard header description for reservation set in receiver";
                // 2. create xml message
                //xmlTotalMessage = Helper.getXmlForNewSession(messageType, headerDescriptionReservation, Helper.SourceType.Planning, sessionUUID, eventUUID, sessionName, maxAttendees, dateTimeStart, dateTimeEnd, speaker, local, type, 1);

                // 3. insert to local db

                int Entity_version = 1; // should come from xml message
                String Status = "1"; // should come from xml message

                Session case2NewSession = new Session(0,1,"1",Helper.getCurrentDateTimeStamp(),sessionUUID,eventUUID,getPropertyFromXml(task, "sessionName"),Integer.parseInt(getPropertyFromXml(task, "maxAttendees")),getPropertyFromXml(task, "dateTimeStart"),getPropertyFromXml(task, "dateTimeEnd"),getPropertyFromXml(task, "speaker"),getPropertyFromXml(task, "local"),getPropertyFromXml(task, "type"));
/*

                fullSession = "\n [.i.] UUID: '" + sessionUUID + "' || sessionName: '" + getPropertyFromXml(task, "sessionName");
                fullSession += "' || dateTimeStart: '" + getPropertyFromXml(task, "dateTimeStart") + "' || dateTimeEnd '" + getPropertyFromXml(task, "dateTimeEnd");
                fullSession += "' || speaker: '" + getPropertyFromXml(task, "speaker") + "' || local: '" + getPropertyFromXml(task, "local");
                fullSession += "' || type: '" + getPropertyFromXml(task, "type") + "' || status: '" + getPropertyFromXml(task, "status");
                fullSession += "' || timestamp: '" + getPropertyFromXml(task, "timestamp") + "'";
*/


                int case2test=0;
                try {
                    case2test = new Session_DAO().insertIntoSession(case2NewSession);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("End of database INSERT...");
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

                System.out.println(" [.i.] Full processed sessionmessage: \n" + fullSession + "\n\n [.i.] END of sessionmessage");


                break;

            case "eventmessage":

                try {

                    UUID = getPropertyFromXml(task, "userUUID");

                } catch (ParserConfigurationException | SAXException | IOException e) {

                    e.printStackTrace();
                    System.out.println(" [!!!] ERROR: No userUuid found in XML: " + e);
                }
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
    }
}

/* old code
    // returned de text die in de node 'messageType' zit, dit moet je weten en nakijken om JAXB zonder fouten naar de juiste klasse te laten mappen.
    public static String getMessageTypeFromXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("messageType");
        String thisMessageType = nodes.item(0).getTextContent();

        return thisMessageType;
    }
    // returned de UUID from the xml messaged send
    public static String getUUIDFromXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);

        NodeList nodes = doc.getElementsByTagName("uuid");

        String thisMessageType = null;
        try {
            thisMessageType = nodes.item(0).getTextContent();
        } catch (DOMException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return thisMessageType;
    }*/