package com.taras.shortway.client.rest;

import com.taras.shortway.client.model.Auto;
import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/loginpass")
    Call<User> getUserFromLogin(@Query("login") String login, @Query("password") String password);

    @GET("/autos")
    Call<List<Auto>> getAutos();

    @GET("/autos/{id}")
    Call<Auto> getAutoById(@Path("id") int id);

    @GET("/users")
    Call<List<User>> getUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("/trips")
    Call<List<Trip>> getTrips();

    @GET("/trips/{id}")
    Call<Trip> getTripById(@Path("id") int id);

    @GET("/users/{id}/trips")
    Call<List<Trip>> getTripsForUser(@Path("id") int id, @Query("driver") boolean isDriver);

    @GET("/trips/{id}/passengers")
    Call<List<User>> getPassengers(@Path("id") int id);

    @GET("/trips/{id}/driver")
    Call<User> getDriver(@Path("id") int id);
}
