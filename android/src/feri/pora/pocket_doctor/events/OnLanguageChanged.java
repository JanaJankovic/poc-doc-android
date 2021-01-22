package feri.pora.pocket_doctor.events;

public class OnLanguageChanged {
    private boolean isSlovenian;

    public OnLanguageChanged(boolean isSlovenian) {
        this.isSlovenian = isSlovenian;
    }

    public boolean isSlovenian() {
        return isSlovenian;
    }

    public void setSlovenian(boolean slovenian) {
        isSlovenian = slovenian;
    }
}

