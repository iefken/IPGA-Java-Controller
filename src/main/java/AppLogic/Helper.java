package AppLogic;

//import AppLogic.Sender;

import DatabaseLogic.*;
import GoogleCalendarApi.GoogleCalenderApi;
import JsonMessage.JSONException;
import com.google.gson.Gson;
import okhttp3.*;
import HttpRequest.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static GoogleCalendarApi.GoogleCalenderApi.getCalendarService;

public interface Helper {

    //enum EntityType {VISITOR, EMPLOYEE, ADMIN, SPONSOR, SPEAKER, CONSULTANT}

    enum EntityType {EMPTY, User, Event, Session, ReservationEvent, ReservationSession, Product, Purchase}

    enum SourceType {Front_End, Planning, Monitor, Kassa, CRM, Facturatie}

    //String TASK_QUEUE_NAME = "planning-queue";
    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;

    //For setting CLI options in main
    static String[] getOptions() {

        //Add CLI options here (a.b. : a: choice, b: sort of message
        String[] options = {
                "[01.V] Create new User without UUID (Front-End Call)",
                "[02.V] Create new Event without UUID (Front-End Call)",
                "[03.V] Create new Session without UUID (Front-End Call)",
                "[04.V] Create new Reservation_Event: Add User to Event",
                "[05.V] Create new Reservation_Session: Add User to Session",
                "[06.V] Get all UUID's from UUID manager",
                "[07.V] updateUuidRecordVersion",
                "[08.V] updateUuidRecordVersionB",
                "[09.V] List upcoming events (Google calendar 1) *authorize not yet included",
                "[10.V] Create new event (Google calendar 2)",
                "[11.V] Create new chosen event (Google calendar 2)",
                "[12.V] Mock XML message",
                "/[13.x] /New Session with UUID",
                "/[14.x] /New Reservation_Session with UUID"

        };
        return options;
    }

    // MAIN CLI

    static void receiverMessageWaitPaint() {

        System.out.println("\n*********************************************************************************");
        System.out.println("* [@" + getCurrentDateTimeStamp() + "]: Waiting with queue for messages. To exit press CTRL+C *");
        System.out.println("*********************************************************************************\n");

    }

    static void receiverCliStartPaint(SourceType yourSourceType) {

        System.out.println("_________________________________________________________________________________");
        System.out.println("*[ooo] ******************************************************************* [ooo]*");
        System.out.println("*[ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]*");
        System.out.println("*[ooo] ___________________________________________________________________ [ooo]*");
        System.out.println("*[ooo] ___________________IPGA-" + ("" + yourSourceType).toUpperCase() + "-RECEIVER-v.1______________________ [ooo]*");
        System.out.println("*[ooo] ___________________________________________________________________ [ooo]*");
        System.out.println("*[ooo] -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- [ooo]*");
        //System.out.println("*_______________________________________________________________________________*");

    }

