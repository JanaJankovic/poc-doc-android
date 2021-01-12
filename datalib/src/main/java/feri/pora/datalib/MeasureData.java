package feri.pora.datalib;

import java.util.ArrayList;

public class MeasureData {
    ArrayList<Double> beatsPerMinute;
    ArrayList<Integer> spo2;

    public MeasureData() {
        beatsPerMinute = new ArrayList<>();
        spo2 = new ArrayList<>();
    }

    public ArrayList<Double> getBeatsPerMinute() {
        return beatsPerMinute;
    }

    public ArrayList<Integer> getSpo2() {
        return spo2;
    }

    public void addBitPerMinuteToList(Double bpm) {
        beatsPerMinute.add(bpm);
    }

    public void addSpo2ToList(int spo2) {
        this.spo2.add(spo2);
    }

    @Override
    public String toString() {
        return "MeasureData{" +
                "bitsPerMinute=" + beatsPerMinute +
                ", spo2=" + spo2 +
                '}';
    }
}
