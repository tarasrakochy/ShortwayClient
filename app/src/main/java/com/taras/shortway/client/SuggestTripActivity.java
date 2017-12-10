package com.taras.shortway.client;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.utils.GoogleMapsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SuggestTripActivity extends AppCompatActivity {

    private Date calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_trip);

        setTitle("Запропонувати поїздку");

        final Calendar calendar = Calendar.getInstance();


        final EditText dateChooser = (EditText) findViewById(R.id.date_field_suggest_trip);
        final EditText fromField = (EditText) findViewById(R.id.from_field_suggest_trip);
        final EditText toField = (EditText) findViewById(R.id.to_field_suggest_trip);
        final EditText timeField = (EditText) findViewById(R.id.time_field_suggest_trip);

        Button addTransitionalPointsButton = (Button) findViewById(R.id.add_transitional_points_button);

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

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                calendarDate = calendar.getTime();
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                timeField.setText(sdf.format(calendar.getTime()));
            }
        };

        dateChooser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SuggestTripActivity.this, R.style.AppTheme, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SuggestTripActivity.this, R.style.AppTheme, time, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        final List<EditText> transitionalEditTexts = new ArrayList<>();

        addTransitionalPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.transitional_layout);
                EditText editText = new EditText(SuggestTripActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editText.setLayoutParams(params);
                editText.setHint("Проміжна точка");
                layout.addView(editText);
                transitionalEditTexts.add(editText);
            }
        });

        Button suggestTripButton = (Button) findViewById(R.id.suggest_trip_for_parameters_button);

        suggestTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromPoint = GoogleMapsUtils.convertToLatLngString(fromField.getText().toString());
                String toPoint = GoogleMapsUtils.convertToLatLngString(toField.getText().toString());

                boolean invalid = false;
                List<String> transitionalPoints = new ArrayList<>();
                for (EditText transitionalEditText : transitionalEditTexts) {
                    String transitionalPoint = GoogleMapsUtils.convertToLatLngString(transitionalEditText.getText().toString());
                    if (transitionalPoint == null) {
                        transitionalEditText.setText("");
                        transitionalEditText.setError(getString(R.string.invalid_data_for_searching_trips));
                        invalid = true;
                    }
                    if (!invalid) {
                        transitionalPoints.add(transitionalPoint);
                    }
                }

                if (fromPoint != null
                        && toPoint != null
                        && !invalid) {
                    Trip trip = new Trip();
                    trip.setFromPoint(GoogleMapsUtils.convertToLatLngString(fromField.getText().toString()));
                    trip.setToPoint(GoogleMapsUtils.convertToLatLngString(toField.getText().toString()));
                    trip.setDate(calendarDate);
                    trip.setTransitionals(transitionalPoints);
                    //TODO server  request

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
