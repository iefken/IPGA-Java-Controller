package GoogleCalendarApi;

import AppLogic.Helper;
import DatabaseLogic.BaseEntityDAO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AddUserToEvent {

    // 1. Class initializers

    /** Application name. */
    private static final String APPLICATION_NAME = "Integration Project Group A: Planning: Google Calendar API";

    /** Directory to store user credentials for this application. */

    // private static final java.io.File DATA_STORE_DIR = new java.io.File( System.getProperty("user.home"), ".credentials/calendar-integration-groupA-java");
    // private static final java.io.File DATA_STORE_DIR2 = new java.io.File( System.getProperty("user.home"), "../opt/lampp/htdocs/Java-Application/IPGA-Java-Controller-git/IPGA-Java-Controller/out/calendar-integration-groupA-java");

    // For server deployment
    //private static final java.io.File DATA_STORE_DIR = new java.io.File( "/opt/lampp/htdocs/Java-Application/IPGA-Java-Controller-git/IPGA-Java-Controller/out/calendar-integration-groupA-java");

    // For local deployment
    private static final File DATA_STORE_DIR = new File( "C:/Users/ief.falot/Documents/GitHub/PLANNING/out/calendar-integration-groupA-java");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;


    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */

   private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {

        // see bottom for tests
        InputStream in = null;

        try {

            String test = DATA_STORE_DIR.getAbsolutePath()+"/client_secret.json";

            //System.out.println("test : "+test);

            in = new FileInputStream(test);


            //in = new FileInputStream("C:/Users/ief.falot/.credentials/client_secret.json");
           } catch (FileNotFoundException|NullPointerException e) {

            System.out.print("\n<Exception 1:> "+e);
            try{
                in = new FileInputStream(DATA_STORE_DIR.getAbsolutePath()+"/client_secret.json");

            }catch (FileNotFoundException ee) {

                System.out.print("\n<Exception 2:> "+ee);
                //ee.printStackTrace();
            }
        }


        //in = new FileInputStream("C:\\Users\\ief.falot\\.credentials\\calendar-integration-groupA-java\\StoredCredential");
        Credential credential = null;

        //Now you can read the file content with in


        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.

        GoogleAuthorizationCodeFlow flow = null;
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(DATA_STORE_FACTORY)
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();

        }
        try {
            credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Calendar getCalendarService() throws IOException {
        System.out.print("Authorizing now...");
        Credential credential = authorize();
        return new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }



    // 3. READ

    public static void listEvents() throws IOException
    {

        Calendar service = getCalendarService();

        service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events:");
            int counter = 1;
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("=> Event "+counter+":\n1. Start: (%s) 2. GCAID: [%s] 3. Summary: [%s]\n4. Link: '%s'\n", start, event.getId(), event.getSummary(), event.getHtmlLink());

                counter++;
            }
        }
    }

    // 4. UPDATE

    public static void updateEventsAttendees(String eventUuid, String userUuid){

        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 1. get GCAEventId from uuid
        String[] propertiesToSelect = {"GCAEventId"};
        String table = "Event";
        String[] selectors = {"uuid"};
        String[] values= {eventUuid};

        String GCAEventId = null;
        try {
            GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];
        } catch (Exception e) {

            table = "Session";
            try {
                GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];
            } catch (Exception ee) {


                ee.printStackTrace();
            }
        }

        // 2. Retrieve the event from the API
        Event event = null;
        try {
            event = service.events().get("primary", GCAEventId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Retrieve the email adress from the userUuid

        propertiesToSelect[0] = "email";
        table = "User";
        selectors[0] = "uuid";
        values[0] = userUuid;

        String clientEmailAddress = null;
        try {
            clientEmailAddress = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 3. Change local variables in object
        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(clientEmailAddress)
        };
        event.setAttendees(Arrays.asList(attendees));

        // 4. Update the event
        try {
            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

            System.out.println("Succes adding user ('"+clientEmailAddress+"')to event with id: '"+event.getId()+"' @ GMT-"+updatedEvent.getUpdated());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updateEventsAttendeesWithEmail(String eventUuid, String email){

        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 1. get GCAEventId from uuid
        String[] propertiesToSelect = {"GCAEventId"};
        String table = "Event";
        String[] selectors = {"uuid"};
        String[] values= {eventUuid};

        String GCAEventId = null;
        try {
            GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];
        } catch (Exception e) {

            System.out.println("Something went wrong getting uuid in Event table, trying to check whether it exists in Session table.");
            table = "Session";
            try {
                GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];
            } catch (Exception ee) {


                System.out.println("Something went wrong getting uuid in both Event and Session table:\n< 1 >: "+e+"\n< 2 >: "+ee);
                ee.printStackTrace();
            }
        }

        // 2. Retrieve the event from the API
        Event event = null;
        try {
            event = service.events().get("primary", GCAEventId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Change local variables in object
        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(email)
        };
        event.setAttendees(Arrays.asList(attendees));

        // 4. Update the event
        try {
            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

            System.out.println("Succes adding user ('"+email+"') to event with id: '"+event.getId()+"' @ GMT-"+updatedEvent.getUpdated());

        } catch (IOException e) {
            System.out.println("Error adding user ('"+email+"') to event with id: '"+event.getId()+"' @ GMT-"+Helper.getCurrentDateTimeStamp()+" !");

            e.printStackTrace();
        }

    }

    public static void updateEventsAttendeesWithEmailGCAEventId(String GCAEventId, String email){

        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Retrieve the event from the API
        Event event = null;
        try {
            event = service.events().get("primary", GCAEventId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Change local variables in object
        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(email)
        };
        event.setAttendees(Arrays.asList(attendees));

        // 4. Update the event
        try {
            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

            System.out.println("Succes adding user ('"+email+"') to event with id: '"+event.getId()+"' @ GMT-"+updatedEvent.getUpdated());

        } catch (IOException e) {
            System.out.println("Error adding user ('"+email+"') to event with id: '"+event.getId()+"' @ GMT-"+Helper.getCurrentDateTimeStamp()+" !");

            e.printStackTrace();
        }

    }

    public static void updateEventWithEventObject(DatabaseLogic.Event updateEventObject)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            // 1. get GCAEventId from uuid
            String[] propertiesToSelect = {"GCAEventId"};
            String table = "Event";
            String[] selectors = {"uuid"};
            String[] values= {updateEventObject.getEventUUID()};
            String GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];

            updateEventObject.setGCAEventId(GCAEventId);

            String eventId = updateEventObject.getGCAEventId();

            // 2. Retrieve the event from the API
            Event event = service.events().get("primary", eventId).execute();

            // 3. Change local variables in object
            event.setSummary(updateEventObject.getSummary());
            event.setLocation(updateEventObject.getLocation());
            event.setDescription(updateEventObject.getDescription());

            String dts = updateEventObject.getDateTimeStart()+":00+02:00";
            DateTime startDateTime = new DateTime(dts);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Europe/Brussels");
            event.setStart(start);

            String dte = updateEventObject.getDateTimeEnd()+":00+02:00";
            DateTime endDateTime = new DateTime(dte);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Europe/Brussels");
            event.setEnd(end);

            // 4. Update the event
            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();


            System.out.println("Succes updating event with id: '"+event.getId()+"' @ GMT-"+updatedEvent.getUpdated());

        } catch (Exception e) {

            System.out.println("Error updating event:\n=> "+e);
            //e.printStackTrace();
        }
    }
    public static void updateSessionWithSessionObject(DatabaseLogic.Session updateSessionObject)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            // 1. get GCAEventId from uuid
            String[] propertiesToSelect = {"GCAEventId"};
            String table = "Session";
            String[] selectors = {"uuid"};
            String[] values= {updateSessionObject.getSessionUUID()};
            String GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];

            updateSessionObject.setGCAEventId(GCAEventId);

            String eventId = updateSessionObject.getGCAEventId();

            // 2. Retrieve the event from the API
            Event event = null;
            try {
                event = service.events().get("primary", eventId).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 3. Change local variables in object
            event.setSummary(updateSessionObject.getSummary());
            event.setLocation(updateSessionObject.getLocation());
            event.setDescription(updateSessionObject.getDescription());

            String dts = updateSessionObject.getDateTimeStart()+":00+02:00";
            DateTime startDateTime = new DateTime(dts);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Europe/Brussels");
            event.setStart(start);

            String dte = updateSessionObject.getDateTimeEnd()+":00+02:00";
            DateTime endDateTime = new DateTime(dte);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Europe/Brussels");
            event.setEnd(end);

            // 4. Update the event
            Event updatedEvent = service.events().update("primary", event.getId(), event).execute();


            System.out.println("Succes updating Session with id: '"+event.getId()+"' @ GMT-"+updatedEvent.getUpdated());

        } catch (Exception e) {

            System.out.println("Error updating event:\n=> "+e);
            //e.printStackTrace();
        }
    }

    // 5. DELETE

    public static void cancelSessionWithSessionObject(DatabaseLogic.Session cancelEventObject)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. get GCAEventId from uuid
        String[] propertiesToSelect = {"GCAEventId"};
        String table = "Session";
        String[] selectors = {"uuid"};
        String[] values= {cancelEventObject.getSessionUUID()};
        String GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];

        cancelEventObject.setGCAEventId(GCAEventId);

        String eventId = cancelEventObject.getGCAEventId();

        // 2. Delete the event through the API
        try {
            service.events().delete("primary", eventId).execute();
            System.out.println("Success cancelling session with id: "+cancelEventObject.getGCAEventId()+"!");
        } catch (IOException e) {
            System.out.print("Error: Something went wrong executing delete session with GCAID: '"+cancelEventObject.getGCAEventId()+"':\n=> ");
            //e.printStackTrace();
        }
    }
    public static void cancelEventWithEventObject(DatabaseLogic.Event cancelEventObject)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. get GCAEventId from uuid
        String[] propertiesToSelect = {"GCAEventId"};
        String table = "Event";
        String[] selectors = {"uuid"};
        String[] values= {cancelEventObject.getEventUUID()};
        String GCAEventId = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values)[0];

        cancelEventObject.setGCAEventId(GCAEventId);

        String eventId = cancelEventObject.getGCAEventId();
        // 2. Delete the event through the API
        try {
            service.events().delete("primary", eventId).execute();
            System.out.println("Success cancelling event with id: "+cancelEventObject.getGCAEventId()+"!");
        } catch (IOException e) {
            System.out.print("Error: Something went wrong executing delete event with GCAID: '"+cancelEventObject.getGCAEventId()+"':\n=> ");
            //e.printStackTrace();
        }
    }
    public static void cancelEventByGCAID(String GCAIDToCancel)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Delete the event through the API
        try {
            service.events().delete("primary", GCAIDToCancel).execute();
            System.out.println("Succes cancelling event with GCAID: "+GCAIDToCancel+"!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cancelEventByUuid(String UuidToCancel)
    {
        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] propertiesToSelect = {"GCAEventId"};
        String table = "Event";
        String[] selectors = {"uuid"};
        String[] values = {"" + UuidToCancel};

        String[] GCAIDToCancel = new BaseEntityDAO().getPropertyValueByTableAndProperty(propertiesToSelect, table, selectors, values);


        // 2. Delete the event through the API
        try {
            service.events().delete("primary", GCAIDToCancel[0]).execute();
            System.out.println("Succes cancelling event with GCAID: '"+GCAIDToCancel[0]+"' and uuid: '"+UuidToCancel+"'!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) throws Exception {

        // 1. Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


/*
        // client id: 7137768012-bitbouglth8dbmicpcm5cu5lb9b36ogq.apps.googleusercontent.com
        // client secret: sIwX3rpUsc3UQMxzaXjAqprO

        // Load client secrets.
        // InputStream in = Quickstart.class.getResourceAsStream("/client_secret.json");

        // C:\Users\ief.falot\Documents\GitHub\PLANNING\src\main\java\GoogleCalendarApi\cred
        //in = new FileInputStream("C:\\Users\\ief.falot\\.credentials\\calendar-integration-groupA-java\\client_secret.json");
        //InputStream in = new FileInputStream("C:\\Users\\W\\Documents\\GitHub\\PLANNING\\src\\main\\java\\GoogleCalendarApi\\cred\\client_secret.json");

        //InputStream in = new FileInputStream("C:\\Users\\ief.falot\\.credentials\\calendar-integration-groupA-java\\client_secret.json");
        //InputStream in = new FileInputStream("C:\\Users\\Wissa\\Documents\\INTEGRATION-WORKSPACE\\PLANNING\\src\\main\\java\\GoogleCalendarApi\\cred\\client_secret.json");

        // InputStream in = new FileInputStream("/opt/lampp/htdocs/Java-Application/IPGA-Java-Controller-git/IPGA-Java-Controller/src/main/java/GoogleCalendarApi/cred/client_secret.json");
        // SmbFile fileToRead= new SmbFile("smb://10.3.50.38/export/myFile.txt");
        try (FileOutputStream fos = new FileOutputStream("smb://iwtsl.ehb.be/~ief.falot/Integration/GoogleCalendarApi/client_secret.json")){
            URL url = new URL("smb://iwtsl.ehb.be/~ief.falot/Integration/GoogleCalendarApi/client_secret.json");
            URLConnection connection = url.openConnection();
            IOUtils.copy( connection.getInputStream(),  fos);
        }catch(Exception e)
        {
            System.out.println("Error getting FileOutputStream: "+e);
        }
        SmbFile in= null;
        try {
            in = new SmbFile("smb://github.com/iefken/IPGA-Java-Controller/src/main/java/GoogleCalendarApi/cred/client_secret.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // "smb://github.com/iefken/IPGA-Java-Controller/src/main/java/GoogleCalendarApi/cred/client_secret.json"
        // "smb://github.com/iefken/IPGA-Java-Controller/blob/master/src/main/java/GoogleCalendarApi/cred/client_secret.json"
        // http://dtsl.ehb.be/~ief.falot/Integration/GoogleCalendarApi/client_secret.json
        //InputStream test = in.getInputStream();
        //String url = "https://github.com/iefken/IPGA-Java-Controller/blob/master/src/main/java/GoogleCalendarApi/cred/client_secret.json";

        url = "http://dtsl.ehb.be/~ief.falot/Integration/GoogleCalendarApi/client_secret.json";
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (IOException e) {
            System.out.println("ERROR: "+e);
            // e.printStackTrace();
        }

        String message ="ERROR";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            //String jsonText = readAll(rd);
            message = rd.lines().collect(Collectors.joining());
            //JSONObject json = new JSONObject(jsonText);
        } finally {
            is.close();
        }

        InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));

        InputStream is = new FileInputStream(in);

        // url = "http://dtsl.ehb.be/~ief.falot/Integration/GoogleCalendarApi/client_secret.json";
        HttpURLConnection connec = (HttpURLConnection)new URL(url).openConnection();
        if(connec.getResponseCode() != connec.HTTP_OK)
        {
            System.err.println("HttpUrlConnection: Not OK");
            return credential;
        }else{

            System.out.println("HttpUrlConnection: OK!");
            System.out.println("HttpUrlConnection: length = " + connec.getContentLength());
            System.out.println("HttpUrlConnection: Type = " + connec.getContentType());
            System.out.println("HttpUrlConnection: InputStream = {\n" + connec.getInputStream().toString()+"\n}\n");
        }

        InputStream in = connec.getInputStream();
            in = new FileInputStream("C:\\Users\\ief.falot\\Documents\\GitHub\\PLANNING\\src\\main\\java\\GoogleCalendarApi\\cred\\client_secret.json");
            in = GoogleCalenderApi.class.getResourceAsStream("cred/client_secret.json");
        */
