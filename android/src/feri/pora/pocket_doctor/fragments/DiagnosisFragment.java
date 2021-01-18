package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import feri.pora.datalib.Data;
import feri.pora.datalib.Diagnosis;
import feri.pora.datalib.Therapy;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.DiagnosisAdapter;
import feri.pora.pocket_doctor.adapters.TherapyAdapter;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DiagnosisFragment extends Fragment {
    private DiagnosisAdapter diagnosisAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Diagnosis> diagnoses;

    private CompositeSubscription subscription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_diagnosis, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle("Diagnosis");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        subscription = new CompositeSubscription();
        bindGUI(rootView);
        getDiagnoses();

        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewDiagnoses);
    }

    public void getDiagnoses() {
        Log.i("PUBLICV", ApplicationState.loadLoggedUser().getPublicKey());
        subscription.add(NetworkUtil.getRetrofit(true)
                .getDiagnosis(new Data(ApplicationState.loadLoggedUser().getPublicKey()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(ArrayList<Diagnosis> diagnoses) {
        if (diagnoses != null && diagnoses.size() > 0) {
            this.diagnoses = diagnoses;
            diagnosisAdapter = new DiagnosisAdapter(diagnoses);
            recyclerView.setAdapter(diagnosisAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            Toast.makeText(requireContext(), "No recorded diagnoses",  Toast.LENGTH_LONG).show();
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
            Toast.makeText(requireContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
        }
    }
}