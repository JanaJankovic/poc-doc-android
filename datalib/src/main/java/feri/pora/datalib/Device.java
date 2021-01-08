package feri.pora.datalib;

import java.util.ArrayList;

public class Device {
    String name;
    String macAddress;
    String status;

    public Device(String name, String macAddress, String status) {
        this.name = name;
        this.macAddress = macAddress;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static boolean checkIfAdded(Device device, ArrayList<Device> devices){
        for (Device d : devices){
            if (d.getMacAddress().equals(device.getMacAddress()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
