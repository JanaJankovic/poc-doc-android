package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;

public class SendRequestFragment extends Fragment {

    private ImageView imageView;
    private Button buttonCancel;
    private Button buttonSendRequest;

    public SendRequestFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_send_request, null);

        bindGUI(rootView);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setPictureBackground(rootView);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, new RequestAnalysisFragment()).commit();
            }
        });

        return  rootView;
    }

    public void bindGUI(View view) {
        imageView = (ImageView) view.findViewById(R.id.chosenImage);
        buttonCancel = (Button) view.findViewById(R.id.buttonClose);
        buttonSendRequest = (Button) view.findViewById(R.id.buttonSendRequest);
    }

    public void setPictureBackground(View view) {
        Bundle bundle = this.getArguments();
        String data = bundle.getString("filepath");
        Log.i("TAGGGG", data);

        if (data.length() > 0) {
            File imageFile = new File(data);
            Picasso.get().load(imageFile).fit().centerCrop().into(imageView);

        }
    }
}