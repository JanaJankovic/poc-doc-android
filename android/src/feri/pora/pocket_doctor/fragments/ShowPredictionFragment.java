package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import java.io.File;

import feri.pora.datalib.Device;
import feri.pora.datalib.Prediction;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;

public class ShowPredictionFragment extends Fragment {
    private TextView predictionTextView;
    private TextView date;
    private ImageView imageView;
    private Button sendDoctor;
    private Button goToList;
    private Button cancel;

    private Prediction prediction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_prediction, container, false);

        ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
        ((UserNavigationActivity) requireActivity()).getSupportActionBar()
                .setTitle("Request analysis");
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bindGUI(rootView);
        Bundle bundle = getArguments();
        prediction = ApplicationState.getGson().fromJson(bundle.getString("prediction"),
                Prediction.class);
        predictionTextView.setText(prediction.getPrediction());
        date.setText("Date of prediction : " + prediction.getDate());
        setPictureBackground(rootView, prediction.getFilePath());

        if (prediction.getId().equals("")){
            cancel.setVisibility(View.INVISIBLE);
            goToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.nav_host_fragment, new AnalysisFragment()).commit();
                }
            });
        } else {
            goToList.setVisibility(View.INVISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO delete request
                }
            });
        }

        sendDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SendDoctorList sendDoctorList = new SendDoctorList();
                Bundle bundle = new Bundle();
                bundle.putString("prediction", ApplicationState.getGson().toJson(prediction));
                sendDoctorList.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, sendDoctorList).commit();
            }
        });

        return rootView;
    }

    private void bindGUI(View v){
        predictionTextView = (TextView) v.findViewById(R.id.textViewPredictionName);
        date = (TextView) v.findViewById(R.id.textViewPredictionDate);
        imageView = (ImageView) v.findViewById(R.id.imageViewPrediction);
        sendDoctor = (Button) v.findViewById(R.id.buttonSendPrediction);
        goToList = (Button) v.findViewById(R.id.buttonListPending);
        cancel = (Button) v.findViewById(R.id.buttonCanelPrediction);
    }

    public void setPictureBackground(View view, String filepath) {
        Log.i("TAGGGG", filepath);
        if (filepath.length() > 0) {
            File imageFile = new File(filepath);
            Picasso.get().load(imageFile).fit().into(imageView);
        }
    }
}
