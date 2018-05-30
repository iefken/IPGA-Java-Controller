package PlanningTests;

public class FlowTest {

    enum EntityType {EMPTY, User, Event, Session, ReservationEvent, ReservationSession, Product, Purchase}
    private EntityType sendThisEntity;

    enum SourceType {Front_End, Planning, Monitor, Kassa, CRM, Facturatie}
    private SourceType sendAsThisSource;

    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;

    //For setting mainCLI options in main
    static String[] getOptions10() {

        //Add CLI options here
        String[] options = {
                "[01.x] Start Full Planning test:\n" +
                        "1. Add event, 2. Add session, 3. Add user, 4. Add reservationEvent, 5. Add reservationSession\n" +
                        "6. Edit event, 7. Edit session, 8. Edit user, 9. Edit reservationEvent, 10. Edit reservationSession\n" +
                        "11. Cancel event, 12. Cancel session, 13. Delete user, 14. Cancel reservationEvent, 15. Cancel reservationSession\n" +
                "[11.] /",
                "[12.] /",

        };
        return options;
    }



}
