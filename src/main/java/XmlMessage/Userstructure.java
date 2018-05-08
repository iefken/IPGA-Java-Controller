package XmlMessage;

import AppLogic.Helper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "usermessage")
@XmlType(propOrder = { "userUUID", "lastName", "firstName", "phoneNumber", "email", "street", "houseNr", "city", "postalCode", "country", "company", "type", "entityVersion","active","timestamp" })
public class Userstructure {

	private String userUUID;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private String email;
	private String street;
	private int houseNr;
	private String city;
	private int postalCode;
	private String country;
	private String company;
	private Helper.EntityType type;
	private int entityVersion;
	private int active;
	private String timestamp;
	
	public Userstructure(String userUUID, String lastName, String firstName, String phoneNumber, String email,
						 String street, int houseNr, String city, int postalCode, String country,
						 String company, Helper.EntityType type, int entityVersion, int active, String timestamp) {
		super();
		this.userUUID = userUUID;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.street = street;
		this.houseNr = houseNr;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.company = company;
		this.type = type;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = timestamp;
	}
	
	public Userstructure() {
		
	}

	@XmlElement(name = "userUUID")
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}


	@XmlElement(name = "lastName")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement(name = "firstName")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement(name = "phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "street")
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	@XmlElement(name = "houseNr")
	public int getHouseNr() {
		return houseNr;
	}
	public void setHouseNr(int houseNr) {
		this.houseNr = houseNr;
	}

	@XmlElement(name = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "postalCode")
	public int getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	@XmlElement(name = "country")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	@XmlElement(name = "company")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	@XmlElement(name = "type")
	public Helper.EntityType getType() {
		return type;
	}
	public void setType(Helper.EntityType type) {
		this.type = type;
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



}
