package DatabaseLogic;

import java.util.Objects;

public class Reservation_Session extends BaseEntity{

    private int reservationId;
    private String reservationUUID;
    private String userUUID;
    private String sessionUUID;
    private String type;

    public Reservation_Session(int ReservationId, int entity_version, String Status, String Timestamp,
                               String reservationUUID, String UserUUID, String SessionUUID, String Type) {

        super(ReservationId,entity_version,Status, Timestamp);

        this.reservationId = ReservationId;
        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.sessionUUID = SessionUUID;
        this.type = Type;
    }
    public Reservation_Session(int ReservationId, String reservationUUID, String UserUUID, String SessionUUID, String Type) {

        //super(ReservationId,entity_version,Status, Timestamp);

        this.reservationId = ReservationId;
        this.reservationUUID = reservationUUID;
        this.userUUID = UserUUID;
        this.sessionUUID = SessionUUID;
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

    public String getSessionUUID(){
        return sessionUUID;
    }
    public void setSessionUUID(String sessionUUID){
        this.sessionUUID=sessionUUID;
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
        if (!(o instanceof Reservation_Session)) return false;
        if (!super.equals(o)) return false;
        Reservation_Session that = (Reservation_Session) o;
        return Objects.equals(getReservationUUID(), that.getReservationUUID()) &&
                Objects.equals(getUserUUID(), that.getUserUUID()) &&
                Objects.equals(getSessionUUID(), that.getSessionUUID()) &&
                Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getReservationUUID(), getUserUUID(), getSessionUUID(), getType());
    }

    @Override
    public String toString() {
        return "Reservation_Session{" +
                "reservationUUID='" + reservationUUID + '\'' +
                ", userUUID='" + userUUID + '\'' +
                ", sessionUUID='" + sessionUUID + '\'' +
                ", type='" + type + '\'' +
                ", '" + super.toString() + '\'' +
                '}';
    }
}
