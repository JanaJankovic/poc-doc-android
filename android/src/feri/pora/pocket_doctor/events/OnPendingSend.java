package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Prediction;

public class OnPendingSend {
    private Prediction prediction;

    public OnPendingSend(Prediction prediction) {
        this.prediction = prediction;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }
}
