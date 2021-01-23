package feri.pora.pocket_doctor.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

import feri.pora.datalib.Analysis;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;

public class ShowAnalysisFragment extends Fragment {
    private TextView title;
    private TextView date;
    private TextView doctor;
    private TextView description;
    private ImageView imageView;
    Analysis analysis;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_analysis, container, false);
        bindGUI(rootView);
        Bundle bundle = getArguments();
        String json = bundle.getString("analysis");
        analysis = ApplicationState.getGson().fromJson(json, Analysis.class);
        bindData();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment, new AnalysisFragment()).commit();
                        return true;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    private void bindGUI(View v){
        title = (TextView) v.findViewById(R.id.textViewAnalysisProfile);
        date = (TextView) v.findViewById(R.id.textViewDateDiagnosis);
        doctor = (TextView) v.findViewById(R.id.textViewDoctorAnalysis);
        description = (TextView) v.findViewById(R.id.textViewAnalysisNote);
        imageView = (ImageView) v.findViewById(R.id.imageView7);
    }

    private void bindData(){
        title.setText(analysis.getTitle());
        long value = (long) Double.parseDouble(analysis.getTimestamp());
        date.setText(getDate(value));
        String name = analysis.getDoctor(ApplicationState.loadLoggedUser().getDoctorList()).getFullName();
        doctor.setText(name);
        description.setText(analysis.getDescription());
        byte[] decodedString = Base64.decode(analysis.getBase64AsciiImageString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
