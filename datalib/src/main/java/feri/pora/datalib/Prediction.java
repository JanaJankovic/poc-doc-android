package feri.pora.datalib;


public class Prediction {
    private String id;
    private String filePath;
    private String prediction;
    private String date;
    private String image;
    private boolean confirmed;
    private String userId;
    private String doctorId;

    public Prediction(String filePath, String prediction, String date) {
        this.filePath = filePath;
        this.prediction = prediction;
        this.date = date;
        id = null;
        image = null;
        confirmed = false;
        userId  = null;
        doctorId = null;
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

    public String getBase64() {
        return image;
    }

    public void setBase64(String base64) {
        this.image = base64;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
