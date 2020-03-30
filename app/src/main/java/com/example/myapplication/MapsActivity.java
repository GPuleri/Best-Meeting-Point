package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> points;

    /**
     * metodo che parte all'avvio dell'activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button button = findViewById(R.id.bestpoint);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    bestPointCalculator();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * prende gli indirizzi delle persone nel gruppo e tramite la funzione getLocationFromAddress() li trasfoma in un oggetto LatLng.
     * una volta che ha tutte le coordinate in formato Latlng di ogni partecipante al gruppo calcola la latitudine e longitudine al centro (best point).
     * trovato il best point fa una richiesta url per cercare Place di tipo "ristorante" vicini a quella coordinata nel raggio di 3km.
     * dopodichè tramite la classe GooglePlacesReadTask fa visualizzare i Places trovati sulla mappa, visualizzando nome del ristorante e la via.
     * @throws IOException
     * @throws JSONException
     */
    private void bestPointCalculator()  throws IOException, JSONException {

        points = new ArrayList<LatLng>();

        // indirizzi fittizi, prendere gli indirizzi veri tramite intent
        ArrayList<String> indirizzi = new ArrayList<>();
        indirizzi.add("Rome, italy");
        indirizzi.add("Milan, italy");
        indirizzi.add("Venice, italy");

        // prendo l'array di indirizzi e li trasformo in LatLng
        for (int i=0; i<indirizzi.size(); i++){
            LatLng point = getLocationFromAddress(indirizzi.get(i));
            points.add(point);
        }

        // calcolo best point
        double latitude = 0.0;
        double longitude = 0.0;

        for (int i = 0; i<points.size(); i++){
            latitude = latitude + points.get(i).latitude;
            longitude = longitude + points.get(i).longitude;
        }

        latitude = latitude/points.size();
        longitude = longitude/points.size();

        // metto un marker sul best point trovato
        LatLng bestPoint = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(bestPoint).title("Best Point"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bestPoint, 10));

        // creo la custom string per la richiesta url dei places
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 3000);
        googlePlacesUrl.append("&types=" + "restaurant");
        googlePlacesUrl.append("&key=" + "AIzaSyByyPXoo6la_E0E5MR7kLUL6Vh_YKgrLLg");

        // eseguo la classe GooglePlacesReadTask per visualizzare i place sulla mappa
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);

    }

    /**
     * metodo che prende in input la stringa "strAddress" che rappresenta l'indirizzo di una persona e ne restituisce un oggetto LatLng contenente latitudine e longitudine di quell'indirizzo
     * @param strAddress
     * @return
     */
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}