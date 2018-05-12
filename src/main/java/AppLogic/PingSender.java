package AppLogic;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PingSender implements Runnable {

    private int idPing;
    private Helper.SourceType thisSourceType;
    private int timeBetweenPings;
    private String timestamp;
    private int numberOfPings;

    public PingSender(int timeBetweenPings) {

        //super();
        this.timeBetweenPings = timeBetweenPings;
        this.timestamp = Helper.getCurrentDateTimeStamp();
        this.numberOfPings = 5;
        this.thisSourceType= Helper.SourceType.Monitor;
        //run();
    }
    public PingSender(int timeBetweenPings, int numberOfPings) {

        //super();
        this.timeBetweenPings = timeBetweenPings;
        this.timestamp = Helper.getCurrentDateTimeStamp();
        this.numberOfPings = numberOfPings;
        this.thisSourceType= Helper.SourceType.Monitor;
        //run();
    }
    public PingSender(int idPing, Helper.SourceType thisSourceType,int timeBetweenPings) {

        super();
        numberOfPings++;

        this.idPing=idPing;

        this.thisSourceType = thisSourceType;
        this.timeBetweenPings = timeBetweenPings;
        this.numberOfPings = 10;
        this.timestamp = Helper.getCurrentDateTimeStamp();

    }
    public PingSender(int idPing, Helper.SourceType thisSourceType,int timeBetweenPings, int numberOfPings) {

        super();
        numberOfPings++;
        if(idPing==0)
        {
            this.idPing=numberOfPings;
        }else{
            this.idPing=idPing;
        }
        this.thisSourceType = thisSourceType;
        this.timeBetweenPings = timeBetweenPings;
        this.timestamp = Helper.getCurrentDateTimeStamp();

    }



    public int getIdPing() {
        return idPing;
    }
    public void setIdPing(int idPing) {
        this.idPing = idPing;
    }

    public Helper.SourceType getThisSourceType() {
        return thisSourceType;
    }
    public void setThisSourceType(Helper.SourceType thisSourceType) {
        this.thisSourceType = thisSourceType;
    }

    public int getTimeBetweenPings() {
        return timeBetweenPings;
    }
    public void setTimeBetweenPings(int timeBetweenPings) {
        this.timeBetweenPings = timeBetweenPings;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setNumberOfPings(int numberOfPings) {
        this.numberOfPings = numberOfPings;
    }
    public int getNumberOfPings() {
        return numberOfPings;
    }

    @Override
    public void run() {
        /*
        for (int i = 0; i < this.i * 10; i++)
            System.out.println(i + ". Test: " + this.i);
        */

        for (int i = 1; i < numberOfPings+1; i++)
        {

            //System.out.println("PING "+i);
            try {

                // # send pingmessage

                // ## save pingmessage to localdb

                // TO DO

                // ## form xml pingmessage

                String xmlMessage = "";
                try {
                    xmlMessage = Helper.getXmlForPingMessage("PingMessage",this.getThisSourceType());
                } catch (JAXBException e) {
                    e.printStackTrace();
                }


                // ## send xml message to exchange

                String returnedMessage="";
                try {
                    returnedMessage = Sender.sendPingMessage(xmlMessage,this.getThisSourceType());
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
                Thread.sleep(this.timeBetweenPings);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] argv) throws Exception {

        System.out.println("1. Start of main\nMaking PingSenders");
        PingSender ping1 = new PingSender(0,Helper.SourceType.Planning,5000,12);
        //PingSender ping2 = new PingSender(5);

        System.out.println("2. Making Threads");
        Thread thread1 = new Thread(ping1);
        //Thread thread2 = new Thread(ping2);

        System.out.println("3. Starting threads");
        thread1.start();
        //thread2.start();

        System.out.println("4. End of main");

    }

}
