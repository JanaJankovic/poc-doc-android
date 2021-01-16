package feri.pora.pocket_doctor.events;

import java.util.ArrayList;

import feri.pora.datalib.Doctor;

public class OnDoctorsLoaded {
    private  ArrayList<Doctor> doctors;

    public OnDoctorsLoaded(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }
}
