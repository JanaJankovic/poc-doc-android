package feri.pora.pocket_doctor.events;

import android.bluetooth.BluetoothSocket;

public class OnSocketConnected {
    private BluetoothSocket bluetoothSocket;

    public OnSocketConnected(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }
}
