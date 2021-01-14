package feri.pora.pocket_doctor.events;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnReadMeasure {
    Double bmp;
    Integer spo2;

    public OnReadMeasure(String bmp, String spo2) {
        try {
            this.bmp = Double.parseDouble(bmp);
        }catch (NumberFormatException e) {
            this.bmp = 0.0;
            e.printStackTrace();
            Log.e("Measured read", "double exception");
        }

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(spo2);
        if (m.find()){
            spo2 = m.group(0);
        }
        try {
            Log.i("Onreadmeasure", spo2);
            this.spo2 = Integer.valueOf(spo2);
        }catch (Exception e) {
            this.spo2 = 0;
            e.printStackTrace();
            Log.e("Measured read", "int exception");
        }

    }

    public void extractNumber(String spo2) {

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
