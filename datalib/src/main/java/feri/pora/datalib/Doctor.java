package feri.pora.datalib;

public class Doctor {
    private String publicKey;
    private String fullName;
    private String phone;
    private String location;
    private String email;

    public Doctor(String publicKey, String fullName, String phone, String location, String email) {
        this.fullName = fullName;
        this.phone = phone;
        this.location = location;
        this.email = email;
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
