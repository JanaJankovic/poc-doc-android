package feri.pora.datalib;

import java.util.ArrayList;

public class Analysis {
    private String id;
    private String doctorPublicKey;
    private String patientPublicKey;
    private String base64AsciiImageString;
    private String description;
    private String title;
    private String diagnosisId;
    private String timestamp;

    public Analysis(String id, String doctorPrivateKey, String patientPublicKey,
                    String analisysBase64AsciiImageString,
                    String analisysDescription, String analisysTitle, String diagnosisId,
                    String timestamp) {
        this.id = id;
        this.doctorPublicKey = doctorPrivateKey;
        this.patientPublicKey = patientPublicKey;
        this.base64AsciiImageString = analisysBase64AsciiImageString;
        this.description = analisysDescription;
        this.title = analisysTitle;
        this.diagnosisId = diagnosisId;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorPublicKey() {
        return doctorPublicKey;
    }

    public void setDoctorPublicKey(String doctorPublicKey) {
        this.doctorPublicKey = doctorPublicKey;
    }

    public String getPatientPublicKey() {
        return patientPublicKey;
    }

    public void setPatientPublicKey(String patientPublicKey) {
        this.patientPublicKey = patientPublicKey;
    }

    public String getBase64AsciiImageString() {
        return base64AsciiImageString;
    }

    public void setBase64AsciiImageString(String base64AsciiImageString) {
        this.base64AsciiImageString = base64AsciiImageString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Doctor getDoctor(ArrayList<Doctor> doctors){
        if (doctors != null)
            for(Doctor doctor : doctors) {
                if(doctor.getPublicKey().equals(doctorPublicKey) || doctor.getPrivateKey().equals(doctorPublicKey)){
                    return doctor;
                }
            }
        return null;
    }
}
