package feri.pora.pocket_doctor.events;

import feri.pora.datalib.Device;

public class OpenMeasureEvent {
    private Device device;

    public OpenMeasureEvent(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
