package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;

public class SettingsFragment extends Fragment {
    private EditText edtName;
    private EditText edtNumber;
    private EditText edtLocation;
    private EditText edtPassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_settings, container, false);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle(getString(R.string.action_settings));
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((UserNavigationActivity) requireActivity()).navigationView
                .setCheckedItem(R.id.nav_settings);

        bindGUI(rootView);

        return rootView;
    }

    private void bindGUI(View v){
        edtName = (EditText) v.findViewById(R.id.edtName);
        edtNumber = (EditText) v.findViewById(R.id.edtNumber);
        edtLocation = (EditText) v.findViewById(R.id.edtLocation);
        edtPassword= (EditText) v.findViewById(R.id.edtPassword);
        //missing Language spinner
    }
}
