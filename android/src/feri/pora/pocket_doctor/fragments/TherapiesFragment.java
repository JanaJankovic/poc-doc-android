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

import feri.pora.datalib.Therapy;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.DiagnosisAdapter;
import feri.pora.pocket_doctor.adapters.TherapyAdapter;

public class TherapiesFragment extends Fragment {
    private TherapyAdapter therapyAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Therapy> therapies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_therapies, null);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle("Therapies");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        therapies = new ArrayList<>();


        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewTherapies);
        therapyAdapter = new TherapyAdapter(therapies);
        recyclerView.setAdapter(therapyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
