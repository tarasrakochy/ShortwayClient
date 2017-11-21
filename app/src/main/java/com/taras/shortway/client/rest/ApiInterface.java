package com.taras.shortway.client.rest;

import com.taras.shortway.client.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/loginpass")
    Call<User> getUserFromLogin(@Query("login") String login, @Query("password") String password);
}
