package com.taras.shortway.client.rest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.taras.shortway.client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackWrapper<T> implements Callback<T> {

    private static final String TAG = CallbackWrapper.class.getName();

    private Context context;

    private boolean failure;

    private T t;

    private boolean flag;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        t = response.body();
        flag = true;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        failure = true;
        Toast.makeText(context, R.string.server_connection_error, Toast.LENGTH_SHORT).show();
        Log.e(TAG, t.getMessage());
        flag = true;
    }

    public CallbackWrapper(Context context) {
        this.context = context;
    }

    public T getResult() {
        //while(!flag);
        return t;
    }

    public boolean isFailure() {
        //while(!flag);
        return failure;
    }
}
