package DatabaseLogic;

import java.util.Objects;

public class Reservation_Session extends BaseEntity{

    private String reservationUUID;
    private String userUUID;
    private String sessionUUID;
//    private String type;
    private float paid;

    public Reservation_Session(int ReservationId, int entity_version, int active, String Timestamp,
                               String reservationUUID, String UserUUID, String SessionUUID, /*String Type,*/ float paid) {

        super(ReservationId,entity_version,active, Timestamp);

        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.sessionUUID = SessionUUID;
//        this.type = Type;
        this.paid = paid;
    }
    public Reservation_Session(int ReservationId, String reservationUUID, String UserUUID, String SessionUUID, /*String Type,*/ float paid) {

        //super(ReservationId,entity_version,Status, Timestamp);

        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.sessionUUID = SessionUUID;
//        this.type = Type;
        this.paid = paid;
    }

    public int getReservationId() {
        // get id from inherited class
        return this.getEntityId();
    }
    public void setReservationId(int reservationId) {
        this.setEntityId(reservationId);
    }


    public String getReservationUUID() {
        return reservationUUID;
    }
    public void setReservationUUID(String reservationUUID) {
        this.reservationUUID = reservationUUID;
    }

    public String getUserUUID(){
        return userUUID;
    }
    public void setUserUUID(String userUUID){
        this.userUUID=userUUID;
    }

    public String getSessionUUID(){
        return sessionUUID;
    }
    public void setSessionUUID(String sessionUUID){
        this.sessionUUID=sessionUUID;
    }

//    public String getType() {
//        return type;
//    }
//    public void setType(String type) {
//        this.type = type;
//    }

    public float getPaid() {
        return paid;
    }
    public void setPaid(float paid) {
        this.paid = paid;
    }

}
