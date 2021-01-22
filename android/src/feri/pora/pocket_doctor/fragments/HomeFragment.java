package feri.pora.pocket_doctor.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.HomeAdapter;

public class HomeFragment extends Fragment {
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private TextView fullName;
    private TextView medicalNumber;

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
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.diagnosis2));
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new DiagnosisFragment()).commit();
                        break;
                    case 1 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_active_therapies);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.therapies));
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new TherapiesFragment()).commit();
                        break;
                    case 2 :
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_list_analysis);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.analysis));
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new AnalysisFragment()).commit();
                        break;
                    case 3:
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_request_analysis);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.req_analysis));
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
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.available_doctors));
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new DoctorListFragment()).commit();
                        break;
                    case 6:
                        ((UserNavigationActivity) requireActivity()).navigationView
                                .setCheckedItem(R.id.nav_settings);
                        ((UserNavigationActivity) requireActivity()).toolbar.setTitle(getString(R.string.action_settings));
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment,
                                new SettingsFragment()).commit();
                        break;
                    case 7:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle(getString(R.string.log_out));
                        builder.setMessage(getString(R.string.are_you_sure));

                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User user = new User();
                                ApplicationState.saveLoggedUser(user);
                                getActivity().finish();
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
            }
        });
        fullName = (TextView) v.findViewById(R.id.textViewFullnamePatient);
        fullName.setText(ApplicationState.loadLoggedUser().getFullName());
        medicalNumber = (TextView) v.findViewById(R.id.textViewHealthStatus);
        medicalNumber.setText(ApplicationState.loadLoggedUser().getMedicalNumber());
    }

}