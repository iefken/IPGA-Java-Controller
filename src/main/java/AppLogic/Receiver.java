package AppLogic;

import DatabaseLogic.*;
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

public class Receiver {

    public static void main(String[] argv) throws Exception {

        // Set your source type here: this will determine what queue you will receive messages from!
        Helper.SourceType yourSourceType = Helper.SourceType.Planning;

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] ___________________IPGA-" + ("" + yourSourceType).toUpperCase() + "-RECEIVER-v.1______________________ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- [ooo]");


        //uncomment for sending pingmessage in a different thread
/*

        // # Send pingmessage every 'timeBetweenPings' milliseconds
        int timeBetweenPings = 5000;

        // ## make new pingSender object
        PingSender pingSender = new PingSender(0, yourSourceType, timeBetweenPings);

        // ## setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // ## start new pingSender thread
        pingThread.start();
*/

        //# Setup connection
        ConnectionFactory factory = new ConnectionFactory();

        //https://www.rabbitmq.com/api-guide.html
        String username = "" + yourSourceType;
        String password = ("" + yourSourceType).toLowerCase();
        String virtualHost = "/";
        String TASK_QUEUE_NAME = ("" + yourSourceType).toLowerCase() + "-queue";

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

//        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
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
        Helper.EntityType thisEntityType = Helper.EntityType.ADMIN;

        // XML -> Data
        // get messagetype from XML (set in sender)
        String messageType = null, xmlTotalMessage = "";
        messageType = getSafeXmlProperty(task, "messageType");

        // get Source from XML (set in sender)
        String messageSource = null;
        messageSource = getSafeXmlProperty(task, "source");

        // Check XML for message

        System.out.println(" -------------------------------------------------------------------------------");
        System.out.println(" [.i.] [NEW '" + messageType + "'] FROM '" + messageSource + "' @ '" + Helper.getCurrentDateTimeStamp() + "' [.i.] ");
        System.out.println(" [.i.] Message length: '" + task.length() + "' characters [.i.]");
        System.out.println(" -------------------------------------------------------------------------------");

        System.out.println(" -------------------------------------------------------------------------------");

        String userUUID = "", sessionUUID = "", reservationUUID = "", eventUUID = "", UUID = "";
        int entity_version = 1;

        // Set your team here
        Helper.SourceType Source_type = Helper.SourceType.Planning;

        boolean allGood = true, uuidExists = false;

