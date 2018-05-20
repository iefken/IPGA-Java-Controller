package XmlMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.StringReader;
import java.io.StringWriter;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "header", "datastructure", "footer" })

public class ReservationSessionMessage {
	private Header header;
	private ReservationSessionStructure datastructure;
	private Footer footer;

	public ReservationSessionMessage(Header header, ReservationSessionStructure reservationStructure) {
		super();
		this.header = header;
		this.datastructure = reservationStructure;
	}

	public ReservationSessionMessage() {

	}

	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Footer getFooter() {
		return footer;
	}
	public void setFooter(Footer footer) {
		this.footer = footer;
	}



	public ReservationSessionStructure getDatastructure() {
		return datastructure;
	}

	public void setDatastructure(ReservationSessionStructure datastructure) {
		this.datastructure = datastructure;
	}

	//deze functie genereert de XML adhv de huidige data: eerst de data toevoegen en dan pas deze functie aanroepen
	public String generateXML() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(this.getClass());
		Marshaller m = context.createMarshaller();
		//m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //string
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // formatted
		StringWriter sw = new StringWriter();
		
		String checksum = Footer.generateChecksum(datastructure);
		footer = new Footer(checksum);
		
		m.marshal(this, sw);
		
		return sw.toString();
	}

	//think there's an error with the reservationmessages here somehow for the receiver

	public static ReservationSessionMessage generateObject(String xml) throws JAXBException {
	    JAXBContext jaxbContext = JAXBContext.newInstance(ReservationSessionMessage.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    StringReader reader = new StringReader(xml);
		ReservationSessionMessage reservationMessage = null;
	    try{

	    	reservationMessage = (ReservationSessionMessage) jaxbUnmarshaller.unmarshal(reader);

		}catch (Exception e)
		{
			System.out.println("Error: ");
			System.out.println(e);
		}


	    return reservationMessage;
	  }
}
