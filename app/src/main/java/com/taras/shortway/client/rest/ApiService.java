package com.taras.shortway.client.rest;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.taras.shortway.client.R;
import com.taras.shortway.client.model.Auto;
import com.taras.shortway.client.model.Trip;
import com.taras.shortway.client.model.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ApiService {

    private static ApiInterface apiInterface = ApiClient
            .getClient()
            .create(ApiInterface.class);

    private Context context;

    private boolean failure;

    public ApiService(Context context) {
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public boolean isFailure() {
        return failure;
    }

    private <T> T handleCall(Call<T> call) {
        try {
            Response<T> response =  call.execute();
            if (response.isSuccessful()) {
                failure = false;
                return response.body();
            } else {
                failure = true;
                Toast.makeText(context, R.string.server_connection_error, Toast.LENGTH_SHORT).show();
            }
        } catch (SocketTimeoutException|ConnectException e) {
            failure = true;
            Toast.makeText(context, R.string.server_connection_error, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            failure = false;
        }
        return null;
    }



    public User getUserFromLogin(String login, String password){
        Call<User> userCall = apiInterface.getUserFromLogin(login, password);
        return handleCall(userCall);
    }

    public List<Auto> getAutos(){
        Call<List<Auto>> autosListCall = apiInterface.getAutos();
        return handleCall(autosListCall);
    }
    
    public Auto getAutoById(int id){
        Call<Auto> autoCall = apiInterface.getAutoById(id);
        return handleCall(autoCall);
    }
    
    public List<User> getUsers(){
        Call<List<User>> usersListCall = apiInterface.getUsers();
        return handleCall(usersListCall);
    }
    
    public User getUserById(int id){
        Call<User> userCall = apiInterface.getUserById(id);
        return handleCall(userCall);
    }
    
    public List<Trip> getTrips(){
        Call<List<Trip>> tripsListCall = apiInterface.getTrips();
        return handleCall(tripsListCall);
    }
    
    public Trip getTripById(int id){
        Call<Trip> tripCall = apiInterface.getTripById(id);
        return handleCall(tripCall);
    }
}