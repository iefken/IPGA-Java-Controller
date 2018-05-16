package DatabaseLogic;

import java.util.Objects;

public class Reservation_Event extends BaseEntity{

    private String reservationUUID;
    private String userUUID;
    private String eventUUID;
    //private String type;
    private float paid;


    public Reservation_Event(int ReservationId, int entity_version, int active, String Timestamp,
                             String reservationUUID, String UserUUID, String EventUUID, float paid) {

        super(ReservationId,entity_version,active,Timestamp);

        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.eventUUID = EventUUID;
        //this.type = Type;
        this.paid = paid;
    }

    public Reservation_Event(int ReservationId, String reservationUUID, String UserUUID, String EventUUID, String Type, float paid) {

        //super(ReservationId,entity_version,active,Timestamp);

        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.eventUUID = EventUUID;
        //this.type = Type;
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

    public String getEventUUID(){
        return eventUUID;
    }
    public void setEventUUID(String eventUUID){
        this.eventUUID=eventUUID;
    }

    public float getPaid() {
        return paid;
    }
    public void setPaid(float paid) {
        this.paid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation_Event)) return false;
        if (!super.equals(o)) return false;
        Reservation_Event that = (Reservation_Event) o;
        return Float.compare(that.getPaid(), getPaid()) == 0 &&
                Objects.equals(getReservationUUID(), that.getReservationUUID()) &&
                Objects.equals(getUserUUID(), that.getUserUUID()) &&
                Objects.equals(getEventUUID(), that.getEventUUID());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getReservationUUID(), getUserUUID(), getEventUUID(), getPaid());
    }

    @Override
    public String toString() {
        return "Reservation_Event{" +
                "reservationUUID='" + reservationUUID + '\'' +
                ", userUUID='" + userUUID + '\'' +
                ", eventUUID='" + eventUUID + '\'' +
                ", paid=" + paid +
                ", {'" + super.toString() + "'}" +
                '}';
    }
}
