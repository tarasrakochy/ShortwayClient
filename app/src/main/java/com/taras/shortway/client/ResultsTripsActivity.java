package com.taras.shortway.client;

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
import android.widget.TextView;
import android.widget.Toast;

import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.model.User;
import com.taras.shortway.client.rest.ApiService;
import com.taras.shortway.client.utils.GoogleMapsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultsTripsActivity extends AppCompatActivity {

    private static final String TRIP_KEY = "com.taras.shortway.client.ResultsTripsActivity.trip_key";
    private static final String MAX_WAIT_TIME_KEY = "com.taras.shortway.client.ResultsTripsActivity.max_wait_time_key";

    private Trip trip;
    private int maxWaitTime;

    private RecyclerView recyclerView;

    private ApiService apiService;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_trips);

        user = Globals.getUser();

        setTitle("Знайдені поїздки");

        apiService = new ApiService(this);

        trip = (Trip) getIntent().getSerializableExtra(TRIP_KEY);
        maxWaitTime = getIntent().getIntExtra(MAX_WAIT_TIME_KEY, 0);

        recyclerView = (RecyclerView) findViewById(R.id.find_trips_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Trip> tripList = apiService.getTripsForConditions(trip, maxWaitTime);

        List<User> driverList = new ArrayList<>();
        for (Trip trip : tripList) {
            driverList.add(apiService.getDriver(trip.getId()));
        }

        RecyclerView.Adapter tripAdapter = new TripAdapter(tripList, driverList);
        recyclerView.setAdapter(tripAdapter);

    }

    public static void sendDataToActivity(Intent intent, Trip trip, int maxWaitTime) {
        intent.putExtra(TRIP_KEY, trip);
        intent.putExtra(MAX_WAIT_TIME_KEY, maxWaitTime);
    }

    private class TripHolder extends RecyclerView.ViewHolder {

        private Trip trip;

        private TextView dateInfoField;
        private TextView timeInfoField;
        private TextView streetsInfoField;
        private TextView costInfoField;

        private CircleImageView userAvatar;
        private TextView minUserInfo;

        private Button acceptTripButton;

        public TripHolder(View itemView) {
            super(itemView);

            dateInfoField = (TextView) itemView.findViewById(R.id.date_info_for_trip);
            timeInfoField = (TextView) itemView.findViewById(R.id.time_info_for_trip);
            streetsInfoField = (TextView) itemView.findViewById(R.id.streets_info_for_trip);
            costInfoField = (TextView) itemView.findViewById(R.id.cost_info_for_trip);

            userAvatar = (CircleImageView) itemView.findViewById(R.id.user_avatar_for_trip);
            minUserInfo = (TextView) itemView.findViewById(R.id.min_user_info_for_trip);

            acceptTripButton = (Button) itemView.findViewById(R.id.accept_trip_button);
            acceptTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userId = user.getId();
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
            dateInfoField.setText(new SimpleDateFormat("dd-MM-yyyy").format(trip.getDate()));
            timeInfoField.setText(new SimpleDateFormat("HH:mm").format(trip.getDate()));
            streetsInfoField.setText(GoogleMapsUtils.convertToString(trip.getFromPoint()) + " - " + GoogleMapsUtils.convertToString(trip.getToPoint()));
            costInfoField.setText(trip.getPrice() + " грн");
            minUserInfo.setText(ResultsTripsActivity.this.getShortUserInfo(user));

            if (user.getAvatar() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
                if (bitmap != null) {
                    userAvatar.setImageBitmap(bitmap);
                }
            }
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
            View v = inflater.inflate(R.layout.trips_item, parent, false);
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
        String name = user.getName();
        String lastName = user.getSurname();
        if (user.getUserInfo() != null) {
            int years = Calendar.getInstance().get(Calendar.YEAR) - user.getUserInfo().getYear();
            return String.format(Locale.getDefault(), "%s %s (%d р.)", name, lastName, years);
        } else {
            return String.format(Locale.getDefault(), "%s %s", name, lastName);
        }
    }
}
