package feri.pora.pocket_doctor.events;

import java.util.ArrayList;

public class OnMeasurementSend {
    private ArrayList<Double> measureDataBitsPerMinute;
    private ArrayList<Integer> measureDataSpo2;
    private String patientPrivateKey;
    private String doctorPublicKey;

    public OnMeasurementSend(ArrayList<Double> measureDataBitsPerMinute, ArrayList<Integer> measureDataSpo2, String patientPrivateKey, String doctorPublicKey) {
        this.measureDataBitsPerMinute = measureDataBitsPerMinute;
        this.measureDataSpo2 = measureDataSpo2;
        this.patientPrivateKey = patientPrivateKey;
        this.doctorPublicKey = doctorPublicKey;
    }

    public ArrayList<Double> getMeasureDataBitsPerMinute() {
        return measureDataBitsPerMinute;
    }

    public void setMeasureDataBitsPerMinute(ArrayList<Double> measureDataBitsPerMinute) {
        this.measureDataBitsPerMinute = measureDataBitsPerMinute;
    }

    public ArrayList<Integer> getMeasureDataSpo2() {
        return measureDataSpo2;
    }

    public void setMeasureDataSpo2(ArrayList<Integer> measureDataSpo2) {
        this.measureDataSpo2 = measureDataSpo2;
    }

    public String getPatientPrivateKey() {
        return patientPrivateKey;
    }

    public void setPatientPrivateKey(String patientPrivateKey) {
        this.patientPrivateKey = patientPrivateKey;
    }

    public String getDoctorPublicKey() {
        return doctorPublicKey;
    }

    public void setDoctorPublicKey(String doctorPublicKey) {
        this.doctorPublicKey = doctorPublicKey;
    }
}
