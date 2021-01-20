package feri.pora.datalib;

import java.util.Base64;

public class Prediction {
    private String id;
    private String filePath;
    private String prediction;
    private String date;
    private String base64;

    public Prediction(String filePath, String prediction, String date) {
        this.filePath = filePath;
        this.prediction = prediction;
        this.date = date;
        id = "";
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
