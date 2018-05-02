package DatabaseLogic;

import java.util.Objects;

public class User extends BaseEntity{

    private String userUUID;
    private String lastName;
    private String firstName;
    private String phonenumber;
    private String email;
    private String address;
    private String company;

    //public enum UserType {Attendant,Speaker,Employee,Sponsor,Admin,Consultant;}

    private String type;
    private String paymentStatus;

    public User(int userId, int Entity_version, String Status, String Timestamp,
                String userUUID, String lastname, String firstname, String phonenumber, String email, String address, String company, String type, String paymentStatus) {

        super(userId, Entity_version, Status,Timestamp);

        this.userUUID = userUUID;
        this.lastName = lastname;
        this.firstName = firstname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.address = address;
        this.company = company;
        this.type = type;
        this.paymentStatus = paymentStatus;
    }

    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        userUUID = userUUID;
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
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(getUserUUID(), user.getUserUUID()) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(getPhonenumber(), user.getPhonenumber()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getAddress(), user.getAddress()) &&
                Objects.equals(getCompany(), user.getCompany()) &&
                Objects.equals(getType(), user.getType()) &&
                Objects.equals(getPaymentStatus(), user.getPaymentStatus());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getUserUUID(), lastName, firstName, getPhonenumber(), getEmail(), getAddress(), getCompany(), getType(), getPaymentStatus());
    }

    @Override
    public String toString() {
        return "User{" +
                "userUUID='" + userUUID + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", company='" + company + '\'' +
                ", type='" + type + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", '" + super.toString() + '\'' +
                '}';
    }
}
