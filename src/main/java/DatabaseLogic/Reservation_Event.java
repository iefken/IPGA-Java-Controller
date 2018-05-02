package DatabaseLogic;

import java.util.Objects;

public class Reservation_Event extends BaseEntity{

    private int reservationId;
    private String reservationUUID;
    private String userUUID;
    private String eventUUID;
    private String type;


    public Reservation_Event(int ReservationId, int entity_version, String Status, String Timestamp,
                             String reservationUUID, String UserUUID, String EventUUID, String Type) {

        super(ReservationId,entity_version,Status,Timestamp);

        this.reservationId=ReservationId;
        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.eventUUID = EventUUID;
        this.type = Type;
    }

    public Reservation_Event(int ReservationId, String reservationUUID, String UserUUID, String EventUUID, String Type) {

        //super(ReservationId,entity_version,Status,Timestamp);

        this.reservationId=ReservationId;
        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.eventUUID = EventUUID;
        this.type = Type;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation_Event)) return false;
        if (!super.equals(o)) return false;
        Reservation_Event that = (Reservation_Event) o;
        return Objects.equals(getReservationUUID(), that.getReservationUUID()) &&
                Objects.equals(getUserUUID(), that.getUserUUID()) &&
                Objects.equals(eventUUID, that.eventUUID) &&
                Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getReservationUUID(), getUserUUID(), eventUUID, getType());
    }

    @Override
    public String toString() {
        return "Reservation_Event{" +
                "reservationUUID='" + reservationUUID + '\'' +
                ", userUUID='" + userUUID + '\'' +
                ", eventUUID='" + eventUUID + '\'' +
                ", type='" + type + '\'' +
                ", '" + super.toString() + '\'' +
                '}';
    }
}
