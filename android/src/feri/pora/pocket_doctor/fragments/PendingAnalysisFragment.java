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
import feri.pora.datalib.Prediction;
import feri.pora.datalib.Response;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.adapters.DoctorAdapter;
import feri.pora.pocket_doctor.adapters.PendingAnalysisAdapter;
import feri.pora.pocket_doctor.events.OnOpenChat;
import feri.pora.pocket_doctor.events.OnPendingCancel;
import feri.pora.pocket_doctor.events.OnPendingSend;
import feri.pora.pocket_doctor.events.OnPendingShow;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PendingAnalysisFragment extends Fragment {
    private CompositeSubscription subscription;

    private RecyclerView recyclerView;
    private PendingAnalysisAdapter adapter;

    private ArrayList<Prediction> predictions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_pending_analysis, container, false);

        predictions = new ArrayList<>();
        subscription = new CompositeSubscription();
        bindGUI(rootView);
        getPending();

        return rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewPendingAnalysis);
    }

    public void getPending(){
        subscription.add(NetworkUtil.getRetrofit()
                .getPending(ApplicationState.loadLoggedUser().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    public void deleteRequest(Prediction prediction) {
        predictions.remove(prediction);
        subscription.add(NetworkUtil.getRetrofit()
                .deletePrediction(prediction.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDelete, this::handleError));
    }

    private void handleResponse(ArrayList<Prediction> predictions) {
        this.predictions = predictions;
        adapter = new PendingAnalysisAdapter(predictions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i("PREDICTIONS", predictions.toString());
    }

    private void handleResponseDelete(Void aVoid) {
        adapter.notifyDataSetChanged();
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
    public void onMessageEvent(OnPendingCancel event) {
        deleteRequest(event.getPrediction());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnPendingShow event) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ShowPredictionFragment showPredictionFragment = new ShowPredictionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prediction", ApplicationState.getGson().toJson(event.getPrediction()));
        showPredictionFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, showPredictionFragment).commit();
    }
}