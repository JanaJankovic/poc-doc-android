package feri.pora.datalib;

import java.util.ArrayList;

public class MeasureData {
    private ArrayList<Double> beatsPerMinute;
    private ArrayList<Integer> spo2;
    //needed for libgdx plotter
    private ArrayList<Integer> timestamp;
    private long lastTime;
    private int startIndex;

    public MeasureData() {
        beatsPerMinute = new ArrayList<>();
        spo2 = new ArrayList<>();
        timestamp = new ArrayList<>();
        timestamp.add(0);
        lastTime = System.currentTimeMillis();
        startIndex = 0;
    }

    public ArrayList<Integer> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ArrayList<Integer> timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Double> getBeatsPerMinute() {
        return beatsPerMinute;
    }

    public ArrayList<Integer> getSpo2() {
        return spo2;
    }

    public void addBitPerMinuteToList(Double bpm) {
        beatsPerMinute.add(bpm);
        int time = (int)(System.currentTimeMillis() - lastTime ) / 1000;
        int previousTime = timestamp.get(timestamp.size() - 1);
        if (previousTime + time <= 298)
            timestamp.add(previousTime + time);
        else {
            timestamp.add(0);
            startIndex = beatsPerMinute.size() - 1;
        }

        lastTime = System.currentTimeMillis();
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
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
