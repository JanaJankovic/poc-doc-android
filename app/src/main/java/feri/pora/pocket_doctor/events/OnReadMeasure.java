package feri.pora.pocket_doctor.events;

public class OnReadMeasure {
    Double bmp;
    int spo2;

    public OnReadMeasure(Double bmp, int spo2) {
        this.bmp = bmp;
        this.spo2 = spo2;
    }

    public Double getBmp() {
        return bmp;
    }

    public void setBmp(Double bmp) {
        this.bmp = bmp;
    }

    public int getSpo2() {
        return spo2;
    }

    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }
}
