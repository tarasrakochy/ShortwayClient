package com.taras.shortway.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.taras.shortway.client.model.User;
import com.taras.shortway.client.rest.ApiClient;
import com.taras.shortway.client.rest.ApiInterface;
import com.taras.shortway.client.rest.ApiService;
import com.taras.shortway.client.rest.CallbackWrapper;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private SharedPreferences sharedPreferences;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ApiService apiService = new ApiService(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                user = apiService.getUserFromLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());

                if (user == null && !apiService.isFailure()) {
                    mEmailView.setText("");
                    mPasswordView.setText("");
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                }

                if (user != null) {
                    sharedPreferences
                            .edit()
                            .putString(getString(R.string.login_key), user.getLoginPass().getLogin())
                            .putString(getString(R.string.password_key), user.getLoginPass().getPassword())
                            .putInt(getString(R.string.password_key), user.getId())
                            .apply();

                    Globals.setUser(user);
                    startNextActivity();
                }
            }
        });

        Button registrationButton = (Button) findViewById(R.id.email_registration_button);
        registrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    private void startNextActivity() {
        Intent intent = new Intent(LoginActivity.this, TripTypeChooserActivity.class);
        startActivity(intent);
    }

}


