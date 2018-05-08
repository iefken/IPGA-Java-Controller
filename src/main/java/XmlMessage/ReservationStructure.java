package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "reservationmessage")
@XmlType(propOrder = { "reservationUUID","userUUID", "eventUUID", "sessionUUID", "type", "paid", "entityVersion", "active", "timestamp" })
public class ReservationStructure {


	private String ReservationUUID;
	private String UserUUID;
	private String EventUUID;
	private String SessionUUID;
	private String Type;
	private float paid;
	private int entityVersion;
	private int active;
	private String Timestamp;

	public ReservationStructure(String ReservationUUID, String UserUUID, String EventUUID, String SessionUUID, String Type, float paid,int entityVersion, int active, String Timestamp) {

		super();
		this.ReservationUUID = ReservationUUID;
		this.UserUUID = UserUUID;
		this.EventUUID = EventUUID;
		this.SessionUUID = SessionUUID;
		this.Type = Type;
		this.paid=paid;
		this.entityVersion = entityVersion;
		this.active = active;
		this.Timestamp = Timestamp;
	}

	public ReservationStructure() {
		
	}


	@XmlElement(name = "reservationUUID")
	public String getReservationUUID() {
		return ReservationUUID;
	}
	public void setReservationUUID(String reservationUUID) {
		ReservationUUID = reservationUUID;
	}

	@XmlElement(name = "userUUID")
	public String getUserUUID() {
		return UserUUID;
	}
	public void setUserUUID(String userUUID) {
		UserUUID = userUUID;
	}

	@XmlElement(name = "eventUUID")
	public String getEventUUID() {
		return EventUUID;
	}
	public void setEventUUID(String eventUUID) {
		EventUUID = eventUUID;
	}

	@XmlElement(name = "sessionUUID")
	public String getSessionUUID() {
		return SessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		SessionUUID = sessionUUID;
	}

	@XmlElement(name = "paid")
	public float getPaid() {
		return paid;
	}
	public void setPaid(float paid) {
		this.paid = paid;
	}

	@XmlElement(name = "entityVersion")
	public int getEntityVersion() {
		return entityVersion;
	}
	public void setEntityVersion(int entityVersion) {
		this.entityVersion = entityVersion;
	}

	@XmlElement(name = "active")
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}

	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	@XmlElement(name = "type")
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}

}
