package DatabaseLogic;

import AppLogic.Helper;

public class dbtestclasse {

    public static void main(String[] argv) throws Exception {

        String reservationUUID = "a94a0212-3065-426c-b41a-9dd9b46fd861";
        String UserUUID= "e0e7e624-ea01-410b-8a8f-25c551d43c25";
        String SessionUUID= "e0e7e624-ea01-410b-8a8f-25c551d43c25";
        String Type;

        Reservation_Session newSessionReservation = new Reservation_Session(0, 1, "1", Helper.getCurrentDateTimeStamp(), reservationUUID, UserUUID, SessionUUID, "Session");




    }
}

