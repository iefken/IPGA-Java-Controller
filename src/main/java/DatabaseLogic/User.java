package DatabaseLogic;

import AppLogic.Helper;

import java.util.Objects;

public class User extends BaseEntity{

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

    //public enum UserType {VISITOR,EMPLOYEE,ADMIN,SPONSOR,SPEAKER}


    public User(int idUser, int Entity_version, int active, String Timestamp,
                String userUUID, String lastname, String firstname, String phoneNumber, String email, String street, int houseNr, String city, int postalCode, String country, String company, Helper.EntityType type) {

        super(idUser, Entity_version, active,Timestamp);

        this.userUUID = userUUID;
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
        this.type = type;
    }

    public int getIdUser() {
        // get id from inherited class
        return this.getEntityId();
    }
    public void setIdUser(int userId) {
        this.setEntityId(userId);
    }

    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
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

    public String getPhonenumber() {
        return phoneNumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phoneNumber = phonenumber;
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

    public int getHouseNr() {
        return houseNr;
    }
    public void setHouseNr(int houseNr) {
        this.houseNr = houseNr;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(int postalCode) {
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

    public Helper.EntityType getType() {
        return type;
    }
    public void setType(Helper.EntityType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return getHouseNr() == user.getHouseNr() &&
                getPostalCode() == user.getPostalCode() &&
                Objects.equals(getUserUUID(), user.getUserUUID()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getPhonenumber(), user.getPhonenumber()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getStreet(), user.getStreet()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getCountry(), user.getCountry()) &&
                Objects.equals(getCompany(), user.getCompany()) &&
                Objects.equals(getType(), user.getType());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getUserUUID(), getLastName(), getFirstName(), getPhonenumber(), getEmail(), getStreet(), getHouseNr(), getCity(), getPostalCode(), getCountry(), getCompany(), getType());
    }

    @Override
    public String toString() {
        return "User{" +
                "userUUID='" + userUUID + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", houseNr=" + houseNr +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", type=" + type +
                '}';
    }
}
