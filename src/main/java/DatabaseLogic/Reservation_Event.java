package DatabaseLogic;

import java.util.Objects;

public class Reservation_Event extends BaseEntity{

    private String uuid;
    private String userUUID;
    private String eventUUID;
    private float paid;


    //CONSTRUCTOR
    public Reservation_Event(int ReservationId, int entity_version, int active, String timestamp,
                             String reservationUUID, String userUUID, String eventUUID, float paid) {

        super(ReservationId,entity_version,active,timestamp);

        this.uuid = reservationUUID;
        this.userUUID = userUUID;
        this.eventUUID = eventUUID;
        this.paid = paid;
    }
    public Reservation_Event(int ReservationId, int entity_version, int active, String timestamp,
                             String reservationUUID, String userUUID, String eventUUID, float paid, boolean insertBaseEntity) {

        super(ReservationId, entity_version, active, timestamp, insertBaseEntity);

        this.uuid = reservationUUID;
        this.userUUID = userUUID;
        this.eventUUID = eventUUID;
        this.paid = paid;
    }

    //GETTERS & SETTERS
    public int getReservationId() {
        // get id from inherited class
        return this.getEntityId();
    }

    public void setReservationId(int reservationId) {
        this.setEntityId(reservationId);
    }

    public String getReservationUUID() {
        return uuid;
    }
    public void setReservationUUID(String reservationUUID) {
        this.uuid = reservationUUID;
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

    //OTHER
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
                "uuid='" + uuid + '\'' +
                ", userUUID='" + userUUID + '\'' +
                ", eventUUID='" + eventUUID + '\'' +
                ", paid=" + paid +
                ", {'" + super.toString() + "'}" +
                '}';
    }
}
