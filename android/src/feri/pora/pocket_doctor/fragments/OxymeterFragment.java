package feri.pora.pocket_doctor.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Set;

import feri.pora.datalib.Device;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.events.OpenMeasureEvent;
import feri.pora.pocket_doctor.adapters.BluetoothAdapter;

public class OxymeterFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 0;
    private android.bluetooth.BluetoothAdapter bluetoothAdapter = null;
    private BluetoothAdapter adapterPairedDevices;
    private RecyclerView pairedRecycleView;
    private ArrayList<Device> pairedDevices;

    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_oxymeter, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
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
        adapterPairedDevices = new BluetoothAdapter(requireContext(), pairedDevices);
        pairedRecycleView = (RecyclerView) v.findViewById(R.id.pairedRecyclerView);
        pairedRecycleView.setAdapter(adapterPairedDevices);
        pairedRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
    }

    private void enableBluetoothOnDevice() {
        if (bluetoothAdapter == null) {
            Toast.makeText(requireContext(),getString(R.string.no_bluetooth), Toast.LENGTH_LONG);
            // If the android device does not have bluetooth, just return and get out.
            // There’s nothing the app can do in this case. Closing app.
        }
        if( !bluetoothAdapter.isEnabled())  {
            Intent enableBtIntent = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
                Toast.makeText(requireContext(), getString(R.string.access_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void scanDevices() {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Device pairedDevice = new Device(device.getName(), device.getAddress(), getString(R.string.paired));
                if (!Device.checkIfAdded(pairedDevice, pairedDevices)) {
                    pairedDevices.add(pairedDevice);
                    adapterPairedDevices.notifyItemInserted(pairedDevices.size() - 1);
                }
            }
        }
        else {
            // In case no device is found
            Toast.makeText(requireContext(), getString(R.string.no_bluetooth_found), Toast.LENGTH_LONG).show();
        }
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
    public void onMessageEvent(OpenMeasureEvent event) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MeasureDataFragment measureDataFragment = new MeasureDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.device), ApplicationState.getGson().toJson(event.getDevice()));
        measureDataFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, measureDataFragment).commit();
    }


}

