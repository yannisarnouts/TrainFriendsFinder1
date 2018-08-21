package com.example.yannis.trainfriendsfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yannis.trainfriendsfinder.adapter.TreinAdapter;
import com.example.yannis.trainfriendsfinder.model.Trein;
import com.example.yannis.trainfriendsfinder.parser.TreinParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
public class Trains extends android.app.Fragment  {
    ListView lv;
    public Button btnZoek;
    public EditText txtZoek;
    TextView txttrein;
    TreinParser treinParser;
    DatabaseReference databaseReference;
    DatabaseReference dbrefUser;
    String userId, groepId, username;
    FirebaseAuth mAuth;
    FirebaseUser user;
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
        txttrein = getView().findViewById(R.id.tv_naam);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        try {
            userId = user.getUid();
        }catch (Exception e){

        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        dbrefUser = FirebaseDatabase.getInstance().getReference().child("Users");

        btnZoek.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                treinParser = new TreinParser(Trains.this);
                treinParser.station = txtZoek.getText().toString();
                treinParser.execute();
            }
        });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String naam = adapterView.getItemAtPosition(i).toString();
                    String[] trein = naam.split(",");
                    String bestemming = trein[0].substring(16);
                    String time = trein[3].substring(6);
                    try {
                    Toast.makeText(getActivity(), "Ingecheckt in trein naar: " + bestemming + " om " + time, Toast.LENGTH_LONG).show();
                    //getcontext
                    SendNotification(bestemming, time);
                } catch (Exception e){
                        //getcontext
                        Toast.makeText(getActivity(), "Om in te kunnen checken moet u aangemeld zijn!", Toast.LENGTH_LONG).show();
                }
                }
            });

        new TreinParser(this).execute();
    }

//    private void checkIn() {
//        //TODO
//        Toast.makeText(getContext(), "There is no internet connection active.", Toast.LENGTH_LONG).show();
//
//        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
//    }

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
            TreinAdapter treinAdapter = new TreinAdapter(getActivity(), countries);
            //getContext()
            lv.setAdapter(treinAdapter);
        } catch (Exception e){
            Toast.makeText(getContext(), "There is no internet connection active.", Toast.LENGTH_LONG).show();
        }
    }

    private void SendNotification(String trein, String tijd){
        dbrefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                groepId= dataSnapshot.child(userId).child("groepId").getValue(String.class);
                username = dataSnapshot.child(userId).child("naam").getValue(String.class);
                } catch (Exception e){
                Toast.makeText(getContext(), "Om in te kunnen checken moet u aangemeld zijn!", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference notification = databaseReference.push();
        notification.child("message").setValue(username + ":"+trein+" "+tijd);
        notification.child("to").setValue(groepId);
        notification.child("from").setValue(userId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),"Notificatie verzonden",Toast.LENGTH_SHORT).show();
                //getcontext
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Notificatie niet verzonden :(",Toast.LENGTH_SHORT).show();
                //getcontext
            }
        });
    }
}
