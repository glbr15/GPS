package com.timur.gps2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SavePositionActivity extends AppCompatActivity {

    //TODO: Möglichkeit einzelne Orte zu löschen
    //TODO: Orte direkt per GPS eintragen (evtl. mit aus vorheriger Activity kopieren)

    public static final String TEXTFILE = "ortspeicher.txt";

    private EditText editLat;
    private EditText editLong;
    private EditText editLocation;

    //Testbereich
    private Button getData;
    private EditText outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_position);

        editLat = (EditText) findViewById(R.id.saveLat);
        editLong = (EditText) findViewById(R.id.saveLong);
        ortAnlegen();
        aktuellenOrtAnlegen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_position,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.deleteAllLocations){
            File file = getFileStreamPath(TEXTFILE);
            file.delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ortAnlegen(){
        Button ortAnlegen = (Button) findViewById(R.id.ortAnlegen);
        editLocation = (EditText) findViewById(R.id.saveOrt);

        ortAnlegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editLat.getText().length() > 0 && editLong.getText().length() > 0 && editLocation.getText().length() > 0){
                    try {
                        FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_APPEND);
                        String query = "\n" + editLocation.getText().toString();
                        query += " " + editLat.getText().toString();
                        query += " " + editLong.getText().toString() + " ;";
                        fos.write(query.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Es müssen alle Felder ausgefüllt werden",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void aktuellenOrtAnlegen(){
        Button aktuellerOrt = (Button) findViewById(R.id.saveThis);
        aktuellerOrt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LocationManager locationManager;
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        editLong.setText(location.getLongitude()+"");
                        editLat.setText(location.getLatitude()+"");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                try {
                    locationManager.requestSingleUpdate("gps",locationListener, null);
                }catch(SecurityException e){}
            }
        });
    }
}
