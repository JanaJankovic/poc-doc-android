package feri.pora.pocket_doctor.fragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

import feri.pora.datalib.Device;
import feri.pora.datalib.MeasureData;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.events.OnReadMeasure;
import feri.pora.pocket_doctor.events.OnStatusChanged;

public class MeasureDataFragment extends Fragment {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final int MESSAGE_RECEIVED = 1;
    public static StringBuilder stringBuilder;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private boolean connected = false;
    private Handler handleMessage;
    private CommunicationThread communicationThread;

    private MeasureData measureData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_measure_data, null);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();

        measureData = new MeasureData();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ConnectModule connectModule = new ConnectModule(this);
        Bundle bundle = getArguments();
        handleMessage = new MessageHandler();
        stringBuilder = new StringBuilder();
        connectModule.execute(ApplicationState.getGson().fromJson(bundle.getString("device"),
                Device.class));
        return  rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnStatusChanged event) {
        connected = event.getStatus();
        if(!connected) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new OxymeterFragment()).commit();
        } else {
            bluetoothSocket = event.getBluetoothSocket();
            communicationThread = new CommunicationThread(bluetoothSocket);
            communicationThread.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnReadMeasure event) {
        if (event.getBmp() > 0 && event.getSpo2() > 0) {
            measureData.addBitPerMinuteToList(event.getBmp());
            measureData.addSpo2ToList(event.getSpo2());
        }
        Log.i("EVENTBUSM", measureData.toString());
    }

    private static class MessageHandler extends Handler {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_RECEIVED:
                    byte[] readBuf = (byte[]) message.obj;
                    String receivedMessage = new String(readBuf, 0, message.arg1);
                    stringBuilder.append(receivedMessage);
                    int endOfLineIndex = stringBuilder.indexOf("\r\n");
                    if (endOfLineIndex > 0) {
                        String bmp = stringBuilder.substring(0, endOfLineIndex);
                        String spo2 = stringBuilder.substring(endOfLineIndex + 2, stringBuilder.length());
                        Double bmpValue = 0.0;
                        int spo2Value = 0;
                        try{
                            bmpValue = Double.parseDouble(bmp);
                            spo2Value = Integer.parseInt(spo2);
                        } catch(NumberFormatException e) {
                            bmpValue = -1.0;
                            spo2Value = -1;
                        }

                        EventBus.getDefault().post(new OnReadMeasure(bmpValue, spo2Value));
                        stringBuilder.delete(0, stringBuilder.length());

                    }
                    break;
            }
        }
    }

    private class ConnectModule extends AsyncTask<Device, Void, OnStatusChanged> {
        WeakReference<MeasureDataFragment> reference;

        ConnectModule(MeasureDataFragment fragment) {
            reference = new WeakReference<MeasureDataFragment>(fragment);
        }

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Connecting...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        public OnStatusChanged connect(Device device) {
            MeasureDataFragment measureDataFragment = reference.get();
            BluetoothSocket bluetoothSocket = null;

            //FIX THIS TO BE BETTER! - problems with already connected
            if(connected) {
                return new OnStatusChanged(false, null);
            }

            try {
                if (bluetoothSocket == null || !connected) {
                    BluetoothDevice hc = bluetoothAdapter.getRemoteDevice(device.getMacAddress());
                    bluetoothSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                    return new OnStatusChanged(true, bluetoothSocket);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return new OnStatusChanged(false, null);
        }

        @Override
        protected OnStatusChanged doInBackground (Device... devices) {
            return connect(devices[0]);
        }

        @Override
        protected void onPostExecute (OnStatusChanged result) {
            MeasureDataFragment measureDataFragment = reference.get();
            progressDialog.hide();
            super.onPostExecute(result);
            if (!result.getStatus()) {
                Toast.makeText(measureDataFragment.requireContext(), "Connection failed. Try again.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(measureDataFragment.requireContext(), "Connected", Toast.LENGTH_SHORT).show();
            }
            EventBus.getDefault().post(result);
        }
    }

    private class CommunicationThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public CommunicationThread(BluetoothSocket socket) {
            InputStream tempIn = null;
            OutputStream tempOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tempIn = socket.getInputStream();
                tempOut = socket.getOutputStream();
            } catch (IOException e) { }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);        // Get number of bytes and message in "buffer"
                    handleMessage.obtainMessage(MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                    handleMessage.handleMessage(new Message());
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}
