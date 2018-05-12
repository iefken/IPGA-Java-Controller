package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "uuid","userUuid", "eventUuid", "sessionUuid", "reservationType", "paid", "entityVersion", "active", "timestamp" })
public class ReservationStructure {


	private String uuid;
	private String userUuid;
	private String eventUuid;
	private String sessionUuid;
	private String reservationType;
	private float paid;
	private int entityVersion;
	private int active;
	private String timestamp;

	public ReservationStructure(String ReservationUUID, String UserUUID, String EventUUID, String SessionUUID, String Type, float paid,int entityVersion, int active, String Timestamp) {

		super();
		this.uuid = ReservationUUID;
		this.userUuid = UserUUID;
		this.eventUuid = EventUUID;
		this.sessionUuid = SessionUUID;
		this.reservationType = Type;
		this.paid=paid;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = Timestamp;
	}

	public ReservationStructure() {
		
	}

	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String reservationUUID) {
		uuid = reservationUUID;
	}

	@XmlElement(name = "userUuid")
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUUID) {
		userUuid = userUUID;
	}

	@XmlElement(name = "eventUuid")
	public String getEventUuid() {
		return eventUuid;
	}
	public void setEventUuid(String eventUUID) {
		eventUuid = eventUUID;
	}

	@XmlElement(name = "sessionUuid")
	public String getSessionUuid() {
		return sessionUuid;
	}
	public void setSessionUuid(String sessionUUID) {
		sessionUuid = sessionUUID;
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
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@XmlElement(name = "reservationType")
	public String getReservationType() {
		return reservationType;
	}
	public void setReservationType(String reservationType) {
		this.reservationType = reservationType;
	}

}
