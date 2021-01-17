package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feri.pora.datalib.Diagnosis;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.DiagnosisAdapter;

public class DiagnosisFragment extends Fragment {
    private DiagnosisAdapter diagnosisAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Diagnosis> diagnoses;

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

        diagnoses = new ArrayList<>();
        bindGUI(rootView);

        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewDiagnoses);
        diagnosisAdapter = new DiagnosisAdapter(diagnoses);
        recyclerView.setAdapter(diagnosisAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}