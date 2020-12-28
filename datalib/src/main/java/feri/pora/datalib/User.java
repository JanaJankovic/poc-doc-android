package feri.pora.datalib;

import java.util.ArrayList;

public class User {
    private String privateKey;
    private String publicKey;
    private String id;
    private String medicalNumber;
    private String fullName;
    private String password;
    private String phone;
    private String location;
    private ArrayList<Doctor> doctorList;
    private ArrayList<Diagnosis> diagnosisList;

    public User(String privateKey, String publicKey, String medicalNumber, String fullName, String password, String phone) {
        this.medicalNumber = medicalNumber;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;

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
}