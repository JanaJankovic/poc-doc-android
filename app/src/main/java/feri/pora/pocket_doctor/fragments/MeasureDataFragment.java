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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import feri.pora.datalib.Device;
import feri.pora.datalib.MeasureData;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.events.OnReadMeasure;
import feri.pora.pocket_doctor.events.OnStatusChanged;
import feri.pora.pocket_doctor.libgdx.GdxFrameLayout;

public class MeasureDataFragment extends Fragment implements AndroidFragmentApplication.Callbacks, View.OnClickListener {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final int MESSAGE_RECEIVED = 1;
    public static StringBuilder stringBuilder;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private boolean connected = false;
    private Handler handleMessage;
    private CommunicationThread communicationThread;

    private TextView textViewBmp;
    private TextView textViewSpo2;
    private Button  buttonStop;
    private Button buttonRequest;
    private Button buttonRetry;
    private FrameLayout frameLayout;


    protected MeasureData measureData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_measure_data, null);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();

        bindGUI(rootView);
        buttonRetry.setVisibility(View.INVISIBLE);
        buttonRequest.setVisibility(View.INVISIBLE);

        buttonStop.setOnClickListener(this);
        buttonRetry.setOnClickListener(this);
        buttonRequest.setOnClickListener(this);

       executeBluetoothConnection();

        return  rootView;
    }

    public void bindGUI(View v){
        textViewBmp = (TextView) v.findViewById(R.id.textViewBmp);
        textViewSpo2 = (TextView) v.findViewById(R.id.textViewSpo2);
        buttonStop = (Button)v.findViewById(R.id.buttonStop);
        buttonRequest = (Button)v.findViewById(R.id.buttonSend);
        buttonRetry = (Button)v.findViewById(R.id.buttonRetry);
        frameLayout = (FrameLayout) v.findViewById(R.id.content_framelayout);

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
//            getActivity().getSupportFragmentManager().beginTransaction().
//                    add(R.id.content_framelayout, new GdxFrameLayout()).
//                    commit();
            bluetoothSocket = event.getBluetoothSocket();
            communicationThread = new CommunicationThread(bluetoothSocket);
            communicationThread.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnReadMeasure event) {
        measureData.addBitPerMinuteToList(event.getBmp());
        measureData.addSpo2ToList(event.getSpo2());
        textViewBmp.setText(String.valueOf(event.getBmp()));
        textViewSpo2.setText(String.valueOf(event.getSpo2()));
        Log.i("MEASURED DATA", measureData.toString());

    }


    @Override
    public void exit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStop:
                buttonRetry.setVisibility(View.VISIBLE);
                buttonRequest.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.INVISIBLE);

                if(communicationThread.isAlive())
                    communicationThread.interrupt();

                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bluetoothSocket = null;
                connected = false;
                break;
            case R.id.buttonRetry:
                buttonRetry.setVisibility(View.INVISIBLE);
                buttonRequest.setVisibility(View.INVISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
                executeBluetoothConnection();
                break;
            case R.id.buttonSend:
                Toast.makeText(requireContext(), "Send", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void executeBluetoothConnection() {
        measureData = new MeasureData();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ConnectModule connectModule = new ConnectModule(this);
        Bundle bundle = getArguments();
        handleMessage = new MessageHandler();
        stringBuilder = new StringBuilder();
        connectModule.execute(ApplicationState.getGson().fromJson(bundle.getString("device"),
                Device.class));
    }

    public void retryMeasuring() {
        measureData = new MeasureData();
    }

    private static class MessageHandler extends Handler {
        public void handleMessage(Message message) {
            if (message.what == MESSAGE_RECEIVED) {
                byte[] readBuf = (byte[]) message.obj;
                String receivedMessage = new String(readBuf, 0, message.arg1);
                stringBuilder.append(receivedMessage);
                int endOfLineIndex = stringBuilder.indexOf("\r\n");
                if (endOfLineIndex > 0) {
                    String bmp = stringBuilder.substring(0, endOfLineIndex);
                    String spo2 = stringBuilder.substring(endOfLineIndex + 2, stringBuilder.length());
                    EventBus.getDefault().post(new OnReadMeasure(bmp, spo2));
                    stringBuilder.delete(0, stringBuilder.length());
                }
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

        public CommunicationThread(BluetoothSocket socket) {
            InputStream tempIn = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tempIn = socket.getInputStream();
            } catch (IOException e) { }

            inputStream = tempIn;
        }

        @Override
        public void run() {
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    byte[] buffer = new byte[1024];  // buffer store for the stream
                    int bytes; // bytes returned from read()
                    bytes = inputStream.read(buffer);        // Get number of bytes and message in "buffer"
                    handleMessage.obtainMessage(MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                    handleMessage.handleMessage(new Message());
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

}
