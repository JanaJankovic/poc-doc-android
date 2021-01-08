package feri.pora.pocket_doctor.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import feri.pora.datalib.Device;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.events.OpenMeasureEvent;
import feri.pora.pocket_doctor.utils.RecycleViewBluetoothAdapter;

public class OxymeterFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 0;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter = null;
    private RecycleViewBluetoothAdapter adapterPairedDevices;
    private RecyclerView pairedRecycleView;
    private ArrayList<Device> pairedDevices;

    private boolean connected = false;

    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_oxymeter, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<>();

        bindGUI(rootView);

        scanDevices();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBluetoothOnDevice();
                if(bluetoothAdapter.isEnabled()){
                    scanDevices();
                }
            }
        });

        return rootView;
    }


    private void bindGUI(View v) {
        adapterPairedDevices = new RecycleViewBluetoothAdapter(requireContext(), pairedDevices);
        pairedRecycleView = (RecyclerView) v.findViewById(R.id.pairedRecyclerView);
        pairedRecycleView.setAdapter(adapterPairedDevices);
        pairedRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
    }

    private void enableBluetoothOnDevice() {
        if (bluetoothAdapter == null) {
            Toast.makeText(requireContext(),"This device does not have a bluetooth adapter", Toast.LENGTH_LONG);
            // If the android device does not have bluetooth, just return and get out.
            // There’s nothing the app can do in this case. Closing app.
        }
        if( !bluetoothAdapter.isEnabled())  {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == 0) {
                // If the resultCode is 0, the user selected “No” when prompt to
                // allow the app to enable bluetooth.
                // You may want to display a dialog explaining what would happen if
                // the user doesn’t enable bluetooth.
                Toast.makeText(requireContext(), "Access denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void scanDevices() {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Device pairedDevice = new Device(device.getName(), device.getAddress(), "paired");
                if (!Device.checkIfAdded(pairedDevice, pairedDevices)) {
                    pairedDevices.add(pairedDevice);
                    adapterPairedDevices.notifyItemInserted(pairedDevices.size() - 1);
                }
            }
        }
        else {
            // In case no device is found
            Toast.makeText(requireContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean connect(Device device) {
        BluetoothSocket bluetoothSocket = null;
        try {
            if (bluetoothSocket == null || !connected) {
                BluetoothDevice hc = bluetoothAdapter.getRemoteDevice(device.getMacAddress());
                bluetoothSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                bluetoothSocket.connect();
                return true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OpenMeasureEvent event) {
        OxymeterFragment.ConnectModule.execute();
    }

    private class ConnectModule extends AsyncTask<Device, Void, Boolean> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(requireContext(), "Connecting...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground (Device... devices) {
           return connect(devices[0]);
        }

        @Override
        protected void onPostExecute (Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(requireContext(), "Connection failed. Try again.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "Connected", Toast.LENGTH_LONG).show();
                connected = true;
            }
        }
    }
}

