package feri.pora.datalib;

import java.util.Base64;

public class Analysis {
    private String id;
    private String name;
    private Base64 image;

    public Analysis(String id, String name, Base64 image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Base64 getImage() {
        return image;
    }

    public void setImage(Base64 image) {
        this.image = image;
    }
}
