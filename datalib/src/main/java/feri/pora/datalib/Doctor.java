package feri.pora.datalib;

public class Doctor extends Person {
    private String publicKey;
    private String email;
    private String privateKey;

    public Doctor(String publicKey, String fullName, String phone, String location, String email,
                  String id, String privateKey) {
        super(id, fullName, phone, location);
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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

    public String GetId() { return  this.id; }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "publicKey='" + publicKey + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
