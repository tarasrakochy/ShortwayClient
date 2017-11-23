package com.taras.shortway.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TripTypeChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_type_chooser);

        Button button = (Button) findViewById(R.id.log_out_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                preferences
                        .edit()
                        .remove(getString(R.string.login_key))
                        .remove(getString(R.string.password_key))
                        .remove(getString(R.string.user_id_key))
                        .apply();
                Intent intent = new Intent(TripTypeChooserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
