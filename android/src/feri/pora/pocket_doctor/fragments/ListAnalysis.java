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

import feri.pora.datalib.Analysis;
import feri.pora.datalib.Data;
import feri.pora.datalib.Diagnosis;
import feri.pora.datalib.Prediction;
import feri.pora.datalib.Therapy;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.adapters.AnalysisAdapter;
import feri.pora.pocket_doctor.adapters.DiagnosisAdapter;
import feri.pora.pocket_doctor.adapters.PendingAnalysisAdapter;
import feri.pora.pocket_doctor.adapters.TherapyAdapter;
import feri.pora.pocket_doctor.events.OnAnalysisShow;
import feri.pora.pocket_doctor.events.OnPendingCancel;
import feri.pora.pocket_doctor.events.OnPendingShow;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ListAnalysis extends Fragment {
    private CompositeSubscription subscription;

    private RecyclerView recyclerView;
    private AnalysisAdapter adapter;

    private ArrayList<Analysis> analyses;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_all_analysis, container, false);
        Log.i("LIST", "here");
        subscription = new CompositeSubscription();
        bindGUI(rootView);
        getAnalyses();
        return rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewAllAnalysis);
    }

    public void getAnalyses(){
        subscription.add(NetworkUtil.getRetrofit(true)
                .getAnalysis(new Data(ApplicationState.loadLoggedUser().getPublicKey()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseAnalysis, this::handleError));
    }

    private void handleResponseAnalysis(ArrayList<Analysis> analyses) {
        if (analyses != null && analyses.size() > 0) {
            this.analyses = analyses;
            adapter = new AnalysisAdapter(this.analyses);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            Toast.makeText(requireContext(), getString(R.string.no_therapies),  Toast.LENGTH_LONG).show();
        }
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
            Log.i("ERROR!", "HERE");
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
    public void onMessageEvent(OnAnalysisShow event) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ShowAnalysisFragment showAnalysisFragment = new ShowAnalysisFragment();
        Bundle bundle = new Bundle();
        bundle.putString("analysis", ApplicationState.getGson().toJson(event.getAnalysis()));
        showAnalysisFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, showAnalysisFragment).commit();
    }
}