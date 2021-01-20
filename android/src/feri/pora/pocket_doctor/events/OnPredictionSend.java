package feri.pora.pocket_doctor.events;

public class OnPredictionSend {
    String doctorId;

    public OnPredictionSend(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}

