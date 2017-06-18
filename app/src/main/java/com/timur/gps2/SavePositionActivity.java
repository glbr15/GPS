package com.timur.gps2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private Button ortAnlegen;
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
        ortAnlegen();
        datenAusgeben();
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
        ortAnlegen = (Button) findViewById(R.id.ortAnlegen);
        editLat = (EditText) findViewById(R.id.saveLat);
        editLong = (EditText) findViewById(R.id.saveLong);
        editLocation = (EditText) findViewById(R.id.saveOrt);

        ortAnlegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editLat.getText().length() > 0 && editLong.getText().length() > 0 && editLocation.getText().length() > 0){
                    try {
                        FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_APPEND);
                        String query = "\n" + editLocation.getText().toString();
                        query += " " + editLat.getText().toString();
                        query += " " + editLong.getText().toString();
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

    private void datenAusgeben(){
        getData = (Button) findViewById(R.id.getData);
        outputText = (EditText) findViewById(R.id.outputText);

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputText.setText("");
                try {
                    FileInputStream fis = openFileInput(TEXTFILE);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
                    String line;

                    while((line = reader.readLine()) != null){
                        outputText.append("\n"+line);
                    }

                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
