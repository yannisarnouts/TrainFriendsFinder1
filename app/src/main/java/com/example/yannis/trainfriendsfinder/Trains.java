package com.example.yannis.trainfriendsfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yannis.trainfriendsfinder.adapter.TreinAdapter;
import com.example.yannis.trainfriendsfinder.model.Trein;
import com.example.yannis.trainfriendsfinder.parser.TreinParser;

import java.util.ArrayList;
public class Trains extends android.app.Fragment  {
    ListView lv;
    public Button btnZoek;
    public EditText txtZoek;
    TreinParser treinParser;
    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;



    public Trains() {
        // Required empty public constructor
    }

    public static Trains newInstance(String param1, String param2) {
        Trains fragment = new Trains();
        // Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (getArguments() != null) {
        //    mParam1 = getArguments().getString(ARG_PARAM1);
        //   mParam2 = getArguments().getString(ARG_PARAM2);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trains, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        lv = getView().findViewById(R.id.list_personen);
        btnZoek = getView().findViewById(R.id.btnZoek);
        txtZoek = getView().findViewById(R.id.zoek);
        btnZoek.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                treinParser = new TreinParser(Trains.this);
                treinParser.station = txtZoek.getText().toString();
                treinParser.execute();
            }
        });
        new TreinParser(this).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getUrl() {
        treinParser.onPreExecute();
        treinParser.station = txtZoek.getText().toString();
        treinParser.doInBackground();
        UpdateUI(treinParser.countries);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void UpdateUI(ArrayList<Trein> countries){
        try{
            TreinAdapter treinAdapter = new TreinAdapter(getContext(), countries);
            lv.setAdapter(treinAdapter);
        } catch (Exception e){
            Toast.makeText(getContext(), "There is no internet connection active.", Toast.LENGTH_LONG).show();
        }
    }
}
