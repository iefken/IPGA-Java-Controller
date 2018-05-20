package XmlMessage;

import AppLogic.Helper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "uuid", "lastName", "firstName", "phoneNumber", "email", "street", "houseNr", "city", "postalCode", "country", "company", "userType", "entityVersion","active","timestamp" })
public class Userstructure {

	private String uuid;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private String email;
	private String street;
	private String houseNr;
	private String city;
	private String postalCode;
	private String country;
	private String company;
	private String userType;
	private int entityVersion;
	private int active;
	private String timestamp;
	
	public Userstructure(String uuid, String lastName, String firstName, String phoneNumber, String email,
						 String street, String houseNr, String city, String postalCode, String country,
						 String company, String type, int entityVersion, int active, String timestamp) {
		super();
		this.uuid = uuid;
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
		this.userType = type;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = timestamp;
	}
	
	public Userstructure() {
		
	}
/*

	@XmlElement(name = "userUUID")
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
*/

	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getHouseNr() {
		return houseNr;
	}
	public void setHouseNr(String houseNr) {
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
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

	@XmlElement(name = "userType")
	public String getUserType() {
		return userType;
	}
	public void setUserType(String type) {
		this.userType = type;
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
