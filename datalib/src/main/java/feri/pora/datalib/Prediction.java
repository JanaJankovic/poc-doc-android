package feri.pora.datalib;

import java.util.Base64;

public class Prediction {
    private String id;
    private String filePath;
    private String prediction;
    private Base64 receivedImage;
    private String date;

    public Prediction(String id, String prediction, Base64 receivedImage) {
        this.id = id;
        this.prediction = prediction;
        this.receivedImage = receivedImage;
    }

    public Prediction(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Base64 getReceivedImage() {
        return receivedImage;
    }

    public void setReceivedImage(Base64 receivedImage) {
        this.receivedImage = receivedImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
