package feri.pora.pocket_doctor.events;

import java.util.ArrayList;

public class OnMeasurementSend {
    private String measureDataBitsPerMinuteArrayJson;
    private String measureDataSpo2ArrayJson;
    private String patientPrivateKey;
    private String doctorPublicKey;


    public OnMeasurementSend(ArrayList<Double> measureDataBitsPerMinute, ArrayList<Integer> measureDataSpo2,
                             String patientPrivateKey, String doctorPublicKey) {
        this.patientPrivateKey = patientPrivateKey;
        this.doctorPublicKey = doctorPublicKey;
        arraysToString(measureDataBitsPerMinute, measureDataSpo2);
    }

    public void arraysToString(ArrayList<Double> bmp, ArrayList<Integer> spo2) {
        measureDataBitsPerMinuteArrayJson = "[";
        measureDataSpo2ArrayJson = "[";
        for(int i = 0; i < bmp.size(); i++){
            if (i != bmp.size() - 1) {
                measureDataBitsPerMinuteArrayJson += String.valueOf(bmp.get(i)) + ", ";
                measureDataSpo2ArrayJson += String.valueOf(spo2.get(i)) + ", ";
            }
            else {
                measureDataBitsPerMinuteArrayJson += String.valueOf(bmp.get(i)) + "]";
                measureDataSpo2ArrayJson += String.valueOf(spo2.get(i)) + "]";
            }
        }
    }

    public String getMeasureDataBitsPerMinuteArrayJson() {
        return measureDataBitsPerMinuteArrayJson;
    }

    public void setMeasureDataBitsPerMinuteArrayJson(String measureDataBitsPerMinuteArrayJson) {
        this.measureDataBitsPerMinuteArrayJson = measureDataBitsPerMinuteArrayJson;
    }

    public String getMeasureDataSpo2ArrayJson() {
        return measureDataSpo2ArrayJson;
    }

    public void setMeasureDataSpo2ArrayJson(String measureDataSpo2ArrayJson) {
        this.measureDataSpo2ArrayJson = measureDataSpo2ArrayJson;
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

    @Override
    public String toString() {
        return "OnMeasurementSend{" +
                "measureDataBitsPerMinute=" + measureDataBitsPerMinuteArrayJson +
                ", measureDataSpo2=" + measureDataSpo2ArrayJson +
                ", patientPrivateKey='" + patientPrivateKey + '\'' +
                ", doctorPublicKey='" + doctorPublicKey + '\'' +
                '}';
    }
}
