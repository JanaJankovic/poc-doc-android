package feri.pora.pocket_doctor.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import feri.pora.datalib.Device;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.utils.RecycleViewBluetoothAdapter;

public class OxymeterFragment extends Fragment {
    private RecycleViewBluetoothAdapter adapterPairedDevices;
    private RecycleViewBluetoothAdapter adapterAvailableDevices;
    private RecyclerView pairedRecycleView;
    private RecyclerView availableRecycleView;
    private FloatingActionButton floatingActionButton;
    ArrayList<Device> availableDevices;
    ArrayList<Device> pairedDevices;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_oxymeter, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();

        availableDevices = new ArrayList<>();
        pairedDevices = new ArrayList<>();
        bindGUI(rootView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return  rootView;
    }



    private void bindGUI(View v) {
        adapterPairedDevices = new RecycleViewBluetoothAdapter(requireContext(), pairedDevices);
        adapterAvailableDevices = new RecycleViewBluetoothAdapter(requireContext(), availableDevices);
        pairedRecycleView = (RecyclerView) v.findViewById(R.id.pairedRecyclerView);
        availableRecycleView = (RecyclerView) v.findViewById(R.id.avialableRecyclerView);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };


}
