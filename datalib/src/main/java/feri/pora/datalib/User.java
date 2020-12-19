package feri.pora.datalib;

public class User {
    private String medicalNumber;
    private String fullName;
    private String password;
    private String phone;
    private String location;

    public User(String medicalNumber, String fullName, String password, String phone) {
        this.medicalNumber = medicalNumber;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
    }

    public String getMedicalNumber() {
        return medicalNumber;
    }

    public void setMedicalNumber(String medicalNumber) {
        this.medicalNumber = medicalNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}