package feri.pora.datalib;

public class Therapy {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String diagnosisId;
    private int repetition;

    public Therapy(String name, String description, String start, int repetition,
                   String diagnosisId) {
        this.name = name;
        this.description = description;
        this.startDate = start;
        this.repetition = repetition;
        this.diagnosisId = diagnosisId;
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

    public String getStart() {
        return startDate;
    }

    public void setStart(String start) {
        this.startDate = start;
    }

    public String getEnd() {
        return endDate;
    }

    public void setEnd(String end) {
        this.endDate = end;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDiagnosis(User user) {
        for (Diagnosis diagnosis : user.getDiagnosisList()){
            if (diagnosis.getId().equals(diagnosisId))
                return diagnosis.getName();
        }

        return "Unknown diagnosis";
    }
}