    static boolean startCliInterface() throws JAXBException, SQLException {

        int choser = 999;
        String responseFromSender = "";
        String[] senderOptions = getOptions();

        //initialize possible variables
        boolean inputSucces = true;
        int Entity_sourceId = 420;

        //preset most used variables for testing purposes
        String UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String uuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
        String userUUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
        String sessionUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String eventUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String eventUuid = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String reservationUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String messageType = "TestMessage";
        String headerDescription = "Standard header description";
        String xmlTotalMessage = "<test>testertester</test>";

        EntityType Entity_type = EntityType.EMPTY;
        SourceType Source_type = SourceType.Planning;
        int Entity_version = 1;
        int maxAttendees = 50;
        float paid = 0;

        //preset objects

        User mockUser = null;
        Event mockEvent = null;
        Session mockSession = null;

        //preset new session variables
        String sessionName = "Session name test";
        String dateTimeStart = "30/05/2018 20:00:00";
        String dateTimeEnd = "31/05/2018 08:00:00";
        String speaker = "Mr. President";
        String local = "Oval office dept.1 Room 420";
        String description = "Description for Main case (2): create new session without UUID";
        String summary = "Summary for Main case (2): create new session without UUID";
        String type = "testType (please set it to something else before using this";
        String lastName = "Test last name";
        String firstName = "Test first name";
        String eventType = "MockerNoonEventType";
        String sessionType = "MockerNoonSessionType";


        //do{}while(choser > 0 && choser <= senderOptions.length + 1);
        do {

            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("[.i.] Options:\n");

            // print options
            for (String option : senderOptions) {
                System.out.println(option);
            }

            // create scanner to read command-line input
            Scanner scanner = new Scanner(System.in);

            // prompt for input
            System.out.print("\n[.i.] Choose a number between 1 and " + senderOptions.length + ". [Any other input to quit!][x: coming, o: working]\n");

            // get input from command-line
            String choice = scanner.next();
            System.out.print(" [.i.] You've chosen '" + choice + "' ...\n");

            //Populate CLI options here
            switch (choice) {
                // 1. Create new User without UUID
                // create user
                case "1":

                    //String xmlHeaderDescription, SourceType Source_type, String userUUID, String lastName, String firstName, String phoneNumber, String email, String street, int houseNr, String city, int postalCode, String country, String company, Helper.SourceType type, int entity_version, int active, String timestamp
                    userUUID = "";
                    lastName = "Case1LN";
                    firstName = "Case1FN";
                    String phoneNumber = "Case1PN";
                    String email = "Case1EM";
                    String street = "Case1ST";
                    String houseNr = "11A";
                    String city = "Case1I";
                    String postalCode = "1111A";
                    String country = "Case1COU";
                    String company = "Case1COM";
                    String userType = "ADMIN";
                    Source_type = SourceType.Planning;
                    Entity_type = EntityType.User;

                    // 1. create user object
                    User newUser = new User(0, 1, 1, getCurrentDateTimeStamp(), userUUID, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType);

                    // 2. insert into local DB
                    int case3test = 0;
                    try {
                        case3test = new User_DAO().insertIntoUser(newUser);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("newUser.getEntityId() before uuid manager call, entity id: " + newUser.getEntityId());

                    // 3. create new UUID
                    try {
                        userUUID = Sender.createUuidRecord("", newUser.getEntityId(), Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\nUserUUID returned: '" + userUUID + "' !");

                    // 4. update local db with UUID
                    if (!new User_DAO().updateTablePropertyValue("User", "uuid", userUUID, "String", "idUser", "" + newUser.getEntityId())) {
                        System.out.println("Something went wrong updating User's userUUID");
                    } else {
                        newUser.setUuid(userUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // System.out.println("MAIN: userUUID: "+newUser.getUserUUID()+" userUUID(...):"+userUUID);
                    // System.out.println("user toString MAIN: "+newUser.toString());

                    // 5. Parse user object to xml String
                    xmlTotalMessage = getXmlFromUserObject(headerDescription, Source_type, newUser);

                    // 6. Send send new object to rabbitExchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException | IOException e) {
                        e.printStackTrace();
                    }

                    break;

                // 2. Create new Event without UUID (Front-End Call)
                case "2":

                    // Change as you wish
                    eventUuid = "";
                    String eventName = "Case 2 eventName()";
                    maxAttendees = 45;
                    description = "Case 2 description()";
                    summary = "Case 2 summary()";
                    String location = "Case 2 location()";
                    String contactPerson = "Case 2 contactPerson()";
                    //String dateTimeStart;
                    //String dateTimeEnd;
                    type = "Case 2 EventType ";
                    float price = 0;
                    Source_type = SourceType.Planning;
                    Entity_type = EntityType.Event;
                    int entityVersion = 1;
                    int active = 1;
                    String timestamp = getCurrentDateTimeStamp();

                    // 1. create Event object

                    Event newEvent = new Event(0, entityVersion, active, timestamp, eventUuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, type, price);

                    // 2. insert to local db

                    int case1test = 0;
                    try {
                        case1test = new Event_DAO().insertIntoEvent(newEvent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 3. create UUID

                    try {
                        eventUuid = Sender.createUuidRecord(messageType, case1test, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 4. update local db with UUID

                    if (!new BaseEntityDAO().updateTablePropertyValue("Event", "uuid", eventUuid, "String", "idEvent", "" + newEvent.getEntityId())) {
                        System.out.println("Something went wrong updating Event's eventUuid");
                    } else {
                        newEvent.setEventUUID(eventUuid);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }
                    // 5. create xml message

                    xmlTotalMessage = getXmlForNewEvent(messageType, headerDescription, Source_type, eventUuid, eventName, maxAttendees, description, summary, location, contactPerson, type, price, entityVersion, active, dateTimeStart, dateTimeEnd);

                    // 6. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                // 3. Create new Session without UUID (Front-End Call)
                case "3":

                    // Change as you wish

                    //new session will be set in Planning
                    Source_type = SourceType.Planning;

                    // variables for session
                    messageType = "sessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = EntityType.Session;
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    location = "Oval office dept.1 Room 420";
                    type = "Speech";
                    Entity_version = 1;
                    price = 2.22f;
                    sessionUUID = "";
                    eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                    description = "Description for Main case (2): create new session without UUID";
                    summary = "Summary for Main case (2): create new session without UUID";

                    System.out.println("\nNew Session made with sessionUUID: " + sessionUUID);

                    // 1. create session object
                    Session case2NewSession = new Session(0, 1, 1, getCurrentDateTimeStamp(), sessionUUID, eventUuid, sessionName, maxAttendees, description, summary, dateTimeStart, dateTimeEnd, speaker, location, type, price);

                    System.out.println("case2NewSession: " + case2NewSession.toString());

                    // 2. insert to local db
                    int case2test = 0;
                    try {
                        case2test = new Session_DAO().insertIntoSession(case2NewSession);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("case2NewSession: " + case2NewSession.toString());

                    // 3. create UUID
                    try {
                        sessionUUID = Sender.createUuidRecord(messageType, case2test, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 4. update local db with UUID
                    if (!new BaseEntityDAO().updateTablePropertyValue("Session", "uuid", sessionUUID, "String", "idSession", "" + case2NewSession.getEntityId())) {
                        System.out.println("Something went wrong updating Session's sessionUUID");
                    } else {
                        case2NewSession.setSessionUUID(sessionUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = getXmlForNewSession(headerDescription, Source_type, sessionUUID, eventUuid, sessionName, maxAttendees, description, summary, location, speaker, dateTimeStart, dateTimeEnd, type, price, Entity_version, 1);

                    // 6. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case "4":
                    // 4. Create new Reservation_Event: Add User to Event

                    //preset variables
                    messageType = "reservationEventMessage";
                    //Entity_sourceId = 200;
                    Entity_type = EntityType.ReservationEvent;
                    Source_type = SourceType.Planning;
                    type = "Case 10 type";
                    paid = 0;

                    //preset UUID's (make them fit your local db structure!!)
                    userUUID = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                    eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                    sessionUUID = "51129fa0-4a6b-44ec-aada-ff082f5db11b";

                    reservationUUID = "";

                    // 1. create Reservation_Event object
                    int eventId = 0;
                    Reservation_Event newEventReservation = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), reservationUUID, userUUID, eventUuid, paid, false);

                    // 2. insert to local db
                    //System.out.println("Reservation to string: "+newEventReservation.toString());

                    int case10test = 0;
                    try {
                        case10test = new Reservation_Event_DAO().insertIntoReservation_Event(newEventReservation);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 3. create UUID
                    try {
                        responseFromSender = Sender.createUuidRecord(messageType, newEventReservation.getEntityId(), Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    reservationUUID = responseFromSender;

                    // 4. update local db with UUID
                    if (!new Reservation_Event_DAO().updateTablePropertyValue("Reservation_Event", "uuid", reservationUUID, "String", "idReservationEvent", "" + newEventReservation.getEntityId())) {
                        System.out.println("Something went wrong updating Reservation_Event's reservationUUID");
                    } else {
                        newEventReservation.setReservationUUID(reservationUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, newEventReservation);

                    // 6. send new object to exchange
                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException | IOException e) {
                        e.printStackTrace();
                    }

                    break;

                // 5. Create new Reservation_Session: Add User to Session
                case "5":

                    //preset variables
                    messageType = "reservationSessionMessage";
                    //Entity_sourceId = 200;
                    //Entity_type=EntityType.Visitor;
                    Source_type = SourceType.Planning;
                    type = "Case 11 type";
                    paid = 0;

                    //get userUUID
                    userUUID = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                    //get sessionUUID
                    sessionUUID = "51129fa0-4a6b-44ec-aada-ff082f5db11b";

                    reservationUUID = "";

                    // 1. create Event object
                    int reservationId = 0;
                    Reservation_Session newSessionReservation = new Reservation_Session(0, 1, 1, getCurrentDateTimeStamp(), reservationUUID, userUUID, sessionUUID, paid);

                    // 2. insert to local db
                    int case11test = 0;
                    try {
                        case11test = new Reservation_Session_DAO().insertIntoReservation_Session(newSessionReservation);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 3. create UUID
                    try {
                        reservationUUID = Sender.createUuidRecord(messageType, newSessionReservation.getReservationId(), Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }


                    // 4. update local db with UUID
                    if (!new Reservation_Session_DAO().updateTablePropertyValue("Reservation_Session", "uuid", reservationUUID, "String", "idReservationSession", "" + newSessionReservation.getEntityId())) {
                        System.out.println("Something went wrong updating Reservation_Event's reservationUUID");
                    } else {
                        newSessionReservation.setReservationUUID(reservationUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = getXmlFromReservation_SessionObject(headerDescription, Source_type, newSessionReservation);

                    // 6. send new object to exchange
                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException | IOException e) {
                        e.printStackTrace();
                    }

                    break;


                // 06. get All UUID's
                case "6":

                    String myRecordsJsonString = "";
                    UUID = "";

                    System.out.println("\nCase " + choice + ": get all UUID's locally\n");


                    //try to get all records from UUID server
                    try {
                        myRecordsJsonString = httpGetAllRecords(10);
                        //System.out.println("myRecordsJsonString: "+myRecordsJsonString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String myJsonStringWithoutEdges = myRecordsJsonString.substring(1, myRecordsJsonString.length() - 1);

                    //System.out.println("myJsonStringWithoutEdges: "+myJsonStringWithoutEdges);
                    //parse response to different lines for readability
                    String[] UUIDRecords = new String[0];
                    try {
                        UUIDRecords = myJsonStringWithoutEdges.split("},");// '}' is deleted due to split
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < UUIDRecords.length; i++) {
                        UUIDRecords[i] += "}";// add '}' back here

                        // for printing all variables on separate lines:
                        System.out.println(i + ". " + UUIDRecords[i]);
                    }
                    // for printing all variables in one line:
                    //System.out.println(Arrays.toString(UUIDRecords));


                    break;


                // 07. Update Session (updateUuidRecordVersion(messageType, Source_type, UUID))"
                case "7":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object without a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Source_type = SourceType.Planning;
                    UUID = "9ab1ffe1-3376-4add-abac-92e9ee00b671";

                    try {

                        Entity_version = Sender.updateUuidRecordVersion(messageType, Source_type, UUID);
                        // No answer yet
                        System.out.println("\nSession updated with answer: " + Entity_version);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }


                    /*
                    // variables for session

                    Entity_sourceId = 100;
                    price=8.88f;
                    Entity_type = EntityType.ADMIN;
                    sessionName = "Session RENAME test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President2";
                    location = "Oval office dept.2 Room 1420";
                    type = "Speech";

                    // 3. update in local db

                    // TO DO
                     */

                    break;

                // 08. Change entity version
                // Alter record directly in UUID manager (select on UUID and Entity_sourceId)
                case "8":

                    messageType = "UpdateEntityVersionMessage";
                    Source_type = SourceType.Planning;
                    UUID = "9ab1ffe1-3376-4add-abac-92e9ee00b671";
                    Entity_version = 20;

                    System.out.println("\nCase " + choice + ": change Entity_version (=>'" + Entity_version + "') of UUID: " + UUID + " with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.updateUuidRecordVersionB(messageType, Source_type, UUID, Entity_version);

                        System.out.println("\nresponseFromSender: " + responseFromSender);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    break;

                // 09. List upcoming events (Google calendar 1)
                case "9":

                    System.out.println("\nCase '" + choice + "': List upcoming events (Google calendar 1)!");

                    System.out.println(" [" + messageType + "] Trying to list events... ");

                    try {
                        System.out.println(" [" + messageType + "] 1. Trying to list events... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.listEvents(service);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                // 10. Create new dummy event (Google calendar 2)
                case "10":

                    System.out.println("\nCase '" + choice + "': Create new dummy event (Google calendar 2)!");

                    try {
                        System.out.println(" [" + messageType + "] 2. Trying to create dummy event... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.createDummyEvent(service);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                // 11. Create new event (Google calendar 3)
                case "11":

                    System.out.println("\nCase '" + choice + "': Create new event (Google calendar 3)!");

                    try {
                        System.out.println(" [" + messageType + "] 3. Creating new event... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.createDummyEvent(service);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                // 12. Mock XML message
                case "12":

                    boolean continueMocking = true;
                    int counter = 1;
                    while (continueMocking) {
                        // CLI message
                        System.out.println("\nCase '" + choice + "': Mock XML message nr." + counter + "!");
                        continueMocking = xmlMessageMocker();
                    }

                    break;

                // 13. Create new Session with UUID (insertUuidRecord,SessionMessage)
                // normally when a new message from another team is received
                case "13":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = EntityType.Session;
                    Source_type = SourceType.Planning;
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    location = "Oval office dept.1 Room 420";
                    type = "Speech";
                    maxAttendees = 50;
                    price = 5.55f;
                    UUID = "531f33b6-88d1-406f-b6f3-1a0c0de9a1de";
                    price = 0;

                    try {

                        String response = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        // No answer yet
                        // System.out.println("\nSession updated with answer: " + response);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 3. insert to local db

                    Session newSession = new Session(0, 1, 1, getCurrentDateTimeStamp(), sessionUUID, eventUuid, sessionName, maxAttendees, description, summary, dateTimeStart, dateTimeEnd, speaker, local, type, price);

                    int case5test = 0;
                    try {
                        case5test = new Session_DAO().insertIntoSession(newSession);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;

                // 14. New Reservation_Session object with UUID:
                // normally when a new message from another team is received
                case "14":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    messageType = "ReservationMessage";
                    Entity_sourceId = 1200;
                    Entity_type = EntityType.ReservationSession;
                    Source_type = SourceType.Planning;

                    UUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //responseFromSender = logic.httpPutUpdateUuidRecordVersion(UUID, Source_type);

                    // 1. create new UUID-record
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new local object with a UUID (=>'" + UUID + "') with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        System.out.println("\nUUID Response From UUID Master: " + responseFromSender);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                        System.out.println("\nERROR IN CASE2: " + e);
                    }
                    // 2. no need to send to exchange

                    break;

                case "":
                case "0":
                default:

                    System.out.println("\nCase '" + choice + "' not worked out yet!");
                    System.out.print("Ending the process!");
                    break;

            }


            System.out.println(responseFromSender);

            System.out.println(" [.i.] End of this loop... ");

            try {
                choser = Integer.parseInt(choice);
                //System.out.print("You entered '" + choser + "'!");
            } catch (NumberFormatException e) {

                System.out.print("You entered choice: '" + choice + "' and got choser: '" + choser + "'!");
                System.out.print("ERROR: '" + e);
                choser = 0;
            }

        } while (choser > 0 && choser <= senderOptions.length + 1);


        return true;

    }

    static int handleNewMessage(String task, int workCounter) throws JAXBException, IOException, ParserConfigurationException, SAXException, Exception {

        //change to true to show full XML message in receiver console when it's received
        boolean showFullXMLMessage = true;

        String messageType = null, xmlTotalMessage = "";
        messageType = getSafeXmlProperty(task, "messageType");
        if (messageType == "false") {
            messageType = getSafeXmlProperty(task, "MessageType");
            if (messageType == "false") {
                System.out.println("[!!!] ERROR: No messageType found in XML...");
            }
        }

        // get Source from XML (set in sender)
        String messageSource = null;
        messageSource = getSafeXmlProperty(task, "source");
        if (messageSource == "false") {
            messageType = getSafeXmlProperty(task, "Source");
            if (messageType == "false") {
                System.out.println(" [!!!] ERROR: No source found in XML...");
            }
        }

        // Check XML for message

        workCounter++;

        System.out.println("_________________________________________________________________________________");
        System.out.println("_________________________START OF MESSAGE________________________________________");
        System.out.println("* [.i.] [NEW MESSAGE]: " + workCounter + " [TYPE]: '" + messageType + "' [FROM]: '" + messageSource + "' [.i.] ");
        //System.out.println("* [.i.] ********* [TYPE]: '" + messageType + "' [FROM]: '" + messageSource + "' ****** [.i.] *");
        System.out.println("* [.i.] ** [@] '" + getCurrentDateTimeStamp() + "' [MESSAGELENGTH]: '" + task.length() + "' characters ** [.i.] ");

        //System.out.println("*[.i.] with length: '" + task.length() + "' characters [.i.]");
        System.out.println("_________________________________________________________________________________");
        System.out.println("Message content:");


        String UUID = "";

        //toLowercase just for catching CaPitAliZatIOn errors...
        switch (messageType.toLowerCase()) {

            case "usermessage":

                handleNewMessageUser(task);

                break;

            case "eventmessage":

                handleNewMessageEvent(task);

                break;

            case "sessionmessage":

                handleNewMessageSession(task);

                break;

            case "reservationSessionMessage":
            case "reservationsessionmessage":

                handleNewMessageReservationSession(task);

                break;


            case "reservationEventMessage":
            case "reservationeventmessage":

                handleNewMessageReservationEvent(task);

                break;

            case "testmessage":

                System.out.println(" [" + messageType + "] for UUID: " + UUID);
                break;

            case "pingmessage":


                // 1. Form XML pingMessage

                String xmlMessage = "";
                try {
                    xmlMessage = getXmlForPingMessage("pingMessage", SourceType.Planning);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 2. send xml message to exchange

                String returnedMessage = "";
                try {
                    returnedMessage = Sender.sendPingMessage(xmlMessage, SourceType.Planning);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }

                System.out.println(returnedMessage);


                break;
            case "errormessage":

                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));
                break;

            case "productmessage":
            case "purchasemessage":
            case "invoicerequestmessage":
            case "creditnotemessage":
            case "invitemessage":
            case "invoiceMessage":

                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));

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

            System.out.println("\n [.!.] XML not shown... Change boolean 'showFullXMLMessage' to show this.\n");
        }


        System.out.println("__________________________END OF MESSAGE______________________________________");
        System.out.println("_________________________________________________________________________________");
        //System.out.println(" [.i.] END OF WORK [************************************************************************]");

        return workCounter;
    }

    static void handleNewMessageUser(String task) {

        String userUUID = "";

        User thisUserInMessage = null;
        Boolean uuidExists = false;

        EntityType thisEntityType = EntityType.User;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to user-object
        try {
            thisUserInMessage = getUserObjectFromXmlMessage(task);

            System.out.println("thisUserInMessage.toString():\n" + thisUserInMessage.toString() + "\n");

            userUUID = thisUserInMessage.getUuid();


            //System.out.println("user toString: "+thisUserInMessage.toString());
            //System.out.println(" [" + messageType + "] for userUUID: " + userUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (thisUserInMessage != null) {

            // 2.1 check if UUID exists in local db

            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("User", userUUID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 2.1.1 check if user is to be 'deleted'

            if (uuidExists) {

                System.out.println("UUID already exists in our PlanningDB table:[User]");

                // 2.2. User record update

                // 2.2.1. get idUser from userUUID in User
                String[] propertiesToSelect = {"idUser"};
                String table = "User";
                String[] selectors = {"uuid"};
                String[] values = {"" + userUUID};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                // 2.2.2. get the entityVersion from idUser
                propertiesToSelect[0] = "entity_version";
                table = "BaseEntity";
                selectors[0] = "idBaseEntity";
                values[0] = selectResults[0];
                int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                //System.out.println("localEntityVersion: "+localEntityVersion + " & thisUserInMessage.getEntityVersion():  "+thisUserInMessage.getEntityVersion());

                if (localEntityVersion < thisUserInMessage.getEntityVersion()) {

                    // 2.3.1.A. set active = 0 on old entity record in db
                    boolean allGood = true;

                    // parse id to int

                    int oldLocalEntityId = Integer.parseInt(selectResults[0]), newLocalEntityId;

                    try {
                        newLocalEntityId = new User_DAO().UpdateUser(thisUserInMessage, oldLocalEntityId);
                    } catch (SQLException e) {
                        System.out.println("Something went wrong updating user: " + e);
                        //e.printStackTrace();
                    }

                    /*
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "active", "0", "int", "idBaseEntity", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.A. set active = 0 on old record in db:\n " + e);
                        allGood = false;
                    }

                    // 2.3.1.B. set uuid = / on old table record in db
                    if (allGood) {

                        try {
                            allGood = new BaseEntityDAO().updateTablePropertyValue("User", "uuid", "/", "String", "idUser", selectResults[0]);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                            allGood = false;
                        }
                    }

                    if (allGood) {

                        // 2.3.2. insert new record into local db
                        int messageUserInsertReturner = 0, updateUuidRecordVersionResponse = 0;
                        try {
                            messageUserInsertReturner = new User_DAO().insertIntoUser(thisUserInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)
                        try {
                            updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, userUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }

                        // 2.3.4. update entity version (on our database)
                        try {
                            allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + thisUserInMessage.getEntityVersion(), "int", "idBaseEntity", "" + messageUserInsertReturner);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                            allGood = false;
                        }
*/
                    System.out.println("We had this User with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisUserInMessage.getEntityVersion() + "'");

                } else {
                    System.out.println("We already had this User with entityVersion: '" + localEntityVersion + "'");
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
                //System.out.println("User.toString(): "+thisUserInMessage.toString());

                // 2.4.2. insertUuidRecord

                try {
                    Sender.insertUuidRecord("", messageUserInsertReturner, thisEntityType, Source_type, userUUID);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }
                System.out.println("Inserted new user record with id='" + messageUserInsertReturner + "' and UUID='" + userUUID + "'");


            }
        } else {
            System.out.println("ERROR: Something went wrong getting user object from xml message!");
        }

        //System.out.println(" [END] ");
    }

    static void handleNewMessageEvent(String task) {

        String eventUuid = "";
        Event thisEventInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.Event;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to event-object
        try {
            thisEventInMessage = getEventObjectFromXmlMessage(task);
            //System.out.println("user toString: "+thisUserInMessage.toString());
            eventUuid = thisEventInMessage.getEventUUID();

            System.out.println("New message for EVENT with UUID: " + eventUuid);

            //System.out.println(" [" + messageType + "] for userUUID: " + userUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (thisEventInMessage != null) {
            // 2.1. check if UUID exists in local db
            uuidExists = false;
            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("Event", eventUuid);
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
                String[] selectors = {"uuid"};
                String[] values = {"" + eventUuid};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                //System.out.println("selectResults: " + selectResults[0]);

                // 2.2.2. get entityVersion from id in BaseEntity
                propertiesToSelect[0] = "entity_version";
                table = "BaseEntity";
                selectors[0] = "idBaseEntity";
                values[0] = selectResults[0];
                int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                if (localEntityVersion < thisEventInMessage.getEntityVersion()) {

                    // 2.3.1.A. set active = 0 on old entity record in db
                    boolean allGood = true;
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "active", "0", "int", "idBaseEntity", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.A. set active = 0 on old record in db:\n " + e);
                        allGood = false;
                    }

                    // 2.3.1.B. set uuid = / on old table record in db
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("Event", "uuid", "/", "String", "idEvent", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                        allGood = false;
                    }

                    if (allGood) {

                        // 2.3.2. insert new record into local db
                        int messageEventInsertReturner = 0, updateUuidRecordVersionResponse = 0;
                        try {
                            messageEventInsertReturner = new Event_DAO().insertIntoEvent(thisEventInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)
                        try {
                            updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, eventUuid);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }

                        // 2.3.4. update entity version (on our database)
                        try {
                            allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + thisEventInMessage.getEntityVersion(), "int", "idBaseEntity", "" + messageEventInsertReturner);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                            allGood = false;
                        }
                        System.out.println("We had this event with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisEventInMessage.getEntityVersion() + "'");

                    }

                } else {
                    // we have the latest version...
                    System.out.println("We already had this event with entityVersion: '" + localEntityVersion + "'");

                }

            } else {
                // New event record

                // 2.4.1. Add new event to Google calendar


                String newEventHtmlLinkAndId = null;
                try {
                    newEventHtmlLinkAndId = GoogleCalenderApi.createEventFromEventObject(thisEventInMessage);

                    String[] newEventProperties = newEventHtmlLinkAndId.split("-=-");

                    thisEventInMessage.setGCAEventId(newEventProperties[1]);
                    thisEventInMessage.setGCAEventLink(newEventProperties[0]);

                } catch (IOException e) {
                    System.out.println("Error adding event to Google calendar API: " + e);
                    //e.printStackTrace();
                }

                // 2.4.2. insert new event into local db
                int messageEventInsertReturner = 0;
                try {
                    messageEventInsertReturner = new Event_DAO().insertIntoEvent(thisEventInMessage);
                } catch (SQLException e) {
                    System.out.println("Error inserting event into the database: " + e);
                    //e.printStackTrace();
                }

                // 2.4.3. insertUuidRecord
                try {
                    Sender.insertUuidRecord("", messageEventInsertReturner, thisEntityType, Source_type, eventUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    System.out.println("Error inserting record into the Uuid Master: " + e);
                    //e.printStackTrace();
                }

                System.out.println("Event.toString(): " + thisEventInMessage.toString());

                System.out.println("Inserted new event record with id='" + messageEventInsertReturner + "' and UUID='" + eventUuid + "'");


            }
        } else {
            System.out.println("Something went wrong getting event object from xml message!");
        }

        //System.out.println(" [END] ");

    }

    static void handleNewMessageSession(String task) {

        String sessionUUID = "";
        Session thisSessionInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.Session;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to session-object
        try {
            thisSessionInMessage = getSessionObjectFromXmlMessage(task);

            sessionUUID = thisSessionInMessage.getSessionUUID();

            System.out.println("New message for SESSION with UUID: " + sessionUUID);

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
                String[] selectors = {"uuid"};
                String[] values = {"" + sessionUUID};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                //System.out.println("selectResults: " + selectResults[0]);

                // 2.2.2. get entityVersion from id in BaseEntity
                propertiesToSelect[0] = "entity_version";
                table = "BaseEntity";
                selectors[0] = "idBaseEntity";
                values[0] = selectResults[0];

                int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);


                if (localEntityVersion < thisSessionInMessage.getEntityVersion()) {

                    // 2.3.1.A. set active = 0 on old entity record in db

                    boolean allGood = true;
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "active", "0", "int", "idBaseEntity", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.A. set active = 0 on old record in db:\n " + e);
                        allGood = false;
                    }

                    // 2.3.1.B. set uuid = / on old table record in db

                    if (allGood) {

                        try {
                            allGood = new BaseEntityDAO().updateTablePropertyValue("Session", "uuid", "/", "String", "idSession", selectResults[0]);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                            allGood = false;
                        }
                    }

                    if (allGood) {
                        // 2.3.2. insert new record into local db
                        int messageSessionInsertReturner = 0, updateUuidRecordVersionResponse = 0;
                        try {
                            messageSessionInsertReturner = new Session_DAO().insertIntoSession(thisSessionInMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)
                        try {
                            updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, sessionUUID);
                        } catch (IOException | TimeoutException | JAXBException e) {
                            e.printStackTrace();
                        }

                        // 2.3.4. update entity version (on our database)
                        try {
                            allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + thisSessionInMessage.getEntityVersion(), "int", "idBaseEntity", "" + messageSessionInsertReturner);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                            allGood = false;
                        }

                        System.out.println("We had this Session with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisSessionInMessage.getEntityVersion() + "'");
                    }
                } else {

                    // we have the latest version...
                    System.out.println("We already had this session with entityVersion: '" + localEntityVersion + "'");

                }

            } else {
                // New session record

                /*
                // 2.4.1. Add new Session to Google calendar
                String newSessionHtmlLinkAndId = "";
                try {
                    newSessionHtmlLinkAndId = GoogleCalenderApi.createEventFromSessionObject(thisSessionInMessage);

                    //System.out.println("newSessionHtmlLinkAndId: "+newSessionHtmlLinkAndId);
                    String[] newSessionProperties = newSessionHtmlLinkAndId.split("-=-");

                    thisSessionInMessage.setGCAEventId(newSessionProperties[1]);
                    thisSessionInMessage.setGCAEventLink(newSessionProperties[0]);
                    //System.out.println("thisSessionInMessage.toString()"+thisSessionInMessage.toString());

                } catch (IOException e) {
                    System.out.println("Error adding session as event to Google calendar API: "+e);
                    //e.printStackTrace();
                }
                */
                // 2.4.2. insert new session into local db
                int messageSessionInsertReturner = 0;
                try {
                    messageSessionInsertReturner = new Session_DAO().insertIntoSession(thisSessionInMessage);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // 2.4.3. insertUuidRecord
                try {
                    Sender.insertUuidRecord("", messageSessionInsertReturner, thisEntityType, Source_type, sessionUUID);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }
                System.out.println("Inserted new session record with id='" + messageSessionInsertReturner + "' and UUID='" + sessionUUID + "'");


            }
        } else {
            System.out.println("Something went wrong getting session object from xml message!");
        }

        //System.out.println(" [END] ");
    }

    static void handleNewMessageReservationEvent(String task) {

        String userUUID = "";
        String reservationUUID = "";
        String eventUUID = "";

        Reservation_Event newReservation_EventObjectFromXml = null;

        Boolean uuidExists = false;
        Boolean allGood = true;

        EntityType thisEntityType = EntityType.ReservationEvent;
        SourceType Source_type = SourceType.Planning;
        /*
        // get UUID's from xml
        reservationUUID = getSafeXmlProperty(task, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(task, "reservationUUID");

            if (reservationUUID == "false") {

                reservationUUID = getSafeXmlProperty(task, "reservationUuid");

                if (reservationUUID == "false") {

                    System.out.println(" [!!!] ERROR: No uuid, reservationUuid or reservationUUID found in XML...");

                    allGood = false;
                }
            }
        }

        userUUID = getSafeXmlProperty(task, "userUuid");
        if (userUUID == "false") {

            userUUID = getSafeXmlProperty(task, "userUUID");

            if (userUUID == "false") {
                System.out.println(" [!!!] ERROR: No userUuid found in XML");
                allGood = false;
            }
        }

        eventUUID = getSafeXmlProperty(task, "eventUuid");

        if (eventUUID == "false" || eventUUID == "") {

            System.out.println(" [!!!] ERROR: No sessionUuid or eventUuid found in XML: ");
            allGood = false;

        } else {

            System.out.println("New RESERVATION_EVENT for Event with UUID: " + eventUUID);
        }
*/
        // 1. transform xml to reservation-object

        newReservation_EventObjectFromXml = getReservation_EventObjectFromXmlMessage(task);

        System.out.println("newReservation_EventObjectFromXml.toString():\n" + newReservation_EventObjectFromXml.toString() + "\n");


        reservationUUID = newReservation_EventObjectFromXml.getReservationUUID();
        // System.out.println("sessionUUID: "+sessionUUID);

        // 2.1 check if UUID exists in local db

        uuidExists = false;

        // 2.1.A. search event table for uuid

        try {
            uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Event", newReservation_EventObjectFromXml.getReservationUUID());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (uuidExists) {

            System.out.println("UUID already exists in our PlanningDB table:\n");

            // 2.2. Reservation_Event record update

            // Prepare new reservation_event object for sending to database
            System.out.println("[Reservation_Event]\n");

            // 2.2.1 get idSession from sessionUUID in Session
            String[] propertiesToSelect = {"idReservationEvent"};
            String table = "Reservation_Event";
            String[] selectors = {"uuid"};
            String[] values = {"" + reservationUUID};

            String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

            //System.out.println("selectResults: " + selectResults[0]);

            // 2.2.2. get entityVersion from id in BaseEntity
            propertiesToSelect[0] = "entity_version";
            table = "BaseEntity";
            selectors[0] = "idBaseEntity";
            values[0] = selectResults[0];
            int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

            Reservation_Event existingReservation_Event = null;
            try {
                existingReservation_Event = getReservation_EventObjectFromXmlMessage(task);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (localEntityVersion < existingReservation_Event.getEntityVersion()) {
                // 2.3.1.A. set active = 0 on old entity record in db
                boolean newREAllGood = true;
                try {
                    newREAllGood = new Reservation_Event_DAO().updateTablePropertyValue("BaseEntity", "active", "0", "int", "idBaseEntity", selectResults[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 2.3.1.B. set uuid = / on old table record in db
                if (newREAllGood) {

                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("Reservation_Event", "uuid", "/", "String", "idReservationEvent", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                        newREAllGood = false;
                    }
                }

                if (newREAllGood) {
                    // 2.3.2. insert new record into local db
                    int messageReservation_EventInsertReturner = 0, updateUuidRecordVersionResponse = 0;
                    try {
                        messageReservation_EventInsertReturner = new Reservation_Event_DAO().insertIntoReservation_Event(existingReservation_Event);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 2.3.3. updateUuidRecordVersion() (To UUID master)
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, reservationUUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 2.3.4. update entity version (on our database)
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + existingReservation_Event.getEntityVersion(), "int", "idBaseEntity", "" + messageReservation_EventInsertReturner);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                        allGood = false;
                    }

                    System.out.println("We had this Reservation_Event with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + existingReservation_Event.getEntityVersion() + "'");

                }

            } else {
                // we have the latest version...
                System.out.println("We already had this Reservation_Event with entityVersion: '" + localEntityVersion + "'");

            }


            // # send update to uuid manager


        } else {

            // uuid doesn't exit locally yet
            // # insert record locally with given info

            System.out.println("uuid doesn't exit locally yet...");

            int messageReservationInsertReturner = 0;

            // TODO

            BaseEntity newTempEntity = new BaseEntity();
            newReservation_EventObjectFromXml.setReservationId(newTempEntity.getEntityId());
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

        }

    }

    static void handleNewMessageReservationSession(String task) {

        String userUUID = "";
        String reservationUUID = "";
        String sessionUUID = "";
        Reservation_Session newReservation_SessionObjectFromXml = null;
        Boolean uuidExists = false;
        Boolean allGood = true;
        EntityType thisEntityType = EntityType.ReservationSession;
        SourceType Source_type = SourceType.Planning;


        // get UUID's from xml
        reservationUUID = getSafeXmlProperty(task, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(task, "reservationUUID");

            if (reservationUUID == "false") {

                reservationUUID = getSafeXmlProperty(task, "reservationUuid");

                if (reservationUUID == "false") {

                    System.out.println(" [!!!] ERROR: No uuid, reservationUuid or reservationUUID found in XML...");

                    allGood = false;
                }
            }
        }

        userUUID = getSafeXmlProperty(task, "userUuid");
        if (userUUID == "false") {

            userUUID = getSafeXmlProperty(task, "userUUID");

            if (userUUID == "false") {
                System.out.println(" [!!!] ERROR: No userUuid found in XML");
                allGood = false;
            }
        }

        sessionUUID = getSafeXmlProperty(task, "sessionUuid");
        if (sessionUUID == "false" || sessionUUID == "") {

            System.out.println(" [!!!] ERROR: No sessionUuid or eventUuid found in XML: ");
            allGood = false;

        }

        // 1. transform xml to reservation-object


        newReservation_SessionObjectFromXml = getReservation_SessionObjectFromXmlMessage(task);


        // System.out.println("sessionUUID: "+sessionUUID);

        // 2.1 check if UUID exists in local db

        uuidExists = false;

        // 2.1.B. search session table for uuid

        try {
            uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Session", newReservation_SessionObjectFromXml.getReservationUUID());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (uuidExists) {

            System.out.println("UUID already exists in our PlanningDB table:\n");

            // 2.2. Reservation_Event record update

            // Prepare new reservation_session object for sending to database
            System.out.println("Reservation_Session");

            // 2.4.1 get idSession from sessionUUID in Session
            String[] propertiesToSelect = {"idReservationSession"};
            String table = "Reservation_Session";
            String[] selectors = {"uuid"};
            String[] values = {"" + sessionUUID};

            String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

            //System.out.println("selectResults: " + selectResults[0]);

            // 2.4.2. get entityVersion from id in BaseEntity
            propertiesToSelect[0] = "entity_version";
            table = "BaseEntity";
            selectors[0] = "idBaseEntity";
            values[0] = selectResults[0];
            int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

            Reservation_Session existingReservation_Session = null;
            try {
                existingReservation_Session = getReservation_SessionObjectFromXmlMessage(task);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (localEntityVersion < existingReservation_Session.getEntityVersion()) {

                // 2.5.1.A. set active = 0 on old entity record in db
                boolean newRSAllGood = true;
                try {
                    newRSAllGood = new BaseEntityDAO().updateTablePropertyValue("Reservation_Session", "active", "0", "int", "idReservationSession", selectResults[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (newRSAllGood) {

                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("Reservation_Session", "uuid", "/", "String", "idReservationSession", selectResults[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                        newRSAllGood = false;
                    }
                }


                if (newRSAllGood) {

                    // 2.5.2. insert new record into local db
                    int messageReservation_SessionInsertReturner = 0, updateUuidRecordVersionResponse = 0;
                    try {
                        messageReservation_SessionInsertReturner = new Reservation_Session_DAO().insertIntoReservation_Session(existingReservation_Session);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 2.3.3. updateUuidRecordVersion() (To UUID master)
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, reservationUUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }


                    // 2.3.4. update entity version (on our database)
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + existingReservation_Session.getEntityVersion(), "int", "idBaseEntity", "" + existingReservation_Session);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR in 2.3.1.B. set uuid = / on old record in db:\n " + e);
                        allGood = false;
                    }

                    System.out.println("We had this Reservation_Session with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + existingReservation_Session.getEntityVersion() + "'");

                }

            } else {
                // we have the latest version...
                System.out.println("We already had this Reservation_Session with entityVersion: '" + localEntityVersion + "'");
            }


            // # send update to uuid manager


        } else {

            // uuid doesn't exit locally yet
            // # insert record locally with given info

            System.out.println("uuid doesn't exit locally yet...");

            int messageReservationInsertReturner = 0;

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


    // UUID: GET: all
    static String httpGetAllRecords(int limit) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        //HttpRequest.UUID_createUuidRecord myLocalUUID_createUuidRecordObject = new HttpRequest.UUID_createUuidRecord(Entity_sourceId, Entity_type, Source_type);

        //post request

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/getall";
        String json = "";
        //myLocalUUID_createUuidRecordObject.toJSONString();

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "get");

        //(handle request)
        //System.out.println("[i] In String httpGetAllRecords(): myLocalUUID_createUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: POST: create
    static String httpPostCreateUuidRecord(int Entity_sourceId, EntityType Entity_type, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        UUID_createUuidRecord myLocalUUID_createUuidRecordObject = new UUID_createUuidRecord(Entity_sourceId, Entity_type, Source_type);

        //post request

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/createUuidRecord";
        String json = myLocalUUID_createUuidRecordObject.toJSONString();

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "post");

        //(handle request)
        //System.out.println("[i] In String httpPostGetNewUuid(): myLocalUUID_createUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: POST: insert
    static String httpPostInsertUuidRecord(String UUID, int Entity_sourceId, EntityType Entity_type, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        UUID_insertUuidRecord myLocalUUID_insertUuidRecordObject = new UUID_insertUuidRecord(Entity_sourceId, Entity_type, Source_type, UUID, 1);

        //post request
        // PHP: still inserts new record with new UUID while

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/insertUuidRecord";
        String json = myLocalUUID_insertUuidRecordObject.toJSONString();

        //System.out.println("json to be sent for httpPostInsertUuidRecord: " + json);

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "post");

        //(handle request)
        //System.out.println("[i] In String httpPostUpdateUuidByUuid(): myLocalUUID_insertUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: PUT: update1
    static String httpPutUpdateUuidRecordVersion(String UUID, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_updateUuidRecordVersion(String myUrl, String UUID, logic.Sender.SourceType Source_type)
        UUID_updateUuidRecordVersion myLocalUUID_updateUuidRecordObject = new UUID_updateUuidRecordVersion(UUID, Source_type);


        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/updateUuidRecordVersion";
        String json = myLocalUUID_updateUuidRecordObject.toJSONString();

        //System.out.println("json: " + json);

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "put");

        //System.out.println("\n[i] In String httpPutUpdateUuidRecordVersion(): myLocalUUID_updateUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: PUT: update2
    static String httpPutUpdateUuidRecordVersionB(String UUID, int Entity_version, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_updateUuidRecordVersion(String myUrl, String UUID, logic.Sender.SourceType Source_type)
        UUID_updateUuidRecordVersionB myLocalUUID_updateUuidRecordObject = new UUID_updateUuidRecordVersionB(UUID, Entity_version, Source_type);

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/updateUuidRecordVersionB";
        String json = myLocalUUID_updateUuidRecordObject.toJSONString();
        System.out.println("json: " + json);


        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "put");

        System.out.println("\n[i] In String httpPutUpdateUuidRecordVersionB(): myLocalUUID_updateUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: General HttpRequest
    static String doHttpRequest(String myUrl, String json, String method) throws IOException {


        MediaType JSON = MediaType.parse("application/json");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = null;

        if (method == "post") {
            request = new Request.Builder()
                    .url(myUrl)
                    .post(body)
                    .build();

        } else if (method == "put") {

            request = new Request.Builder()
                    .url(myUrl)
                    .put(body)
                    .build();

        } else if (method == "get") {

            request = new Request.Builder()
                    .url(myUrl)
                    .build();
        } else {

            return "Something went wrong in the logic.Helper: method not correct";

        }

        try (Response response = client.newCall(request).execute()) {

//            String thisResponse = response.body().string();

            return response.body().string();

        } catch (Exception e) {

            System.out.println(e.toString());
            return e.toString();
        }


    }


    // User: (params) => XML
    static String getXmlForNewUser(String xmlHeaderDescription, SourceType Source_type, String userUUID, String lastName, String firstName, String phoneNumber, String email, String street, String houseNr, String city, String postalCode, String country, String company, String type, int entity_version, int active, String timestamp) throws JAXBException {

        String messageType = "userMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.Userstructure userStructure = new XmlMessage.Userstructure(userUUID, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, type, entity_version, active, timestamp);
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.UserMessage xmlUserMessage = new XmlMessage.UserMessage(header, userStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlUserMessage.generateXML();


        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;

    }

    // User: Object => XML
    static String getXmlFromUserObject(String xmlHeaderDescription, SourceType Source_type, User user) throws JAXBException {

        String userUUID = user.getUuid();
        //System.out.println("HELPER: userUUID: "+userUUID);
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();
        String street = user.getStreet();
        String houseNr = user.getHouseNr();
        String city = user.getCity();
        String postalCode = user.getPostalCode();
        String country = user.getCountry();
        String company = user.getCompany();
        String type = user.getUserType();
        int entityVersion = user.getEntityVersion();
        int active = user.getActive();
        String timestamp = user.getTimestamp();

        String messageType = "userMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.Userstructure userStructure = new XmlMessage.Userstructure(userUUID, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, type, entityVersion, active, timestamp);
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.UserMessage xmlUserMessage = new XmlMessage.UserMessage(header, userStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlUserMessage.generateXML();


        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;

    }

    // User: XML => Object
    static User getUserObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        User userObject = null;

        String uuid = "false";
        String lastName = "false";
        String firstName = "false";
        String phoneNumber = "false";
        String email = "false";
        String street = "false";
        String houseNr = "false";
        String city = "false";
        String postalCode = "false";
        String country = "false";
        String company = "false";
        String userType = "false";
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        uuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (uuid == "false") {

            uuid = getSafeXmlProperty(xmlMessage, "UUID");
            if (uuid == "false") {

                uuid = getSafeXmlProperty(xmlMessage, "userUuid");

                if (uuid == "false") {
                    uuid = getSafeXmlProperty(xmlMessage, "userUUID");

                    if (uuid == "false") {
                        uuid = getSafeXmlProperty(xmlMessage, "Uuid");
                        if (uuid == "false") {
                            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
                            allGood = false;
                        }
                    }
                }
            }
        }
        lastName = getSafeXmlProperty(xmlMessage, "lastName");
        if (lastName == "false") {

            System.out.println(" [!!!] ERROR: No lastName found in XML: ");
            allGood = false;

        }
        firstName = getSafeXmlProperty(xmlMessage, "firstName");
        if (firstName == "false") {

            System.out.println(" [!!!] ERROR: No firstName found in XML: ");
            allGood = false;

        }
        phoneNumber = getSafeXmlProperty(xmlMessage, "phoneNumber");
        if (phoneNumber == "false") {

            System.out.println(" [!!!] ERROR: No phoneNumber found in XML: ");
            allGood = false;

        }
        email = getSafeXmlProperty(xmlMessage, "email");
        if (email == "false") {

            System.out.println(" [!!!] ERROR: No email found in XML: ");
            allGood = false;

        }
        street = getSafeXmlProperty(xmlMessage, "street");
        if (street == "false") {

            System.out.println(" [!!!] ERROR: No street found in XML: ");
            allGood = false;

        }
        houseNr = getSafeXmlProperty(xmlMessage, "houseNr");
        if (houseNr == "false") {

            System.out.println(" [!!!] ERROR: No houseNr found in XML: ");
            allGood = false;

        }
        city = getSafeXmlProperty(xmlMessage, "city");
        if (city == "false") {

            System.out.println(" [!!!] ERROR: No city found in XML: ");
            allGood = false;

        }
        postalCode = getSafeXmlProperty(xmlMessage, "postalCode");
        if (postalCode == "false") {

            System.out.println(" [!!!] ERROR: No postalCode found in XML: ");
            allGood = false;

        }
        country = getSafeXmlProperty(xmlMessage, "country");
        if (country == "false") {

            System.out.println(" [!!!] ERROR: No country found in XML: ");
            allGood = false;

        }
        company = getSafeXmlProperty(xmlMessage, "company");
        if (company == "false") {

            System.out.println(" [!!!] ERROR: No company found in XML: ");
            allGood = false;

        }
        //type = EntityType.valueOf(getSafeXmlProperty(xmlMessage, "type"));
        userType = getSafeXmlProperty(xmlMessage, "userType");

        if (userType == "false") {

            userType = getSafeXmlProperty(xmlMessage, "type");

            if (userType == "false") {

                userType = getSafeXmlProperty(xmlMessage, "Type");

                if (userType == "false") {

                    System.out.println(" [!!!] ERROR: No userType found in XML: ");
                    allGood = false;
                }
            }

        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        try {
            active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();

            //catch boolean instead of int

            if (getSafeXmlProperty(xmlMessage, "active") == "true") {
                active = 1;
            } else if (getSafeXmlProperty(xmlMessage, "active") == "false") {
                active = 0;
            } else {
                active = -1;
            }
        }
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {

                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    allGood = false;
                }
            }
        }

        if (allGood) {

            userObject = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType, false);

            return userObject;
        } else {
            return null;
        }

    }


    // Session: (params) => XML
    static String getXmlForNewSession(String xmlHeaderDescription, SourceType Source_type, String sessionUUID, String eventUUID, String sessionName, int maxAttendees, String description, String summary, String location, String speaker, String dateTimeStart, String dateTimeEnd, String type, float price, int entityVersion, int active) throws JAXBException {
        String messageType = "sessionMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.SessionStructure sessionStructure = new XmlMessage.SessionStructure(sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, dateTimeStart, dateTimeEnd, speaker, type, price, entityVersion, active, getCurrentDateTimeStamp());
        // steek header en datastructure (SessionStructure) in message klasse

        XmlMessage.SessionMessage sessionMessage = new XmlMessage.SessionMessage(header, sessionStructure);
        String xmlTotalMessage = sessionMessage.generateXML();
        return xmlTotalMessage;
    }

    // Session: Object => XML
    static String getXmlFromSessionObject(String headerDescription, SourceType Source_type, Session newSession) throws JAXBException {

        //SourceType Source_type = Source_type;
        String sessionUUID = newSession.getSessionUUID();
        String eventUUID = newSession.getEventUUID();
        String sessionName = newSession.getSessionName();
        int maxAttendees = newSession.getMaxAttendees();
        String sessionDescription = newSession.getDescription();
        String summary = newSession.getSummary();
        String location = newSession.getLocation();
        String contactPerson = newSession.getSpeaker();
        String sessionType = newSession.getType();
        float price = newSession.getPrice();
        int entityVersion = newSession.getEntityVersion();
        int active = newSession.getActive();
        String dateTimeStart = newSession.getDateTimeStart();
        String dateTimeEnd = newSession.getDateTimeEnd();

        String messageType = "sessionMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, headerDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.SessionStructure eventStructure = new XmlMessage.SessionStructure(sessionUUID, eventUUID, sessionName, maxAttendees, sessionDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, sessionType, price, entityVersion, active, getCurrentDateTimeStamp());
        XmlMessage.SessionMessage xmlReservationMessage = new XmlMessage.SessionMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Session: XML => Object
    static Session getSessionObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Session sessionObject = null;

        String sessionUUID = "false";
        String eventUUID = "false";
        String sessionName = "false";
        int maxAttendees = 0;
        String description = "false";
        String summary = "false";
        String location = "false";
        String speaker = "false";
        String dateTimeStart = "false";
        String dateTimeEnd = "false";
        String sessionType = "false";
        float price = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        sessionUUID = getSafeXmlProperty(xmlMessage, "uuid");

        if (sessionUUID == "false") {

            sessionUUID = getSafeXmlProperty(xmlMessage, "UUID");

            if (sessionUUID == "false") {

                sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUUID");

                if (sessionUUID == "false") {
                    System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
                    allGood = false;
                }
            }
        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }
        sessionName = getSafeXmlProperty(xmlMessage, "sessionName");
        if (sessionName == "false") {

            System.out.println(" [!!!] ERROR: No sessionName found in XML: ");
            allGood = false;

        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR(intc): No maxAttendees found in XML: ");
            allGood = false;
        }

        if (maxAttendees < 0) {

            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            allGood = false;

        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {

            System.out.println(" [!!!] ERROR: No description found in XML: ");
            allGood = false;

        }
        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {

            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            allGood = false;

        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {

            System.out.println(" [!!!] ERROR: No location found in XML: ");
            allGood = false;

        }
        speaker = getSafeXmlProperty(xmlMessage, "speaker");
        if (speaker == "false") {

            System.out.println(" [!!!] ERROR: No speaker found in XML: ");
            allGood = false;

        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            allGood = false;

        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            allGood = false;

        }
        sessionType = getSafeXmlProperty(xmlMessage, "sessionType");
        if (sessionType == "false") {

            sessionType = getSafeXmlProperty(xmlMessage, "type");

            if (sessionType == "false") {

                System.out.println(" [!!!] ERROR: No sessionType/type found in XML: ");
                allGood = false;
            }

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            price = -1;
        }
        if (price < 0) {

            System.out.println(" [!!!] ERROR: No price found in XML: ");
            allGood = false;

        }
        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            price = -1;
        }
        if (entityVersion < 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }

        try {
            active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();

            //catch boolean instead of int
            if (getSafeXmlProperty(xmlMessage, "active") == "true") {
                active = 1;
            } else if (getSafeXmlProperty(xmlMessage, "active") == "false") {
                active = 0;
            } else {
                active = -1;
            }
        }
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {

                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    allGood = false;
                }
            }
        }

        sessionObject = new Session(0, entityVersion, active, timestamp, sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, speaker, dateTimeStart, dateTimeEnd, sessionType, "", "", price);
        //System.out.println("sessionObject.toString()"+sessionObject.toString());
        return sessionObject;
    }


    // Event: (params) => XML
    static String getXmlForNewEvent(String messageType, String description, SourceType Source_type, String eventUUID, String eventName, int maxAttendees, String eventDescription, String summary,
                                    String location, String contactPerson, String eventType, float price, int entityVersion, int active, String dateTimeStart, String dateTimeEnd) throws JAXBException {

        messageType = "eventMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.EventStructure eventStructure = new XmlMessage.EventStructure(eventUUID, eventName, maxAttendees, eventDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, entityVersion, active, getCurrentDateTimeStamp());
        XmlMessage.EventMessage xmlReservationMessage = new XmlMessage.EventMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Event: Object => XML
    static String getXmlFromEventObject(String headerDescription, SourceType Source_type, Event newEvent) throws JAXBException {

        //SourceType Source_type = Source_type;
        String eventUUID = newEvent.getEventUUID();
        String eventName = newEvent.getEventName();
        int maxAttendees = newEvent.getMaxAttendees();
        String eventDescription = newEvent.getDescription();
        String summary = newEvent.getSummary();
        String location = newEvent.getLocation();
        String contactPerson = newEvent.getContactPerson();
        String eventType = newEvent.getType();
        float price = newEvent.getPrice();
        int entityVersion = newEvent.getEntityVersion();
        int active = newEvent.getActive();
        String dateTimeStart = newEvent.getDateTimeStart();
        String dateTimeEnd = newEvent.getDateTimeEnd();

        String messageType = "eventMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, headerDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.EventStructure eventStructure = new XmlMessage.EventStructure(eventUUID, eventName, maxAttendees, eventDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, entityVersion, active, getCurrentDateTimeStamp());
        XmlMessage.EventMessage xmlReservationMessage = new XmlMessage.EventMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Event: XML => Object
    static Event getEventObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Event eventObject = null;

        String uuid = "false";
        String eventName = "false";
        int maxAttendees = 0;
        String description = "false";
        String summary = "false";
        String location = "false";
        String contactPerson = "false";
        String dateTimeStart = "false";
        String dateTimeEnd = "false";
        String type = "false";
        float price = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        uuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (uuid == "false") {

            uuid = getSafeXmlProperty(xmlMessage, "eventUuid");
            if (uuid == "false") {
                System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
                allGood = false;
            }
        }


        eventName = getSafeXmlProperty(xmlMessage, "eventName");
        if (eventName == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: Something went wrong parsing maxAttendees found in XML: ");
        }

        if (maxAttendees < 0) {

            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            allGood = false;

        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {

            System.out.println(" [!!!] ERROR: No description found in XML: ");
            allGood = false;

        }
        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {

            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            allGood = false;

        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {

            System.out.println(" [!!!] ERROR: No location found in XML: ");
            allGood = false;

        }
        contactPerson = getSafeXmlProperty(xmlMessage, "contactPerson");
        if (contactPerson == "false") {

            System.out.println(" [!!!] ERROR: No contactPerson found in XML: ");
            allGood = false;

        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            allGood = false;

        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            allGood = false;

        }
        type = getSafeXmlProperty(xmlMessage, "eventType");

        if (type == "false") {

            type = getSafeXmlProperty(xmlMessage, "type");

            if (type == "false") {

                System.out.println(" [!!!] ERROR: No type found in XML: ");
                allGood = false;
            }

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            System.out.println(" [!!!] ERROR: No price found in XML: " + e);
            allGood = false;
        }
        if (price < 0) {

            System.out.println(" [!!!] ERROR: Negative price found in XML: ");
            allGood = false;

        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion <= 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        eventObject = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, type, price, false);
        return eventObject;
    }


    // Reservation_Session: (params) => XML
    static String getXmlForNewReservation_Session(String xmlHeaderDescription, SourceType Source_type, String reservationUUID, String userUUID, String sessionUUID, float paid, int entityVersion) throws JAXBException {

        String messageType = "reservationSessionMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationSessionStructure reservationStructure = new XmlMessage.ReservationSessionStructure(reservationUUID, userUUID, sessionUUID, paid, entityVersion, 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationSessionMessage xmlReservationMessage = new XmlMessage.ReservationSessionMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Session: object => XML
    static String getXmlFromReservation_SessionObject(String xmlHeaderDescription, SourceType Source_type, Reservation_Session thisReservationObject) throws JAXBException {

        String messageType = "reservationSessionMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationSessionStructure reservationStructure = new XmlMessage.ReservationSessionStructure(thisReservationObject.getReservationUUID(), thisReservationObject.getUserUUID(), thisReservationObject.getSessionUUID(), thisReservationObject.getPaid(), 1, 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationSessionMessage xmlReservationMessage = new XmlMessage.ReservationSessionMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Session: XML => Object
    static Reservation_Session getReservation_SessionObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Reservation_Session reservationSessionObject = null;

        String reservationUUID = "false";
        String userUUID = "false";
//        String eventUUID = "false";
        String sessionUUID = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");
            if (reservationUUID == "false") {
                System.out.println(" [!!!] ERROR: No reservationUUID found in XML: ");
                allGood = false;
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {

            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
            allGood = false;

        }
        /*eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }*/
        sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUuid");
        if (sessionUUID == "false") {

            System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
            allGood = false;

        }

        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No paid found in XML: ");
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR(in tc): No entityVersion found in XML: ");
            allGood = false;
        }

        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active == 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        reservationSessionObject = new Reservation_Session(0, entityVersion, active, timestamp, reservationUUID, userUUID, sessionUUID, paid, false);

        return reservationSessionObject;
    }


    // Reservation_Event: (params) => XML
    static String getXmlForNewReservation_Event(String xmlHeaderDescription, SourceType Source_type, String reservationUUID, String userUUID, String eventUUID, float paid, int entityVersion) throws JAXBException {

        String messageType = "reservationEventMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationEventStructure reservationStructure = new XmlMessage.ReservationEventStructure(reservationUUID, userUUID, eventUUID, messageType.split("Message")[0], paid, entityVersion, 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationEventMessage xmlReservationMessage = new XmlMessage.ReservationEventMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Event: object => XML
    static String getXmlFromReservation_EventObject(String xmlHeaderDescription, SourceType Source_type, Reservation_Event thisReservationObject) throws JAXBException {

        String messageType = "reservationEventMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationEventStructure reservationStructure = new XmlMessage.ReservationEventStructure(thisReservationObject.getReservationUUID(), thisReservationObject.getUserUUID(), thisReservationObject.getEventUUID(), messageType.split("Message")[0], thisReservationObject.getPaid(), thisReservationObject.getEntityVersion(), 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationEventMessage xmlReservationMessage = new XmlMessage.ReservationEventMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Event: XML => Object
    static Reservation_Event getReservation_EventObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Reservation_Event reservationEventObject = null;

        String reservationUUID = "false";
        String userUUID = "false";
        String eventUUID = "false";
        String sessionUUID = "false";
        //String type = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");

            if (reservationUUID == "false") {

                reservationUUID = getSafeXmlProperty(xmlMessage, "UUID");

                if (reservationUUID == "false") {

                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML: ");

                    allGood = false;
                }
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {

            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
            allGood = false;

        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }
        /*sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUUID");
        if (sessionUUID == "false") {

            System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
            allGood = false;

        }*/

        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No paid found in XML: ");
            allGood = false;
        }
        /*
        type = getSafeXmlProperty(xmlMessage, "type");
        if (type == "false") {

            System.out.println(" [!!!] ERROR: No type found in XML: ");
            allGood = false;

        }
*/
        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        reservationEventObject = new Reservation_Event(0, entityVersion, active, timestamp, reservationUUID, userUUID, eventUUID, paid, false);

        return reservationEventObject;
    }


    // pingMessage:
    static void startSendingPingMessages(int millisBetweenPings, SourceType yourSourceType) {

        // ## make new pingSender object
        PingSender pingSender = new PingSender(0, yourSourceType, millisBetweenPings);

        // ## setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // ## start new pingSender thread
        pingThread.start();
    }

    static String getXmlForPingMessage(String messageType, SourceType Source_type) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, "", Source_type.toString());
        // set datastructure
        XmlMessage.PingStructure pingStructure = new XmlMessage.PingStructure();
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.PingMessage xmlPingMessage = new XmlMessage.PingMessage(header, pingStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlPingMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }


    static String getPropertyFromXml(String xml, String property) throws
            ParserConfigurationException, SAXException, IOException, NullPointerException {
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
        String value = "";
        try {

            value = getPropertyFromXml(xml, property);
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            //e.printStackTrace();
            System.out.println("ERROR: No '" + property + "' found in XML: " + e);
            value = "false";
        }
        return value;
    }

    // Mock Message functionality
    static boolean xmlMessageMocker() {

        String responseFromSender = "";
        String[] senderOptions = getOptions();

        //initialize possible variables
        boolean inputSucces = true;
        boolean continueMocking = true;
        int Entity_sourceId = 420;

        //preset most used variables for testing purposes
        String uuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
        String eventUuid = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String reservationUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String messageType = "TestMessage";
        String headerDescription = "Standard header description";
        String xmlTotalMessage = "<test>testertester</test>";

        EntityType Entity_type = EntityType.EMPTY;
        SourceType Source_type = SourceType.Planning;
        int Entity_version = 1;
        int maxAttendees = 50;
        float paid = 0;

        //preset objects

        User mockUser = null;
        Event mockEvent = null;
        Session mockSession = null;
        Reservation_Event mockReservation_Event = null;
        Reservation_Session mockReservation_Session = null;

        //preset new session variables
        String sessionName = "Session name test";
        String dateTimeStart = "30/05/2018 20:00:00";
        String dateTimeEnd = "31/05/2018 08:00:00";
        String speaker = "Mr. President";
        String local = "Oval office dept.1 Room 420";
        String description = "Description for Main case (2): create new session without UUID";
        String summary = "Summary for Main case (2): create new session without UUID";
        String type = "testType (please set it to something else before using this";
        String lastName = "Test last name";
        String firstName = "Test first name";
        String eventType = "MockerNoonEventType";
        String sessionType = "MockerNoonSessionType";


        System.out.println("Choose the message to mock:\n");

        System.out.println("[10.V] User: New with Uuid: 'fbea0671-1324-4f92-a0b4-cc6e56c537d7' ");
        System.out.println("[11.V] User: New with chosen Uuid: ");
        System.out.println("[12.x] User: New without Uuid: ");
        System.out.println("[15.V] User: Update with Uuid: 'fbea0671-1324-4f92-a0b4-cc6e56c537d7' ");

        System.out.println("[20.V] Event: New with Uuid: 'd5b0f1ea-a8db-4186-b3fe-f37654eebe65' ");
        System.out.println("[21.V] Event: New with chosen Uuid: ");
        System.out.println("[22.V] Event: New without Uuid: ");
        System.out.println("[25.V] Event: Update with Uuid: 'd5b0f1ea-a8db-4186-b3fe-f37654eebe65' ");

        System.out.println("[30.V] Session: New with Uuid: 'c1a89eff-0a22-454d-aecc-44c19c95c261' ");
        System.out.println("[31.V] Session: New with chosen Uuid: ");
        System.out.println("[32.V] Session: New without Uuid: ");
        System.out.println("[35.V] Session: Update with Uuid: 'c1a89eff-0a22-454d-aecc-44c19c95c261' ");

        System.out.println("[40.x] Reservation_Event: New with Uuid: '0b136ea0-19f3-42de-aff4-2b5ecf1b88cb' ");
        System.out.println("[41.x] Reservation_Event: New with chosen Uuid: ");
        System.out.println("[42.x] Reservation_Event: New without Uuid: ");
        System.out.println("[45.x] Reservation_Event: Update with Uuid: '0b136ea0-19f3-42de-aff4-2b5ecf1b88cb' ");

        System.out.println("[50.x] Reservation_Session: New with Uuid: '293c7ef1-d8e7-4393-a8ee-b89cd09f927b' ");
        System.out.println("[51.x] Reservation_Session: New with chosen Uuid: ");
        System.out.println("[52.x] Reservation_Session: New without Uuid: ");
        System.out.println("[55.x] Reservation_Session: Update with Uuid: '293c7ef1-d8e7-4393-a8ee-b89cd09f927b' ");


        System.out.print("\nChoose a number [0 to quit!]\n");

        // Get chosen number
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        System.out.print("You've chosen '" + choice + "' ...\n");

        switch (choice) {
            case "10":
                // User with UUID

                uuid = "fbea0671-1324-4f92-a0b4-cc6e56c537d7";
                System.out.print("You've chosen '" + choice + "': User with uuid '" + uuid + "' ...\n");

                System.out.println("Mocking user 'John Parker' ...");
                // 1. Preset variables
                headerDescription = "Mocking user message";
                // Source_type= ... ;
                lastName = "Parker";
                firstName = "John";
                String phoneNumber = "+(32) 499 88 77 33";
                String email = "mockedUser@mocker.com";
                String street = "MockedNamelaan";
                String houseNr = "420 Mock";
                String city = "Mockels";
                String postalCode = "4501 Mock";
                String country = "Mockelgium";
                String company = "JP Mocked";
                String userType = "VISITOR";
                int entityVersion = 1;
                int active = 1;
                String timestamp = getCurrentDateTimeStamp();

                // 1. Preset variables
                // Source_type= ... ;
                // 2. Form user object
                mockUser = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType, false);

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromUserObject(headerDescription, Source_type, mockUser);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "11":
                // User with chosen UUID

                System.out.print("You've chosen '" + choice + "': User with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // Set chosen firstName
                System.out.print("\nEnter the firstName to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                firstName = choice;

                // Set chosen firstName
                System.out.print("\nEnter the lastName to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                lastName = choice;

                System.out.println("Mocking user '" + firstName + " " + lastName + "' with uuid: '" + uuid + "' ... Other variables are preset in Main.java around line 666");

                // 1. Preset variables
                // Source_type= ... ;
                phoneNumber = "+(32) 499 88 77 33";
                email = "mockedUser@gmail.com";
                street = "MockedNamelaan";
                houseNr = "420 Mock";
                city = "Mockels";
                postalCode = "4501 Mock";
                country = "Mockelgium";
                company = "JP Mocked";
                userType = "VISITOR";
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form user object
                mockUser = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType, false);

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromUserObject(headerDescription, Source_type, mockUser);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }


                break;

            case "15":

                uuid = "fbea0671-1324-4f92-a0b4-cc6e56c537d7";
                System.out.print("You've chosen '" + choice + "': Update user with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                System.out.println("Updating user 'John Parker' ...");

                // 1. Preset variables
                headerDescription = "Mocking user message";
                // Source_type= ... ;
                lastName = "Parker";
                firstName = "John";
                phoneNumber = "+(32) 499 88 77 33";
                email = "mockedUserUpdatedEmail@mocker.com";
                street = "MockedNamelaan";
                houseNr = "420 Mock";
                city = "Mockels";
                postalCode = "4501 Mock";
                country = "Mockelgium";
                company = "JP Mocked";
                userType = "VISITOR";
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form user object
                mockUser = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType, false);

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromUserObject(headerDescription, Source_type, mockUser);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "20":
                // Event with UUID

                uuid = "d5b0f1ea-a8db-4186-b3fe-f37654eebe65";
                System.out.println("Mocking Event 'MockFest' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                headerDescription = "Mocking Event message";
                // Source_type= ... ;
                String eventName = "Mocked eventName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                String location = "Mocked location";
                String contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                eventType = "MockerNoon";
                float price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromEventObject(headerDescription, Source_type, mockEvent);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "21":
                // Event with chosen UUID

                System.out.println("You've chosen '" + choice + "': Event with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use:_ ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // Set chosen eventName
                System.out.print("\nEnter the eventName to use:_ ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                eventName = choice;

                // 1. Preset variables

                headerDescription = "Mocking Event message";
                // Source_type= ... ;
                //uuid="83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                //eventName = "Mocked eventName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                eventType = "MockerNoon";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                System.out.println("Mocking event '" + eventName + "' with uuid: '" + uuid + "' ... Other variables are preset in Main.java around line 800");

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                // 3. Form XML
                try {
                    xmlTotalMessage = getXmlFromEventObject(headerDescription, Source_type, mockEvent);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML
                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "22":
                // new event without UUID

                System.out.println("You've chosen '" + choice + "': New Event without UUID ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Event message";
                // Source_type= ... ;
                //uuid="83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                //eventName = "Mocked eventName";
                maxAttendees = 45;
                description = "Mocked Event description";
                summary = "Mocked Event summary";
                location = "Mocked Event location";
                contactPerson = "Mocked Event contactPerson";
//                dateTimeStart = "2018-05-28T09:00:00+02:00";
//                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                eventType = "EventType";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // Set chosen eventName
                System.out.print("\nEnter the new event's name: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                eventName = choice;

                // Set chosen DateTimeStart
                System.out.print("\nEnter the new event's start datetime (2018-05-01T09:00) ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                dateTimeStart = choice;

                // Set chosen DateTimeEnd
                System.out.print("\nEnter the new event's end datetime (2018-05-01T09:00) ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                dateTimeEnd = choice;

                String UuidInsertReturner = "";
                try {
                    //UuidInsertReturner = httpPostCreateUuidRecord(Entity_sourceId, Entity_type, Source_type);
                    UuidInsertReturner = httpPostCreateUuidRecord(50, EntityType.Event, SourceType.Planning);

                    UuidInsertReturner = UuidInsertReturner.substring(1, UuidInsertReturner.length() - 1);

                    //System.out.println("\nMessage From UUID server: " + UuidInsertReturner);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Error during http post request: createUuidRecord();");
                }

                // handle uuid response to get uuid out of it

                //UUID_insertUuidRecord obj = null;

                try {
                    Gson gson = new Gson();

                    UUID_insertUuidRecord firstTest = gson.fromJson(UuidInsertReturner, UUID_insertUuidRecord.class);

                    uuid = firstTest.getUuid();

                } catch (JSONException e) {

                    System.out.println("ERROR: " + e);

                }

                System.out.println("Mocking event '" + eventName + "' with uuid: '" + uuid + "' ... Other variables are preset in Helper.java around line 3000");

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                // 3. Form XML
                try {
                    xmlTotalMessage = getXmlFromEventObject(headerDescription, Source_type, mockEvent);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML
                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "25":
                // Event with UUID

                uuid = "d5b0f1ea-a8db-4186-b3fe-f37654eebe65";
                System.out.print("You've chosen '" + choice + "': Update Event with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Event message UPDATED";
                // Source_type= ... ;
                eventName = "'MockFest' UPDATED";
                maxAttendees = 45;
                description = "Mocked description UPDATED";
                summary = "Mocked summary UPDATED";
                location = "Mocked location UPDATED";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                eventType = "MockerNoon";
                price = 60;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                // 3. Form XML
                try {
                    xmlTotalMessage = getXmlFromEventObject(headerDescription, Source_type, mockEvent);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML
                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "30":
                // Session with UUID

                uuid = "c1a89eff-0a22-454d-aecc-44c19c95c261";
                System.out.println("Mocking Session 'MockSess' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                headerDescription = "Mocking Session message";
                // Source_type= ... ;
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                sessionName = "Mocked sessionName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                sessionType = "SessionMockerType";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Session;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockSession = new Session(0, entityVersion, active, timestamp, uuid, eventUuid, sessionName, maxAttendees, description, summary, dateTimeStart, dateTimeEnd, contactPerson, location, sessionType, price, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromSessionObject(headerDescription, Source_type, mockSession);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "31":
                //Session with chosen UUID

                System.out.print("You've chosen '" + choice + "': Session with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // Set chosen eventName
                System.out.print("\nEnter the sessionName to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                sessionName = choice;

                //uuid="e319f8aa-1910-442c-8b17-5e809d713ee4";
                //System.out.println("Mocking Session 'MockSess' with uuid: '"+uuid+"' ");

                // 1. Preset variables

                headerDescription = "Mocking Session message";
                // Source_type= ... ;
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee";
                //sessionName = "Mocked sessionName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked speaker";
                dateTimeStart = "2018-05-28T12:00:00+02:00";
                dateTimeEnd = "2018-05-29T14:00:00+02:00";
                sessionType = "SessionMockerType";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Session;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Session object
                mockSession = new Session(0, entityVersion, active, timestamp, uuid, eventUuid, sessionName, maxAttendees, description, summary, dateTimeStart, dateTimeEnd, contactPerson, location, sessionType, price, false);

                // 3. Form XML
                try {
                    xmlTotalMessage = getXmlFromSessionObject(headerDescription, Source_type, mockSession);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML
                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "32":
                // new session without UUID

                System.out.println("You've chosen '" + choice + "': New Session without UUID ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Session message";
                // Source_type= ... ;
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                //sessionName = "Mocked sessionName";
                maxAttendees = 45;
                description = "Mocked Session description";
                summary = "Mocked Session summary";
                location = "Mocked Session location";
                contactPerson = "Mocked Session speaker";
                dateTimeStart = "2018-05-28T12:00:00+02:00";
                dateTimeEnd = "2018-05-29T14:00:00+02:00";
                sessionType = "SessionMockedType";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Session;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // Set chosen sessionName
                System.out.print("\nEnter the new session's name: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                sessionName = choice;

                // Set chosen DateTimeStart
                System.out.print("\nEnter the new session's start datetime (2018-05-01T09:00) ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                dateTimeStart = choice;

                // Set chosen DateTimeEnd
                System.out.print("\nEnter the new session's end datetime (2018-05-01T09:00) ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                dateTimeEnd = choice;

                UuidInsertReturner = "";
                try {
                    //UuidInsertReturner = httpPostCreateUuidRecord(Entity_sourceId, Entity_type, Source_type);
                    UuidInsertReturner = httpPostCreateUuidRecord(50, EntityType.Session, SourceType.Planning);

                    UuidInsertReturner = UuidInsertReturner.substring(1, UuidInsertReturner.length() - 1);

                    //System.out.println("\nMessage From UUID server: " + UuidInsertReturner);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Error during http post request: createUuidRecord();");
                }

                // handle uuid response to get uuid out of it

                //obj = null;

                try {
                    Gson gson = new Gson();
                    UUID_insertUuidRecord firstTest = gson.fromJson(UuidInsertReturner, UUID_insertUuidRecord.class);

                    uuid = firstTest.getUuid();

                } catch (JSONException e) {
                    //e.printStackTrace();
                    System.out.println(e);
                    System.out.println("uuid: " + uuid);
                }

                System.out.println("Mocking session '" + sessionName + "' with uuid: '" + uuid + "' ... Other variables are preset in Helper.java around line 3250");

                // 2. Form Session object
                mockSession = new Session(0, entityVersion, active, timestamp, uuid, eventUuid, sessionName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, sessionType, "", "", price, false);

                // 3. Form XML
                try {
                    xmlTotalMessage = getXmlFromSessionObject(headerDescription, Source_type, mockSession);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                // 4. Send XML
                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "35":
                // Session with UUID

                uuid = "c1a89eff-0a22-454d-aecc-44c19c95c261";

                System.out.print("You've chosen '" + choice + "': Update Session with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Session message";
                // Source_type= ... ;
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                sessionName = "Mocked sessionName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                sessionType = "SessionMockerType";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Session;
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockSession = new Session(0, entityVersion, active, timestamp, uuid, eventUuid, sessionName, maxAttendees, description, summary, dateTimeStart, dateTimeEnd, contactPerson, location, sessionType, price, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromSessionObject(headerDescription, Source_type, mockSession);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;


            case "40":
                // Reservation_Event with UUID

                uuid = "0b136ea0-19f3-42de-aff4-2b5ecf1b88cb";
                System.out.println("Mocking Reservation_Event 'REMOCK' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message";
                // Source_type= ... ;
                String userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "41":
                //Reservation_Event with chosen UUID

                System.out.print("You've chosen '" + choice + "': Reservation_Event with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message with chosen uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "42":
                // new Reservation_Event without UUID

                System.out.println("You've chosen '" + choice + "': New Reservation_Event without UUID ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message without uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();


                UuidInsertReturner = "";
                try {
                    //UuidInsertReturner = httpPostCreateUuidRecord(Entity_sourceId, Entity_type, Source_type);
                    UuidInsertReturner = httpPostCreateUuidRecord(50, Entity_type, Source_type);

                    UuidInsertReturner = UuidInsertReturner.substring(1, UuidInsertReturner.length() - 1);

                    //System.out.println("\nMessage From UUID server: " + UuidInsertReturner);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Error during http post request: createUuidRecord();");
                }

                // handle uuid response to get uuid out of it

                //obj = null;

                try {
                    Gson gson = new Gson();
                    UUID_insertUuidRecord firstTest = gson.fromJson(UuidInsertReturner, UUID_insertUuidRecord.class);

                    uuid = firstTest.getUuid();

                } catch (JSONException e) {
                    //e.printStackTrace();
                    System.out.println(e);
                    System.out.println("uuid: " + uuid);
                }

                System.out.println("Mocking Reservation_Event with uuid: '" + uuid + "' ... Other variables are preset in Helper.java around line 3750");


                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "45":
                // Reservation_Event with UUID

                uuid = "0b136ea0-19f3-42de-aff4-2b5ecf1b88cb";

                System.out.print("You've chosen '" + choice + "': Update Reservation_Event with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event UPDATE message with uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 20.42f;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();


                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, entityVersion, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;


            case "50":
                // Reservation_Session with UUID

                uuid = "0b136ea0-19f3-42de-aff4-2b5ecf1b88cb";
                System.out.println("Mocking Reservation_Session 'REMOCK' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                String sessionUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Event object

                mockReservation_Session = new Reservation_Session(0, entityVersion, 1, getCurrentDateTimeStamp(), uuid, userUuid, sessionUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "51":
                //Reservation_Event with chosen UUID

                System.out.print("You've chosen '" + choice + "': Reservation_Event with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message with chosen uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;

            case "52":
                // new Reservation_Event without UUID

                System.out.println("You've chosen '" + choice + "': New Reservation_Event without UUID ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event message without uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();


                UuidInsertReturner = "";
                try {
                    //UuidInsertReturner = httpPostCreateUuidRecord(Entity_sourceId, Entity_type, Source_type);
                    UuidInsertReturner = httpPostCreateUuidRecord(50, Entity_type, Source_type);

                    UuidInsertReturner = UuidInsertReturner.substring(1, UuidInsertReturner.length() - 1);

                    //System.out.println("\nMessage From UUID server: " + UuidInsertReturner);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Error during http post request: createUuidRecord();");
                }

                // handle uuid response to get uuid out of it

                //obj = null;

                try {
                    Gson gson = new Gson();
                    UUID_insertUuidRecord firstTest = gson.fromJson(UuidInsertReturner, UUID_insertUuidRecord.class);

                    uuid = firstTest.getUuid();

                } catch (JSONException e) {
                    //e.printStackTrace();
                    System.out.println(e);
                    System.out.println("uuid: " + uuid);
                }

                System.out.println("Mocking Reservation_Event with uuid: '" + uuid + "' ... Other variables are preset in Helper.java around line 3750");


                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }

                break;

            case "55":

                // Reservation_Event with UUID

                uuid = "0b136ea0-19f3-42de-aff4-2b5ecf1b88cb";

                System.out.print("You've chosen '" + choice + "': Update Reservation_Event with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Event UPDATE message with uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 20.42f;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();


                // 2. Form Reservation_Event object

                mockReservation_Event = new Reservation_Event(0, entityVersion, 1, getCurrentDateTimeStamp(), uuid, userUuid, eventUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_EventObject(headerDescription, Source_type, mockReservation_Event);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

                //System.out.println("xmlTotalMessage toString(): \n"+xmlTotalMessage);

                // 4. Send XML

                try {
                    Sender.sendMessage(xmlTotalMessage);
                } catch (TimeoutException | IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;


            case "0":

                System.out.println("You've chosen '" + choice + "': quiting this mocking session!");
                continueMocking = false;
                break;

            default:

                System.out.println("You've chosen '" + choice + "' but this one doesn't seem to have a case ! (around line 3350 in Helper.java)\n");

                break;
        }
        return continueMocking;
    }


    //https://stackoverflow.com/a/8345074
    static String getCurrentDateTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        return sdf.format(date) + "T" + sdf2.format(date);
    }

}


/*
old code:

    static String getOurXmlMessage(String messageType, String description, SourceType Source_type, String UUID) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.MessageStructure messageStructure = new XmlMessage.MessageStructure(UUID, "1", messageType, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.MessageMessage xmlReservationMessage = new XmlMessage.MessageMessage(header, messageStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

=======*/
