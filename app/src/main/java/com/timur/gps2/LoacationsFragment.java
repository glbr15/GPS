package com.timur.gps2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        //TODO: arrayList not scrollable if using data.split() -> keine Ahnung warum
        String[] dataListArray = data.split(";");
        //String[] dataListArray = {"Hans","Peter","Jens","Hans","Peter","Jens","Hans","Peter","Jens"};
        List<String> dataList = new ArrayList<>(Arrays.asList(dataListArray));

        listAdapter = new ArrayAdapter<>(getActivity(),R.layout.list_item_locations,R.id.list_item_locations_textview,dataList);
        ListView loactionListeListView = (ListView) rootView.findViewById(R.id.listview_locations);
        loactionListeListView.setAdapter(listAdapter);

        return rootView;
    }
}