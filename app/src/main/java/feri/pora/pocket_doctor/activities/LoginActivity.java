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

public class LoginActivity extends AppCompatActivity {
    private Button buttonLogin;
    private Button buttonRegister;
    private EditText medicalNumberText;
    private EditText passwordText;
    private User user;
    private CompositeSubscription subscription;

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        subscription = new CompositeSubscription();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        bindGUI();

        if(ApplicationState.checkLoggedUser()){
            user = ApplicationState.loadLoggedUser();
            Log.i("LOGIN Nav", user.toString());
            Intent intent = new Intent(this, UserNavigationActivity.class);
            startActivity(intent);
        }

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });

    }

    private void onClickLogin() {
        if(!medicalNumberText.getText().toString().equals("")
                && !passwordText.getText().toString().equals("")) {
        this.user = new User();
        user.setMedicalNumber(medicalNumberText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        //API REQUEST
        loginProcess();
        } else {
            Toast.makeText(getBaseContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
    }


    private void loginProcess() {
        subscription.add(NetworkUtil.getRetrofit().login(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(User user) {
        Log.i("LOGIN", user.toString());
        ApplicationState.saveLoggedUser(user);
        Intent intent = new Intent(this, UserNavigationActivity.class);
        startActivity(intent);
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
            Toast.makeText(getBaseContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
        }
    }

    public void bindGUI(){
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        medicalNumberText = (EditText) findViewById(R.id.editTextMedicalNumber);
        passwordText = (EditText) findViewById(R.id.editTextTextPassword);
    }

    public void onClickRegister() {
        this.startActivityForResult(
                new Intent(this.getBaseContext(), RegisterActivity.class),
                REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(getBaseContext(), "Registration complete", Toast.LENGTH_LONG).show();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show();
        }
    }
}