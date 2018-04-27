package logic;

import GoogleCalendarApi.Quickstart;
import XmlMessage.MessageMessage;
import XmlMessage.ReservationMessage;
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

import static GoogleCalendarApi.Quickstart.getCalendarService;

public class ReceiverPlanning {

    //change task queue name to crm-queue, facturatie-queue, frontend-queue, kassa-queue, monitor-queue, planning-queue

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        //for localhost
        //factory.setHost("localhost");

        //for our external server (ipv4 =  10.3.50.38)

        //https://www.rabbitmq.com/api-guide.html
        String username = "Planning";
        String password = "planning";
        String virtualHost = "/";


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

        channel.queueBind(Helper.TASK_QUEUE_NAME, Helper.EXCHANGE_NAME, "");


        System.out.println(" [.o.] Waiting with exchange for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println("\n [x] Start\n");
                System.out.println("\n [.i.] Received message with length: '" + message.length() + "' characters @ datetime: '" + Helper.getCurrentDateTimeStamp() + "'!");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                }
            }
        };
        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(Helper.TASK_QUEUE_NAME, autoAck, consumer);

    }

    private static void doWork(String task) throws InterruptedException, JAXBException, IOException {

        System.out.println("\n [.i.] STARTING WORK [************************************************************************]\n");

        //change to true to show full XML message in receiver console when it's received

        boolean showFullXMLMessage = false;

        // XML -> Data

        // get messagetype from XML (set in sender)
        String messageType = null;
        try {

            messageType = getProperyFromXml(task,"messageType");
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No messageType found in XML: " + e);
        }

        // get UUID from XML (set in sender)
        String UUID = null;
        try {

            UUID = getProperyFromXml(task,"uuid");
            //System.out.println(" [i] UUID: " + UUID);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No uuid found in XML: " + e);
        }

        // Check XML for message

        switch (messageType) {

            case "ReservationMessage":

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
                ReservationMessage reservationMessage = ReservationMessage.generateObject(task);
                try {
                    System.out.println("UserUUID from XML: " + reservationMessage.getDatastructure().getUserUUID());
                    System.out.println("SessionUUID from XML: " + reservationMessage.getDatastructure().getSessionUUID());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
                System.out.println(" [.!.] ReservationMessage:\n"+reservationMessage.getHeader().getMessageTypeFromXml());
                System.out.println(" [.!.] ReservationMessage:\n"+messageType);
                */
                break;

            case "SessionMessage":

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
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
            case "EventMessage":

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
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

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
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

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
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

                System.out.println(" ["+messageType+"] for UUID: "+UUID);/*
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

                System.out.println(" ["+messageType+"] Trying to list events... ");

                try {
                    com.google.api.services.calendar.Calendar service = getCalendarService();

                    Quickstart.listEvents(service);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "GetAllUUIDs":

                String allRecords = Helper.httpGetAllRecords(50);

                System.out.println(" [.!.] GetAllUUIDs:\n" + allRecords);

            case "":
            default:

                System.out.println(" [.!.] ERROR: Message type NOT recognized: '"+messageType+"' ...");

                break;

        }

        System.out.println(" [.!.] Full received message:\n\n -*- START OF TASK -*-\n");
        if(showFullXMLMessage)
        {
            System.out.println( task );

        }else{

            System.out.println( " [.i.] XML not shown... Change boolean 'showFullXMLMessage in receiver to show this." );
        }
        System.out.println( "\n -*- END OF TASK -*-\n");

        System.out.println(" [.i.] END OF WORK [************************************************************************]");
    }

    public static String getProperyFromXml(String xml,String property) throws ParserConfigurationException, SAXException, IOException {
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