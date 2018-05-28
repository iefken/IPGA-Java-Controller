
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

        try {

            //this boolean is set when you enter '0' or something faulty in cli
            boolean endOfCLIBoolean = Helper.startCliInterface();
            if (endOfCLIBoolean) {
                System.out.println("\n\nProcess terminated correctly!");
            } else {
                System.out.println("\n\nProcess terminated incorrectly!");
            }
        } catch (Exception e) {
            System.out.println("\n\nProcess terminated incorrectly: "+e+"\n\n");
            e.printStackTrace();
        }
    }//end main
}
