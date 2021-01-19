package feri.pora.datalib;

public class ResponsePython {
    private boolean status;
    private String imageBytes;
    private String category;

    public ResponsePython(boolean status, String imageBytes, String category) {
        this.status = status;
        this.imageBytes = imageBytes;
        this.category = category;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
