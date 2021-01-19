package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Prediction;

public class OnPendingShow {
    private Prediction prediction;

    public OnPendingShow(Prediction prediction) {
        this.prediction = prediction;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }
}
