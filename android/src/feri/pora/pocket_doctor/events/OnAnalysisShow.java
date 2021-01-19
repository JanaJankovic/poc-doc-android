package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Analysis;
import feri.pora.datalib.Diagnosis;

public class OnAnalysisShow {
    private Analysis analysis;
    private Diagnosis diagnosis;

    public OnAnalysisShow(Analysis analysis, Diagnosis diagnosis) {
        this.analysis = analysis;
        this.diagnosis = diagnosis;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }
}
