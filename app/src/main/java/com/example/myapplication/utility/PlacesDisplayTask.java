package com.example.myapplication.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.data.Place;
import com.example.myapplication.parser.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    ArrayList<Marker> bestMarkers;

    /**
     * takes as input the data in json format and through the Places class it appears
     */
    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            bestMarkers = (ArrayList<Marker>) inputObj[2];
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }


    /**
     * put a marker on the map for all the Places found, writing the name and street for each one
     */
    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        for (int i = 0; i < 5; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markerOptions.alpha(0.5f);

            //Creation of place for database
            Place temp_place = new Place();
            temp_place.setFull_address(googlePlace.get("vicinity"));
            temp_place.setId_google_place(googlePlace.get("reference"));
            temp_place.setVotes(0);
            temp_place.saveAsync(new AsyncCallback<Place>() {
                @Override
                public void handleResponse(Place response) {
                    TestApplication.best_places.add(response);
                    Log.i("place_saved", response.getFull_address());
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e("place_saved", fault.getMessage());
                }
            });
            bestMarkers.add(googleMap.addMarker(markerOptions));

        }

    }
}