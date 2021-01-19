package feri.pora.datalib;

import java.util.ArrayList;

public class Diagnosis {
    private String id;
    private String name;
    private String description;
    private String doctorId;
    private String timestamp;
    private String  idAnalysis;

    public Diagnosis(String id, String name, String description, String doctorId,
                     String timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.doctorId = doctorId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDoctorFullname(User user){
        for(Doctor doctor : user.getDoctorList()){
            if (doctor.getId().equals(doctorId))
                return doctor.getFullName();
        }
        return "Unknown doctor";
    }

    public String getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(String idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
//                ", therapyList=" + therapyList.toString() +
                ", doctorId='" + doctorId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
