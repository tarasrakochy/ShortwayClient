package com.taras.shortway.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.model.User;
import com.taras.shortway.client.rest.ApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ResultsTripsActivity extends AppCompatActivity {

    private static final String EXTRA_KEY = "com.taras.shortway.client.ResultsTripsActivity.extra_key";

    private Trip trip;

    private RecyclerView recyclerView;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_trips);

        apiService = new ApiService(this);

        trip = (Trip) getIntent().getSerializableExtra(EXTRA_KEY);

        recyclerView = (RecyclerView) findViewById(R.id.find_trips_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Trip> tripList = apiService.getTripsForConditions(trip);
        List<User> driverList = new ArrayList<>();
        for (Trip trip : tripList) {
            driverList.add(apiService.getDriver(trip.getId()));
        }

        RecyclerView.Adapter tripAdapter = new TripAdapter(tripList, driverList);
        recyclerView.setAdapter(tripAdapter);

    }

    public static void sendDataToActivity(Intent intent, Trip trip) {
        intent.putExtra(EXTRA_KEY, trip);
    }

    private class TripHolder extends RecyclerView.ViewHolder {

        private Trip trip;

        private TextView timeInfoField;
        private TextView streetsInfoField;
        private TextView costInfoField;

        private ImageView userAvatar;
        private TextView minUserInfo;

        private Button acceptTripButton;

        public TripHolder(View itemView) {
            super(itemView);

            timeInfoField = (TextView) findViewById(R.id.time_info_for_trip);
            streetsInfoField = (TextView) findViewById(R.id.streets_info_for_trip);
            costInfoField = (TextView) findViewById(R.id.cost_info_for_trip);

            userAvatar = (ImageView) findViewById(R.id.user_avatar_for_trip);
            minUserInfo = (TextView) findViewById(R.id.min_user_info_for_trip);

            acceptTripButton = (Button) findViewById(R.id.accept_trip_button);
            acceptTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userId = getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.user_id_key), 0);
                    boolean success = apiService.acceptTrip(userId, trip.getId(), trip.getFromPoint(), trip.getToPoint());
                    Toast.makeText(ResultsTripsActivity.this, success ? R.string.accept_trip_success : R.string.accept_trip_fail, Toast.LENGTH_SHORT).show();
                    if (success) {
                        Intent intent = new Intent(ResultsTripsActivity.this, TripTypeChooserActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        public void bindTrip(Trip trip, User user) {
            this.trip = trip;
            timeInfoField.setText(trip.getTime().toString());
            streetsInfoField.setText(trip.getFromPoint() + " - " + trip.getToPoint());
            costInfoField.setText("15");
            minUserInfo.setText(ResultsTripsActivity.this.getShortUserInfo(user));

            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            userAvatar.setImageBitmap(bitmap);
        }
    }

    private class TripAdapter extends RecyclerView.Adapter<TripHolder> {

        private List<Trip> trips;
        private List<User> users;

        public TripAdapter(List<Trip> trips, List<User> users) {
            this.trips = trips;
            this.users = users;
        }

        @Override
        public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ResultsTripsActivity.this);
            View v = inflater.inflate(R.layout.trips_item, parent);
            return new TripHolder(v);
        }

        @Override
        public void onBindViewHolder(TripHolder holder, int position) {
            Trip trip = trips.get(position);
            User user = users.get(position);

            holder.bindTrip(trip, user);
        }

        @Override
        public int getItemCount() {
            return trips.size();
        }
    }

    private String getShortUserInfo(User user) {
        String name = "Mykola";
        String lastNameShort = "M";
        int years = Calendar.getInstance().get(Calendar.YEAR) - user.getUserInfo().getYear();
        return String.format(Locale.getDefault(), "%s %s (%d р.)", name, lastNameShort, years);
    }
}
