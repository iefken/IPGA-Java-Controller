package DatabaseLogic;

import java.util.Objects;

public class User extends BaseEntity{

    //private String userUUID;
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

    //public enum UserType {VISITOR,EMPLOYEE,ADMIN,SPONSOR,SPEAKER}


    public User(int idUser, int Entity_version, int active, String timestamp,
                String userUUID, String lastname, String firstname, String phoneNumber, String email, String street, String houseNr, String city, String postalCode, String country, String company, String type) {

        super(idUser, Entity_version, active, timestamp);

        this.uuid = userUUID;
        this.lastName = lastname;
        this.firstName = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
        this.houseNr = houseNr;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.company = company;
        this.userType = type;
    }

    public User(int idUser, int Entity_version, int active, String Timestamp,
                String userUUID, String lastname, String firstname, String phoneNumber, String email, String street, String houseNr, String city, String postalCode, String country, String company, String type, boolean insertBaseEntity) {

        super(idUser, Entity_version, active, Timestamp, insertBaseEntity);

        this.uuid = userUUID;
        this.lastName = lastname;
        this.firstName = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
        this.houseNr = houseNr;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.company = company;
        this.userType = type;
    }


    public int getIdUser() {
        // get id from inherited class
        return this.getEntityId();
    }
    public void setIdUser(int userId) {
        this.setEntityId(userId);
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastname() {
        return lastName;
    }
    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getFirstname() {
        return firstName;
    }
    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNr() {
        return houseNr;
    }
    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String type) {
        this.userType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(getUuid(), user.getUuid()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getPhoneNumber(), user.getPhoneNumber()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getStreet(), user.getStreet()) &&
                Objects.equals(getHouseNr(), user.getHouseNr()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getPostalCode(), user.getPostalCode()) &&
                Objects.equals(getCountry(), user.getCountry()) &&
                Objects.equals(getCompany(), user.getCompany()) &&
                Objects.equals(getUserType(), user.getUserType());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getUuid(), getLastName(), getFirstName(), getPhoneNumber(), getEmail(), getStreet(), getHouseNr(), getCity(), getPostalCode(), getCountry(), getCompany(), getUserType());
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", houseNr='" + houseNr + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", userType='" + userType + '\'' +
                ", {'" + super.toString() + "'}" +
                '}';
    }
}
