package com.timur.gps2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SavePositionActivity extends AppCompatActivity {

    private Button ortAnlegen;
    private EditText editLat;
    private EditText editLong;
    private EditText editLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_position);
        ortAnlegen();
    }

    private void ortAnlegen(){
        ortAnlegen = (Button) findViewById(R.id.ortAnlegen);
        editLat = (EditText) findViewById(R.id.editTextLatitude);
        editLong = (EditText) findViewById(R.id.editTextLongitude);
        editLocation = (EditText) findViewById(R.id.eingabeOrt);

        ortAnlegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editLat.getText().length() > 0 && editLong.getText().length() > 0 && editLocation.getText().length() > 0){
                    //TODO: Speicherzugriff
                }else{
                    Toast.makeText(getApplicationContext(),"Es müssen alle Felder ausgefüllt werden",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