        //toLowercase just for catching CaPitAliZatIOn errors...
        switch (messageType.toLowerCase()) {

            case "usermessage":

                User thisUserInMessage = null;

                // 1. transform xml to user-object
                try {
                    thisUserInMessage = Helper.getUserObjectFromXmlMessage(task);

                    userUUID = thisUserInMessage.getUserUUID();

                    System.out.println("New message for USER with UUID: "+userUUID);

                    //System.out.println("user toString: "+thisUserInMessage.toString());
                    //System.out.println(" [" + messageType + "] for userUUID: " + userUUID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (thisUserInMessage != null) {
                    // 2.1 check if UUID exists in local db
                    uuidExists = false;
                    try {
                        uuidExists = new BaseEntityDAO().doesUUIDExist("User", userUUID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (uuidExists) {

                        System.out.println("UUID already exists in our PlanningDB table:\nUser");

                        // 2.2. User record update

                        // 2.2.1. get idUser from userUUID in User
                        String[] propertiesToSelect = {"idUser"};
                        String table = "User";
                        String[] selectors = {"userUUID"};
                        String[] values = {"" + userUUID};

                        String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                        //System.out.println("selectResults: " + selectResults[0]);

                        // 2.2.2. get the entityVersion from idUser
                        propertiesToSelect[0] = "entity_version";
                        table = "BaseEntity";
                        selectors[0] = "entityId";
                        values[0] = selectResults[0];
                        int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                        if (localEntityVersion < thisUserInMessage.getEntityVersion()) {
                            // 2.3.1. check what's different between thisUserInMessage and user in our local db (selectResults[0] still has it's id in it)

                            // TO DO

                            /*
                            User userFromDataBase = new BaseEntityDAO().getBaseEntityByEntityId()


                            // 2.3.2. update what's different into local db
                            if (!new BaseEntityDAO().updateTablePropertyValue("User", "userUUID", userUUID, "String", "idUser", thisUserInMessage.getEntityId())) {
                                System.out.println("Something went wrong updating User's userUUID");
                            } else {
                                thisUserInMessage.setUserUUID(userUUID);
                                //System.out.println(" HERE XXX: userUUID: "+userUUID);
                            }
                            */

                            // 2.3.3. updateUuidRecordVersion()

                            try {
                                Sender.updateUuidRecordVersion("", Source_type, userUUID);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                e.printStackTrace();
                            }
                            System.out.println("TO DO: We had this user with entityVersion: '"+localEntityVersion+"'. Updated to latest version with entityVersion: '"+thisUserInMessage.getEntityVersion()+"'");

                        } else {
                            // we have the latest version...
                            System.out.println("We already had this session with entityVersion: '"+localEntityVersion+"'");

                        }

                    } else {
                        // New user record

                        // 2.4.1. insert new user into local db
                        int messageUserInsertReturner = 0;
                        try {
                            messageUserInsertReturner = new User_DAO().insertIntoUser(thisUserInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.4.2. insertUuidRecord

                        try {
                            Sender.insertUuidRecord("", messageUserInsertReturner, thisEntityType, Source_type, userUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Inserted new user record with id='"+messageUserInsertReturner+"' and UUID='"+userUUID+"'");


                    }
                } else {
                    System.out.println("Something went wrong getting user object from xml message!");
                }

                //System.out.println(" [END] ");

                break;

            case "eventmessage":


                Event thisEventInMessage = null;

                // 1. transform xml to event-object
                try {
                    thisEventInMessage = Helper.getEventObjectFromXmlMessage(task);
                    //System.out.println("user toString: "+thisUserInMessage.toString());
                    eventUUID = thisEventInMessage.getEventUUID();

                    System.out.println("New message for EVENT with UUID: "+eventUUID);

                    //System.out.println(" [" + messageType + "] for userUUID: " + userUUID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (thisEventInMessage != null) {
                    // 2.1. check if UUID exists in local db
                    uuidExists = false;
                    try {
                        uuidExists = new BaseEntityDAO().doesUUIDExist("Event", eventUUID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (uuidExists) {

                        System.out.println("UUID already exists in our PlanningDB table:\nEvent");

                        // 2.2 Session record update
                        // public String[] getPropertyValueByTableAndProperty(String[] propertiesToSelect, String table, String[] selectors, String[] values)

                        // 2.2.1. get idEvent from eventUUID in Event
                        String[] propertiesToSelect = {"idEvent"};
                        String table = "Event";
                        String[] selectors = {"eventUUID"};
                        String[] values = {"" + eventUUID};

                        String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                        System.out.println("selectResults: " + selectResults[0]);

                        // 2.2.2. get entityVersion from id in BaseEntity
                        propertiesToSelect[0] = "entity_version";
                        table = "BaseEntity";
                        selectors[0] = "entityId";
                        values[0] = selectResults[0];
                        int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                        if (localEntityVersion < thisEventInMessage.getEntityVersion()) {
                            // 2.3.1. update local db

                            // 2.3.2. update what's different into local db
                            // TO DO

                            // 2.3.3. updateUuidRecordVersion()

                            try {
                                Sender.updateUuidRecordVersion("", Source_type, eventUUID);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                e.printStackTrace();
                            }
                            System.out.println("TO DO: We had this event with entityVersion: '"+localEntityVersion+"'. Updated to latest version with entityVersion: '"+thisEventInMessage.getEntityVersion()+"'");

                        } else {
                            // we have the latest version...
                            System.out.println("We already had this event with entityVersion: '"+localEntityVersion+"'");

                        }

                    } else {
                        // New event record
                        // 2.3.1. insert new event into local db

                        int messageEventInsertReturner = 0;
                        try {
                            messageEventInsertReturner = new Event_DAO().insertIntoEvent(thisEventInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.3.2. insertUuidRecord
                        try {
                            Sender.insertUuidRecord("", messageEventInsertReturner, thisEntityType, Source_type, eventUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Inserted new event record with id='"+messageEventInsertReturner+"' and UUID='"+eventUUID+"'");


                    }
                } else {
                    System.out.println("Something went wrong getting event object from xml message!");
                }

                //System.out.println(" [END] ");

                break;

            case "sessionmessage":


                Session thisSessionInMessage = null;

                // 1. transform xml to event-object
                try {
                    thisSessionInMessage = Helper.getSessionObjectFromXmlMessage(task);

                    sessionUUID = thisSessionInMessage.getSessionUUID();

                    System.out.println("New message for SESSION with UUID: "+sessionUUID);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (thisSessionInMessage != null) {
                    // 2.1. check if UUID exists in local db
                    uuidExists = false;
                    try {
                        uuidExists = new BaseEntityDAO().doesUUIDExist("Session", sessionUUID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (uuidExists) {

                        System.out.println("UUID already exists in our PlanningDB table:\nSession:");

                        // 2.2 Session record update

                        // 2.2.1. get idSession from sessionUUID in Session
                        String[] propertiesToSelect = {"idSession"};
                        String table = "Session";
                        String[] selectors = {"sessionUUID"};
                        String[] values = {"" + sessionUUID};

                        String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                        //System.out.println("selectResults: " + selectResults[0]);

                        // 2.2.2. get entityVersion from id in BaseEntity
                        propertiesToSelect[0] = "entity_version";
                        table = "BaseEntity";
                        selectors[0] = "entityId";
                        values[0] = selectResults[0];
                        int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                        if (localEntityVersion < thisSessionInMessage.getEntityVersion()) {

                            // 2.3.1. update local db
                            // 2.3.2. update what's different into local db
                            // TO DO

                            // 2.3.3. updateUuidRecordVersion()
                            try {
                                Sender.updateUuidRecordVersion("", Source_type, sessionUUID);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                e.printStackTrace();
                            }
                            System.out.println("TO DO: We had this session with entityVersion: '"+localEntityVersion+"'. Updated to latest version with entityVersion: '"+thisSessionInMessage.getEntityVersion()+"'");

                        } else {

                            // we have the latest version...
                            System.out.println("We already had this session with entityVersion: '"+localEntityVersion+"'");

                        }

                    } else {
                        // New session record

                        // 2.4.1. insert new session into local db
                        int messageSessionInsertReturner = 0;
                        try {
                            messageSessionInsertReturner = new Session_DAO().insertIntoSession(thisSessionInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.4.2. insertUuidRecord
                        try {
                            Sender.insertUuidRecord("", messageSessionInsertReturner, thisEntityType, Source_type, sessionUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Inserted new session record with id='"+messageSessionInsertReturner+"' and UUID='"+sessionUUID+"'");


                    }
                } else {
                    System.out.println("Something went wrong getting session object from xml message!");
                }

                //System.out.println(" [END] ");

                break;


            case "reservationmessage":

                // get UUID's from xml
                reservationUUID = getSafeXmlProperty(task, "reservationUUID");
                if (reservationUUID == "false") {
                    reservationUUID = getPropertyFromXml(task, "UUID");
                    if (reservationUUID == "false") {

                        System.out.println(" [!!!] ERROR: No reservationUUID or UUID found in XML... Looking for UUID...");

                        allGood = false;
                    }
                }
                userUUID = getSafeXmlProperty(task, "userUUID");
                if (userUUID == "false") {

                    System.out.println(" [!!!] ERROR: No userUuid found in XML");
                    allGood = false;
                }

                sessionUUID = getSafeXmlProperty(task, "sessionUUID");
                if (sessionUUID == "false" || sessionUUID == "") {

                    sessionUUID="false";
                    eventUUID = getSafeXmlProperty(task, "eventUUID");
                    if (eventUUID == "false" || sessionUUID == "") {
                        System.out.println(" [!!!] ERROR: No sessionUUID or eventUUID found in XML: ");
                        allGood = false;
                    }else{

                        System.out.println("New RESERVATION_EVENT with UUID: "+eventUUID);
                    }
                }else{
                    System.out.println("New RESERVATION_SESSION with UUID: "+sessionUUID);
                }

                Reservation_Event newReservation_EventObjectFromXml = null;
                Reservation_Session newReservation_SessionObjectFromXml = null;

                // 1. transform xml to user-object

                if (sessionUUID == "false") {
                    newReservation_EventObjectFromXml = Helper.getReservation_EventObjectFromXmlMessage(task);

                } else {

                    newReservation_SessionObjectFromXml = Helper.getReservation_SessionObjectFromXmlMessage(task);
                }

                // System.out.println("sessionUUID: "+sessionUUID);

                // 2.1 check if UUID exists in local db

                uuidExists = false;

                if (sessionUUID == "false") {

                    // 2.1.A. search event table for uuid

                    try {
                        uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Event", newReservation_EventObjectFromXml.getReservationUUID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    // 2.1.B. search session table for uuid

                    try {
                        uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Session", newReservation_SessionObjectFromXml.getReservationUUID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (uuidExists) {

                    System.out.println("UUID already exists in our PlanningDB table:\n");

                    // 2.2. User record update

                    if (sessionUUID == "false") {

                        // Prepare new reservation_event object for sending to database

                        //Event case2NewEvent = Helper.getEventObjectFromXmlMessage(task);

                        int case2test = 0;
                        /*
                        try {
                            case2test = new Event_DAO().insertIntoEvent(case2NewEvent);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        */
                        System.out.println("Reservation_Event\n");
                    } else {

                        // Prepare new reservation_session object for sending to database
                        /*
                        Session case2NewSession = new Session(0, entity_version, Status, Helper.getCurrentDateTimeStamp(), sessionUUID, eventUUID, sessionName, MaxAttendees, dateTimeStart, dateTimeEnd, speaker, local, type);

                        int case2test = 0;
                        try {
                            case2test = new Session_DAO().insertIntoSession(case2NewSession);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println("End of database INSERT... response: " + case2test);
                        */
                        System.out.println("Reservation_Session");
                    }


                    // # send update to uuid manager


                } else {

                    // uuid doesn't exit locally yet
                    // # insert record locally with given info

                    int messageReservationInsertReturner = 0;
                    if (sessionUUID == "false") {

                        try {
                            messageReservationInsertReturner = new Reservation_Event_DAO().insertIntoReservation_Event(newReservation_EventObjectFromXml);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // insertUuidRecord

                        try {
                            Sender.insertUuidRecord("", messageReservationInsertReturner, thisEntityType, Source_type, eventUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            messageReservationInsertReturner = new Reservation_Session_DAO().insertIntoReservation_Session(newReservation_SessionObjectFromXml);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // insertUuidRecord

                        try {
                            Sender.insertUuidRecord("", messageReservationInsertReturner, thisEntityType, Source_type, sessionUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }
                    }
                }

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

            case "TestMessage":

                System.out.println(" [" + messageType + "] for UUID: " + UUID);
                break;

            case "pingmessage":

                System.out.println(" [" + messageType + "] Received from " + Helper.getSafeXmlProperty(task, "source"));
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

    static String getPropertyFromXml(String xml, String property) throws
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

    static String getSafeXmlProperty(String xml, String property) {
        String messageType = "";
        try {

            messageType = getPropertyFromXml(xml, property);
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            messageType = "ERROR: No "+property+" found in XML: " + e;
        }
        return messageType;
    }

    //END of Receiver class
}
