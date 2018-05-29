package AppLogic;

//import AppLogic.Sender;

import DatabaseLogic.*;
import GoogleCalendarApi.*;
import JsonMessage.JSONException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
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

    enum EntityType {EMPTY, User, Event, Session, ReservationEvent, ReservationSession, Product, Purchase, Task, AssignTask}

    enum SourceType {Front_End, Planning, Monitor, Kassa, CRM, Facturatie}

    //String TASK_QUEUE_NAME = "planning-queue";
    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;
    SourceType Source_type = SourceType.Planning;

    //For setting mainCLI options in main
    static String[] getOptions() {

        //Add CLI options here
        String[] options = {
                "[01.V] Create new User without UUID (Front-End Call)",
                "[02.V] Create new Event without UUID (Front-End Call)",
                "[03.V] Create new Session without UUID (Front-End Call)",
                "[04.V] Create new Reservation_Event: Add User to Event",
                "[05.V] Create new Reservation_Session: Add User to Session",
                "[06.V] Get all UUID's from UUID manager",
                "[07.V] updateUuidRecordVersion",
                "[08.V] updateUuidRecordVersionB",
                "[09.V] Google Calendar Api",
                "[10.V] Mock XML message",
                "[11.V] Start receiver",
                "[12.x]",
                "[13.x] /New Session with UUID",
                "[14.x] /New Reservation_Session with UUID",
                "[15.V] Testing message by sending to test queue ",
                "[16.x] Create new taskMessage and assignTaskMessage, update taskMessage and update assignTaskMessage"

        };
        return options;
    }
    // For setting xml messages to mock
    static String[] getXmlMessageOptions() {

        String[] options = {
                "[10.V] User: New with Uuid: 'fbea0671-1324-4f92-a0b4-cc6e56c537d7' ",
                "[11.V] User: New with chosen Uuid: ",
                "[12.x] User: New without Uuid: ",
                "[15.V] User: Update with Uuid: 'fbea0671-1324-4f92-a0b4-cc6e56c537d7' ",
                "[20.V] Event: New with Uuid: 'd5b0f1ea-a8db-4186-b3fe-f37654eebe65' ",
                "[21.V] Event: New with chosen Uuid: ",
                "[22.V] Event: New without Uuid: ",
                "[25.V] Event: Update with Uuid: 'd5b0f1ea-a8db-4186-b3fe-f37654eebe65' ",
                "[30.V] Session: New with Uuid: 'c1a89eff-0a22-454d-aecc-44c19c95c261' ",
                "[31.V] Session: New with chosen Uuid: ",
                "[32.V] Session: New without Uuid: ",
                "[35.V] Session: Update with Uuid: 'c1a89eff-0a22-454d-aecc-44c19c95c261' ",
                "[40.x] Reservation_Event: New with Uuid: '0b136ea0-19f3-42de-aff4-2b5ecf1b88cb' ",
                "[41.x] Reservation_Event: New with chosen Uuid: ",
                "[42.x] Reservation_Event: New without Uuid: ",
                "[45.x] Reservation_Event: Update with Uuid: '0b136ea0-19f3-42de-aff4-2b5ecf1b88cb' ",
                "[50.x] Reservation_Session: New with Uuid: '293c7ef1-d8e7-4393-a8ee-b89cd09f927b' ",
                "[51.x] Reservation_Session: New with chosen Uuid: ",
                "[52.x] Reservation_Session: New without Uuid: ",
                "[55.x] Reservation_Session: Update with Uuid: '293c7ef1-d8e7-4393-a8ee-b89cd09f927b' "

        };
        return options;
    }
    // for setting google calendar api options
    static String[] getGoogleCalendarApiOptions() {

        //Add CLI options here
        String[] options = {
                "[01.V] GCA: List upcoming events",
                "[02.V] GCA: Create new dummy event",
                "[03.V] GCA: Create new event with mocked variables",
                "[04.V] GCA: Create new event with some chosen variables",
                "[05.V] GCA: Creating new event, updating it (with preset variables)",
                "[06.V] GCA: Cancel event by event and a chosen GCAEventId",
                "[07.V] GCA: Cancel event by chosen GCAEventId ",
                "[08.x] GCA: Cancel event by chosen Uuid ",
                "[09.V] GCA: Add user to event by GCAEventId ",
                "[10.V] GCA: Delete user from event by GCAEventId ",
                "[11.x] GCA: Delete events from current user (according to client_secret.json) user from event by GCAEventId "
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

                // 09. Google Calendar Api Calls
                case "9":

                    boolean continueCalendarApi = true;
                    int counter = 1;
                    while (continueCalendarApi) {
                        // CLI message

                        System.out.println("\nCase '" + choice + "': Mock Google Calendar Api Call nr." + counter + "!\n");
                        continueCalendarApi = googleCalendarApiMocker();
                        counter++;

                    }


                    break;
                // 10. Mock XML message
                case "10":

                    boolean continueMocking = true;
                    counter = 1;
                    while (continueMocking) {
                        // CLI message
                        System.out.println("\nCase '" + choice + "': Mock XML message nr." + counter + "!");
                        continueMocking = xmlMessageMocker();
                        counter++;
                    }

                    break;

                // 11. Start receiver locally

                case "11":

                    // start different thread for database check
                    try{
                        // # Send pingmessage every 'timeBetweenPings' milliseconds
                        int timeBetweenChecks = 5000;

                        // ## make new pingSender object
                        DatabaseBackupChecker myDatabaseBackupChecker = new DatabaseBackupChecker(5000, 1000);

                        // ## setup new pingSender thread
                        Thread dbCheckThread = new Thread(myDatabaseBackupChecker);

                        // ## start new pingSender thread
                        dbCheckThread.start();

                    }catch(Exception e)
                    {
                        System.out.println("Error during backupDbChecker thread startup: "+e);
                    }


                    try {
                        Receiver.startReceiver();
                        System.out.print("Listening for 3000 seconds... ");

                        for (int i = 1; i <= 100; i++) {
                            for(int j = 1; j<=6;j++){

                                Thread.sleep(5000);
                            }
                            System.out.print(3000 - i * 30 + " ... ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // 12.
                case "12":

                    System.out.println("\nCase " + choice + ": Not worked out yet!");

                    break;
                // 13. Create new Session with UUID (insertUuidRecord,SessionMessage)
                // normally when a new message from another team is received
                case "13":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = EntityType.Session;
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

                case "15":

                    System.out.println("\nCase " + choice + ": Testing message by sending to test queue: '");

                    // 1. Form XML testMessage

                    String xmlMessage = "";
                    try {
                        xmlMessage = getXmlForTestMessage("testMessage", SourceType.Planning);
                    } catch (JAXBException e) { e.printStackTrace(); }

                    // 2. send xml message to monitor-queue


                    try {
                        Sender.sendTestMessage(xmlMessage);
                    } catch (IOException e) { e.printStackTrace(); }
                    catch (TimeoutException e) { e.printStackTrace(); }
                    break;

                    //"[16.x] Create new taskMessage and assignTaskMessage, update taskMessage and assignTaskMessage"
                case "16":

                    System.out.println("\nCase " + choice + ": Create new taskMessage and assignTaskMessage: '");

                    String taskUuid = "taskuuid-4203-4xxb-bace-7planningxx7", assignTaskUuid = "xassignx-task-4203-8planningxx7";

                    String userUuid="83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                    eventUuid = "11ff6e8f-aef4-4f2b-b6e6-f3dae7e116ce";


                    System.out.println("Creating task and assigntask, sleeping for 5 seconds...\n");
                    // 1. Form XML taskMessage

                    String xmlTaskMessage = "";
                    try {
                        xmlTaskMessage = getXmlForNewTask("Header description for test new task Message",SourceType.Planning,taskUuid,eventUuid,"Task Name: Task description","2018-06-01T00:00","2018-07-01T00:00",1,1);
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    // 2. send XML taskMessage to planning-queue

                    try {
                        Sender.sendMessage(xmlTaskMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    // 3. Form XML assignTaskMessage

                    String assignTaskMessage = "";
                    try {
                        xmlTaskMessage = getXmlForNewAssign_Task("Header description for test new assignTaskMessage",SourceType.Planning,assignTaskUuid,userUuid,taskUuid,1);
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    // 4. send XML assignTaskMessage to planning-queue
                    try {
                        Sender.sendMessage(xmlTaskMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println("Created task and assigntask, sleeping for 10 seconds...\n");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Updating task and assigntask...\n");
                    // 5. Form XML update taskMessage

                    String xmlTaskUpdateMessage = "";
                    try {
                        xmlTaskUpdateMessage = getXmlForNewTask("Header description for test update task Message",SourceType.Planning,taskUuid,eventUuid,"Task Name: Task description v2","2018-06-05T00:00","2018-07-05T00:00",2,1);
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    // 6. send XML update taskMessage to planning-queue

                    try {
                        Sender.sendMessage(xmlTaskUpdateMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    // 7. Form XML update assignTaskMessage

                    String assignTaskUpdateMessage = "";
                    try {
                        assignTaskUpdateMessage = getXmlForNewAssign_Task("Header description for test update assignTaskMessage",SourceType.Planning,assignTaskUuid,userUuid,taskUuid,2);
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    // 2. send XML assignTaskMessage to planning-queue
                    try {
                        Sender.sendMessage(assignTaskUpdateMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Updated task and assigntask...\n");

                    try {
                        System.out.println("Created task and assigntask, sleeping for 10 seconds...\n");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case "17":
                    //String xmlHeaderDescription, SourceType Source_type, String timestamp;

                    // 5. Parse error message to xml String
                    xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);

                    // 6. Send new object to rabbitExchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException | IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "":
                    System.out.println("\nReceived 'Empty' (\"\") String as option...!");
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


    // HANDLERS
    static int handleNewMessage(String task, int workCounter) throws JAXBException, IOException, ParserConfigurationException, SAXException, Exception {

        String errorMessage = "";
        String xmlTotalMessage = "";

        //change to true to show full XML message in receiver console when it's received
        boolean showFullXMLMessage = true, isPingMessage = false;

        String messageType = null;
        messageType = getSafeXmlProperty(task, "messageType");
        if (messageType == "false") {
            messageType = getSafeXmlProperty(task, "MessageType");
            if (messageType == "false") {
                System.out.println("[!!!] ERROR: No messageType found in XML...");
                errorMessage += " [!!!] ERROR: No messageType found in XML...\n";
            }
        }


        if (messageType.equals("pingMessage")) {

            showFullXMLMessage = false;
            isPingMessage = true;
        }


        // get Source from XML (set in sender)
        String messageSource = null;
        messageSource = getSafeXmlProperty(task, "source");
        if (messageSource == "false") {
            messageType = getSafeXmlProperty(task, "Source");
            if (messageType == "false") {
                System.out.println(" [!!!] ERROR: No source found in XML...");
                errorMessage += " [!!!] ERROR: No source found in XML...\n";
            }
        }

        // Check XML for message

        workCounter++;

        if (!isPingMessage) {
            System.out.println("_________________________________________________________________________________");
            System.out.println("_________________________START OF KUTMESSAGE________________________________________");
            System.out.println("* [.i.] [NEW MESSAGE]: " + workCounter + " [TYPE]: '" + messageType + "' [FROM]: '" + messageSource + "' [.i.] ");
            //System.out.println("* [.i.] ********* [TYPE]: '" + messageType + "' [FROM]: '" + messageSource + "' ****** [.i.] *");
            System.out.println("* [.i.] ** [@] '" + getCurrentDateTimeStamp() + "' [MESSAGELENGTH]: '" + task.length() + "' characters ** [.i.] ");

            //System.out.println("*[.i.] with length: '" + task.length() + "' characters [.i.]");
            System.out.println("_________________________________________________________________________________");
            System.out.println("Message content:");
        }

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

            case "reservationMessage":
                System.out.print("Trying to recover from incorrect 'reservationMessage' from '" + messageSource + "'!");
                String eventUuid = getSafeXmlProperty(task, "eventUuid");
                if (eventUuid == "false" || eventUuid == "") {
                    String sessionUuid = getSafeXmlProperty(task, "sessionUuid");
                    if (sessionUuid == "false" || sessionUuid == "") {
                        System.out.print("Failed to recover...");
                        errorMessage += "Failed to recover...\n";
                    } else {
                        //reservationsession message
                        System.out.print("Trying to recover... This seems to be a reservation for a Session...");
                        handleNewMessageReservationSession(task);
                    }
                } else {
                    //reservationevent message
                    System.out.print("Trying to recover... This seems to be a reservation for an Event...");
                    handleNewMessageReservationEvent(task);
                }
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
                    System.out.println(" [!!!] ERROR: No source found in XML...\n"+e.toString());
                    errorMessage += " [!!!] ERROR: No source found in XML...\n"+e.toString()+"\n";
                    //e.printStackTrace();
                }

                // 2. send xml message to monitor-queue
                String returnedMessage = "";
                try {
                    returnedMessage = Sender.sendPingMessage(xmlMessage, SourceType.Planning);
                } catch (IOException | TimeoutException | JAXBException e) {
                    System.out.println(" [!!!]Failed to execute Sender.sendPingMessage: " + e.toString());
                   errorMessage += " [!!!]Failed to execute Sender.sendPingMessage: " + e.toString() + "\n";
                    //e.printStackTrace();
                }

                System.out.print("\n... PING @ [" + Helper.getCurrentDateTimeStamp() + "]...");

                showFullXMLMessage = false;
                break;

            case "errormessage":
                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));
                break;

            case "taskmessage":
                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));
                handleNewMessageTask(task);
                break;

            case "assigntaskmessage":
                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));
                handleNewMessageAssignTask(task);
                break;

            case "productmessage":
            case "purchasemessage":
            case "invoicerequestmessage":
            case "creditnotemessage":
            case "invitemessage":
            case "invoicemessage":

                showFullXMLMessage = false;
                System.out.println(" [" + messageType + "] Received from " + getSafeXmlProperty(task, "source"));

                break;
            case "":
            default:

                System.out.println(" [.!.] ERROR: Message type NOT recognized: '" + messageType + "' ...");
                errorMessage += " [.!.] ERROR: Message type NOT recognized: '" + messageType + "' ... \n";
                break;
        }

        if (showFullXMLMessage && !isPingMessage) {
            System.out.println("\n [.i.] Full received message:\n\n -*- START OF TASK -*-\n");
            System.out.println(task);
            System.out.println("\n -*- END OF TASK -*-\n");
        } else {
            System.out.println("\n [.!.] XML not shown... Change boolean 'showFullXMLMessage' to show this.\n");
        }


        if (errorMessage == "") {
            if(!isPingMessage) {
                System.out.println("\n [.V.] No error message sent.");
            }
        } else {
            System.out.println("\n [.!.] Error-message sent.");
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;

            SourceType Source_type = SourceType.Planning;

            // 5. Parse user object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("\nERROR: parser for getXmlForErrorMessage:\n");
                e.printStackTrace();
            }

            // 6. Send
            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("\nERROR: message sender for errorMessage:\n");
                e.printStackTrace();
            }
        }

        if(!isPingMessage) {
            System.out.println("__________________________END OF MESSAGE______________________________________");
            System.out.println("_________________________________________________________________________________");
            //System.out.println(" [.i.] END OF WORK [************************************************************************]");
        }else{
            System.out.println(".pINg.");
        }
        return workCounter;
    }
    //Handler for usermessage
    static void handleNewMessageUser(String task) {

        String userUuid = "";
        User thisUserInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.User;
        SourceType Source_type = SourceType.Planning;
        String xmlTotalMessage = "";
        String errorMessage = "";

        // 1. transform xml to user-object
        try {
            thisUserInMessage = getUserObjectFromXmlMessage(task);
            userUuid = thisUserInMessage.getUuid();
            System.out.println("New message for [User] with Uuid:" + userUuid);

        } catch (Exception e) {
            System.out.println(" [!!!] ERROR: handleNewMessageUser - 1. transform xml to user-object :\n" + e.toString());
            errorMessage += " [!!!] ERROR: handleNewMessageUser - 1. transform xml to user-object :\n" + e.toString() + "\n";
            //e.printStackTrace();
        }

        if (thisUserInMessage != null) {

            // 2.1 check if UUID exists in local db

            uuidExists = false;
            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("User", userUuid);
            } catch (Exception e) {
                System.out.println(" [!!!] ERROR: handleNewMessageUser - 2. Check if UUID exists in local db :\n" + e.toString());
                errorMessage += " [!!!] ERROR: handleNewMessageUser - 2. Check if UUID exists in local db :\n" + e.toString() + "\n";
                //e.printStackTrace();
            }

            // 2.1.1 check if user is to be 'deleted'

            if (uuidExists) {

                System.out.println("Uuid already exists in our PlanningDB table:[User]\n");

                // 2.2.1. get idUser from userUUID in User
                String[] propertiesToSelect = {"idUser"};
                String table = "User";
                String[] selectors = {"uuid"};
                String[] values = {"" + userUuid};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
                if (thisUserInMessage.getActive() == 0) {

                    boolean allGood = true;

                    // 1. Perform soft-delete on local db
                    int alterThisEntityId = 0;
                    try {
                        alterThisEntityId = Integer.parseInt(selectResults[0]);
                    } catch (NumberFormatException e) {
                        System.out.println(" [!!!] ERROR: handleNewMessageUser - alterThisEntityId = Integer.parseInt(selectResults[0]); FAIL :\n" + e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageUser - alterThisEntityId = Integer.parseInt(selectResults[0]); FAIL :\n" + e.toString() + "\n";
                        //e.printStackTrace();
                        allGood = false;
                    }
                    try {
                        new BaseEntityDAO().softDeleteBaseEntity(alterThisEntityId);
                        System.out.println("SoftDelete executed on [User] with entityId: " + alterThisEntityId + "\n");
                    } catch (Exception e) {
                        System.out.println(" [!!!] ERROR: handleNewMessageUser - softDelete FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageUser - softDelete FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }

                    // 2. update local database entity version
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + thisUserInMessage.getEntityVersion(), "int", "idBaseEntity", "" + alterThisEntityId);
                    } catch (Exception e) {
                        System.out.println("ERROR in 3. update entity version (on our database) :\n " + e.toString());
                        errorMessage += "ERROR in 3. update entity version (on our database) :\n" + e.toString() +"\n";
                        //e.printStackTrace();
                        allGood = false;
                    }

                } else {

                    // 2.2.2. get the entityVersion from idUser
                    propertiesToSelect[0] = "entity_version";
                    table = "BaseEntity";
                    selectors[0] = "idBaseEntity";
                    values[0] = selectResults[0];

                    int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                    //System.out.println("localEntityVersion: "+localEntityVersion + " & thisUserInMessage.getEntityVersion():  "+thisUserInMessage.getEntityVersion());

                    try {
                        thisUserInMessage.setEntityId(Integer.parseInt(selectResults[0]));
                        //System.out.println("thisUserInMessage.getIdUser(): " + thisUserInMessage.getIdUser());
                    } catch (NumberFormatException e) {
                        System.out.println("get the entityVersion from idUser FAILED\n"+e.toString());
                        errorMessage += "get the entityVersion from idUser FAILED\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }

                    boolean allGood = true;

                    if (localEntityVersion < thisUserInMessage.getEntityVersion()) {

                        // 2.3.2. update record in our local db

                        try {
                            allGood = new User_DAO().updateUserByObject(thisUserInMessage);
                        } catch (Exception e) {
                            errorMessage +=" [!!!] ERROR: updating user in the database FAILED:\n" + e.toString() + "\n";
                            System.out.println(" [!!!] ERROR: updating user in the database FAILED: " + e.toString());
                            //e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)

                        int updateUuidRecordVersionResponse = 0;
                        if (allGood) {
                            try {
                                updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, userUuid);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                errorMessage += " [!!!] ERROR: updateUuidRecordVersion FAILED \n" + e.toString() + "\n";
                                System.out.println(" [!!!] ERROR: updateUuidRecordVersion FAILED \n" + e.toString());
                            }
                        } else {
                            errorMessage +=" [!!!] ERROR: updatingUserByObject FAILED\n";
                            System.out.println(" [!!!] ERROR: updatingUserByObject FAILED");
                        }
                        System.out.println("We had this [User] with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisUserInMessage.getEntityVersion() + "'");

                    } else {
                        System.out.println("We already had this [User] with entityVersion: '" + localEntityVersion + "'");
                    }
                }

            } else {
                // New user record

                // 2.4.1. insert new user into local db

                int messageUserInsertReturner = 0;
                try {
                    messageUserInsertReturner = new User_DAO().insertIntoUser(thisUserInMessage);
                } catch (SQLException e) {
                    errorMessage += " [!!!] ERROR: inserting [User] into the database:\n" + e.toString() + "\n";
                    System.out.println(" [!!!] ERROR: inserting [User] into the database:\n" + e.toString());
                }
                //System.out.println("User.toString(): "+thisUserInMessage.toString());

                // 2.4.2. insertUuidRecord

                try {
                    Sender.insertUuidRecord("", messageUserInsertReturner, thisEntityType, Source_type, userUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    errorMessage += " [!!!] ERROR: insertUuidRecord FAILED:\n" + e.toString() + "\n";
                    System.out.println(" [!!!] ERROR: insertUuidRecord FAILED:\n" + e.toString());
                    //e.printStackTrace();
                }
                System.out.println("Inserted new user record with id='" + messageUserInsertReturner + "' and uuid='" + userUuid + "'");


            }
        }
        if (errorMessage == "") {
            System.out.println("\n [.i.] No error-message sent.");

        } else {
            System.out.println("\n [.!.] Error-message sent.");
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;

            // 5. Parse user object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("\nERROR: parser for getXmlForErrorMessage:\n");
                e.printStackTrace();
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("\nERROR: message sender for errorMessage:\n");
                e.printStackTrace();
            }
        }
    }
    //Handler for eventmessage
    static void handleNewMessageEvent(String task) {

        String eventUuid = "";
        Event thisEventInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.Event;
        SourceType Source_type = SourceType.Planning;
        String xmlTotalMessage = "";
        String errorMessage = "";

        // 1. transform xml to event-object
        try {
            thisEventInMessage = getEventObjectFromXmlMessage(task);
            eventUuid = thisEventInMessage.getEventUUID();
            System.out.println("New message for [Event] with Uuid: " + eventUuid);

        } catch (Exception e) {
            System.out.println(" [!!!] ERROR: handleNewMessageEvent - 1. transform xml to event-object :\n" + e.toString());
            errorMessage += " [!!!] ERROR: handleNewMessageEvent - 1. transform xml to event-object :\n" + e.toString() + "\n";
            //e.printStackTrace();
        }

        if (thisEventInMessage != null) {

            // 2.1. check if UUID exists in local db
            uuidExists = false;
            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("Event", eventUuid);
            } catch (Exception e) {
                System.out.println(" [!!!] ERROR: handleNewMessageEvent - 2. Check if UUID exists in local db :\n" + e.toString());
                errorMessage += " [!!!] ERROR: handleNewMessageEvent - 2. Check if UUID exists in local db :\n" + e.toString() + "\n";
                //e.printStackTrace();
            }

            if (uuidExists) {

                System.out.println("Uuid already exists in our PlanningDB table:[Event]");

                // 2.2 Session record update
                // 2.2.1. get idEvent from eventUUID in Event
                String[] propertiesToSelect = {"idEvent"};
                String table = "Event";
                String[] selectors = {"uuid"};
                String[] values = {"" + eventUuid};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
                if (thisEventInMessage.getActive() == 0) {

                    // 1. Cancel Google Calendar event
                    try {
                        GoogleCalenderApi.cancelEventWithEventObject(thisEventInMessage);
                    } catch (Exception e){
                        System.out.println(" [!!!] ERROR: handleNewMessageEvent - cancleEventGCA FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageEvent - cancleEventGCA FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }

                    // 2. Perform soft-delete on local db
                    try {
                        new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                        System.out.println("SoftDelete executed on [Event] with entityId: " + Integer.parseInt(selectResults[0]) + "\n");
                    } catch (Exception e){
                        System.out.println(" [!!!] ERROR: handleNewMessageEvent - softDelete FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageEvent - softDelete FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }


                    // 3. updateUuidRecordVersion() (To UUID master)
                    int updateUuidRecordVersionResponse = 0;
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, eventUuid);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        System.out.println(" [!!!] ERROR: handleNewMessageEvent - updateUuidRecordVersion FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageEvent - updateUuidRecordVersion FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }
                } else {

                    // 2.2.2. get entityVersion from id in BaseEntity
                    propertiesToSelect[0] = "entity_version";
                    table = "BaseEntity";
                    selectors[0] = "idBaseEntity";
                    values[0] = selectResults[0];
                    int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                    try {
                        thisEventInMessage.setEntityId(Integer.parseInt(selectResults[0]));
                        //System.out.println("thisEventInMessage.getEventId(): " + thisEventInMessage.getEventId());
                    } catch (NumberFormatException e) {
                        System.out.println(" [!!!] ERROR: handleNewMessageEvent - setEntityId FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageEvent - setEntityId FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }

                    boolean allGood = true;


                    if (localEntityVersion < thisEventInMessage.getEntityVersion()) {

                        // 2.3.1. update google calender through API
                        try {
                            GoogleCalenderApi.updateEventWithEventObject(thisEventInMessage);
                        } catch (Exception e) {
                            System.out.println(" [!!!] ERROR: updating GCA Event with Event object:\n"+e.toString());
                            errorMessage += " [!!!] ERROR: updating GCA Event with Event object:\n"+e.toString()+"\n";
                            //e.printStackTrace();
                        }

                        // 2.3.2. update record in our local db
                        int updateUuidRecordVersionResponse = 0;
                        try {
                            allGood = new Event_DAO().updateEventByObject(thisEventInMessage);
                        } catch (Exception e) {
                            System.out.println(" [!!!] ERROR: updating event in the database:\n"+e.toString());
                            errorMessage += " [!!!] ERROR: updating event in the database:\n"+e.toString()+"\n";
                            //e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)
                        if (allGood) {
                            try {
                                updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, eventUuid);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                System.out.println(" [!!!] ERROR: updateUuidRecordVersion:\n"+e.toString());
                                errorMessage += " [!!!] ERROR: updateUuidRecordVersion:\n"+e.toString()+"\n";
                                //e.printStackTrace();
                            }
                        } else {
                            System.out.println(" [!!!] ERROR: updateUuidRecordVersion:");
                            errorMessage += " [!!!] ERROR: updateUuidRecordVersion:\n";
                        }

                        System.out.println("We had this [Event] with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisEventInMessage.getEntityVersion() + "'");

                    } else {
                        // we have the latest version...
                        System.out.println("We already had this [Event] with entityVersion: '" + localEntityVersion + "'");
                    }
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
                    System.out.println(" [!!!] ERROR:  adding event to Google calendar API:\n" + e.toString());
                    errorMessage += " [!!!] ERROR:  adding event to Google calendar API:\n" + e.toString() + "\n";
                    //e.printStackTrace();
                }
                // 2.4.2. insert new event into local db
                int messageEventInsertReturner = 0;
                try {

                    messageEventInsertReturner = new Event_DAO().insertIntoEvent(thisEventInMessage);
                } catch (SQLException e) {
                    System.out.println(" [!!!] ERROR:  inserting event into the database:\n" + e.toString());
                    errorMessage += " [!!!] ERROR:  inserting event into the database:\n" + e.toString() + "\n";
                    //e.printStackTrace();
                }

                // 2.4.3. insertUuidRecord
                try {
                    Sender.insertUuidRecord("", messageEventInsertReturner, thisEntityType, Source_type, eventUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    System.out.println(" [!!!] ERROR:  inserting record into the Uuid Master:\n" + e.toString());
                    errorMessage += " [!!!] ERROR:  inserting record into the Uuid Master:\n" + e.toString() + "\n";
                    //e.printStackTrace();
                }

                //System.out.println("Event.toString(): " + thisEventInMessage.toString());

                System.out.println("Inserted new event record with id='" + messageEventInsertReturner + "' and UUID='" + eventUuid + "'");


            }
        } else {
            System.out.println("Something went wrong getting event object from xml message!");
        }

        if (errorMessage == "") {
            System.out.println("\n [.i.] No error-message sent.");

        } else {
            System.out.println("\n [.!.] Error-message sent.");
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;

            // 5. Parse user object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("\nERROR: parser for getXmlForErrorMessage:\n");
                e.printStackTrace();
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("\nERROR: message sender for errorMessage:\n");
                e.printStackTrace();
            }
        }

    }

    //Handler for sessionmessage
    static void handleNewMessageSession(String task) {

        String sessionUuid = "";
        Session thisSessionInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.Session;
        SourceType Source_type = SourceType.Planning;
        String errorMessage = "";

        // 1. transform xml to session-object
        try {
            thisSessionInMessage = getSessionObjectFromXmlMessage(task);
            sessionUuid = thisSessionInMessage.getSessionUUID();
            System.out.println("New message for [Session] with Uuid: " + sessionUuid);
        } catch (Exception e) {
            System.out.println(" [!!!] ERROR: handleNewMessageSession - 1. transform xml to session-object :\n" + e.toString());
            errorMessage += " [!!!] ERROR: handleNewMessageSession - 1. transform xml to session-object :\n" + e.toString() + "\n";
            //e.printStackTrace();
        }

        if (thisSessionInMessage != null) {

            // 2. check if UUID exists in local db
            uuidExists = false;
            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("Session", sessionUuid);
            } catch (Exception e) {
                System.out.println(" [!!!] ERROR: handleNewMessageSession - 2. Check if UUID exists in local db :\n" + e.toString());
                errorMessage += " [!!!] ERROR: handleNewMessageSession - 2. Check if UUID exists in local db :\n" + e.toString() + "\n";
                //e.printStackTrace();
            }

            // 2.1.1 check if session is to be 'deleted'
            if (uuidExists) {

                System.out.println("UUID already exists in our PlanningDB table:[Session]\n");

                // 2.2 Session record update

                // 2.2.1. get idSession from sessionUUID in Session
                String[] propertiesToSelect = {"idSession"};
                String table = "Session";
                String[] selectors = {"uuid"};
                String[] values = {"" + sessionUuid};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
                if (thisSessionInMessage.getActive() == 0) {

                    // 1. Cancel Google Calendar event
                    try {
                        GoogleCalenderApi.cancelSessionWithSessionObject(thisSessionInMessage);
                    } catch (Exception e) {
                        System.out.println(" [!!!] ERROR: handleNewMessageSession - cancleSessionGCA FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageSession - cancleSessionGCA FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }

                    // 2. Perform soft-delete on local db
                    try {
                        new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                        System.out.println("SoftDelete executed on object with entityId: " + Integer.parseInt(selectResults[0]) + "\n");
                    } catch (Exception e){
                        System.out.println(" [!!!] ERROR: handleNewMessageSession - softDelete FAIL :\n"+e.toString());
                        errorMessage += " [!!!] ERROR: handleNewMessageSession - softDelete FAIL :\n"+e.toString()+"\n";
                        //e.printStackTrace();
                    }


                    // 3. updateUuidRecordVersion() (To UUID master)
                    int updateUuidRecordVersionResponse = 0;
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, sessionUuid);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                } else {

                    // 2.2.2. get entityVersion from id in BaseEntity
                    propertiesToSelect[0] = "entity_version";
                    table = "BaseEntity";
                    selectors[0] = "idBaseEntity";
                    values[0] = selectResults[0];

                    int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                    try {
                        thisSessionInMessage.setEntityId(Integer.parseInt(selectResults[0]));
                        //System.out.println("thisSessionInMessage.getSessionId(): " + thisSessionInMessage.getSessionId());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    boolean allGood = true;

                    if (localEntityVersion < thisSessionInMessage.getEntityVersion()) {

                        // 2.3.1. update google calender through API

                        try {
                            GoogleCalenderApi.updateSessionWithSessionObject(thisSessionInMessage);
                        } catch (Exception e) {
                            System.out.println(" [!!!] ERROR: handleNewMessageSession - updateSessionGCA FAIL :\n"+e.toString());
                            errorMessage += " [!!!] ERROR: handleNewMessageSession - updateSessionGCA FAIL :\n"+e.toString()+"\n";
                            //e.printStackTrace();
                        }
                        // 2.3.2. update record in our local db

                        try {
                            allGood = new Session_DAO().updateSessionByObject(thisSessionInMessage);
                        } catch (Exception e) {
                            System.out.println(" [!!!] ERROR: handleNewMessageSession - softDelete FAIL :\n"+e.toString());
                            errorMessage += " [!!!] ERROR: handleNewMessageSession - softDelete FAIL :\n"+e.toString()+"\n";
                            //e.printStackTrace();
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)

                        int updateUuidRecordVersionResponse = 0;
                        if (allGood) {
                            try {
                                updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, sessionUuid);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                System.out.println(" [!!!] ERROR:  updating records of into the Uuid Master:\n" + e.toString());
                                errorMessage += " [!!!] ERROR:  updating records of the Uuid Master:\n" + e.toString() + "\n";
                                //e.printStackTrace();
                            }
                        } else {
                            //updateEventError
                        }


                        System.out.println("We had this [Session] with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisSessionInMessage.getEntityVersion() + "'");

                    } else {

                        // we have the latest version...
                        System.out.println("We already had this [Session] with entityVersion: '" + localEntityVersion + "'");

                    }
                }

            } else {

                // New session record

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
                    System.out.println("Error adding session as event to Google calendar API: " + e);
                    //e.printStackTrace();
                }

                // 2.4.2. insert new session into local db
                int messageSessionInsertReturner = 0;
                try {
                    messageSessionInsertReturner = new Session_DAO().insertIntoSession(thisSessionInMessage);
                } catch (SQLException e) {
                    System.out.println("Error inserting user into the database: " + e);
                }

                // 2.4.3. insertUuidRecord
                try {
                    Sender.insertUuidRecord("", messageSessionInsertReturner, thisEntityType, Source_type, sessionUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }
                System.out.println("Inserted new session record with id='" + messageSessionInsertReturner + "' and UUID='" + sessionUuid + "'");


            }
        } else {
            System.out.println("ERROR: Something went wrong getting session object from xml message!");
        }

        //System.out.println(" [END] ");
    }

    //TODO ERROR MESSAGE STUREN 4 hieronder
    //Handler for taskmessage
    static void handleNewMessageTask(String task) {

        String taskUuid = "";
        Task thisTaskInMessage = null;
        Boolean uuidExists = false;
        EntityType thisEntityType = EntityType.Task;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to event-object
        try {
            thisTaskInMessage = getTaskObjectFromXmlMessage(task);
            taskUuid = thisTaskInMessage.getTaskUuid();
            System.out.println("New message for [Task] with Uuid: " + taskUuid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (thisTaskInMessage != null) {

            // 2.1. check if UUID exists in local db
            uuidExists = false;
            try {
                uuidExists = new BaseEntityDAO().doesUUIDExist("Task", taskUuid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (uuidExists) {

                System.out.println("Uuid already exists in our PlanningDB table:[Task]");

                // 2.2 Task record update
                // 2.2.1. get idTask by uuid in Task
                String[] propertiesToSelect = {"idTask"};
                String table = "Task";
                String[] selectors = {"uuid"};
                String[] values = {"" + taskUuid};

                String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
                if (thisTaskInMessage.getActive() == 0) {

                    // 1. Cancel Google Calendar event
                    String[] propertiesToSelect2 = {"GCAEventId"};
                    String table2 = "Task";
                    String[] selectors2 = {"uuid"};
                    String[] values2 = {"" + taskUuid};

                    String[] selectResults2 = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect2, table2, selectors2, values2);
                    GoogleCalenderApi.cancelEventByGCAID(selectResults2[0]);

                    // 2. Perform soft-delete on local db
                    new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                    System.out.println("SoftDelete executed on [Task] with entityId: " + Integer.parseInt(selectResults[0]) + "\n");

                    // 3. updateUuidRecordVersion() (To UUID master)
                    int updateUuidRecordVersionResponse = 0;
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, taskUuid);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                } else {

                    // 2.2.2. get entityVersion from id in BaseEntity
                    propertiesToSelect[0] = "entity_version";
                    table = "BaseEntity";
                    selectors[0] = "idBaseEntity";
                    values[0] = selectResults[0];
                    int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                    try {
                        thisTaskInMessage.setEntityId(Integer.parseInt(selectResults[0]));
                        //System.out.println("thisEventInMessage.getEventId(): " + thisEventInMessage.getEventId());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    boolean allGood = true;


                    if (localEntityVersion < thisTaskInMessage.getEntityVersion()) {

                        // 2.3.1. update google calender through API

                        try {
                            GoogleCalenderApi.updateTaskWithTaskObject(thisTaskInMessage);
                        } catch (Exception e) {
                            System.out.println("Error updating Event with Event object:" + e);
                        }

                        // 2.3.2. update record in our local db
                        int updateUuidRecordVersionResponse = 0;
                        try {
                            //System.out.println("2.3.2. update record in our local db: thisTaskInMessage.toString(): "+thisTaskInMessage.toString());
                            allGood = new Task_DAO().updateTaskByObject(thisTaskInMessage);
                        } catch (Exception e) {
                            System.out.println("Error updating event in the database: " + e);
                        }

                        // 2.3.3. updateUuidRecordVersion() (To UUID master)
                        if (allGood) {
                            try {
                                updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, taskUuid);
                            } catch (IOException | TimeoutException | JAXBException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //updateEventError
                        }

                        System.out.println("We had this [Task] with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + thisTaskInMessage.getEntityVersion() + "'");

                    } else {
                        // we have the latest version...
                        System.out.println("We already had this [Task] with entityVersion: '" + localEntityVersion + "'");
                    }
                }
            } else {
                // New Task record

                // 2.4.1. Add new Task as event to Google calendar

                String newEventHtmlLinkAndId = null;
                try {
                    newEventHtmlLinkAndId = GoogleCalenderApi.createEventFromTaskObject(thisTaskInMessage);

                    String[] newEventProperties = newEventHtmlLinkAndId.split("-=-");

                    thisTaskInMessage.setGCAEventId(newEventProperties[1]);
                    //System.out.println("CGAEventId in newEventProperties[1]: " + newEventProperties[1]);
                    thisTaskInMessage.setGCAEventLink(newEventProperties[0]);
                    //System.out.println("CGAEventLink in newEventProperties[0]: " + newEventProperties[0]);

                } catch (IOException e) {
                    System.out.println("Error adding event to Google calendar API: " + e);
                    //e.printStackTrace();
                }
                // 2.4.2. insert new task into local db
                int messageTaskInsertReturner = 0;
                try {

                    messageTaskInsertReturner = new Task_DAO().insertIntoTask(thisTaskInMessage);
                    System.out.println("Inserted new task record with id='" + messageTaskInsertReturner + "' and UUID='" + taskUuid + "'");
                } catch (SQLException e) {
                    System.out.println("Error inserting task into the database: " + e);
                    //e.printStackTrace();
                }

                // 2.4.3. insertUuidRecord
                try {
                    Sender.insertUuidRecord("", messageTaskInsertReturner, thisEntityType, Source_type, taskUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    System.out.println("Error inserting record into the Uuid Master: " + e);
                    //e.printStackTrace();
                }
            }
        } else {
            System.out.println("Something went wrong getting task object from xml message!");
        }
    }
    //Handler for resservationeventmessage
    static void handleNewMessageReservationEvent(String task) {

        String userUUID = "";
        String reservationUUID = "";
        String eventUUID = "";

        Reservation_Event newReservation_EventObjectFromXml = null;

        Boolean uuidExists = false;
        Boolean allGood = true;

        EntityType thisEntityType = EntityType.ReservationEvent;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to reservation-object

        newReservation_EventObjectFromXml = getReservation_EventObjectFromXmlMessage(task);

        System.out.println("New message for [Reservation_Event] with Uuid: " + newReservation_EventObjectFromXml.getReservationUUID());
        //System.out.println("newReservation_EventObjectFromXml.toString():\n" + newReservation_EventObjectFromXml.toString() + "\n");


        reservationUUID = newReservation_EventObjectFromXml.getReservationUUID();
        // System.out.println("sessionUUID: "+sessionUUID);

        // 2.1 check if UUID exists in local db

        uuidExists = false;

        // 2.1.A. search event table for uuid

        try {
            uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Event", reservationUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2.1.1 check if reservation event is to be 'deleted'

        if (uuidExists) {

            System.out.println("UUID already exists in our PlanningDB table:[Reservation_Event]");

            // 2.2. Reservation_Event record update

            //System.out.println("[Reservation_Event]\n");

            // 2.2.1 get idSession from sessionUUID in Session
            String[] propertiesToSelect = {"idReservationEvent"};
            String table = "Reservation_Event";
            String[] selectors = {"uuid"};
            String[] values = {"" + reservationUUID};

            String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

            // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
            if (newReservation_EventObjectFromXml.getActive() == 0) {

                // 1. Delete user from Google calendar api

                try {
                    GoogleCalenderApi.deleteAttendeeFromEventWithREO(newReservation_EventObjectFromXml);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 2. Perform soft-delete on local db
                new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                System.out.println("SoftDelete executed on object with entityId: " + Integer.parseInt(selectResults[0]) + "\n");

                // 3. updateUuidRecordVersion() (To UUID master)
                int updateUuidRecordVersionResponse = 0;
                try {
                    updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, reservationUUID);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }
            } else {

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

                    // 2.3.1. update google calender through API

                    GoogleCalenderApi.updateEventsAttendees(existingReservation_Event.getEventUUID(),existingReservation_Event.getUserUUID(), "Event");

                    // 2.3.2. update record in our local db

                    int updateUuidRecordVersionResponse = 0;
                    try {
                        allGood = new Reservation_Event_DAO().updateReservationEventByObject(existingReservation_Event);
                    } catch (Exception e) {
                        System.out.println("Error updating reservation event in the database: " + e);
                    }
                    // 2.3.3. updateUuidRecordVersion() (To UUID master)
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, reservationUUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    // 2.3.4. update entity version (on our database)
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + existingReservation_Event.getEntityVersion(), "int", "idBaseEntity", "" + selectResults[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR in 2.3.4. update entity version:\n " + e);
                        allGood = false;
                    }

                    System.out.println("We had this Reservation_Event with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + existingReservation_Event.getEntityVersion() + "'");


                } else {
                    // we have the latest version...
                    System.out.println("We already had this Reservation_Event with entityVersion: '" + localEntityVersion + "'");

                }
            }


        } else {

            // uuid doesn't exit locally yet

            // 1. Add user to google calendar api event

            GoogleCalenderApi.addAttendeeForEvent(newReservation_EventObjectFromXml.getEventUUID(), newReservation_EventObjectFromXml.getUserUUID());

            // 2. Insert record locally with given info

            int messageReservationInsertReturner = 0;

            BaseEntity newTempEntity = new BaseEntity();
            newReservation_EventObjectFromXml.setReservationId(newTempEntity.getEntityId());
            try {
                messageReservationInsertReturner = new Reservation_Event_DAO().insertIntoReservation_Event(newReservation_EventObjectFromXml);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 3. InsertUuidRecord
            try {
                Sender.insertUuidRecord("", messageReservationInsertReturner, thisEntityType, Source_type, reservationUUID);
            } catch (IOException | TimeoutException | JAXBException e) {
                e.printStackTrace();
            }
            System.out.println("Inserted new Reservation_Event record with Uuid: " + reservationUUID + "!");

        }
    }
    //Handler for resservationsessionmessage
    static void handleNewMessageReservationSession(String task) {

        String userUUID = "";
        String reservationUUID = "";
        String sessionUUID = "";

        Reservation_Session newReservation_SessionObjectFromXml = null;
        Boolean uuidExists = false;
        Boolean allGood = true;

        EntityType thisEntityType = EntityType.ReservationSession;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to reservation-object

        newReservation_SessionObjectFromXml = getReservation_SessionObjectFromXmlMessage(task);

        reservationUUID = newReservation_SessionObjectFromXml.getReservationUUID();

        System.out.println("New message for [Reservation_Session] with Uuid: " + newReservation_SessionObjectFromXml.getReservationUUID());
        // System.out.println("sessionUUID: "+sessionUUID);

        // 2.1 check if UUID exists in local db

        uuidExists = false;

        // 2.1.B. search Reservation_Session table for uuid

        try {
            uuidExists = new BaseEntityDAO().doesUUIDExist("Reservation_Session", reservationUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2.1.1 check if reservation session is to be 'deleted'

        if (uuidExists) {

            System.out.println("UUID already exists in our PlanningDB table:[Reservation_Session]\n");


            // 2.2.1 get idSession from sessionUUID in Session
            String[] propertiesToSelect = {"idReservationSession"};
            String table = "Reservation_Session";
            String[] selectors = {"uuid"};
            String[] values = {"" + reservationUUID};

            String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

            // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
            if (newReservation_SessionObjectFromXml.getActive() == 0) {

                // 1. Delete user from Google Calender Api
                try {
                    GoogleCalenderApi.deleteAttendeeFromEventWithRSO(newReservation_SessionObjectFromXml);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 2. Perform soft-delete on local db
                new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                System.out.println("SoftDelete executed on [Reservation_Session] with entityId: " + Integer.parseInt(selectResults[0]) + "\n");

                // 3. updateUuidRecordVersion() (To UUID master)
                int updateUuidRecordVersionResponse = 0;
                try {
                    updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, reservationUUID);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }


            } else {

                // 2.2.2. get entityVersion from id in BaseEntity
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

                int updateUuidRecordVersionResponse = 0;

                if (localEntityVersion < existingReservation_Session.getEntityVersion()) {

                    // 2.3.1. update google calender through API


                    GoogleCalenderApi.updateEventsAttendees(existingReservation_Session.getSessionUUID(),existingReservation_Session.getUserUUID(), "Session");


                    // 2.3.2. update record in our local db

                    updateUuidRecordVersionResponse = 0;
                    try {
                        allGood = new Reservation_Session_DAO().updateReservationSessionByObject(existingReservation_Session);
                    } catch (Exception e) {
                        System.out.println("Error updating reservation session in the database: " + e);
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
                        System.out.println("ERROR in 2.3.4. update entity version (on our database) :\n " + e);
                        allGood = false;
                    }

                    System.out.println("We had this Reservation_Session with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + existingReservation_Session.getEntityVersion() + "'");


                } else {
                    // we have the latest version...
                    System.out.println("We already had this Reservation_Session with entityVersion: '" + localEntityVersion + "'");
                }

            }


        } else {

            // uuid doesn't exit locally yet
            // # insert record locally with given info

            // 1. Add user to google calendar api event

            GoogleCalenderApi.addAttendeeForSession(newReservation_SessionObjectFromXml.getSessionUUID(), newReservation_SessionObjectFromXml.getUserUUID());

            // 2. Insert record locally with given info
            int messageReservationInsertReturner = 0;

            BaseEntity newTempEntity = new BaseEntity();
            newReservation_SessionObjectFromXml.setReservationId(newTempEntity.getEntityId());
            //System.out.println("newReservation_SessionObjectFromXml: " + newReservation_SessionObjectFromXml.toString());

            try {
                messageReservationInsertReturner = new Reservation_Session_DAO().insertIntoReservation_Session(newReservation_SessionObjectFromXml);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 3. InsertUuidRecord
            try {
                Sender.insertUuidRecord("", messageReservationInsertReturner, thisEntityType, Source_type, reservationUUID);
            } catch (IOException | TimeoutException | JAXBException e) {
                e.printStackTrace();
            }
            System.out.println("Inserted new Reservation_Session record with Uuid: " + reservationUUID + "!");

        }

    }
    //Handler for assigntaskmessage
    static void handleNewMessageAssignTask(String task) {

        String userUuid = "";
        String assignTaskUuid = "";
        String taskUuid = "";

        Assign_Task newAssign_TaskObjectFromXml = null;
        Boolean uuidExists = false;
        Boolean allGood = true;

        EntityType thisEntityType = EntityType.AssignTask;
        SourceType Source_type = SourceType.Planning;

        // 1. transform xml to Assign_Task-object

        newAssign_TaskObjectFromXml = getAssign_TaskObjectFromXmlMessage(task);

        assignTaskUuid = newAssign_TaskObjectFromXml.getAssignTaskUuid();

        System.out.println("New message for [Assign_Task] with Uuid: " + assignTaskUuid);
        // System.out.println("sessionUUID: "+sessionUUID);

        // 2.1 check if UUID exists in local db

        uuidExists = false;

        // 2.1.B. search Assign_Task table for uuid

        try {
            uuidExists = new BaseEntityDAO().doesUUIDExist("Assign_Task", assignTaskUuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2.1.1 check if reservation session is to be 'deleted'

        if (uuidExists) {

            System.out.println("UUID already exists in our PlanningDB table:[Assign_Task]\n");


            // 2.2.1 get idSession from sessionUUID in Session
            String[] propertiesToSelect = {"idAssignTask"};
            String table = "Assign_Task";
            String[] selectors = {"uuid"};
            String[] values = {"" + assignTaskUuid};

            String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

            // Check if active is still 1 for local object, otherwise softdelete it in baseEntity
            if (newAssign_TaskObjectFromXml.getActive() == 0) {

//                 1. Delete user from Google Calender Api
                try {
                    GoogleCalenderApi.deleteAttendeeFromEventWithATO(newAssign_TaskObjectFromXml);
                } catch (Exception e) {
                   e.printStackTrace();
                }

                // 2. Perform soft-delete on local db
                new BaseEntityDAO().softDeleteBaseEntity(Integer.parseInt(selectResults[0]));
                System.out.println("SoftDelete executed on [Assign_Task] with entityId: " + Integer.parseInt(selectResults[0]) + "\n");

                // 3. updateUuidRecordVersion() (To UUID master)
                int updateUuidRecordVersionResponse = 0;
                try {
                    updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, assignTaskUuid);
                } catch (IOException | TimeoutException | JAXBException e) {
                    e.printStackTrace();
                }


            } else {

                // 2.2.2. get entityVersion from id in BaseEntity
                propertiesToSelect[0] = "entity_version";
                table = "BaseEntity";
                selectors[0] = "idBaseEntity";
                values[0] = selectResults[0];
                int localEntityVersion = Integer.parseInt(new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0]);

                Assign_Task existingAssign_Task = null;
                try {
                    existingAssign_Task = getAssign_TaskObjectFromXmlMessage(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int updateUuidRecordVersionResponse = 0;

                if (localEntityVersion < existingAssign_Task.getEntityVersion()) {

                    // 2.3.1. update google calender through API

                    GoogleCalenderApi.updateEventsAttendees(existingAssign_Task.getTaskUuid(),existingAssign_Task.getUserUuid(), "Task");

                    // 2.3.2. update record in our local db

                    System.out.println("Current task to be updated .toString: "+existingAssign_Task.toString());
                    try {
                        allGood = new Assign_Task_DAO().updateAssignTaskByObject(existingAssign_Task);
                    } catch (Exception e) {
                        System.out.println("Error updating assign task in the database: " + e);
                    }

                    // 2.3.3. updateUuidRecordVersion() (To UUID master)
                    try {
                        updateUuidRecordVersionResponse = Sender.updateUuidRecordVersion("", Source_type, assignTaskUuid);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 2.3.4. update entity version (on our database)
                    try {
                        allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + existingAssign_Task.getEntityVersion(), "int", "idBaseEntity", "" + existingAssign_Task.getAssignTaskId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR in 2.3.4. update entity version (on our database) :\n " + e);
                        allGood = false;
                    }

                    System.out.println("We had this Assign_Task with entityVersion: '" + localEntityVersion + "'. Updated to latest version with entityVersion: '" + existingAssign_Task.getEntityVersion() + "'");


                } else {
                    // we have the latest version...
                    System.out.println("We already had this Assign_Task with entityVersion: '" + localEntityVersion + "'");
                }

            }


        } else {

            // uuid doesn't exit locally yet
            // # insert record locally with given info

            // 1. Add employee to google calendar api event
            GoogleCalenderApi.addEmployeeForTask(newAssign_TaskObjectFromXml.getTaskUuid(), newAssign_TaskObjectFromXml.getUserUuid());

            // 2. Insert record locally with given info
            int messageAssignTaskInsertReturner = 0;

            BaseEntity newTempEntity = new BaseEntity();
            newAssign_TaskObjectFromXml.setAssignTaskId(newTempEntity.getEntityId());
            //System.out.println("newReservation_SessionObjectFromXml: " + newReservation_SessionObjectFromXml.toString());

            try {
                messageAssignTaskInsertReturner = new Assign_Task_DAO().insertIntoAssign_Task(newAssign_TaskObjectFromXml);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 3. InsertUuidRecord
            try {
                Sender.insertUuidRecord("", messageAssignTaskInsertReturner, thisEntityType, Source_type, assignTaskUuid);
            } catch (IOException | TimeoutException | JAXBException e) {
                e.printStackTrace();
            }
            System.out.println("Inserted new Assign_Task record with Uuid: " + assignTaskUuid + "!");

        }

    }


    // HTTP REQUESTS
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

    static String getXmlForErrorMessage(String xmlHeaderDescription, SourceType Source_type) throws JAXBException {

        String messageType = "errorMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ErrorStructure errorStructure = new XmlMessage.ErrorStructure();
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ErrorMessage xmlErrorMessage = new XmlMessage.ErrorMessage(header, errorStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlErrorMessage.generateXML();


        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;

    }


    //XML PARSERS
    // User: (params) => XML
    static String getXmlForNewUser(String xmlHeaderDescription, SourceType Source_type, String
            userUUID, String lastName, String firstName, String phoneNumber, String email, String street, String houseNr, String
            city, String postalCode, String country, String company, String type, int entity_version, int active, String timestamp)
            throws JAXBException {

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
        String errorMessage = "";
        String xmlTotalMessage = "";

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
                            errorMessage += " [!!!] ERROR: No userUUID found in XML:\n";
                            allGood = false;
                        }
                    }
                }
            }
        }
        lastName = getSafeXmlProperty(xmlMessage, "lastName");
        if (lastName == "false") {
            System.out.println(" [!!!] ERROR: No lastName found in XML: ");
            errorMessage += " [!!!] ERROR: No lastName found in XML:\n";
            allGood = false;
        }
        firstName = getSafeXmlProperty(xmlMessage, "firstName");
        if (firstName == "false") {
            System.out.println(" [!!!] ERROR: No firstName found in XML: ");
            errorMessage += " [!!!] ERROR: No firstName found in XML:\n";
            allGood = false;
        }
        phoneNumber = getSafeXmlProperty(xmlMessage, "phoneNumber");
        if (phoneNumber == "false") {
            System.out.println(" [!!!] ERROR: No phoneNumber found in XML: ");
            errorMessage += " [!!!] ERROR: No phoneNumber found in XML:\n";
            allGood = false;
        }
        email = getSafeXmlProperty(xmlMessage, "email");
        if (email == "false") {
            System.out.println(" [!!!] ERROR: No email found in XML: ");
            errorMessage += " [!!!] ERROR: No email found in XML:\n";
            allGood = false;
        }
        street = getSafeXmlProperty(xmlMessage, "street");
        if (street == "false") {
            System.out.println(" [!!!] ERROR: No street found in XML: ");
            errorMessage += " [!!!] ERROR: No street found in XML:\n";
            allGood = false;
        }
        houseNr = getSafeXmlProperty(xmlMessage, "houseNr");
        if (houseNr == "false") {
            System.out.println(" [!!!] ERROR: No houseNr found in XML: ");
            errorMessage += " [!!!] ERROR: No houseNr found in XML:\n";
            allGood = false;
        }
        city = getSafeXmlProperty(xmlMessage, "city");
        if (city == "false") {
            System.out.println(" [!!!] ERROR: No city found in XML: ");
            errorMessage += " [!!!] ERROR: No city found in XML:\n";
            allGood = false;
        }
        postalCode = getSafeXmlProperty(xmlMessage, "postalCode");
        if (postalCode == "false") {
            System.out.println(" [!!!] ERROR: No postalCode found in XML: ");
            errorMessage += " [!!!] ERROR: No postalCode found in XML:\n";
            allGood = false;
        }
        country = getSafeXmlProperty(xmlMessage, "country");
        if (country == "false") {
            System.out.println(" [!!!] ERROR: No country found in XML: ");
            errorMessage += " [!!!] ERROR: No country found in XML:\n";
            allGood = false;
        }
        company = getSafeXmlProperty(xmlMessage, "company");
        if (company == "false") {
            System.out.println(" [!!!] ERROR: No company found in XML: ");
            errorMessage += " [!!!] ERROR: No company found in XML:\n";
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
                    errorMessage += " [!!!] ERROR: No userType found in XML:\n";
                    allGood = false;
                }
            }

        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion == 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
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
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {
                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
                    allGood = false;
                }
            }
        }

        if (errorMessage == "" ) {
            userObject = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType, false);
            return userObject;
        }
        else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse user object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e);
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e);
            }
            return null;
        }

    }


    // Session: (params) => XML
    static String getXmlForNewSession(String xmlHeaderDescription, SourceType Source_type, String
            sessionUUID, String eventUUID, String sessionName, int maxAttendees, String description, String summary, String location, String speaker,
                                      String dateTimeStart, String dateTimeEnd, String type, float price, int entityVersion, int active) throws JAXBException {
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
        String errorMessage = "";
        String xmlTotalMessage = "";

                // xmlMessage parsing

        sessionUUID = getSafeXmlProperty(xmlMessage, "uuid");

        if (sessionUUID == "false") {

            sessionUUID = getSafeXmlProperty(xmlMessage, "UUID");

            if (sessionUUID == "false") {

                sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUUID");

                if (sessionUUID == "false") {
                    System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
                    errorMessage += " [!!!] ERROR: No sessionUUID found in XML:\n";
                    allGood = false;
                }
            }
        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {
            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            errorMessage += " [!!!] ERROR: No eventUUID found in XML:\n";
            allGood = false;
        }
        sessionName = getSafeXmlProperty(xmlMessage, "sessionName");
        if (sessionName == "false") {
            System.out.println(" [!!!] ERROR: No sessionName found in XML: ");
            errorMessage += " [!!!] ERROR: No sessionName found in XML:\n";
            allGood = false;
        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR(intc): No maxAttendees found in XML: ");
            errorMessage += " [!!!] ERROR(intc): No maxAttendees found in XML:\n";
            allGood = false;
        }

        if (maxAttendees < 0) {
            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            errorMessage += " [!!!] ERROR: No maxAttendees found in XML:\n";
            allGood = false;
        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {
            System.out.println(" [!!!] ERROR: No description found in XML: ");
            errorMessage += " [!!!] ERROR: No description found in XML:\n";
            allGood = false;
        }
        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {
            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            errorMessage += " [!!!] ERROR: No summary found in XML:\n";
            allGood = false;
        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {
            System.out.println(" [!!!] ERROR: No location found in XML: ");
            errorMessage += " [!!!] ERROR: No location found in XML:\n";
            allGood = false;
        }
        speaker = getSafeXmlProperty(xmlMessage, "speaker");
        if (speaker == "false") {
            System.out.println(" [!!!] ERROR: No speaker found in XML: ");
            errorMessage += " [!!!] ERROR: No speaker found in XML:\n";
            allGood = false;
        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeStart found in XML:\n";
            allGood = false;
        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeEnd found in XML:\n";
            allGood = false;
        }
        sessionType = getSafeXmlProperty(xmlMessage, "sessionType");
        if (sessionType == "false") {

            sessionType = getSafeXmlProperty(xmlMessage, "type");

            if (sessionType == "false") {
                System.out.println(" [!!!] ERROR: No sessionType/type found in XML: ");
                errorMessage += " [!!!] ERROR: No sessionType/type found in XML\n";
                allGood = false;
            }

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR: price = Float.parseFloat(getSafeXmlProperty(xmlMessage, \"price\")); FAIL");
            errorMessage += " [!!!] ERROR: price = Float.parseFloat(getSafeXmlProperty(xmlMessage, \"price\")); FAIL \n";
            e.printStackTrace();
            price = -1;
        }
        if (price < 0) {
            System.out.println(" [!!!] ERROR: No price found in XML: ");
            errorMessage += " [!!!] ERROR: No price found in XML:\n";
            allGood = false;
        }
        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR: entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, \"entityVersion\")); FAIL");
            errorMessage += " [!!!] ERROR: entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, \"entityVersion\")); FAIL\n";
            e.printStackTrace();
            price = -1;
        }
        if (entityVersion < 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
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
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {

                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
                    allGood = false;
                }
            }
        }
        if(errorMessage == "") {
            sessionObject = new Session(0, entityVersion, active, timestamp, sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, speaker, dateTimeStart, dateTimeEnd, sessionType, "", "", price, false);
            return sessionObject;
        }else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse session object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e.toString());
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e.toString());
            }
            return null;
        }
    }


    // Task: (params) => XML
    static String getXmlForNewTask(String xmlHeaderDescription, SourceType Source_type, String
            taskUuid, String eventUuid, String description, String dateTimeStart, String dateTimeEnd, int entityVersion, int active) throws JAXBException {
        String messageType = "taskMessage";
        String taskName = "Task name";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.TaskStructure TaskStructure = new XmlMessage.TaskStructure(taskUuid, eventUuid, taskName, description,  dateTimeStart, dateTimeEnd, entityVersion, active, getCurrentDateTimeStamp());
        // steek header en datastructure (TaskStructure) in message klasse

        XmlMessage.TaskMessage TaskMessage = new XmlMessage.TaskMessage(header, TaskStructure);
        String xmlTotalMessage = TaskMessage.generateXML();
        return xmlTotalMessage;
    }
    // Task: Object => XML
    static String getXmlFromTaskObject(String headerDescription, SourceType Source_type, Task newTask) throws JAXBException {

        //SourceType Source_type = Source_type;
        String taskUuid = newTask.getTaskUuid();
        String eventUuid = newTask.getEventUuid();
        String taskDescription = newTask.getDescription();
        int entityVersion = newTask.getEntityVersion();
        int active = newTask.getActive();
        String dateTimeStart = newTask.getDateTimeStart();
        String dateTimeEnd = newTask.getDateTimeEnd();
        String taskName = newTask.getTaskName();

        String messageType = "taskMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, headerDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.TaskStructure eventStructure = new XmlMessage.TaskStructure(taskUuid, eventUuid, taskName, taskDescription, dateTimeStart, dateTimeEnd, entityVersion, active, getCurrentDateTimeStamp());
        XmlMessage.TaskMessage xmlReservationMessage = new XmlMessage.TaskMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }
    // Task: XML => Object
    static Task getTaskObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Task taskObject = null;

        String taskUuid = "false";
        String eventUuid = "false";
        String description = "false";
        String dateTimeStart = "false";
        String dateTimeEnd = "false";
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";
        String errorMessage = "";
        String xmlTotalMessage = "";

                // xmlMessage parsing

        taskUuid = getSafeXmlProperty(xmlMessage, "uuid");

        if (taskUuid == "false") {

            taskUuid = getSafeXmlProperty(xmlMessage, "UUID");

            if (taskUuid == "false") {

                taskUuid = getSafeXmlProperty(xmlMessage, "taskUUID");
                if (taskUuid == "false") {
                    System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
                    errorMessage += " [!!!] ERROR: No sessionUUID found in XML:\n";
                    allGood = false;
                }
            }
        }
        eventUuid = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUuid == "false") {
            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            errorMessage += " [!!!] ERROR: No eventUUID found in XML:\n";
            allGood = false;
        }


        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {
            System.out.println(" [!!!] ERROR: No description found in XML: ");
            errorMessage += " [!!!] ERROR: No description found in XML:\n";
            allGood = false;
        }

        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeStart found in XML:\n";
            allGood = false;
        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeEnd found in XML:\n";
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (entityVersion < 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
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
                    errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
                    allGood = false;
                }
            }
        }
        if(errorMessage == "") {
            taskObject = new Task(0, entityVersion, active, timestamp, taskUuid, eventUuid, description, dateTimeStart, dateTimeEnd, "", "", false);
            return taskObject;
        } else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse Task object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e.toString());
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e.toString());
            }
            return null;
        }
    }


    // Event: (params) => XML
    static String getXmlForNewEvent(String messageType, String description, SourceType
            Source_type, String eventUUID, String eventName, int maxAttendees, String eventDescription, String summary,
                                    String location, String contactPerson, String eventType, float price, int entityVersion,
                                    int active, String dateTimeStart, String dateTimeEnd) throws JAXBException {

        messageType = "eventMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.EventStructure eventStructure = new XmlMessage.EventStructure(eventUUID, eventName, maxAttendees, eventDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, entityVersion, active, getCurrentDateTimeStamp());
        XmlMessage.EventMessage xmlReservationMessage = new XmlMessage.EventMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }
    // Event: Object => XML
    static String getXmlFromEventObject(String headerDescription, SourceType Source_type, Event
            newEvent) throws
            JAXBException {

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
        String errorMessage = "";
        String xmlTotalMessage = "";

                // xmlMessage parsing

        uuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (uuid == "false") {

            uuid = getSafeXmlProperty(xmlMessage, "eventUuid");
            if (uuid == "false") {
                System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
                errorMessage += " [!!!] ERROR: No eventUUID found in XML:\n";
                allGood = false;
            }
        }


        eventName = getSafeXmlProperty(xmlMessage, "eventName");
        if (eventName == "false") {
            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            errorMessage += " [!!!] ERROR: No eventUUID found in XML:\n";
            allGood = false;
        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: Something went wrong parsing maxAttendees found in XML: ");
            errorMessage += " [!!!] ERROR: Something went wrong parsing maxAttendees found in XML:\n";
        }

        if (maxAttendees < 0) {
            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            errorMessage += " [!!!] ERROR: No maxAttendees found in XML:\n";
            allGood = false;
        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {
            System.out.println(" [!!!] ERROR: No description found in XML: ");
            errorMessage += " [!!!] ERROR: No description found in XML:\n";
            allGood = false;
        }else{
            description = "Name: '"+eventName+" "+description;
        }

        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {
            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            errorMessage += " [!!!] ERROR: No summary found in XML:\n";
            allGood = false;
        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {
            System.out.println(" [!!!] ERROR: No location found in XML: ");
            errorMessage += " [!!!] ERROR: No location found in XML:\n";
            allGood = false;
        }
        contactPerson = getSafeXmlProperty(xmlMessage, "contactPerson");
        if (contactPerson == "false") {
            System.out.println(" [!!!] ERROR: No contactPerson found in XML: ");
            errorMessage += " [!!!] ERROR: No contactPerson found in XML:\n";
            allGood = false;
        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeStart found in XML:\n";
            allGood = false;
        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {
            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            errorMessage += " [!!!] ERROR: No dateTimeEnd found in XML:\n";
            allGood = false;
        }
        type = getSafeXmlProperty(xmlMessage, "eventType");

        if (type == "false") {

            type = getSafeXmlProperty(xmlMessage, "type");

            if (type == "false") {
                System.out.println(" [!!!] ERROR: No type found in XML: ");
                errorMessage += " [!!!] ERROR: No type found in XML:\n";
                allGood = false;
            }

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            System.out.println(" [!!!] ERROR: No price found in XML: " + e);
            errorMessage += " [!!!] ERROR: No price found in XML:\n";
            allGood = false;
        }
        if (price < 0) {
            System.out.println(" [!!!] ERROR: Negative price found in XML: ");
            errorMessage += " [!!!] ERROR: Negative price found in XML:\n";
            allGood = false;
        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion <= 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
            allGood = false;
        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active < 0) {
            System.out.println(" [!!!] ERROR: No active found in XML: ");
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;
        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {
            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
            allGood = false;
        }
        if(errorMessage=="") {
            eventObject = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, type, price, false);
            return eventObject;
        } else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse event object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e.toString());
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e.toString());
            }
            return null;
        }
    }


    // Reservation_Session: (params) => XML
    static String getXmlForNewReservation_Session(String xmlHeaderDescription, SourceType
            Source_type, String reservationUUID, String userUUID, String sessionUUID, float paid, int entityVersion) throws
            JAXBException {

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
    static String getXmlFromReservation_SessionObject(String xmlHeaderDescription, SourceType
            Source_type, Reservation_Session thisReservationObject) throws JAXBException {

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
        String sessionUUID = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";
        String errorMessage = "";
        String xmlTotalMessage = "";

        // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUuid");

            if (reservationUUID == "false") {

                reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");

                if (reservationUUID == "false") {
                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML: ");
                    errorMessage += " [!!!] ERROR: No reservationUUID found in XML:\n";
                    allGood = false;
                }
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {
            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
            errorMessage += " [!!!] ERROR: No userUUID found in XML:\n";
            allGood = false;
        }

        sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUuid");
        if (sessionUUID == "false") {
            System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
            errorMessage += " [!!!] ERROR: No sessionUUID found in XML:\n";
            allGood = false;
        }

        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
           // e.printStackTrace();
            System.out.println(" [!!!] ERROR: No paid found in XML: ");
            errorMessage += " [!!!] ERROR: No paid found in XML:\n";
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR(in tc): No entityVersion found in XML:");
            errorMessage += " [!!!] ERROR(in tc): No entityVersion found in XML:\n";
           //e.printStackTrace();
            allGood = false;
        }

        if (entityVersion == 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
            allGood = false;
        }

        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active == 0) {
            System.out.println(" [!!!] ERROR: No active found in XML: ");
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;
        }

        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {
            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
            allGood = false;
        }

        if(errorMessage=="") {
            reservationSessionObject = new Reservation_Session(0, entityVersion, active, timestamp, reservationUUID, userUUID, sessionUUID, paid, false);
            return reservationSessionObject;
        }
        else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse Reservation_Session object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e);
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e);
            }
            return null;
        }
    }


    // Assign_Task: (params) => XML
    static String getXmlForNewAssign_Task(String xmlHeaderDescription, SourceType
            Source_type, String assignTaskUuid, String userUuid, String taskUuid, int entityVersion) throws
            JAXBException {

        String messageType = "assignTaskMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.AssignTaskStructure assignTaskStructure = new XmlMessage.AssignTaskStructure(assignTaskUuid, userUuid, taskUuid, entityVersion, 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.AssignTaskMessage xmlAssignTaskMessage = new XmlMessage.AssignTaskMessage(header, assignTaskStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlAssignTaskMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }
    // Assign_Task: object => XML
    static String getXmlFromAssign_TaskObject(String xmlHeaderDescription, SourceType
            Source_type, Assign_Task thisAssignTaskObject) throws JAXBException {

        String messageType = "assignTaskMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.AssignTaskStructure AssignTaskStructure = new XmlMessage.AssignTaskStructure(thisAssignTaskObject.getAssignTaskUuid(), thisAssignTaskObject.getUserUuid(), thisAssignTaskObject.getTaskUuid(), 1, 1, getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.AssignTaskMessage xmlAssignTaskMessage = new XmlMessage.AssignTaskMessage(header, AssignTaskStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlAssignTaskMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }
    // Assign_Task: XML => Object
    static Assign_Task getAssign_TaskObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Assign_Task assignTaskObject = null;

        String assignTaskUuid = "false";
        String userUuid = "false";
        String taskUuid = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";
        String errorMessage = "";
        String xmlTotalMessage = "";

        // xmlMessage parsing

        assignTaskUuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (assignTaskUuid == "false") {

            assignTaskUuid = getSafeXmlProperty(xmlMessage, "assignTaskUuid");

            if (assignTaskUuid == "false") {

                assignTaskUuid = getSafeXmlProperty(xmlMessage, "assignTaskUUID");

                if (assignTaskUuid == "false") {
                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML");
                    errorMessage += " [!!!] ERROR: No reservationUUID found in XML\n ";
                    allGood = false;
                }
            }
        }
        userUuid = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUuid == "false") {
            System.out.println(" [!!!] ERROR: No userUuid found in XML");
            errorMessage += " [!!!] ERROR: No userUuid found in XML\n ";
            allGood = false;
        }
        taskUuid = getSafeXmlProperty(xmlMessage, "taskUuid");
        if (taskUuid == "false") {
            System.out.println(" [!!!] ERROR: No taskUuid found in XML:");
            errorMessage += " [!!!] ERROR: No taskUuid found in XML:\n";
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR(in tc): No entityVersion found in XML:");
            errorMessage += " [!!!] ERROR(in tc): No entityVersion found in XML:\n";
            //e.printStackTrace();
            allGood = false;
        }

        if (entityVersion == 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML:");
            errorMessage +=" [!!!] ERROR: No entityVersion found in XML:\n";
            allGood = false;
        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active == 0) {
            System.out.println(" [!!!] ERROR: No active found in XML:");
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;
        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {
            System.out.println(" [!!!] ERROR: No timestamp found in XML:");
            errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
            allGood = false;
        }

        if(errorMessage=="")
        {
            assignTaskObject = new Assign_Task(0, entityVersion, active, timestamp, assignTaskUuid, userUuid, taskUuid, false);
            return assignTaskObject;
        }else{
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse Assign_Task object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e);
                //e.printStackTrace();
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e);
                //e.printStackTrace();
            }
            return null;
        }

    }


    // Reservation_Event: (params) => XML
    static String getXmlForNewReservation_Event(String xmlHeaderDescription, SourceType
            Source_type, String reservationUUID, String userUUID, String eventUUID, float paid, int entityVersion)
            throws JAXBException {

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
    static String getXmlFromReservation_EventObject(String xmlHeaderDescription, SourceType
            Source_type, Reservation_Event thisReservationObject) throws JAXBException {

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
        String errorMessage = "";
        String xmlTotalMessage = "";

                // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");

            if (reservationUUID == "false") {

                reservationUUID = getSafeXmlProperty(xmlMessage, "UUID");

                if (reservationUUID == "false") {
                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML:");
                    errorMessage += " [!!!] ERROR: No reservationUUID found in XML:\n";
                    allGood = false;
                }
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {
            System.out.println(" [!!!] ERROR: No userUUID found in XML:");
            errorMessage += " [!!!] ERROR: No userUUID found in XML:\n";
            allGood = false;
        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {
            System.out.println(" [!!!] ERROR: No eventUUID found in XML:");
            errorMessage += " [!!!] ERROR: No eventUUID found in XML:\n";
            allGood = false;
        }

        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR: No paid found in XML:");
            errorMessage += " [!!!] ERROR: No paid found in XML:\n";
            //e.printStackTrace();
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            System.out.println(" [!!!] ERROR: NumberFormatException");
            errorMessage += " [!!!] ERROR: NumberFormatException\n";
            //e.printStackTrace();
        }

        if (entityVersion == 0) {
            System.out.println(" [!!!] ERROR: No entityVersion found in XML:");
            errorMessage += " [!!!] ERROR: No entityVersion found in XML:\n";
            allGood = false;
        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active < 0) {
            System.out.println(" [!!!] ERROR: No active found in XML:");
            errorMessage += " [!!!] ERROR: No active found in XML:\n";
            allGood = false;
        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {
            System.out.println(" [!!!] ERROR: No timestamp found in XML:");
            errorMessage += " [!!!] ERROR: No timestamp found in XML:\n";
            allGood = false;
        }

        if(errorMessage == "") {
            reservationEventObject = new Reservation_Event(0, entityVersion, active, timestamp, reservationUUID, userUUID, eventUUID, paid, false);
            return reservationEventObject;
        } else {
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse Reservation_Event object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e);
                //e.printStackTrace();
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e);
                //e.printStackTrace();
            }
            return null;
        }
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
    static String getXmlForTestMessage(String messageType, SourceType sourceType) throws JAXBException {

        XmlMessage.Header header = new XmlMessage.Header(messageType, "", sourceType.toString());
        XmlMessage.MessageStructure messageStructure = new XmlMessage.MessageStructure();

        XmlMessage.MessageMessage xmlTestMessage = new XmlMessage.MessageMessage(header, messageStructure);
        String xmlTotalMessage = xmlTestMessage.generateXML();
        return xmlTotalMessage;

    }
    static String getPropertyFromXml(String xml, String property) throws ParserConfigurationException,
            SAXException, IOException, NullPointerException {

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        String errorMessage = "";
        String xmlTotalMessage = "";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);

        NodeList nodes = doc.getElementsByTagName(property);

        String thisMessageType = null;
        try {
            thisMessageType = nodes.item(0).getTextContent();
        } catch (DOMException e) {
            errorMessage += " [!!!] ERROR: DOMException\n";
            System.out.println(" [!!!] ERROR: DOMException");
            //e.printStackTrace();
        }

        if(errorMessage=="") {
            return thisMessageType;
        }else{
            //4. String xmlHeaderDescription, SourceType Source_type, String timestamp;
            String headerDescription = errorMessage;
            SourceType Source_type = SourceType.Planning;

            // 5. Parse object to xml String
            try {
                xmlTotalMessage = getXmlForErrorMessage(headerDescription, Source_type);
            } catch (JAXBException e){
                System.out.print("ERROR: parser for getXmlForErrorMessage: " + e);
            }

            // 6. Send

            try {
                Sender.publishXmlMessageToQueue("monitor-queue",xmlTotalMessage);
            } catch (TimeoutException | IOException | JAXBException e) {
                System.out.print("ERROR: message sender for errorMessage: " + e);
            }
            return null;
        }
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

    //Mocking
    static boolean googleCalendarApiMocker() {

        // get calendar api options
        String[] calenderApiOptions = getGoogleCalendarApiOptions();
        boolean continueMocking = true;

        System.out.println("Choose the Google Calendar Api call to mock:\n");

        for (String option : calenderApiOptions) {
            System.out.println(option);
        }

        // prompt for input
        System.out.print("\n[.i.] Choose a number between 1 and " + calenderApiOptions.length + ". [Any other input to quit!][x: coming, o: working]\n");

        // Get chosen number
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();

        //System.out.print("You've chosen '" + choice + "' ...\n");

        switch (choice) {
            case "1":

                // List upcoming events (Google calendar 1)
                System.out.println("\nCase '" + choice + "': List upcoming events (Google calendar 1)!");

                try {

                    GoogleCalenderApi.listEvents();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case "2":

                // Create new dummy event (Google calendar 2)
                System.out.println("\nCase '" + choice + "': Create new dummy event (Google calendar 2)!");

                try {

                    com.google.api.services.calendar.Calendar service = getCalendarService();

                    GoogleCalenderApi.createDummyEvent(service);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "3":

                // Create new event (Google calendar 3)
                System.out.println("\nCase '" + choice + "': Create new event with set variables (Google calendar 3)!");

                String uuid = "axxxxxd-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
                System.out.println("Mocking Event 'MockFest' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                String headerDescription = "Mocking Event message";
                // Source_type= ... ;
                String eventName = "Mocked eventName";
                int maxAttendees = 45;
                String description = "Mocked description";
                String summary = "Mocked summary";
                String location = "Mocked location";
                String contactPerson = "Mocked contactPerson";
                String dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeStart = "2018-05-28T09:00";
                String dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
                String eventType = "MockerNoon";
                float price = 0;
                SourceType Source_type = SourceType.Planning;
                EntityType Entity_type = EntityType.Event;
                int entityVersion = 1;
                int active = 1;
                String timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                Event mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                String newEventHtmlLinkAndId = null;
                try {
                    newEventHtmlLinkAndId = GoogleCalenderApi.createEventFromEventObject(mockEvent);

                    String[] newEventProperties = newEventHtmlLinkAndId.split("-=-");

                    mockEvent.setGCAEventId(newEventProperties[1]);
                    //System.out.println("CGAEventId in newEventProperties[1]: " + newEventProperties[1]);
                    mockEvent.setGCAEventLink(newEventProperties[0]);
                    //System.out.println("CGAEventLink in newEventProperties[0]: " + newEventProperties[0]);

                } catch (IOException e) {
                    System.out.println("Error adding event to Google calendar API: " + e);
                    //e.printStackTrace();
                }

                System.out.println("Created event: " + mockEvent.toString());

                break;

            case "4":
                // Create new event with chosen variables (Google calendar 4)
                System.out.println("\nCase '" + choice + "': Create new event (Google calendar 4)!");

                uuid = "axxxxxd-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
                System.out.println("Mocking Event 'MockFest' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                // prompt for input
                System.out.print("Give the headerDescription to add: ");

                // Get chosen email
                scanner = new Scanner(System.in);
                headerDescription = scanner.next();

                // prompt for input
                System.out.print("Give the eventName to add: ");

                // Get chosen eventName
                scanner = new Scanner(System.in);
                eventName = scanner.next();

                maxAttendees = 45;
                description = "Mocked MockFest";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeStart = "2018-05-28T09:00";

                // prompt for input
                System.out.print("Give the dateTimeStart to add: (" + dateTimeStart + ")");

                // Get chosen eventName
                scanner = new Scanner(System.in);
                dateTimeStart = scanner.next();

                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
                // prompt for input
                System.out.print("Give the dateTimeEnd to add: (" + dateTimeEnd + ")");

                // Get chosen eventName
                scanner = new Scanner(System.in);
                dateTimeEnd = scanner.next();

                eventType = "MockerNoon";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                newEventHtmlLinkAndId = null;
                try {
                    newEventHtmlLinkAndId = GoogleCalenderApi.createEventFromEventObject(mockEvent);

                    String[] newEventProperties = newEventHtmlLinkAndId.split("-=-");

                    mockEvent.setGCAEventId(newEventProperties[1]);
                    //System.out.println("CGAEventId in newEventProperties[1]: " + newEventProperties[1]);
                    mockEvent.setGCAEventLink(newEventProperties[0]);
                    //System.out.println("CGAEventLink in newEventProperties[0]: " + newEventProperties[0]);

                } catch (IOException e) {
                    System.out.println("Error adding event to Google calendar API: " + e);
                    //e.printStackTrace();
                }

                System.out.println("Created event: " + mockEvent.toString());

                break;

            case "5":

                // Creating new event and updating it with preset variables  (Google calendar 5)

                uuid = "axxxxxd-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
                System.out.println("\nCase '" + choice + "': Creating new event and updating it with preset variables (Google calendar 5)!");

                // 1. Preset variables

                headerDescription = "Mocking Event message";
                // Source_type= ... ;
                eventName = "Mocked eventName";
                maxAttendees = 45;
                description = "Mocked description";
                summary = "Mocked summary";
                location = "Mocked location";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeStart = "2018-05-28T09:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
                eventType = "MockerNoon";
                price = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, false);

                newEventHtmlLinkAndId = null;
                try {
                    newEventHtmlLinkAndId = GoogleCalenderApi.createEventFromEventObject(mockEvent);

                    String[] newEventProperties = newEventHtmlLinkAndId.split("-=-");

                    mockEvent.setGCAEventId(newEventProperties[1]);
                    mockEvent.setGCAEventLink(newEventProperties[0]);
                    System.out.println("1. Succesfully created event: " + newEventProperties[0] + " with eventId: " + newEventProperties[1]);

                } catch (IOException e) {
                    System.out.println("Error adding event to Google calendar API: " + e);
                    //e.printStackTrace();
                }
/*
                System.out.println("Created event: " + mockEvent.toString());*/

                uuid = "axxxxxd-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
                System.out.println("2. Updating Event with uuid: '" + uuid + "' ...");

                // 3. Preset variables

                headerDescription = "Mocking Event message Updated";
                // Source_type= ... ;
                eventName = "Mocked eventName Updated";
                maxAttendees = 45;
                description = "Mocked description Updated";
                summary = "Mocked summary Updated";
                location = "Mocked location Updated";
                contactPerson = "Mocked contactPerson Updated";
                dateTimeStart = "2018-05-28T09:42:00+02:00";
                dateTimeStart = "2018-05-28T09:42";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:42";
                eventType = "MockerNoon Updated";
                price = 0;
                String GCAEventId = mockEvent.getGCAEventId();
                String GCAEventLink = mockEvent.getGCAEventLink();
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 4. Form Event object
                mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, GCAEventId, GCAEventLink, false);

                System.out.println("Just before updating... Waiting 5 seconds for update...");

                try {

                    for (int i = 0; i < 5; i++) {
                        System.out.print(".");
                        Thread.sleep(1000);
                    }

                    System.out.println("Starting update Event!");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String updateEventAnswer = null;
                try {

                    GoogleCalenderApi.updateEventWithEventObject(mockEvent);

                    System.out.println("3. Succesfully updated event! ");


                } catch (Exception e) {
                    System.out.println("Error updating event to Google calendar API:\n=> " + e);
                    //e.printStackTrace();
                }
/*
                // 5. Deleting event from google calendar

                try {

                    GoogleCalenderApi.cancelEventWithEventObject(mockEvent);

                    System.out.println("4. Succesfully cancelled event! ");


                } catch (Exception e) {
                    System.out.println("Error cancelling event to Google calendar API:\n=> " + e);
                    //e.printStackTrace();
                }*/
                break;

            case "6":
                // GCA: Cancel event by event and a chosen GCAEventId
                System.out.println("\nCase '" + choice + "': Creating new event and updating it with preset variables (Google calendar 5)!");

                // 1. Preset variables
                headerDescription = "Cancelling Event message Updated";
                // Source_type= ... ;
                uuid = "";
                eventName = "Mocked eventName Updated";
                maxAttendees = 45;
                description = "Mocked description Updated";
                summary = "Mocked summary Updated";
                location = "Mocked location Updated";
                contactPerson = "Mocked contactPerson Updated";
                dateTimeStart = "2018-05-28T09:42:00+02:00";
                dateTimeStart = "2018-05-28T09:42";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:42";
                eventType = "MockerNoon Updated";
                price = 0;
                GCAEventLink = "";
                Source_type = SourceType.Planning;
                Entity_type = EntityType.Event;
                entityVersion = 1;
                active = 0;
                timestamp = getCurrentDateTimeStamp();


                do {
                    System.out.print("Give the Google Calender API eventId to delete (example: 'tcostu045mdm8jd5pmi0ef9qs4') : [0 to quit] \n");
                    // Get chosen number
                    scanner = new Scanner(System.in);
                    GCAEventId = scanner.next();
                    System.out.print("\nYou've chosen '" + choice + "' ...\n");
                } while (GCAEventId.length() < 20 && GCAEventId.length() > 30 && GCAEventId != "0");

                if (GCAEventId == "0") {
                    System.out.print("You've chosen 0 to quit...");

                } else {

                    // 2. Form Event object
                    mockEvent = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, GCAEventId, GCAEventLink, false);

                    // 3. Deleting event from google calendar
                    try {

                        GoogleCalenderApi.cancelEventWithEventObject(mockEvent);

                        System.out.println("4. Succesfully cancelled event! ");


                    } catch (Exception e) {
                        System.out.println("Error cancelling event to Google calendar API:\n=> " + e);
                        //e.printStackTrace();
                    }

                }

                break;

            case "7":
                // GCA: Cancel event by GCAEventId

                // 1. Initialize Calendar service with valid OAuth credentials
                Calendar service = null;
                try {
                    service = getCalendarService();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // prompt for input
                System.out.print("Give the GCAEventId to cancel this event: ");

                // Get chosen number
                scanner = new Scanner(System.in);
                String GCAEventIdToAddUserTo = scanner.next();

                try {
                    service.events().delete("primary", GCAEventIdToAddUserTo).execute();
                    System.out.println("Success cancelling event with id: " + GCAEventIdToAddUserTo + "!");
                } catch (IOException e) {
                    System.out.print("Error: Something went wrong executing delete event with GCAID: '" + GCAEventIdToAddUserTo + "':\n=> ");
                    //e.printStackTrace();
                }
                break;

            case "8":
                // GCA: Cancel event by Uuid

                System.out.println("\nCase '" + choice + "' NOT worked out yet!");
                break;

            case "9":
                // GCA: Add user to event by GCAEventId

                System.out.println("\nCase '" + choice + "': Add user to event by GCAEventId! ");

                // prompt for input
                System.out.print("Give the email to add: ");

                // Get chosen email
                scanner = new Scanner(System.in);
                String emailToAdd = scanner.next();

                // prompt for input
                System.out.print("Give the GCAEventId to add user '" + emailToAdd + "' to: ");

                // Get chosen number
                scanner = new Scanner(System.in);
                GCAEventIdToAddUserTo = scanner.next();

                try {
                    //GoogleCalenderApi.updateEventsAttendeesWithEmailGCAEventId(GCAEventIdToAddUserTo,emailToAdd);

                    GoogleCalenderApi.addAttendeeWithEmailToEventWithGCAEventId(GCAEventIdToAddUserTo, emailToAdd);

                    System.out.println("User with email '" + emailToAdd + "' should be added to event with id '" + GCAEventIdToAddUserTo);
                } catch (Exception e) {

                    System.out.println("Error in : Case '" + choice + "': Add user to event by GCAEventId: " + e);
                }


                break;

            case "10":
                // GCA: Delete user From event by GCAEventId

                System.out.println("\nCase '" + choice + "': Delete user From event by GCAEventId! ");

                // prompt for input
                System.out.print("Give the email to delete: ");

                // Get chosen email
                scanner = new Scanner(System.in);
                String emailToDelete = scanner.next();

                // prompt for input
                System.out.print("Give the GCAEventId to delete user '" + emailToDelete + "' from: ");

                // Get chosen number
                scanner = new Scanner(System.in);
                String GCAEventIdToDeleteUserFrom = scanner.next();


                try {
                    //GoogleCalenderApi.updateEventsAttendeesWithEmailGCAEventId(GCAEventIdToAddUserTo,emailToAdd);

                    GoogleCalenderApi.deleteAttendeeWithEmailFromEventWithGCAEventId(GCAEventIdToDeleteUserFrom, emailToDelete);

                    System.out.println("User with email '" + emailToDelete + "' should be deleted to event with id '" + GCAEventIdToDeleteUserFrom);
                } catch (Exception e) {

                    System.out.println("Error in : Case '" + choice + "': Add user to event by GCAEventId: " + e);
                }

                break;

            case "11":

                GoogleCalenderApi.deleteEventsFromCurrentClient();

                break;
            case "0":

                System.out.println("You pressed 0 to quit... Quiting...");
                continueMocking = false;

                break;

            case "":
            default:

                System.out.println("You pressed '" + choice + "'... Didn't understand... Quiting...");
                continueMocking = false;
                break;
        }

        return continueMocking;

    }
    // Mock Message functionality
    static boolean xmlMessageMocker() {

        String responseFromSender = "";
        String[] xmlMessageOptions = getXmlMessageOptions();

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


        System.out.println("Choose the xml-message to mock:\n");

        for (String option : getXmlMessageOptions()) {
            System.out.println(option);
        }

        System.out.print("\nChoose a number [0 to quit!]\n");

        // Get chosen number
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        System.out.print("You've chosen '" + choice + "' ...\n");

        switch (choice) {
            case "10":
                // User with UUID

                uuid = "fxXxXxX1-1xx4-4xx2-axx4-cxxxxxxxxxx7";
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

                uuid = "fxXxXxX1-1xx4-4xx2-axx4-cxxxxxxxxxx7";
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

                uuid = "xxEVENT-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
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
                dateTimeStart = "2018-05-28T09:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
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
                dateTimeStart = "2018-05-28T09:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
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

                uuid = "xxEVENT-9xxx-4xxx-bxx2-2xxxxxxxxxxa";
                System.out.print("You've chosen '" + choice + "': Update Event with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Event message UPDATED";
                // Source_type= ... ;
                eventName = "MockFest UPDATED";
                maxAttendees = 45;
                description = "Mocked description UPDATED";
                summary = "Mocked summary UPDATED";
                location = "Mocked location UPDATED";
                contactPerson = "Mocked contactPerson";
                dateTimeStart = "2018-05-28T09:00:00+02:00";
                dateTimeStart = "2018-05-28T09:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
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

                uuid = "xSession-0a22-454d-aecc-44c19c95c261";
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
                dateTimeStart = "2018-05-28T09:00";
                dateTimeEnd = "2018-05-29T09:00:00+02:00";
                dateTimeEnd = "2018-05-29T09:00";
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
                // update Session with UUID


                uuid = "xSession-0a22-454d-aecc-44c19c95c261";
                System.out.print("You've chosen '" + choice + "': Update Session with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Session message UPDATE";
                // Source_type= ... ;
                eventUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                sessionName = "Mocked sessionName UPDATE";
                maxAttendees = 45;
                description = "Mocked description UPDATE";
                summary = "Mocked summary UPDATE";
                location = "Mocked location UPDATE";
                contactPerson = "Mocked contactPerson UPDATE";
                dateTimeStart = "2018-05-29T09:00:00+02:00";
                dateTimeStart = "2018-05-29T09:00";
                dateTimeEnd = "2018-05-30T09:00:00+02:00";
                dateTimeEnd = "2018-05-30T09:00";
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

                uuid = "RESEVENT-19f3-42de-aff4-2b5ecf1b88cb";
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

                uuid = "RESEVENT-19f3-42de-aff4-2b5ecf1b88cb";

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

                uuid = "RESSESSI-19f3-42de-aff4-2b5ecf1b88cb";
                System.out.println("Mocking Reservation_Session 'REMOCK' with uuid: '" + uuid + "' ...");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Session message";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                String sessionUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 10;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Session object

                mockReservation_Session = new Reservation_Session(0, entityVersion, 1, getCurrentDateTimeStamp(), uuid, userUuid, sessionUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_SessionObject(headerDescription, Source_type, mockReservation_Session);
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
                //Reservation_Session with chosen UUID

                System.out.print("You've chosen '" + choice + "': Reservation_Session with chosen UUID ...\n");

                // Set chosen uuid
                System.out.print("\nEnter the uuid to use: ");
                scanner = new Scanner(System.in);
                choice = scanner.next();
                uuid = choice;

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Session message with chosen uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                sessionUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 0;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 1;
                active = 1;
                timestamp = getCurrentDateTimeStamp();

                // 2. Form Reservation_Event object

                mockReservation_Session = new Reservation_Session(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, sessionUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_SessionObject(headerDescription, Source_type, mockReservation_Session);
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

                System.out.println("You've chosen '" + choice + "': New Reservation_Session without UUID ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Session message without uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                sessionUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
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

                System.out.println("Mocking Reservation_Session with uuid: '" + uuid + "' ... Other variables are preset in Helper.java around line 3750");


                // 2. Form Reservation_Event object

                mockReservation_Session = new Reservation_Session(0, 1, 1, getCurrentDateTimeStamp(), uuid, userUuid, sessionUuid, paid, false);

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

                uuid = "RESSESSI-19f3-42de-aff4-2b5ecf1b88cb";

                System.out.print("You've chosen '" + choice + "': Update Reservation_Session with UUID: '" + uuid + "' ...\n");

                // 1. Preset variables

                headerDescription = "Mocking Reservation_Session UPDATE message with uuid";
                // Source_type= ... ;
                userUuid = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                sessionUuid = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                paid = 20.42f;
                Source_type = SourceType.Planning;
                Entity_type = EntityType.ReservationEvent;
                entityVersion = 2;
                active = 1;
                timestamp = getCurrentDateTimeStamp();


                // 2. Form Reservation_Event object

                mockReservation_Session = new Reservation_Session(0, entityVersion, 1, getCurrentDateTimeStamp(), uuid, userUuid, sessionUuid, paid, false);

                //System.out.println("mockSession toString(): "+mockSession.toString());

                // 3. Form XML

                try {
                    xmlTotalMessage = getXmlFromReservation_SessionObject(headerDescription, Source_type, mockReservation_Session);
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
