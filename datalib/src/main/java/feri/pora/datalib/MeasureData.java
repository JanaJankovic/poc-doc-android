package feri.pora.datalib;

import java.util.ArrayList;

public class MeasureData {
    ArrayList<Integer> bitsPerMinute;
    ArrayList<Integer> spo2;

    public MeasureData() {
        bitsPerMinute = new ArrayList<>();
        spo2 = new ArrayList<>();
    }

    public ArrayList<Integer> getBitsPerMinute() {
        return bitsPerMinute;
    }

    public ArrayList<Integer> getSpo2() {
        return spo2;
    }

    public void addBitPerMinuteToList(int bpm) {
        bitsPerMinute.add(bpm);
    }

    public void addSpo2ToList(int spo2) {
        this.spo2.add(spo2);
    }

}
