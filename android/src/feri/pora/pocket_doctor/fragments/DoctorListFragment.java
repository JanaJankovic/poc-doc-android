package feri.pora.pocket_doctor.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import feri.pora.datalib.Device;
import feri.pora.datalib.Doctor;
import feri.pora.datalib.Response;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.BluetoothAdapter;
import feri.pora.pocket_doctor.adapters.DoctorAdapter;
import feri.pora.pocket_doctor.events.OnDoctorsLoaded;
import feri.pora.pocket_doctor.events.OnListChanged;
import feri.pora.pocket_doctor.events.OnOpenChat;
import feri.pora.pocket_doctor.events.OpenMeasureEvent;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DoctorListFragment extends Fragment {
    private CompositeSubscription subscription;

    private RecyclerView doctorsRecycleView;
    private DoctorAdapter doctorAdapter;

    private ArrayList<Doctor> doctors;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_doctor_list, null);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        doctors = new ArrayList<>();
        subscription = new CompositeSubscription();
        bindGUI(rootView);
        getDoctors();

        return  rootView;
    }

    private void bindGUI(View v) {
        doctorsRecycleView = (RecyclerView) v.findViewById(R.id.recyclerViewDoctors);
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
    public void onMessageEvent(OnOpenChat event) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("doctor", ApplicationState.getGson().toJson(event.getDoctor()));
        chatFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, chatFragment).commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnListChanged event) {
        getDoctors();
    }

    private void getDoctors() {
        subscription.add(NetworkUtil.getRetrofit().getDoctors()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(ArrayList<Doctor> doctors) {
       this.doctors = doctors;
        doctorAdapter = new DoctorAdapter(requireContext(), doctors);
        doctorsRecycleView.setAdapter(doctorAdapter);
        doctorsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i("DOCTORZ", doctors.toString());
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = ApplicationState.getGson();
            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(requireContext(), response.getData(),  Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(requireContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
        }
    }
}