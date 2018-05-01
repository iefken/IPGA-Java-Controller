package logic;//import GoogleCalendarApi.Quickstart;

// if you're running this on a remote server:
// https://stackoverflow.com/questions/15869784/how-to-run-a-maven-created-jar-file-using-just-the-command-line
// running the project .jar files: java -jar <jarfilename>.jar

import GoogleCalendarApi.Quickstart;
import logic.Helper;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//import static GoogleCalendarApi.Quickstart.getCalendarService;

public class Main {

    public static void main(String[] argv) throws Exception {

        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println(" [ooo] _________________________________________________________________________________________________________________ [ooo]");
        System.out.println(" [ooo] _____________________________________________IPGA-JAVA-CONTROLLER-v.1____________________________________________ [ooo]");
        System.out.println(" [ooo] _________________________________________________________________________________________________________________ [ooo]");
        System.out.println(" [ooo] -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- [ooo]");
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

    public static boolean startCliInterface() throws JAXBException {

        int choser = 999;
        String responseFromSender = "";
        String[] senderOptions = Helper.getOptions();

        //initialize possible variables
        boolean inputSucces = true;
        int Entity_sourceId = 420;

        //preset most used variables
        String UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String userUUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
        String sessionUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String eventUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
        String messageType = "TestMessage";
        String headerDescription = "Standard header description";
        String xmlTotalMessage = "<test>testertester</test>";

        Helper.EntityType Entity_type = Helper.EntityType.Admin;
        Helper.SourceType Source_type = Helper.SourceType.Planning;
        int Entity_version = 1;

        //preset new session variables
        String sessionName = "Session name test";
        String dateTimeStart = "30/05/2018 20:00:00";
        String dateTimeEnd = "31/05/2018 08:00:00";
        String speaker = "Mr. President";
        String local = "Oval office dept.1 Room 420";
        String type = "Speech";

        //do{}while(choser > 0 && choser <= senderOptions.length + 1);
        do {

            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\n [.i.] What do you want to do?\n");

            // print options
            for (String option : senderOptions) {
                System.out.println(option);
            }

            // create scanner to read command-line input
            Scanner scanner = new Scanner(System.in);

            // prompt for input
            System.out.print("\n [.i.] Choose a number between 1 and " + senderOptions.length + ". [Any other input to quit!][x: coming, o: working]\n");

            // get input from command-line
            String choice = scanner.next();
            System.out.print(" [.i.] You've chosen '" + choice + "' ...\n");


            //Populate CLI options here
            switch (choice) {

                // 1.1. New object without UUID:
                // create Reservation
                case "1":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                //2. Create new Session without UUID (1)
                case "2":

                    // create new SessionUUID
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new session object without a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.Admin;

                    //new session will be set in Front_End
                    Source_type = Helper.SourceType.Front_End;

                    // variables for session
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    local = "Oval office dept.1 Room 420";
                    type = "Speech";

                    UUID = "";

                    try {
                        UUID = Sender.createUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\nNew Session made with UUID: " + UUID);

                    // 2. create xml message
                    xmlTotalMessage = Helper.getXmlForSession(messageType, headerDescription, Source_type, UUID, sessionName, dateTimeStart, dateTimeEnd, speaker, local, type, 1);

                    // 3. insert to local db

                    // TO DO

                    // 4. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;



                    // 03. Create new User without UUID (1)
                    // create user
                case "3":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;


                    // 04. Create new Event with UUID (1)
                    // create event
                case "4":
                    messageType = "EventMessage";
                    Entity_sourceId = 300;
                    Entity_type = Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;

                    UUID = "780df99e-7254-4166-8898-ad31a77eb4be";

                    // 1. create new UUID
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID (=>'" + UUID + "') with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nResponseFromSender: " + responseFromSender);


                    // 2. no need to send to exchange


                    break;

                    // 05. Create new Session with UUID (insertUuidRecord,SessionMessage)
                    // normally when a new message from another team is received
                case "5":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object without a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.Admin;

                    Source_type = Helper.SourceType.Planning;

                    // variables for session
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    local = "Oval office dept.1 Room 420";
                    type = "Speech";

                    UUID = "531f33b6-88d1-406f-b6f3-1a0c0de9a1de";

                    try {

                        String response = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        // No answer yet
                        // System.out.println("\nSession updated with answer: " + response);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

//                    System.out.println("\nNew Session made with UUID: " + UUID);

                    // 2. create xml message
                    xmlTotalMessage = Helper.getXmlForSession(messageType, headerDescription, Source_type, UUID, sessionName, dateTimeStart, dateTimeEnd, speaker, local, type, 1);

                    // 3. insert to local db

                    // TO DO

                    // 4. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 06. create new User with UUID
                case "6":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                    // 07. Update Event (UpdateUuidRecordVersion,EventMessage)"
                case "7":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                    // 08. Update Session (UpdateUuidRecordVersion,SessionMessage)"
                case "8":

                    // update session
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object without a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;


                    // variables for session
                    sessionName = "Session RENAME test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President2";
                    local = "Oval office dept.2 Room 1420";
                    type = "Speech";

                    UUID = "531f33b6-88d1-406f-b6f3-1a0c0de9a1de";

                    try {

                        Entity_version = Sender.updateUuidRecordVersion(messageType, Source_type, UUID);
                        // No answer yet
                        System.out.println("\nSession updated with answer: " + Entity_version);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

//                    System.out.println("\nNew Session made with UUID: " + UUID);

                    // 2. create xml message
                    xmlTotalMessage = Helper.getXmlForSession(messageType, headerDescription, Source_type, UUID, sessionName, dateTimeStart, dateTimeEnd, speaker, local, type, 1);

                    System.out.println("\n xmlTotalMessage: \n" + xmlTotalMessage);
                    // 3. insert to local db

                    // TO DO

                    // 4. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                    // 09. Update User (UpdateUuidRecordVersion,UserMessage)
                case "9":

                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                    // 10. Add User to Event
                case "10":

                    // not quiet correct yet

                    //preset variables
                    messageType = "ReservationMessage";
                    //Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Visitor;
                    Source_type = Helper.SourceType.Planning;


                    //get userUUID
                    userUUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //get eventUUID
                    eventUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";

                    //insert into local db

                    // TO DO

                    // send ReservationMessage to exchange
                    System.out.println("\nCase " + choice + ": message for adding a userUUID: " + userUUID + " to a sessionUUID = '" + sessionUUID + "'");

                    try {
                        responseFromSender = Sender.sendReservationMessage(messageType, Source_type, userUUID, sessionUUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    break;

                    //11. Add User to Session
                case "11":

                    //preset variables
                    messageType = "ReservationMessage";
                    //Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Visitor;
                    Source_type = Helper.SourceType.Planning;

                    //get userUUID
                    userUUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //get sessionUUID
                    sessionUUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";

                    //insert into local db

                    // TO DO


                    // send ReservationMessage to exchange
                    System.out.println("\nCase " + choice + ": message for adding a userUUID: " + userUUID + " to a sessionUUID = '" + sessionUUID + "'");

                    try {
                        responseFromSender = Sender.sendReservationMessage(messageType, Source_type, userUUID, sessionUUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    break;

                //12. get All UUID's (limit)
                case "12":

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

                    System.out.println("END OF UUIDS...\nPs: Nothing send to MQ");

                    //13. Fill in a (test message)
                case "13":

/*
                    do {//id
                        System.out.print("What's the id of your new session?\n");

                        try {

                            // get input from command-line
                            Entity_sourceId = Integer.parseInt(scanner.next());
                        } catch (NumberFormatException e) {

                            System.out.print("Please enter a number!");
                            inputSucces = false;
                        }

                    } while (!inputSucces);

                    while (inputSucces) {//messageType
                        System.out.print("What's the message type of your new session? (ReservationMessage,SessionMessage,TestMessage,ListEventsMessage,GetAllUUIDs)\n");

                        try {
                            // get input from command-line
                            messageType = scanner.next();

                        } catch (Exception e) {

                            System.out.print("Please enter a correct message type!");
                            inputSucces = false;
                            if (messageType == "0") {
                                inputSucces = true;
                            }
                        }

                    }

                    String possibleUUID = "";
                    do {

                        System.out.print("Do you want to add a UUID? (0, no for no)\n");

                        try {

                            // get input from command-line
                            possibleUUID = scanner.next();

                            System.out.println("you pressed: '" + possibleUUID + "'.");
                            if (possibleUUID == "0" || possibleUUID == "" || possibleUUID.toLowerCase() == "no" || possibleUUID.toLowerCase() == "n") {
                                UUID = "";
                                System.out.println("UUID: '" + UUID + "'.");
                            } else {
                                try {
                                    // check if UUID input is valid
                                    // https://bukkit.org/threads/best-way-to-check-if-a-string-is-a-uuid.258625/

                                    if (!UUID.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[0-9a-f]{12}")) {
                                        inputSucces = false;
                                        System.out.print("Please enter a correct UUID!");

                                    } else {

                                        int newEntity_version = 0;
                                        newEntity_version = Sender.updateUuidRecordVersion(messageType, Source_type, UUID);


                                    }
                                } catch (Exception e) {
                                    System.out.print("Error: 1. Please enter a correct UUID!");
                                    System.out.print("Error: 2. " + e);
                                }

                            }

                        } catch (Exception e) {

                            inputSucces = false;
                            System.out.print("Please enter a correct message type!");

                            //in case you get stuck in the loop
                            if (messageType == "0") {
                                inputSucces = true;
                            }
                        }

                    } while (!inputSucces);

*/
                    System.out.println("Not working yet!");
                    break;

                // 14.1. New Reservation object without UUID:
                // create Reservation
                case "14":

                    messageType = "ReservationMessage";
                    Entity_sourceId = 100;
                    Entity_type = Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;

                    UUID = "";

                    // 1. create new UUID
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID (=>'" + UUID + "') with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'\n");


                    try {
                        responseFromSender = Sender.createUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }


                    System.out.println("\nResponseFromSender: " + responseFromSender);

                    // 2. send new object to exchange


                    break;

                // 15.2. New Reservation object with UUID:
                // normally when a new message from another team is received
                case "15":

                    messageType = "ReservationMessage";
                    Entity_sourceId = 1200;
                    Entity_type = Helper.EntityType.Admin;
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

                //16. New Session without UUID
                case "16":

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new session object without a UUID with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    // preset variables (should be set later)
                    messageType = "SessionMessage";
                    Entity_sourceId = 1600;
                    Entity_type = Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Front_End;

                    // variables for session
                    sessionName = "Session name test";
                    dateTimeStart = "30/05/2018 20:00:00";
                    dateTimeEnd = "31/05/2018 08:00:00";
                    speaker = "Mr. President";
                    local = "Oval office dept.1 Room 420";
                    type = "Speech";

                    UUID = "";

                    try {
                        UUID = Sender.createUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\nNew Session made with UUID: " + UUID);

                    // 2. create xml message
                    xmlTotalMessage = Helper.getXmlForSession(messageType, headerDescription, Source_type, UUID, sessionName, dateTimeStart, dateTimeEnd, speaker, local, type, 1);

                    // 3. insert to local db

                    // TO DO

                    // 4. send new object to exchange

                    try {
                        Sender.sendMessage(xmlTotalMessage);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;
                //17. New Session with UUID: normally when a new message from another team is received
                case "17":

                    messageType = "SessionMessage";
                    Entity_sourceId = 1400;
                    Entity_type = Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;

                    UUID = "d7842766-922b-45d7-a821-a37274814c5e";

                    // 1. insert new UUID
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new local object with a UUID (=>'" + UUID + "') with messageType: '" + messageType + "' and with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.insertUuidRecord(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nresponseFromSender: " + responseFromSender);

                    // 2. no need to send to exchange

                    break;

                // 18.3 Alter existing entity and update UUID mgr
                // update event, update session, add User to session
                case "18":
                    messageType = "UpdateLocalMessage";
                    Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;
                    UUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";

                    xmlTotalMessage = "";
                    String description = "Standard description set in sendMessage3()";

                    int newEntity_version = 0;
                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a local update (=>'Entity_version++') of UUID: " + UUID + " with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        newEntity_version = Sender.updateUuidRecordVersion(messageType, Source_type, UUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\nnewEntity_version: " + newEntity_version);

                    //Process xml

                    xmlTotalMessage = Helper.getOurXmlMessage(messageType, description, Source_type, UUID);
                    //System.out.println("Generated XML: " + xmlTotalMessage);

                    //Send message

                    break;

                // 19.4 Change entity version
                // Alter record directly in UUID (select on UUID and SOURCE)
                case "19":

                    messageType = "UpdateEntityVersionMessage";
                    Entity_sourceId = 424;
                    //Entity_type=Helper.EntityType.Admin;
                    Source_type = Helper.SourceType.Planning;
                    UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
                    //UUID = "d7842766-922b-45d7-a821-a37274814c5e";
                    Entity_version = 20;

                    System.out.println("\nCase " + choice + ": change Entity_version (=>'" + Entity_version + "') of UUID: " + UUID + " with Entity_sourceId = '" + Entity_sourceId + "'");

                    try {
                        responseFromSender = Sender.updateUuidRecordVersionB(messageType, Source_type, UUID, Entity_version);

                        System.out.println("\nresponseFromSender: " + responseFromSender);

                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("\nresponseFromSender: " + responseFromSender);

                    break;

                case "listEvents":
                case "20":


                    System.out.println("\nCase '" + choice + "' not worked out yet!");

                    break;

                //tests//list events
                    /*com.google.api.services.calendar.Calendar service = getCalendarService();
                    System.out.println("Before list events:\n ");
                    Quickstart.listEvents(service);
                    System.out.println("After list events:\n ");*/


                default:

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
