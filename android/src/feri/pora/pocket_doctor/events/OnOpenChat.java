package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Doctor;

public class OnOpenChat {
    private Doctor doctor;

    public OnOpenChat(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
