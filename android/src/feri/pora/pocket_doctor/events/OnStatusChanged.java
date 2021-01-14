package feri.pora.pocket_doctor.events;

import android.bluetooth.BluetoothSocket;

public class OnStatusChanged {
    boolean status;
    private BluetoothSocket bluetoothSocket;

    public OnStatusChanged(boolean status, BluetoothSocket bluetoothSocket) {
        this.status = status;
        this.bluetoothSocket = bluetoothSocket;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }
}
