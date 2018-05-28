package XmlMessage;

import AppLogic.Helper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "basemessage")
@XmlType(propOrder = { "timestamp" })
public class ErrorStructure {

	private String timestamp;

	public ErrorStructure() {
		super();
		this.timestamp = Helper.getCurrentDateTimeStamp();
	}

	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
