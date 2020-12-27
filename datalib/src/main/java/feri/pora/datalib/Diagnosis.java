package feri.pora.datalib;

import java.util.ArrayList;

public class Diagnosis {
    private String name;
    private String description;
    private ArrayList<Therapy> therapyList;

    public Diagnosis(String name, String description) {
        this.name = name;
        this.description = description;
        therapyList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addTherapyToList(Therapy t) {
        if(! therapyList.contains(t))
            therapyList.add(t);
    }

    public void removeFromTherapyList(Therapy t) {
        if(therapyList.contains(t))
            therapyList.remove(t);
    }
}
