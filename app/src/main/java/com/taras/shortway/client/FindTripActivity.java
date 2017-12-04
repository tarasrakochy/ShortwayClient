package com.taras.shortway.client;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.utils.GoogleMapsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FindTripActivity extends AppCompatActivity {

    private Date calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_trip);

        final Calendar calendar = Calendar.getInstance();

        final EditText dateChooser = (EditText) findViewById(R.id.date_field_find_trip);
        final EditText fromField = (EditText) findViewById(R.id.from_field_find_trip);
        final EditText toField = (EditText) findViewById(R.id.to_field_find_trip);
        EditText timeField = (EditText) findViewById(R.id.time_field_find_trip);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarDate = calendar.getTime();
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateChooser.setText(sdf.format(calendar.getTime()));
            }

        };

        dateChooser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(FindTripActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button findTripButton = (Button) findViewById(R.id.find_trip_for_parameters_button);

        findTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromPoint = GoogleMapsUtils.convertToLatLngString(fromField.getText().toString());
                String toPoint = GoogleMapsUtils.convertToLatLngString(toField.getText().toString());

                if (fromPoint != null
                        && toPoint != null) {
                    Trip trip = new Trip();
                    trip.setFromPoint(GoogleMapsUtils.convertToLatLngString(fromField.getText().toString()));
                    trip.setToPoint(GoogleMapsUtils.convertToLatLngString(fromField.getText().toString()));
                    trip.setDate(calendarDate);

                    Intent intent = new Intent(FindTripActivity.this, ResultsTripsActivity.class);
                    ResultsTripsActivity.sendDataToActivity(intent, trip);
                    startActivity(intent);
                } else {
                    if (fromPoint == null) {
                        fromField.setText("");
                        fromField.setError(getString(R.string.invalid_data_for_searching_trips));
                    }
                    if (toPoint == null) {
                        toField.setText("");
                        toField.setError(getString(R.string.invalid_data_for_searching_trips));
                    }
                }
            }
        });


    }

}
