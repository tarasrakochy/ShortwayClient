package com.taras.shortway.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String login = preferences.getString(getString(R.string.login_key), null);
        String password = preferences.getString(getString(R.string.password_key), null);

        Intent intent = new Intent();

        if (login != null && password != null) {
            intent.setClass(this, TripTypeChooserActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }

        startActivity(intent);


    }
}
