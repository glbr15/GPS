package com.timur.gps2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GPSModeActivity extends AppCompatActivity implements SensorEventListener{

    private ImageView arrowImage;
    private TextView distanceText;
    private TextView bearingText;
    private TextView headingText;

    private Location actualLocation;
    private Location destination;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private Float azimut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmode);

        distanceText = (TextView) findViewById(R.id.textViewDistance);
        bearingText = (TextView) findViewById(R.id.textViewBearing);
        headingText = (TextView) findViewById(R.id.textViewHeading);
        arrowImage = (ImageView) findViewById(R.id.imageViewArrow);
        int width = arrowImage.getWidth();
        int height = arrowImage.getHeight();

        if (width < height) {
            arrowImage.setMinimumHeight(width);
            arrowImage.setMaxHeight(width);
        } else {
            arrowImage.setMinimumWidth(height);
            arrowImage.setMaxWidth(height);
        }

        destination = new Location("");
        destination.setLatitude(49.595644);
        destination.setLongitude(10.952686);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float[] distance = new float[1];
                Location.distanceBetween(destination.getLatitude(), destination.getLongitude(), location.getLatitude(), location.getLongitude(), distance);
                distanceText.setText("Distance: " + Integer.toString((int)distance[0])+"m");

                actualLocation = location;
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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //NOP
    }

    float[] mGravity;
    float[] mGeomagnetic;
    GeomagneticField geoField;
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll

                float bearing = -azimut*360/(2*3.14159f);
                if(actualLocation != null) {
                    geoField = new GeomagneticField(
                            Double.valueOf(actualLocation.getLatitude()).floatValue(),
                            Double.valueOf(actualLocation.getLongitude()).floatValue(),
                            Double.valueOf(actualLocation.getAltitude()).floatValue(),
                            System.currentTimeMillis()
                    );
                    bearing += geoField.getDeclination();   //Korrektur der Ortsmissweisung
                }
                bearingText.setText("Bearing: "+Float.toString(Math.round(bearing)));

                if(actualLocation != null){
                    float heading = actualLocation.bearingTo(destination);
                    heading = bearing - heading;
                    headingText.setText("Heading: "+Float.toString(Math.round(heading)));

                    arrowImage.setRotation(90f + heading);
                }
            }
        }
    }
}