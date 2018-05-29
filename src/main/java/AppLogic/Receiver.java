package AppLogic;

import DatabaseLogic.*;

import com.rabbitmq.client.*;

public class Receiver {

    public static void startDatabaseChecker() {

        // start different thread for database check
        try{
            // # Send pingmessage every 'timeBetweenPings' milliseconds
            int timeBetweenChecks = 2500;

            // ## make new pingSender object
            DatabaseBackupChecker myDatabaseBackupChecker = new DatabaseBackupChecker(5000, 2000);

            // ## setup new pingSender thread
            Thread dbCheckThread = new Thread(myDatabaseBackupChecker);

            // ## start new pingSender thread
            dbCheckThread.start();

        }catch(Exception e)
        {
            System.out.println("Error during backupDbChecker thread startup: "+e);
        }
    }
    public static void startReceiver() {

        // this initializes rabbit mq connection with variables set in BaseRMQ().setRmqChannel();
        Channel channel = new BaseRMQ().getRmqChannel();
    }


    public static void main(String[] argv) throws Exception {

        startDatabaseChecker();
        startReceiver();

    } //END of Receiver class
}