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
import com.taras.shortway.client.model.UserInfo;
import com.taras.shortway.client.model.enums.Gender;
import com.taras.shortway.client.rest.ApiService;
import com.taras.shortway.client.utils.GoogleMapsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultsTripsActivity extends AppCompatActivity {

    private static final String EXTRA_KEY = "com.taras.shortway.client.ResultsTripsActivity.extra_key";

    private Trip trip;

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

        trip = (Trip) getIntent().getSerializableExtra(EXTRA_KEY);

        recyclerView = (RecyclerView) findViewById(R.id.find_trips_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Trip> tripList = apiService.getTripsForConditions(trip);

//        List<Trip> tripList = new ArrayList<>();
//
//        Trip trip = new Trip();
//        trip.setId(1);
//        trip.setDate(new Date());
//        trip.setFromPoint("Городоцька");
//        trip.setToPoint("Наукова");
//        tripList.add(trip);
//
//        Trip trip2 = new Trip();
//        trip2.setId(2);
//        trip2.setDate(new Date());
//        trip2.setFromPoint("Кульпарківська");
//        trip2.setToPoint("Зелена");
//        tripList.add(trip2);
//
        List<User> driverList = new ArrayList<>();
        for (Trip trip : tripList) {
            driverList.add(apiService.getDriver(trip.getId()));
        }
//        User user = new User();
//        user.setId(10);
//        user.setEmail("email");
//        user.setPhone("097");
//        UserInfo userInfo = new UserInfo();
//        userInfo.setId(1);
//        userInfo.setGender(Gender.MALE);
//        userInfo.setYear(1995);
//        user.setUserInfo(userInfo);
//        driverList.add(user);
//
//        User user2 = new User();
//        user2.setId(11);
//        user2.setEmail("email2");
//        user2.setPhone("098");
//        UserInfo userInfo1 = new UserInfo();
//        userInfo1.setId(2);
//        userInfo1.setYear(1986);
//        user2.setUserInfo(userInfo1);
//        driverList.add(user2);
//
        RecyclerView.Adapter tripAdapter = new TripAdapter(tripList, driverList);
        recyclerView.setAdapter(tripAdapter);

    }

    public static void sendDataToActivity(Intent intent, Trip trip) {
        intent.putExtra(EXTRA_KEY, trip);
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
            costInfoField.setText(trip.getPrice());
            minUserInfo.setText(ResultsTripsActivity.this.getShortUserInfo(user));

            if (user.getAvatar() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
                userAvatar.setImageBitmap(bitmap);
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
