package feri.pora.pocket_doctor.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Set;

import feri.pora.datalib.Device;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.utils.RecycleViewBluetoothAdapter;

public class OxymeterFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 0;

    private RecycleViewBluetoothAdapter adapterPairedDevices;
    private RecyclerView pairedRecycleView;
    private FloatingActionButton floatingActionButton;

    ArrayList<Device> availableDevices;
    ArrayList<Device> pairedDevices;

    private BluetoothAdapter bluetoothAdapter = null;
    private Set<BluetoothDevice> devices;

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
        
        if( !bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == 0)
            {
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
}
