package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Analysis;
import feri.pora.datalib.Diagnosis;

public class OnAnalysisShow {
    private Analysis analysis;

    public OnAnalysisShow(Analysis analysis) {
        this.analysis = analysis;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

}
