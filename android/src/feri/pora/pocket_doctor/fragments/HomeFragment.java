package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.HomeAdapter;

public class HomeFragment extends Fragment {
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private TextView fullName;
    private TextView status;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home, null);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bindGUI(rootView);

        return  rootView;
    }

    public void bindGUI(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewHome);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        homeAdapter = new HomeAdapter();
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.setOnClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.i("CLICKZ", "Clicked me!!!!");
                switch (position){
                    case 0 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_diagnosis);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle("Diagnosis");
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new DiagnosisFragment()).commit();
                        break;
                    case 1 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_active_therapies);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle("Therapies");
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new TherapiesFragment()).commit();
                        break;
                    case 2 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_list_analysis);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle("Analysis");
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new ListAnalysisFragment()).commit();
                        break;
                    case 3:
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_request_analysis);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle("Request analysis");
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new RequestAnalysisFragment()).commit();
                        break;
                    case 4 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_oxymeter);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new OxymeterFragment()).commit();
                        break;
                    case 5 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_doctors);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle("Available doctors");
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new DoctorListFragment()).commit();
                        break;
                }
            }
        });
        fullName = (TextView) v.findViewById(R.id.textViewFullnamePatient);
        fullName.setText(ApplicationState.loadLoggedUser().getFullName());
        status = (TextView) v.findViewById(R.id.textViewHealthStatus);
        status.setText(ApplicationState.loadLoggedUser().getStatus());
    }

}