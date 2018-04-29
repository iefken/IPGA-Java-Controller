package logic;//import GoogleCalendarApi.Quickstart;

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

    public static boolean startCliInterface() {

        int choser = 999;
        String responseFromSender = "";
        String[] senderOptions = Helper.getOptions();

        //do{}while(choser > 0 && choser <= senderOptions.length + 1);
        do {

            System.out.println("\n [.i.] What do you want to do?");

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
            System.out.print(" [.i.] You've chosen '" + choice + "' ...");

            //initialize possible variables
            boolean inputSucces = true;
            int Entity_sourceId = 420;
            String UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
            String messageType = "TestMessage";
            Helper.EntityType Entity_type = Helper.EntityType.Admin;
            Helper.SourceType Source_type = Helper.SourceType.Planning;

            //Populate CLI options here
            switch (choice) {

                // 1.1. New object without UUID:
                // create event
                case "1":

                    messageType = "EventMessage";
                    Entity_sourceId = 100;
                    Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Planning;

                    UUID = "";

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID (=>'"+UUID+"') with messageType: '"+messageType+"' and with Entity_sourceId = '"+Entity_sourceId+"'\n");

                    try {
                        responseFromSender = Sender.sendMessage1(messageType, Entity_sourceId, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\nResponseFromSender: " + responseFromSender);
                    break;

                // 2.2. New object with UUID: normally when a new message from another team is received
                // update event
                case "2":

                    messageType = "EventMessage";
                    Entity_sourceId = 1200;
                    Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Front_End;

                    UUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //responseFromSender = logic.Helper.httpPutUpdateUuidRecordVersion(UUID, Source_type);

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new local object with a UUID (=>'"+UUID+"') with messageType: '"+messageType+"' and with Entity_sourceId = '"+Entity_sourceId+"'");

                    try {
                        responseFromSender = Sender.sendMessage2(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                        System.out.println("\nResponseFromSender: " + responseFromSender);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                        System.out.println("\nERROR IN CASE2: " + e);
                    }

                    break;

                // 3.1. New object without UUID
                // create session for an event
                case "3":

                    messageType = "SessionMessage";
                    Entity_sourceId = 300;
                    Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Planning;

                    UUID = null;

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new object with a UUID (=>'"+UUID+"') with messageType: '"+messageType+"' and with Entity_sourceId = '"+Entity_sourceId+"'");

                    try {
                        responseFromSender = Sender.sendMessage1(messageType, Entity_sourceId, Entity_type, Source_type);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nResponseFromSender: " + responseFromSender);
                    break;

                // 4.2. New object with UUID: normally when a new message from another team is received
                // update record
                case "4":

                    messageType = "SessionMessage";
                    Entity_sourceId = 1400;
                    Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Front_End;

                    UUID = "d7842766-922b-45d7-a821-a37274814c5e";

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a new local object with a UUID (=>'"+UUID+"') with messageType: '"+messageType+"' and with Entity_sourceId = '"+Entity_sourceId+"'");

                    try {
                        responseFromSender = Sender.sendMessage2(messageType, Entity_sourceId, Entity_type, Source_type, UUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nresponseFromSender: " + responseFromSender);

                    break;

                // 5.3 Add user to certain session (now only updates this record in UUID API)
                //add user to session
                case "5":

                    messageType = "UpdateLocalMessage";
                    Entity_sourceId = 200;
                    //Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Planning;
                    UUID = "e0e7e624-ea01-410b-8a8f-25c551d43c25";
                    //UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";

                    System.out.println("\nCase " + choice + ": message for letting UUID manager know of a local update (=>'Entity_version++') of UUID: "+UUID+" with Entity_sourceId = '"+Entity_sourceId+"'");

                    try {
                        responseFromSender = Sender.sendMessage3(messageType, Source_type, UUID);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }

                    //System.out.println("\nresponseFromSender: " + responseFromSender);
                    break;

                // 6.4 Change entity version of a certain uuid with sourceID
                // change Entity_version
                case "6":

                    messageType = "UpdateEntityVersionMessage";
                    Entity_sourceId = 400;
                    //Entity_type=Helper.EntityType.Admin;
                    Source_type=Helper.SourceType.Planning;
                    UUID = "da4bc50d-9268-4cf6-bb52-24f7917d31fa";
                    UUID = "d7842766-922b-45d7-a821-a37274814c5e";
                    int Entity_version = 10;

                    System.out.println("\nCase " + choice + ": change Entity_version (=>'"+Entity_version+"') of UUID: "+UUID+" with Entity_sourceId = '"+Entity_sourceId+"'");

                    try {
                        responseFromSender = Sender.sendMessage4(messageType, Source_type, UUID, Entity_version);
                    } catch (IOException | TimeoutException | JAXBException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("\nresponseFromSender: " + responseFromSender);
                    break;

                case "7":
                    //get All UUID's (limit)
                    String myRecordsJsonString = "";
                    UUID="";

                    System.out.println("\nCase " + choice + ": get all UUID's locally\n");


                    //try to get all records from UUID server
                    try {
                        myRecordsJsonString = Helper.httpGetAllRecords(10);
                        //System.out.println("myRecordsJsonString: "+myRecordsJsonString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String myJsonStringWithoutEdges=myRecordsJsonString.substring(1,myRecordsJsonString.length()-1);

                    //System.out.println("myJsonStringWithoutEdges: "+myJsonStringWithoutEdges);
                    //parse response to differnt lines for readability
                    String[] UUIDRecords = new String[0];
                    try {
                        UUIDRecords = myJsonStringWithoutEdges.split("},");// '}' is deleted due to split
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for(int i = 0; i<UUIDRecords.length; i++)
                    {
                        UUIDRecords[i]+="}";// add '}' back here

                        // for printing all variables on separate lines:
                        System.out.println(i+". "+UUIDRecords[i]);
                    }
                    // for printing all variables in one line:
                    //System.out.println(Arrays.toString(UUIDRecords));

                    System.out.println("END OF UUIDS...\nPs: Nothing send to MQ");

                    break;

                case "8":
                    //Test current message"
                    // prompt for input

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

                                        responseFromSender = Sender.sendMessage3(messageType, Source_type, UUID);


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


                    break;

                case "9":

                    //tests//list events
                    /*com.google.api.services.calendar.Calendar service = getCalendarService();

                    System.out.println("Before list events:\n ");

                    Quickstart.listEvents(service);

                    System.out.println("After list events:\n ");*/

                    //break;

                default:

                    System.out.print("Ending the process!");
                    break;

            }


            System.out.println(responseFromSender);

            System.out.println("\n [.i.] End of this loop... \n");

            try {
                choser = Integer.parseInt(choice);
                //System.out.print("You entered '" + choser + "'!");
            } catch (NumberFormatException e) {

                System.out.print("You entered choice: '" + choice + "' and got choser: '"+choser+"'!");
                System.out.print("ERROR: '" + e );
                choser = 0;
            }

        } while (choser > 0 && choser <= senderOptions.length + 1);


        return true;

    }

}
