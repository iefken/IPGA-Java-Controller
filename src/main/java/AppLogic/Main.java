package AppLogic;

// if you're running this on a remote server:
// https://stackoverflow.com/questions/15869784/how-to-run-a-maven-created-jar-file-using-just-the-command-line
// running the project .jar files: java -jar <jarfilename>.jar


import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import DatabaseLogic.*;
import GoogleCalendarApi.GoogleCalenderApi;

import static GoogleCalendarApi.GoogleCalenderApi.getCalendarService;

public class Main {

    public static void main(String[] argv) throws Exception {

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] ______________________IPGA-JAVA-CONTROLLER-v.1_____________________ [ooo]");
        System.out.println(" [ooo] ___________________________________________________________________ [ooo]");
        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        //cli interface

        try {

            //this boolean is set when you enter '0' or something faulty in cli
            boolean endOfCLIBoolean = startCliInterface();

            if (endOfCLIBoolean) {
                System.out.println("\n\nProcess terminated correctly!");
            } else {

                System.out.println("\n\nProcess terminated incorrectly!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println("\n\nProcess terminated incorrectly!");

        }

    }//end main

    public static boolean startCliInterface() throws JAXBException, SQLException {

        int choser = 999;
        String responseFromSender = "";
        String[] senderOptions = Helper.getOptions();

        //initialize possible variables
        boolean inputSucces = true;
        int Entity_sourceId = 420;

        //preset most used variables for testing purposes
        String UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String userUUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
        String sessionUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String eventUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String reservationUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String messageType = "TestMessage";
        String headerDescription = "Standard header description";
        String xmlTotalMessage = "<test>testertester</test>";

        Helper.EntityType Entity_type = Helper.EntityType.ADMIN;
        Helper.SourceType Source_type = Helper.SourceType.Front_End;
        int Entity_version = 1;
        int maxAttendees = 50;
        float paid = 0;

        //preset new session variables
        String sessionName = "Session name test";
        String dateTimeStart = "30/05/2018 20:00:00";
        String dateTimeEnd = "31/05/2018 08:00:00";
        String speaker = "Mr. President";
        String local = "Oval office dept.1 Room 420";
        String description = "Description for Main case (2): create new session without UUID";
        String summary = "Summary for Main case (2): create new session without UUID";
        String type = "testType (please set it to something else before using this";


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
                    userUUID="";
                    String lastName = "Case3LN";
                    String firstname = "Case3FN";
                    String phoneNumber = "Case3PN";
                    String email = "Case3EM";
                    String street = "Case3ST";
                    int houseNr = 12;
                    String city = "Case3CI";
                    int postalCode = 1212;
                    String country = "Case3COU";
                    String company = "Case3COM";
                    Helper.SourceType sourceType = Helper.SourceType.Front_End;

                    Helper.EntityType entity_type = Helper.EntityType.ADMIN;

                    // 1. create user object
                    User newUser = new User(0,1,1,Helper.getCurrentDateTimeStamp(),userUUID, lastName, firstname, phoneNumber, email, street, houseNr, city, postalCode, country, company, entity_type);


                    // 2. insert into local DB

                    int case3test=0;
                    try {
                        case3test = new User_DAO().insertIntoUser(newUser);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("newUser.getEntityId() before uuid manager call"+newUser.getEntityId());

                    // 3. create new UUID
                    try {
                        userUUID = Sender.createUuidRecord("", newUser.getEntityId(), Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    //System.out.println("\nUserUUID returned: '" + userUUID + "' !");

                    // 4. update local db with UUID
                    if(!new User_DAO().updateTablePropertyValue("User","userUUID",userUUID,"String","idUser",newUser.getEntityId()))
                    {
                        System.out.println("Something went wrong updating User's userUUID");
                    }else{
                        newUser.setUserUUID(userUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // System.out.println("MAIN: userUUID: "+newUser.getUserUUID()+" userUUID(...):"+userUUID);
                    // System.out.println("user toString MAIN: "+newUser.toString());

                    // 5. Parse user object to xml String
                    xmlTotalMessage = Helper.getXmlFromUserObject(headerDescription,Source_type,newUser);

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

                    //String eventUUID;
                    String eventName="Case 1 eventName()";
                    maxAttendees=45;
                    //String description;
                    //String summary;
                    String location="Case 1 location()";
                    String contactPerson="Case 1 contactPerson()";
                    //String dateTimeStart;
                    //String dateTimeEnd;
                    type="Case 1 EventType ";
                    float price=0;
                    int entityVersion=1;
                    int active=1;
                    String timestamp =Helper.getCurrentDateTimeStamp();

                    // 1. create Event object

                    Event newEvent = new Event(0,entityVersion,active,timestamp,eventUUID,eventName,maxAttendees,description,summary,location,contactPerson,dateTimeStart,dateTimeEnd,type,price);

                    // 2. insert to local db

                    int case1test=0;
                    try {
                        case1test = new Event_DAO().insertIntoEvent(newEvent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // 3. create UUID

                    try {
                        eventUUID = Sender.createUuidRecord(messageType, case1test, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 4. update local db with UUID

                    if(!new BaseEntityDAO().updateTablePropertyValue("Event","eventUUID",eventUUID,"String","idEvent",newEvent.getEntityId()))
                    {
                        System.out.println("Something went wrong updating Event's eventUUID");
                    }else{
                        newEvent.setEventUUID(eventUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }
                    // 5. create xml message

                    xmlTotalMessage =  Helper.getXmlForNewEvent(messageType,headerDescription,Source_type,eventUUID,eventName,maxAttendees,description,summary,location, contactPerson, type, price, entityVersion, active, dateTimeStart, dateTimeEnd);

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

                    //new session will be set in Front_End
                    Source_type = AppLogic.Helper.SourceType.Front_End;

                    // variables for session
                    messageType = "sessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.ADMIN;
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    location = "Oval office dept.1 Room 420";
                    type = "Speech";
                    Entity_version=1;
                    price=2.22f;
                    sessionUUID = "";
                    eventUUID = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                    description = "Description for Main case (2): create new session without UUID";
                    summary = "Summary for Main case (2): create new session without UUID";

                    System.out.println("\nNew Session made with sessionUUID: " + sessionUUID);

                    // 1. create session object
                    Session case2NewSession = new Session(0,1,1,Helper.getCurrentDateTimeStamp(),sessionUUID,eventUUID,sessionName,maxAttendees,description,summary,dateTimeStart,dateTimeEnd,speaker,location,type, price);

                    System.out.println("case2NewSession: "+case2NewSession.toString());

                    // 2. insert to local db
                    int case2test=0;
                    try {
                        case2test = new Session_DAO().insertIntoSession(case2NewSession);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("case2NewSession: "+case2NewSession.toString());

                    // 3. create UUID
                    try {
                        sessionUUID = Sender.createUuidRecord(messageType, case2test, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 4. update local db with UUID
                    if(!new BaseEntityDAO().updateTablePropertyValue("Session","sessionUUID",sessionUUID,"String","idSession",case2NewSession.getEntityId()))
                    {
                        System.out.println("Something went wrong updating Session's sessionUUID");
                    }else{
                        case2NewSession.setSessionUUID(sessionUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = Helper.getXmlForNewSession(headerDescription, Source_type, sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, speaker, dateTimeStart, dateTimeEnd, type, price, Entity_version,1);

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
                    messageType = "ReservationMessage";
                    //Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Visitor;
                    Source_type = Helper.SourceType.Front_End;
                    type = "Case 10 type";
                    paid=0;

                    //preset UUID's (make them fit your local db structure!!)
                    userUUID = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                    eventUUID = "e319f8aa-1910-442c-8b17-5e809d713ee4";
                    sessionUUID = "51129fa0-4a6b-44ec-aada-ff082f5db11b";

                    reservationUUID = "";

                    // 1. create Event object
                    int eventId = 0;
                    Reservation_Event newEventReservation = new Reservation_Event(0, 1, 1, Helper.getCurrentDateTimeStamp(), reservationUUID, userUUID, eventUUID, type, paid);

                    // 2. insert to local db
                    //System.out.println("Reservation to string: "+newEventReservation.toString());

                    int case10test=0;
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
                    if(!new Reservation_Event_DAO().updateTablePropertyValue("Reservation_Event","reservationUUID",reservationUUID,"String","idReservationEvent",newEventReservation.getEntityId()))
                    {
                        System.out.println("Something went wrong updating Reservation_Event's reservationUUID");
                    }else{
                        newEventReservation.setReservationUUID(reservationUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = Helper.getXmlFromReservation_EventObject(headerDescription, Source_type, newEventReservation);

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
                    messageType = "ReservationMessage";
                    //Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Visitor;
                    Source_type = Helper.SourceType.Front_End;
                    type = "Case 11 type";
                    paid=0;

                    //get userUUID
                    userUUID = "83a02f40-ee76-4ba1-9bd7-80b5a163c61e";
                    //get sessionUUID
                    sessionUUID = "51129fa0-4a6b-44ec-aada-ff082f5db11b";

                    reservationUUID = "";

                    // 1. create Event object
                    int reservationId = 0;
                    Reservation_Session newSessionReservation = new Reservation_Session(0, 1, 1, Helper.getCurrentDateTimeStamp(), reservationUUID, userUUID, sessionUUID, type, paid);

                    // 2. insert to local db
                    int case11test=0;
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
                    if(!new Reservation_Session_DAO().updateTablePropertyValue("Reservation_Session","sessionUUID",sessionUUID,"String","idReservationSession",newSessionReservation.getEntityId()))
                    {
                        System.out.println("Something went wrong updating Reservation_Event's reservationUUID");
                    }else{
                        newSessionReservation.setReservationUUID(reservationUUID);
                        //System.out.println(" HERE XXX: userUUID: "+userUUID);
                    }

                    // 5. create xml message
                    xmlTotalMessage = Helper.getXmlFromReservation_SessionObject(headerDescription, Source_type, newSessionReservation);

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
                        myRecordsJsonString = Helper.httpGetAllRecords(10);
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
                    Source_type = Helper.SourceType.Planning;
                    UUID = "531f33b6-88d1-406f-b6f3-1a0c0de9a1de";

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
                    Entity_type = Helper.EntityType.ADMIN;
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
                    Source_type = Helper.SourceType.Planning;
                    UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
                    Entity_version = 20;

                    System.out.println("\nCase " + choice + ": change Entity_version (=>'" + Entity_version + "') of UUID: " + UUID + " with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.updateUuidRecordVersionB(messageType, Source_type, UUID, Entity_version);

                        System.out.println("\nresponseFromSender: " + responseFromSender);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    break;

                    // 09. Google calendar tests
                case "9":

                    System.out.println("\nCase '" + choice + "': Google calendar tests!");

                    System.out.println(" [" + messageType + "] Trying to list events... ");

                    try {
                        System.out.println(" [" + messageType + "] 1. Trying to list events... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.listEvents(service);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println(" [" + messageType + "] 2. Trying to create dummy event... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.createDummyEvent(service);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    try {
                        System.out.println(" [" + messageType + "] 3. Trying to list events... ");
                        com.google.api.services.calendar.Calendar service = getCalendarService();

                        GoogleCalenderApi.listEvents(service);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;

                    // 10.
                case "10":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                // 11. Create new Session with UUID (insertUuidRecord,SessionMessage)
                // normally when a new message from another team is received
                case "11":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.ADMIN;
                    Source_type = Helper.SourceType.Planning;
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    location = "Oval office dept.1 Room 420";
                    type = "Speech";
                    maxAttendees = 50;
                    price=5.55f;
                    UUID = "531f33b6-88d1-406f-b6f3-1a0c0de9a1de";
                    price=0;

                    try {

                        String response = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        // No answer yet
                        // System.out.println("\nSession updated with answer: " + response);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    // 3. insert to local db

                    Session newSession = new Session(0,1,1,Helper.getCurrentDateTimeStamp(),sessionUUID,eventUUID,sessionName,maxAttendees,description,summary,dateTimeStart,dateTimeEnd,speaker,local,type,price);

                    int case5test=0;
                    try {
                        case5test = new Session_DAO().insertIntoSession(newSession);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;

                    // 12. New Reservation_Session object with UUID:
                    // normally when a new message from another team is received
                case "12":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    messageType = "ReservationMessage";
                    Entity_sourceId = 1200;
                    Entity_type = Helper.EntityType.ADMIN;
                    Source_type = Helper.SourceType.Planning;

                    UUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //responseFromSender = logic.Helper.httpPutUpdateUuidRecordVersion(UUID, Source_type);

                    // 1. create new UUID-record
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new local object with a UUID (=>'" + UUID + "') with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        System.out.println("\nResponseFromSender: " + responseFromSender);
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

}
