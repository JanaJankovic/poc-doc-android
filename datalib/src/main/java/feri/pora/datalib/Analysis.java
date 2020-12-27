package feri.pora.datalib;

import java.util.ArrayList;

public class Analysis {
    // will do when we finish URVRV projektna naloga (to see what data we will need form pictures and what we will give back to app)
    private String filePath;
    private ArrayList<String> possibleIllnesses;

    public Analysis(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<String> getPossibleIllnesses() {
        return possibleIllnesses;
    }
}
