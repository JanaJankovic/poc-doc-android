package feri.pora.pocket_doctor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import feri.pora.datalib.Response;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RegisterActivity extends AppCompatActivity {

    ApplicationState applicationState;

    private Button buttonClose;
    private Button buttonRegister;
    private EditText fullnameText;
    private EditText medicalNumberText;
    private EditText phoneText;
    private EditText passwordText;

    private CompositeSubscription subscription;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        subscription = new CompositeSubscription();
        user = new User();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark));
        }

        applicationState = (ApplicationState)getApplication();

        bindGUI();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonCloseClick();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonRegisterClick();
            }
        });



    }

    public void bindGUI() {
        buttonClose = (Button) findViewById(R.id.buttonClose);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        fullnameText = (EditText) findViewById(R.id.editTextFullName);
        phoneText = (EditText) findViewById(R.id.editTextPhone);
        medicalNumberText = (EditText) findViewById(R.id.editTextMedicalNumber);
        passwordText = (EditText) findViewById(R.id.editTextTextPassword);
    }

    public void onButtonCloseClick() {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }


    public void onButtonRegisterClick() {
        if(!fullnameText.getText().toString().equals("") && !phoneText.getText().toString().equals("") &&
        !medicalNumberText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {
            if (passwordText.getText().toString().length() < 8) {
                Toast.makeText(getBaseContext(), getString(R.string.short_pass), Toast.LENGTH_LONG).show();
            } else {
               user.setFullName(fullnameText.getText().toString());
               user.setMedicalNumber(medicalNumberText.getText().toString());
               user.setPhone(phoneText.getText().toString());
               user.setPassword(passwordText.getText().toString());
               user.setLocation("");

                //API REQUEST
                registerProcess();

            }
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private void registerProcess() {

        subscription.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(User user) {
        Log.i("REGISTER", user.toString());
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = ApplicationState.getGson();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(getBaseContext(), response.getData(),  Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), error.getMessage(),  Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

}