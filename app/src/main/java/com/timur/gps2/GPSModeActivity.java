package com.timur.gps2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GPSModeActivity extends AppCompatActivity {

    private ImageView image;
    private TextView distanceText;
    private double latHome;
    private double longHome;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmode);

        distanceText = (TextView) findViewById(R.id.textViewDistance);
        image = (ImageView) findViewById(R.id.imageViewArrow);
        int width = image.getWidth();
        int height = image.getHeight();

        if (width < height) {
            image.setMinimumHeight(width);
            image.setMaxHeight(width);
        } else {
            image.setMinimumWidth(height);
            image.setMaxWidth(height);
        }

        /** original coordinates
        latHome = 49.59614003;
        longHome = 10.95803477;
         */

        latHome = 49.54682;
        longHome = 10.7895;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float[] result = new float[1];
                Location.distanceBetween(latHome, longHome, location.getLatitude(), location.getLongitude(), result);
                distanceText.setText("Distance: " + Float.toString(result[0]));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS wird benötigt", Toast.LENGTH_LONG).show();
            }
        };

        gpsUpdate();
    }

    private void gpsUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
                }
            }
            return;
        }
        //request a new Location every 2seconds, no movement neeeded for new Location
        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
    }
}