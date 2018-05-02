package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "reservationmessage")
@XmlType(propOrder = { "reservationId","reservationUUID","userUUID", "sessionUUID", "status", "timestamp", "type" })
public class ReservationStructure {

	private int reservationId;
	private String ReservationUUID;
	private String UserUUID;
	private String SessionUUID;
	private String Status;
	private String Timestamp;
	private String Type;

	public ReservationStructure(int reservationId,  String ReservationUUID, String UserUUID, String SessionUUID, String Status, String Timestamp, String Type) {
		super();
		this.reservationId=reservationId;
		this.ReservationUUID = ReservationUUID;
		this.UserUUID = UserUUID;
		this.SessionUUID = SessionUUID;
		this.Status = Status;
		this.Timestamp = Timestamp;
		this.Type = Type;
	}

	public ReservationStructure() {
		
	}

	@XmlElement(name = "reservationId")
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
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

	@XmlElement(name = "sessionUUID")
	public String getSessionUUID() {
		return SessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		SessionUUID = sessionUUID;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
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
