package elec5620.sydney.edu.au.smarthealth;

import java.io.Serializable;

public class Doctor implements Serializable {
    public Doctor(String firstName, String lastName, String specialization, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    String firstName;
    String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String specialization;
    String address;
    String phoneNumber;




}