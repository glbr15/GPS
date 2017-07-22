package com.timur.gps2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoacationsFragment extends Fragment {

    public static final String TEXTFILE = "ortspeicher.txt";
    ArrayAdapter<String> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        String data = "";
        try {
            FileInputStream fis = getActivity().openFileInput(TEXTFILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
            String line;

            while ((line = reader.readLine()) != null) {
                data += line;
            }

            fis.close();
        }catch(IOException e){}

        String[] dataListArray = data.split(";");
        List<String> dataList = new ArrayList<>(Arrays.asList(dataListArray));

        listAdapter = new ArrayAdapter<>(getActivity(),R.layout.list_item_locations,R.id.list_item_locations_textview,dataList);
        ListView locationListeListView = (ListView) rootView.findViewById(R.id.listview_locations);
        locationListeListView.setAdapter(listAdapter);

        //attach onClickListener to ListView
        locationListeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ortInfo = (String) parent.getItemAtPosition(position);

                String array[] = ortInfo.split(" ");
                String latitude = "";
                String longitude = "";
                for(int i=0 ; i<array.length ; i++){
                    try{
                        Double.parseDouble(array[i]);
                        latitude = array[i];
                        longitude = array[i+1];
                        break;
                    }catch(NumberFormatException e){}
                }

                Intent intent = new Intent(getActivity(),GPSModeActivity.class);
                intent.putExtra("Latitude",latitude);
                intent.putExtra("Longitude",longitude);
                startActivity(intent);
            }
        });

        return rootView;
    }
}