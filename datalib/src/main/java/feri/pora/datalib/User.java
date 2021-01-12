package feri.pora.datalib;
import java.util.ArrayList;

public class User extends Person {
    private String privateKey;
    private String publicKey;
    private String medicalNumber;
    private String password;
    private ArrayList<Doctor> doctorList;
    private ArrayList<Diagnosis> diagnosisList;

    public User(){
        super();
        doctorList = new ArrayList<Doctor>();
        diagnosisList = new ArrayList<Diagnosis>();
    }


    public User(String id, String privateKey, String publicKey, String medicalNumber, String fullName, String password, String phone) {
        super(id, fullName, phone, "");
        this.medicalNumber = medicalNumber;
        this.password = password;

        this.privateKey = privateKey;
        this.publicKey = publicKey;

        doctorList = new ArrayList<Doctor>();
        diagnosisList = new ArrayList<Diagnosis>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
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

    public void addDoctorToList(Doctor d) {
        if(! doctorList.contains(d) )
            doctorList.add(d);
    }

    public void removeDoctorFromList(Doctor d) {
        if(doctorList.contains(d))
            doctorList.remove(d);
    }

    @Override
    public String toString() {
        return "User{" +
                "privateKey='" + privateKey + '\'' +
                ", id='" + id + '\'' +
                ", medicalNumber='" + medicalNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}