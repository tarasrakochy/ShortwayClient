package com.taras.shortway.client.utils;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodedWaypoint;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class GoogleMapsUtils {
    private static final String GOOGLE_API_KEY = "AIzaSyA6lK_fbfSoUkkwa5RcmoL8ZkoZNjrJLe4";

    public static String convertToLatLngString(String place) {
        try {
            GeocodingApiRequest geocodingApiRequest = GeocodingApi
                    .newRequest(new GeoApiContext.Builder()
                            .apiKey(GOOGLE_API_KEY)
                            .build())
                    .address(place + " ");
            GeocodingResult[] results = geocodingApiRequest.await();
            if (results.length == 0) {
                return null;
            } else {
                LatLng location = results[0].geometry.location;
                return location.lat + "," + location.lng;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertToString(String latLngString) {
        String[] latLngStringArray = latLngString.split(",");
        LatLng latLng = new LatLng(Double.parseDouble(latLngStringArray[0]), Double.parseDouble(latLngStringArray[1]));
        try {
            GeocodingApiRequest geocodingApiRequest = GeocodingApi
                    .newRequest(new GeoApiContext.Builder()
                            .apiKey(GOOGLE_API_KEY)
                            .build())
                    .language("uk")
                    .latlng(latLng);
            GeocodingResult[] results = geocodingApiRequest.await();
            if (results.length == 0) {
                return null;
            } else {
                return results[0].addressComponents[1].shortName + " " + results[0].addressComponents[0].shortName;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
