package feri.pora.pocket_doctor.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.Response;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.DoctorAdapter;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SettingsFragment extends Fragment {
    private CompositeSubscription subscription;
    private EditText edtName;
    private EditText edtNumber;
    private EditText edtLocation;
    private EditText edtPassword;
    private Spinner spinner;
    private Button button;
    private ArrayList<String> languages = new ArrayList<>();

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
       languages.add(getString(R.string.english));
       languages.add(getString(R.string.slovenian));
       bindGUI(rootView);
       bindValues();
       subscription = new CompositeSubscription();

        return rootView;
    }

    private void bindGUI(View v){
        edtName = (EditText) v.findViewById(R.id.edtName);
        edtNumber = (EditText) v.findViewById(R.id.edtNumber);
        edtLocation = (EditText) v.findViewById(R.id.edtLocation);
        edtPassword= (EditText) v.findViewById(R.id.edtPassword);
        button = (Button) v.findViewById(R.id.btnSetSettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SETTINGS", "clcikced");
                User user = new User();
                if (!edtName.getText().toString().equals(""))
                    user.setFullName(edtName.getText().toString());
                if (!edtNumber.getText().toString().equals(""))
                    user.setPhone(edtNumber.getText().toString());
                if (!edtLocation.getText().toString().equals(""))
                    user.setLocation(edtLocation.getText().toString());
                if (!edtPassword.getText().toString().equals(""))
                    user.setPassword(edtPassword.getText().toString());
                user.setId(ApplicationState.loadLoggedUser().getId());
                Log.i("USER!!!", user.toString());
                updateProfile(user);
            }
        });
        spinner = (Spinner) v.findViewById(R.id.spinnerLanguage);
        spinner.setAdapter(new LanguageAdapter(requireContext(),
                R.layout.item_language, languages));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //change language
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    private void bindValues(){
        User user = ApplicationState.loadLoggedUser();
        edtName.setHint(user.getFullName());
        edtPassword.setHint(getString(R.string.password_hint));
        edtLocation.setHint(user.getLocation());
        edtNumber.setHint(user.getPhone());
    }

    private void updateProfile(User user) {
        subscription.add(NetworkUtil.getRetrofit().updateProfile(user.getId(), user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(User user) {
        ApplicationState.saveLoggedUser(user);
        TextView name = (TextView) ((UserNavigationActivity)getActivity()).navigationView.getHeaderView(0)
                .findViewById(R.id.userFullName);
        name.setText(ApplicationState.loadLoggedUser().getFullName());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new SettingsFragment()).commit();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = ApplicationState.getGson();
            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Log.i("ERROR!", errorBody);
                //Response response = gson.fromJson(errorBody,Response.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(requireContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
        }
    }

    public class LanguageAdapter extends ArrayAdapter<String>{
        ArrayList<String> languages = new ArrayList<>();

        public LanguageAdapter(Context context, int textViewResourceId,
                               ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.languages = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row = inflater.inflate(R.layout.item_language, parent, false);

            TextView label = (TextView) row.findViewById(R.id.textViewLanguage);
            ImageView icon = (ImageView) row.findViewById(R.id.imageViewFlag);

            label.setText(languages.get(position));
            if (languages.get(position).equals(getString(R.string.english)))
                icon.setImageResource(R.drawable.ic_united_kingdom);
            else
                icon.setImageResource(R.drawable.ic_slovenia);
            return row;
        }
    }
}

