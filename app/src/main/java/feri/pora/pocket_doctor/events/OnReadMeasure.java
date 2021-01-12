package feri.pora.pocket_doctor.events;

public class OnReadMeasure {
    double bmp;
    int spo2;

    public OnReadMeasure(String bmp, String spo2) {
        try {
            this.bmp = Double.parseDouble(bmp);
            this.spo2 = Integer.parseInt(spo2);
        }catch (NumberFormatException e) {
            System.out.println("INCORRECT NUMBERS");
        }
    }

    public double getBmp() {
        return bmp;
    }

    public void setBmp(double bmp) {
        this.bmp = bmp;
    }

    public int getSpo2() {
        return spo2;
    }

    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }
}
