package com.taras.shortway.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TripTypeChooserActivity extends AppCompatActivity {

    private Button findTripButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_type_chooser);

        Button findTripButton = (Button) findViewById(R.id.find_trip_button);
        findTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripTypeChooserActivity.this, FindTripActivity.class);
                startActivity(intent);
            }
        });

        Button suggestTripButton = (Button) findViewById(R.id.suggest_trip_button);
        suggestTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripTypeChooserActivity.this, SuggestTripActivity.class);
                startActivity(intent);
            }
        });

        ImageView goToProfileButton = (ImageView) findViewById(R.id.go_to_profile);
        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripTypeChooserActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
