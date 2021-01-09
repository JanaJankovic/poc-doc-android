package feri.pora.pocket_doctor.fragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import java.lang.ref.WeakReference;
import java.util.UUID;

import feri.pora.datalib.Device;
import feri.pora.datalib.MeasureData;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.events.OnSocketConnected;
import feri.pora.pocket_doctor.events.OnStatusChanged;

public class MeasureDataFragment extends Fragment {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private boolean connected = false;

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
}
