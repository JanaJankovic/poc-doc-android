package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.MeasureData;
import feri.pora.datalib.Prediction;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.SendDoctorAdapter;
import feri.pora.pocket_doctor.events.OnMeasurementSend;
import feri.pora.pocket_doctor.events.OnSendCancel;
import feri.pora.pocket_doctor.events.OnPredictionSend;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SendDoctorList extends Fragment {
    private RecyclerView recyclerView;
    private SendDoctorAdapter adapter;

    private ArrayList<Doctor> doctors;
    private MeasureData measureData;
    private Prediction predictionData;

    private CompositeSubscription subscription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_send_doctor, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle(getString(R.string.send_measurement));

        subscription = new CompositeSubscription();
        doctors = ApplicationState.loadLoggedUser().getDoctorList();
        Log.i("DOCTSDS", doctors.toString());
        Bundle bundle = getArguments();

        String jsonMeasure = bundle.getString("measurement");
        String jsonPrediction = bundle.getString("prediction");
        if (jsonMeasure == null) {
            ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                    .setTitle(getString(R.string.send_prediction));
            predictionData = ApplicationState.getGson().fromJson(jsonPrediction, Prediction.class);
        } else {
            measureData = ApplicationState.getGson().fromJson(jsonMeasure, MeasureData.class);
        }

        bindGUI(rootView);

        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView)v.findViewById(R.id.sendDoctorRecycleView);
        adapter = new SendDoctorAdapter(doctors, measureData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void postMeasurement(OnMeasurementSend data) {
        subscription.add(NetworkUtil.getRetrofit(true).postMeasureData(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Void aVoid) {
        Toast.makeText(requireContext(), getString(R.string.measurement_sent), Toast.LENGTH_SHORT).show();
        ((UserNavigationActivity) requireActivity()).navigationView
                .setCheckedItem(R.id.nav_home);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new HomeFragment()).commit();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = ApplicationState.getGson();
            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Log.i("ERROR!", errorBody);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(requireContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
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
    public void onMessageEvent(OnMeasurementSend event) {
         postMeasurement(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPredictionSend event) {
        predictionData.setDoctorId(event.getDoctorId());
        updatePrediction(predictionData);
    }

    private void updatePrediction(Prediction prediction) {
        subscription.add(NetworkUtil.getRetrofit().updatePrediction(prediction.getId(), prediction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponsePrediction, this::handleError));
    }

    private void handleResponsePrediction(Prediction prediction) {
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, new AnalysisFragment()).commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnSendCancel event) {
        if (measureData != null ) {
            ((UserNavigationActivity) requireActivity()).navigationView
                    .setCheckedItem(R.id.nav_oxymeter);
            ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new OxymeterFragment()).commit();
        } else {
            ((UserNavigationActivity) requireActivity()).navigationView
                    .setCheckedItem(R.id.nav_request_analysis);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new RequestAnalysisFragment()).commit();
        }
    }
}