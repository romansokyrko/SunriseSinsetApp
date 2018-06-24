package com.sokyrko.sunrisesunsetapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.sokyrko.sunrisesunsetapp.dto.SunriseSunsetInfo;
import com.sokyrko.sunrisesunsetapp.dto.SunriseSunsetResponse;
import com.sokyrko.sunrisesunsetapp.dto.TimeZoneResponse;
import com.sokyrko.sunrisesunsetapp.model.LocationSunriseSunsetInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String SUNSET_SUNRISE_API_URL = "https://api.sunrise-sunset.org/json";
    private static final String TIME_ZONE_API_URL = "https://maps.googleapis.com/maps/api/timezone/json";

    private GoogleApiClient mGoogleApiClient;

    private TextView locationTextView;
    private TextView todayTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView firstLightTextView;
    private TextView lastLightTextView;
    private TextView dayLengthTextView;
    private PlaceAutocompleteFragment autocompleteFragment;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;

    private LocationSunriseSunsetInfo selectedLocationInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = findViewById(R.id.location_text_view);
        todayTextView = findViewById(R.id.todayTextView);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        firstLightTextView = findViewById(R.id.firstLightTextView);
        lastLightTextView = findViewById(R.id.lastLightTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        dayLengthTextView = findViewById(R.id.dayLengthTextView);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getResources().getString(R.string.autocomplete_hint));
        autocompleteFragment.setOnPlaceSelectedListener(this);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        progressBar= findViewById(R.id.progress_bar);

        selectedLocationInfo=new LocationSunriseSunsetInfo();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }




    }

    private void updateView(){
        locationTextView.setText(selectedLocationInfo.getAddress());
        todayTextView.setText(selectedLocationInfo.getFormattedCurentTime());
        sunriseTextView.setText(selectedLocationInfo.getFormattedSunriseTime());
        firstLightTextView.setText(selectedLocationInfo.getFormattedFirstLightTime());
        lastLightTextView.setText(selectedLocationInfo.getFormattedLastLightTime());
        sunsetTextView.setText(selectedLocationInfo.getFormattedSunsetTime());
        dayLengthTextView.setText(selectedLocationInfo.getFormattedDayLength());
    }


    private void getCurrentLocation() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                if (likelyPlaces.getCount()==0) {
                    Toast.makeText(MainActivity.this, "Can't retrieve location\n" +
                            "Check your network/gps", Toast.LENGTH_LONG).show();
                } else {
                    Place currentPlace = likelyPlaces.get(0).getPlace();
                    selectedLocationInfo.setAddress(currentPlace.getAddress().toString());
                    new RetrieveTimeZoneRESTTask().execute(currentPlace.getLatLng().latitude, currentPlace.getLatLng().longitude);
                    new RetrieveSunriseSunsetRESTTask().execute(currentPlace.getLatLng().latitude, currentPlace.getLatLng().longitude);
                }
                likelyPlaces.release();
            }


        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error: Connection Failed!", Toast.LENGTH_LONG).show();
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission isn't granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        selectedLocationInfo.setAddress(place.getAddress().toString());
        new RetrieveTimeZoneRESTTask().execute(place.getLatLng().latitude, place.getLatLng().longitude);
        new RetrieveSunriseSunsetRESTTask().execute(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    @Override
    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
            autocompleteFragment.setText("");
        }
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    class RetrieveSunriseSunsetRESTTask extends AsyncTask<Double, Void, ResponseEntity<SunriseSunsetResponse>> {

        private final String TAG = RetrieveSunriseSunsetRESTTask.class.getName();

        @Override
        protected ResponseEntity<SunriseSunsetResponse> doInBackground(Double... args) {
            final double lat = args[0];
            final double lng = args[1];
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(SUNSET_SUNRISE_API_URL)
                    .queryParam("lat", lat)
                    .queryParam("lng", lng)
                    .queryParam("formatted", 0);
            String url=builder.build().toUriString();
            RestTemplate restTemplate = new RestTemplate();
            try{
                publishProgress();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<SunriseSunsetResponse> response = restTemplate.exchange(url,
                        HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), SunriseSunsetResponse.class);
                return response;
            } catch(Exception e){
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ResponseEntity<SunriseSunsetResponse> sunriseSunsetInfoResponseEntity) {
            HttpStatus statusCode = sunriseSunsetInfoResponseEntity.getStatusCode();
            SunriseSunsetResponse sunriseSunsetResponse = sunriseSunsetInfoResponseEntity.getBody();
            SunriseSunsetInfo sunriseSunsetInfo = sunriseSunsetResponse.getSunriseSunsetInfo();
            selectedLocationInfo.setSunriseTime(sunriseSunsetInfo.getSunrise());
            selectedLocationInfo.setSunsetTime(sunriseSunsetInfo.getSunset());
            selectedLocationInfo.setDayLength(sunriseSunsetInfo.getDayLength());
            selectedLocationInfo.setFirstLightTime(sunriseSunsetInfo.getCivilTwilightBegin());
            selectedLocationInfo.setLastLightTime(sunriseSunsetInfo.getCivilTwilightEnd());
            progressBar.setVisibility(View.GONE);
            updateView();
        }
    }

    class RetrieveTimeZoneRESTTask extends AsyncTask<Double, Void, ResponseEntity<TimeZoneResponse>> {

        private final String TAG = RetrieveTimeZoneRESTTask.class.getName();

        @Override
        protected ResponseEntity<TimeZoneResponse> doInBackground(Double... args) {
            final double lat = args[0];
            final double lng = args[1];
            String location = lat +"," +lng;
            long timestamp = System.currentTimeMillis()/1000;
            ApplicationInfo ai = null;
            try {
                ai = getPackageManager().getApplicationInfo(MainActivity.this.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to load meta-data, NameNotFound", e);
                return null;
            }
            Bundle bundle = ai.metaData;
            String myApiKey = bundle.getString("com.google.android.geo.API_KEY");
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(TIME_ZONE_API_URL)
                    .queryParam("location", location)
                    .queryParam("timestamp", timestamp)
                    .queryParam("key", myApiKey);
            String url=builder.build().toUriString();
            RestTemplate restTemplate = new RestTemplate();
            try{
                publishProgress();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<TimeZoneResponse> response = restTemplate.exchange(url,
                        HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), TimeZoneResponse.class);
                return response;
            } catch(Exception e){
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ResponseEntity<TimeZoneResponse> timeZoneResponseEntity) {
            HttpStatus statusCode = timeZoneResponseEntity.getStatusCode();
            TimeZoneResponse timeZoneResponse = timeZoneResponseEntity.getBody();
            selectedLocationInfo.setTimeZone(TimeZone.getTimeZone(timeZoneResponse.getTimeZoneId()));
            progressBar.setVisibility(View.GONE);

        }
    }
}
