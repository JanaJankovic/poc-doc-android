package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import feri.pora.datalib.Device;
import feri.pora.datalib.Doctor;
import feri.pora.datalib.MeasureData;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.SendDoctorAdapter;
import feri.pora.pocket_doctor.events.OnMeasurementSend;
import feri.pora.pocket_doctor.events.OnSendMeasurementCancel;
import feri.pora.pocket_doctor.events.OpenMeasureEvent;

public class SendDoctorList extends Fragment {
    private RecyclerView recyclerView;
    private SendDoctorAdapter adapter;

    private ArrayList<Doctor> doctors;
    private MeasureData measureData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_send_doctor, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle("Send measurement");

        doctors = ApplicationState.loadLoggedUser().getDoctorList();
        Log.i("DOCTSDS", doctors.toString());
        Bundle bundle = getArguments();
        measureData = ApplicationState.getGson().fromJson(bundle.getString("measurement"),
                MeasureData.class);
        bindGUI(rootView);

        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView)v.findViewById(R.id.sendDoctorRecycleView);
        adapter = new SendDoctorAdapter(doctors, measureData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onMessageEvent(OnMeasurementSend event) {
        //API REQUEST
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnSendMeasurementCancel event) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new OxymeterFragment()).commit();
    }
}