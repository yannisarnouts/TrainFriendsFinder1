package com.example.yannis.trainfriendsfinder;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yannis.trainfriendsfinder.model.Groep;
import com.example.yannis.trainfriendsfinder.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroepFragment extends android.app.Fragment {
EditText txtgroepsnaam, txtgroepscode, txtgroepslogin, txtgroepslogincode;
Button btnSubmit, btnVolg;
    private DatabaseReference dbref;
    FirebaseUser fbuser;
    FirebaseAuth mAuth;

    public CreateGroepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        fbuser = mAuth.getCurrentUser();
        return inflater.inflate(R.layout.fragment_create_groep, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtgroepsnaam = getView().findViewById(R.id.groepnaam);
        txtgroepscode = getView().findViewById(R.id.groepcode);
        txtgroepslogin = getView().findViewById(R.id.groeploginnaam);
        txtgroepslogincode = getView().findViewById(R.id.groeplogincode);
        btnSubmit = getView().findViewById(R.id.groepsubmit);
        btnVolg = getView().findViewById(R.id.groeplogin);
        dbref = FirebaseDatabase.getInstance().getReference("Groepen");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroep();
            }
        });
        btnVolg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });
    }

    private void joinGroup() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    String naam = s.child("naam").getValue(String.class);
                    String code = s.child("code").getValue(String.class);
                    if(naam.equals(txtgroepslogin.getText().toString())){
                        updateUser(s.getKey());
                        FirebaseMessaging.getInstance().subscribeToTopic(s.getKey());
                        Toast.makeText(getActivity(), "U bent toegevoegd aan de groep", Toast.LENGTH_LONG).show();
                        //getContext()
                        Intent restartIntent = getActivity().getPackageManager() // getContext
                                .getLaunchIntentForPackage(getActivity().getPackageName());
                        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restartIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}

    private void createGroep(){
        String groepsnaam = txtgroepsnaam.getText().toString();
        String groepscode = txtgroepscode.getText().toString();
        if(!TextUtils.isEmpty(groepsnaam)){
            String groepId = dbref.push().getKey();
            Groep groep = new Groep(groepId, groepsnaam, groepscode);
            dbref.child(groepId).setValue(groep);
            updateUser(groepId);
            FirebaseMessaging.getInstance().subscribeToTopic(groepId);
            Toast.makeText(getActivity(), "Groep aangemaakt", Toast.LENGTH_LONG).show(); //getContext
            Intent restartIntent = getActivity().getPackageManager() // getContext
                    .getLaunchIntentForPackage(getActivity().getPackageName());
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(restartIntent);
        }else{
            Toast.makeText(getActivity(), "naam en code invullen aub!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser(String groepId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fbuser.getUid());
        //Groep user = new Groep(groepId);
        //User u = new User(fbuser.getDisplayName(), fbuser.getEmail(), new Groep(groepId));
        //databaseReference.setValue(u);
        databaseReference.child("groepId").setValue(groepId);
    }
}
