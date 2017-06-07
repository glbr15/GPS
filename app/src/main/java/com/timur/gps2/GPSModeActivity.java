package com.timur.gps2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class GPSModeActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmode);

        image = (ImageView) findViewById(R.id.imageViewArrow);
        int width = image.getWidth();
        int height = image.getHeight();

        if(width < height){
            image.setMinimumHeight(width);
            image.setMaxHeight(width);
        }else{
            image.setMinimumWidth(height);
            image.setMaxWidth(height);
        }
    }

    public class GPSUpdater extends AsyncTask<Integer,String,String>{

        @Override
        protected String doInBackground(Integer... params) {
            return null;
        }
    }
}
