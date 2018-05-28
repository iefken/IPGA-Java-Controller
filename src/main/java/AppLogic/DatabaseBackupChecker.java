package AppLogic;

import DatabaseLogic.BaseEntityDAO;
import DatabaseLogic.EntitiesToAdd;
import DatabaseLogic.EntitiesToAdd_DAO;

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
        this.thisSourceType= Helper.SourceType.Planning;
        //run();
    }
    //sends ping message based on chosen time between pings and chosen number of pings.
    public DatabaseBackupChecker(int timeBetweenChecks, int numberOfChecks) {

        //super();
        this.timeBetweenChecks = timeBetweenChecks;
        this.timestamp = Helper.getCurrentDateTimeStamp();
        this.numberOfChecks = numberOfChecks;
        this.thisSourceType= Helper.SourceType.Planning;
        //run();
    }
    //sends ping message based on chosen ping id, sourceType and time between pings.
    public DatabaseBackupChecker(int idPing, Helper.SourceType thisSourceType, int timeBetweenChecks) {

        super();

        this.idDatabaseBackupChecker=idPing;

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

        for (int i = 1; i < numberOfChecks+1; i++)
        {

            //System.out.println("PING "+i);
            try {

                // setup RMQ channel

                // check for new entities in [EntitiesToAdd]

                List<EntitiesToAdd> entitiesToAddList = new EntitiesToAdd_DAO().getRecordsFromEntitiesToAdd();

                for(EntitiesToAdd thisEntityToAdd: entitiesToAddList)
                {
                    // TODO

                    // get correct entity
                    // 2.2.1. get idUser from userUUID in User
                    String[] propertiesToSelect = {"*"};
                    String table = thisEntityToAdd.getTable() ;
                    String[] selectors = {"id"+(thisEntityToAdd.getTable())};
                    String[] values = {"" + thisEntityToAdd.getIdEntitiesToAdd()};

                    String[] selectResults = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);

                    switch (table){
                        case "User":

                            break;

                        case "Event":

                            break;

                        case "Session":

                            break;

                        case "Reservation_Event":

                            break;
                        case "Reservation_Session":

                            break;
                        case "Task":

                            break;
                        case "Assign_Task":

                            break;

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


                String xmlMessage = "";
                try {
                    xmlMessage = Helper.getXmlForPingMessage("pingMessage",this.getThisSourceType());
                } catch (JAXBException e) {
                    e.printStackTrace();
                }


                // ## send xml message to exchange

                String returnedMessage="";
                try {
                    returnedMessage = Sender.sendMessage(xmlMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

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
        DatabaseBackupChecker planningBackupDbChecker = new DatabaseBackupChecker(5000,12);
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
