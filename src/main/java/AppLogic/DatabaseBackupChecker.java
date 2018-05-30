package AppLogic;

import DatabaseLogic.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DatabaseBackupChecker implements Runnable {

    private int idDatabaseBackupChecker;
    private Helper.SourceType thisSourceType;
    private int timeBetweenChecks;
    private String timestamp;
    private int numberOfChecks;

    //sends ping message based on chosen time between pings.
    public DatabaseBackupChecker(int timeBetweenChecks) {

        //super();
        this.timeBetweenChecks = timeBetweenChecks;
        this.timestamp = Helper.getCurrentDateTimeStamp();
        this.numberOfChecks = 5;
        this.thisSourceType = Helper.SourceType.Planning;
        //run();
    }

    //sends ping message based on chosen time between pings and chosen number of pings.
    public DatabaseBackupChecker(int timeBetweenChecks, int numberOfChecks) {

        //super();
        this.timeBetweenChecks = timeBetweenChecks;
        this.timestamp = Helper.getCurrentDateTimeStamp();
        this.numberOfChecks = numberOfChecks;
        this.thisSourceType = Helper.SourceType.Planning;
        //run();
    }

    //sends ping message based on chosen ping id, sourceType and time between pings.
    public DatabaseBackupChecker(int idPing, Helper.SourceType thisSourceType, int timeBetweenChecks) {

        super();

        this.idDatabaseBackupChecker = idPing;

        this.thisSourceType = thisSourceType;
        this.timeBetweenChecks = timeBetweenChecks;
        this.numberOfChecks = 10;
        this.timestamp = Helper.getCurrentDateTimeStamp();

    }


    //GETTERS & SETTERS
    public int getIdPing() {
        return idDatabaseBackupChecker;
    }

    public void setIdPing(int idPing) {
        this.idDatabaseBackupChecker = idPing;
    }

    public Helper.SourceType getThisSourceType() {
        return thisSourceType;
    }

    public void setThisSourceType(Helper.SourceType thisSourceType) {
        this.thisSourceType = thisSourceType;
    }

    public int getTimeBetweenPings() {
        return timeBetweenChecks;
    }

    public void setTimeBetweenPings(int timeBetweenPings) {
        this.timeBetweenChecks = timeBetweenPings;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setNumberOfChecks(int numberOfChecks) {
        this.numberOfChecks = numberOfChecks;
    }

    public int getNumberOfChecks() {
        return numberOfChecks;
    }

    @Override
    public void run() {
        /*
        for (int i = 0; i < this.i * 10; i++)
            System.out.println(i + ". Test: " + this.i);
        */

        for (int i = 1; i < numberOfChecks + 1; i++) {

            //System.out.println("1. Check: " + i);
            try {

                String errorMessage = "";

                // check for new entities in [EntitiesToAdd]

                List<EntitiesToAdd> entitiesToAddList = null;
                try {
                    entitiesToAddList = new EntitiesToAdd_DAO().getRecordsFromEntitiesToAdd();
                } catch (Exception e) {
                    errorMessage += "ERROR: during query: getRecordsFromEntitiesToAdd: " + e + "\n";
                    e.printStackTrace();
                }

                for (EntitiesToAdd thisEntityToAdd : entitiesToAddList) {
                    // get correct entity
                    // 2.2.1. get idUser from userUUID in User
                    String[] propertiesToSelect = {"*"};
                    String table = thisEntityToAdd.getTable();

                    String tableForId = table;
                    if (tableForId.equals("Reservation_Event") || tableForId.equals("Reservation_Session") || tableForId.equals("Assign_Task")) {
                        String[] splitter = table.split("_");
                        tableForId = splitter[0] + splitter[1];
                        System.out.println("tableForId: " + tableForId);
                    }

                    String[] selectors = {"id" + tableForId};
                    String[] values = {"" + thisEntityToAdd.getIdEntitiesToAdd()};

                    System.out.println("thisEntityToAdd: " + thisEntityToAdd.toString() + " table: " + table);

                    String[] selectResult = new String[0];
                    try {
                        selectResult = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);
                    } catch (Exception e) {
                        errorMessage += "[.!.] ERROR: getting select * from id[Table]: " + e + "\n";
                        e.printStackTrace();
                    }
//
/*
                    System.out.println("3. Check: " + i);

                    System.out.println("selectresults.length: " + selectResult.length);
                    System.out.println("selectresults['last']: " + selectResult[selectResult.length-1]);
*/

                    String[] objectProperties = new String[0];
                    try {
                        String objectString = selectResult[selectResult.length - 1];

                        //System.out.println("ObjectSTring: "+objectString);
                        objectProperties = objectString.split("', '");
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                        e.printStackTrace();
                    }
//
//                    int counter=0;
//                    for(String property: objectProperties)
//                    {
//                        counter++;
//
//                        System.out.println(counter+". property: "+property);
//                    }

                    // send rabbitMQ message

                    boolean rmqSendSuccess = false;

                    switch (table) {
                        case "User":

                            System.out.println("In User case!");

                            // 1. setup object

                            User userFromDashboard = null;

                            try {
                                userFromDashboard = new User(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], objectProperties[4],
                                        objectProperties[5], objectProperties[6], objectProperties[7], objectProperties[8], objectProperties[9],
                                        objectProperties[10], objectProperties[11], objectProperties[12], false);
                            } catch (NumberFormatException e) {
                                errorMessage += "[.!.] ERROR: setting User object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }

                            System.out.println("User object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "userFromDashboard.toString(): " + userFromDashboard.toString());

                            // 1.2. Check if active = 0

                            // 2. send to rmq
                            String xmlMessage = "";

                            try {
                                xmlMessage = Helper.getXmlFromUserObject("", thisSourceType, userFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from User object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            try {
                                Sender.publishXmlMessageToExchange(Helper.EXCHANGE_NAME, xmlMessage);
                                System.out.println("[.V.] Xml message seems to be published correctly!");
                            } catch (IOException | TimeoutException | JAXBException e) {
                                errorMessage += "[.!.] ERROR: Something went wrong publishing user xml message to the exchange:\n" + e + "\n";
                                e.printStackTrace();
                            }
                            System.out.println("Show xml: \n" + xmlMessage);

                            // 3. check if an error was catched

                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                                // 5. Send send new object to rabbitExchange
                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case "Event":

                            System.out.println("In Event case!");
                            // 1. setup object

                            Event eventFromDashboard = null;
                            float thisEventPrice = 0;

                            //System.out.println("objectProperties[11]: " + objectProperties[11]);
                            if (objectProperties[11] == "0" || objectProperties[11] == null || objectProperties[11].equals("null")) {
                                thisEventPrice=0;
                            } else {
                                try {
                                    thisEventPrice = Float.parseFloat(objectProperties[11]);
                                } catch (NumberFormatException e) {

                                    errorMessage += "[.!.] ERROR: setting Session object:\n" + e + "\n";
                                    e.printStackTrace();
                                    break;

                                }
                            }

                            System.out.println("Prop[0] (id): " + objectProperties[0] + " , prop[1]: " + objectProperties[1]);

                            try {
                                eventFromDashboard = new Event(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], Integer.parseInt(objectProperties[3]), objectProperties[4],
                                        objectProperties[5], objectProperties[6], objectProperties[7], objectProperties[8], objectProperties[9],
                                        objectProperties[10], thisEventPrice, objectProperties[12], objectProperties[13], false);
                            } catch (NumberFormatException e) {
                                errorMessage += "[.!.] ERROR: setting event object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            System.out.println("Event object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "eventFromDashboard.toString(): " + eventFromDashboard.toString());

                            // 1.2. Check if active = 0

                            // 2. send to rmq

                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromEventObject("", thisSourceType, eventFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from event object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            try {
                                Sender.publishXmlMessageToExchange(Helper.EXCHANGE_NAME, xmlMessage);
                                System.out.println("[.V.] Xml Event seems to be published correctly!");
                            } catch (IOException | TimeoutException | JAXBException e) {

                                errorMessage += "[.!.] ERROR: Something went wrong publishing event xml message to the exchange:\n" + e + "\n";
                                e.printStackTrace();

                            }
                            // 3. check if an error was catched
                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                                // 5. Send send new object to rabbitExchange
                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case "Session":

                            System.out.println("In Session case!");

                            // 1. setup object

                            Session sessionFromDashboard = null;

                            float thisSessionPrice = 0;

                            System.out.println("objectProperties[12]: " + objectProperties[12]);
                            if (objectProperties[12] == "0" || objectProperties[12] == null || objectProperties[12].equals("null")) {
                                thisSessionPrice=0; //sessionFromDashboard.setPrice(0);
                            } else {
                                try {
                                    thisSessionPrice = Float.parseFloat(objectProperties[12]);
                                } catch (NumberFormatException e) {
                                    errorMessage += "[.!.] ERROR: setting Session object:\n" + e + "\n";
                                    e.printStackTrace();
                                    break;
                                }
                            }
                            try {
                                sessionFromDashboard = new Session(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], Integer.parseInt(objectProperties[4]),
                                        objectProperties[5], objectProperties[6], objectProperties[7], objectProperties[8], objectProperties[9],
                                        objectProperties[13], objectProperties[14], objectProperties[15], objectProperties[16], thisSessionPrice, false);
                            } catch (NumberFormatException e) {

                                errorMessage += "[.!.] ERROR: setting Session object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            //User userFromDashboard = new User(objectProperties[0],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1])

                            System.out.println("Session object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "sessionFromDashboard.toString(): " + sessionFromDashboard.toString());
                            // 2. send to rmq
                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromSessionObject("", thisSourceType, sessionFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from Session object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            try {
                                Sender.publishXmlMessageToExchange(Helper.EXCHANGE_NAME, xmlMessage);
                                System.out.println("[.V.] Session Xml message seems to be published correctly!");
                            } catch (IOException | TimeoutException | JAXBException e) {

                                errorMessage += "[.!.] ERROR: Something went wrong publishing event xml message to the exchange:\n" + e + "\n";
                                e.printStackTrace();

                            }
                            // 3. check if an error was catched

                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }

                                // 5. Send new error object to rabbitExchange

                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Getting entities for table '" + table + "' is not worked out yet!");
                            break;


                        case "Reservation_Event":

                            System.out.println("In Reservation_Event case!");
                            // TODO TEST THIS

                            // 1. setup object

                            Reservation_Event reservation_EventFromDashboard = null;

                            float thisReservation_EventPrice = 0;

                            try {

                                if (objectProperties[4] == "0" || objectProperties[4] == null || objectProperties[4].equals("null")) {
                                    //System.out.println("Float.parseFloat(objectProperties[4]): "+Float.parseFloat(objectProperties[4]));
                                    thisReservation_EventPrice=0;
                                } else {
                                    System.out.println("Error: "+objectProperties[4]);
                                    thisReservation_EventPrice = Float.parseFloat(objectProperties[4]);
                                }

                            } catch (NumberFormatException e) {
                                errorMessage += "[.X.] ERROR: setting Reservation_Event object PRICE PROPERTY:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }

                            try {
                                reservation_EventFromDashboard = new Reservation_Event(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], thisReservation_EventPrice, false);
                            } catch (NumberFormatException e) {
                                errorMessage += "[.x.] ERROR: setting Reservation_Event object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            System.out.println("Reservation_Event object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "reservation_EventFromDashboard.toString(): " + reservation_EventFromDashboard.toString());
                            // 2. send to rmq

                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromReservation_EventObject("", thisSourceType, reservation_EventFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from Reservation_Event object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            // 3. check if an error was catched
                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }

                                // 5. Send send new object to rabbitExchange

                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Getting entities for table '" + table + "' is not worked out yet!");
                            break;

                        case "Reservation_Session":

                            System.out.println("In Reservation_Session case!");
                            // TODO TEST THIS

                            // 1. setup object

                            Reservation_Session reservation_SessionFromDashboard = null;

                            float thisReservation_SessionPrice = 0;

                            if (objectProperties[4] == "0" || objectProperties[4] == null || objectProperties[4].equals("null")) {
                                //System.out.println("Float.parseFloat(objectProperties[4]): " + Float.parseFloat(objectProperties[4]));
                                thisReservation_SessionPrice=0;
                            } else {
                                try {
                                    thisReservation_SessionPrice = Float.parseFloat(objectProperties[4]);
                                } catch (NumberFormatException e) {
                                    errorMessage += "[.!.] ERROR: setting Reservation_Session object:\n" + e + "\n";
                                    e.printStackTrace();
                                    break;
                                }
                            }
                            try {
                                reservation_SessionFromDashboard = new Reservation_Session(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], thisReservation_SessionPrice, false);


                            } catch (NumberFormatException e) {
                                errorMessage += "[.!.] ERROR: setting Reservation_Session object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            //User userFromDashboard = new User(objectProperties[0],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1])

                            System.out.println("Reservation_Session object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "reservation_SessionFromDashboard.toString(): " + reservation_SessionFromDashboard.toString());
                            // 2. send to rmq

                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromReservation_SessionObject("", thisSourceType, reservation_SessionFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from Reservation_Session object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            // 3. check if an error was catched
                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }

                                // 5. Send send new object to rabbitExchange

                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Getting entities for table '" + table + "' is not worked out yet!");
                            break;

                        case "Task":

                            System.out.println("In Task case!");
                            // TODO TEST THIS

                            // 1. setup object

                            Task taskFromDashboard = null;

                            try {
                                taskFromDashboard = new Task(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], objectProperties[4], objectProperties[5], false);
                            } catch (NumberFormatException e) {
                                errorMessage += "[.!.] ERROR: setting Task object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }

                            System.out.println("Task object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "taskFromDashboard.toString(): " + taskFromDashboard.toString());
                            // 2. send to rmq
                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromTaskObject("", thisSourceType, taskFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from Task object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            // 3. check if an error was catched
                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }

                                // 5. Send send new object to rabbitExchange

                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Getting entities for table '" + table + "' is not worked out yet!");
                            break;

                        case "Assign_Task":

                            System.out.println("In Assign_Task case!");
                            // TODO TEST THIS

                            // 1. setup object

                            Assign_Task assign_TaskFromDashboard = null;

                            try {
                                assign_TaskFromDashboard = new Assign_Task(Integer.parseInt(objectProperties[0]), thisEntityToAdd.getEntity_version(), thisEntityToAdd.getActive(), Helper.getCurrentDateTimeStamp(),
                                        objectProperties[1], objectProperties[2], objectProperties[3], false);
                            } catch (NumberFormatException e) {
                                errorMessage += "[.!.] ERROR: setting event object:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            //User userFromDashboard = new User(objectProperties[0],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1],objectProperties[1])

                            System.out.println("Assign_Task object from Dashboard (planning db EntitiesToAdd) => RabbitMQ:\n" +
                                    "assign_TaskFromDashboard.toString(): " + assign_TaskFromDashboard.toString());
                            // 2. send to rmq

                            xmlMessage = "";
                            try {
                                xmlMessage = Helper.getXmlFromAssign_TaskObject("", thisSourceType, assign_TaskFromDashboard);
                            } catch (JAXBException e) {
                                errorMessage += "[.!.] ERROR: getting xml from Session object: xmlMessage: " + xmlMessage + "\nError:\n" + e + "\n";
                                e.printStackTrace();
                                break;
                            }
                            // 3. check if an error was catched
                            if (errorMessage != "") {
                                // 4. Parse error object to xml String
                                try {
                                    xmlMessage = Helper.getXmlForErrorMessage(errorMessage, thisSourceType);
                                } catch (JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong getting error xml message:\n" + e + "\n";
                                    e.printStackTrace();
                                }

                                // 5. Send send new object to rabbitExchange

                                try {
                                    Sender.sendMessage(xmlMessage);
                                } catch (TimeoutException | IOException | JAXBException e) {
                                    errorMessage += "[.!.] ERROR: Something went wrong publishing error xml message to the exchange:\n" + e + "\n";
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Getting entities for table '" + table + "' is not worked out yet!");

                            break;

                    }
                    // last: update EntitiesToAdd record 'status' to UPTODATE
                    if (errorMessage == "") {
                        try {
                            // updateTablePropertyValue(String table, String property, String value, String valueType, String whereProperty, String whereValue) {
                            if (!new BaseEntityDAO().updateTablePropertyValue("EntitiesToAdd", "status", "UPTODATE", "String", "idEntitiesToAdd", "" + thisEntityToAdd.getIdEntitiesToAdd())) {
                                errorMessage += "[.!.] ERROR: updating table with id '" + thisEntityToAdd.getIdEntitiesToAdd() + "'";
                            }
                        } catch (Exception e) {
                            errorMessage += "[.!.] ERROR: Exception during updateTablePropertyValue:\n " + e + "\n ";
                            e.printStackTrace();
                        }
                    } else {
                        String headerDescription = errorMessage;

                        Helper.SourceType Source_type = Helper.SourceType.Planning;

                        String xmlTotalMessage = "";
                        // 5. Parse user object to xml String
                        try {
                            xmlTotalMessage = Helper.getXmlForErrorMessage(headerDescription, Source_type);
                        } catch (JAXBException e) {
                            System.out.print("\nERROR: parser for getXmlForErrorMessage:\n");
                            e.printStackTrace();
                        }

                        // 6. Send
                        try {
                            Sender.publishXmlMessageToQueue("monitor-queue", xmlTotalMessage);
                        } catch (TimeoutException | IOException | JAXBException e) {
                            System.out.print("\nERROR: message sender for errorMessage:\n");
                            e.printStackTrace();
                        }
                    }
/*
                    if(thisEntityToAdd){

                    }*/
                    // 1) check if active == 0
                    // => softdelete in local db, xmlmessage with active = 0 to queue, api call to cancel from calenar
                    // 2) check if entity_version > 1 => update in local db, xmlmessage to queue, api call to update calendar
                    // 3) if entity_version == 1 => insert

                }

                // foreach newEntity:
                // {
                // 1) check if active == 0
                // => softdelete in local db, xmlmessage with active = 0 to queue, api call to cancel from calenar
                // 2) check if entity_version > 1 => update in local db, xmlmessage to queue, api call to update calendar
                // 3) if entity_version == 1 => insert

                // }


                // Full message Ping sleeping for 'timeBetweenPings' seconds
                // System.out.println("Ping ("+i+"/"+numberOfPings+")! 'Sleeping' for '"+this.timeBetweenPings/1000+"' seconds...");

                System.out.print(".");
                Thread.sleep(this.timeBetweenChecks);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] argv) throws Exception {

        System.out.println("1. Start of main\nMaking DatabaseBackupChecker");
        DatabaseBackupChecker planningBackupDbChecker = new DatabaseBackupChecker(30000, 12);
        //PingSender ping2 = new PingSender(5);

        System.out.println("2. Making Thread");
        Thread thread1 = new Thread(planningBackupDbChecker);
        //Thread thread2 = new Thread(ping2);

        System.out.println("3. Starting thread");
        thread1.start();
        //thread2.start();

        System.out.println("4. End of main");

    }

}
