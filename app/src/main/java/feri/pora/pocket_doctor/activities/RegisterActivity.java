package feri.pora.pocket_doctor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import feri.pora.datalib.User;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;

public class RegisterActivity extends AppCompatActivity {

    ApplicationState applicationState;

    private Button buttonClose;
    private Button buttonRegister;
    private EditText fullnameText;
    private EditText medicalNumberText;
    private EditText phoneText;
    private EditText passwordText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                Toast.makeText(getBaseContext(), "Password is too short", Toast.LENGTH_LONG).show();
            } else {
               // user = new User(medicalNumberText.getText().toString(), fullnameText.getText().toString()
                    //    , passwordText.getText().toString(), phoneText.getText().toString());

                //API REQUEST

                ApplicationState.saveLoggedUser(user);
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }
        } else {
            Toast.makeText(getBaseContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
    }

}