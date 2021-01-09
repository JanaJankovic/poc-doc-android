package feri.pora.datalib;

import java.util.ArrayList;

public class MeasureData {
    ArrayList<Double> bitsPerMinute;
    ArrayList<Integer> spo2;

    public MeasureData() {
        bitsPerMinute = new ArrayList<>();
        spo2 = new ArrayList<>();
    }

    public ArrayList<Double> getBeatsPerMinute() {
        return bitsPerMinute;
    }

    public ArrayList<Integer> getSpo2() {
        return spo2;
    }

    public void addBitPerMinuteToList(Double bpm) {
        bitsPerMinute.add(bpm);
    }

    public void addSpo2ToList(int spo2) {
        this.spo2.add(spo2);
    }

    @Override
    public String toString() {
        return "MeasureData{" +
                "bitsPerMinute=" + bitsPerMinute +
                ", spo2=" + spo2 +
                '}';
    }
}
