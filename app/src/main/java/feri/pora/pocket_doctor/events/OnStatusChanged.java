package feri.pora.pocket_doctor.events;

public class OnStatusChanged {
    boolean status;

    public OnStatusChanged(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
