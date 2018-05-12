package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")

@XmlType(propOrder = { "uuid","status", "type", "timestamp" })
public class MessageStructure {

	private String Uuid;
	private String Status;
	private String Type;
	private String Timestamp;

	public MessageStructure(String Uuid,String Status, String Type, String Timestamp) {
		super();
		this.Uuid = Uuid;
		this.Status = Status;
		this.Type = Type;
		this.Timestamp = Timestamp;
	}

	public MessageStructure() {
		
	}


	@XmlElement(name = "uuid")
	public String getUuid() {
		return Uuid;
	}

	public void setUuid(String Uuid) {
		this.Uuid = Uuid;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@XmlElement(name = "type")
	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
}
