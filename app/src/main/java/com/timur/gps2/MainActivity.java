package com.timur.gps2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonLocate;
    private TextView textLeft;
    private TextView textRight;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private EditText editLong;
    private EditText editLat;
    private Button buttonEnter;

    private Location actualPosition;

    //TODO: falls keine Location vorhanden -> Fehler mit if abfangen!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonEnter = (Button) findViewById(R.id.buttonEnter);
        buttonLocate = (Button) findViewById(R.id.buttonGetLocation);
        textLeft = (TextView) findViewById(R.id.textLeft);
        textRight = (TextView) findViewById(R.id.textRight);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //TODO: Test how it effects the app
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                actualPosition = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        actualPosition = null;
        configureButton();
        configureEnter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.gpsMode){
            Intent intent = new Intent(this,GPSModeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        //TODO: stop updating location
        super.onPause();
    }

    @Override
    protected void onResume() {
        //TODO: resume updating location
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
                break;
            default:
                break;
        }
    }

    private void configureButton() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
                }
            }
            return;
        }

        locationManager.requestLocationUpdates("gps", 2500, 10, locationListener);

        buttonLocate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Warnung kann ignoriert werden da der Check zuvor durchgeführt wird
                // -provider- -refreshing time- -minDistance refresh- -location Listener-
                locationManager.requestLocationUpdates("gps", 2500, 10, locationListener);
                if(actualPosition != null) {
                    textLeft.append("\n" + actualPosition.getLatitude());
                    textRight.append("\n" + actualPosition.getLongitude());
                }else{
                    Toast.makeText(getApplicationContext(),"No Location found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configureEnter(){
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLong = (EditText) findViewById(R.id.editTextLongitude);
                editLat = (EditText) findViewById(R.id.editTextLatitude);
                buttonEnter = (Button) findViewById(R.id.buttonEnter);

                //Timur daheim
                String latitude = "49.59616331";
                String longitude = "10.95821912";

                if(editLat.getText().length() != 0 || editLong.getText().length() != 0){
                    latitude = editLat.getText().toString();
                    longitude = editLong.getText().toString();
                }

                Intent intent = new Intent(MainActivity.this,GPSModeActivity.class);
                intent.putExtra("Latitude",latitude);
                intent.putExtra("Longitude",longitude);
                startActivity(intent);
            }
        });
    }
}