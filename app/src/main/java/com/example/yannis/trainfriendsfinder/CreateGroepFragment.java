package com.example.yannis.trainfriendsfinder;


import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroepFragment extends android.app.Fragment {
EditText txtgroepsnaam, txtgroepscode;
Button btnSubmit;
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
        btnSubmit = getView().findViewById(R.id.groepsubmit);
        dbref = FirebaseDatabase.getInstance().getReference("Groepen");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroep();
            }
        });
    }
    private void createGroep(){
        String groepsnaam = txtgroepsnaam.getText().toString();
        String groepscode = txtgroepscode.getText().toString();
        if(!TextUtils.isEmpty(groepsnaam)){
            String groepId = dbref.push().getKey();
            Groep groep = new Groep(groepId, groepsnaam, groepscode);
            dbref.child(groepId).setValue(groep);
            updateUser(groepId);
            Toast.makeText(getContext(), "Groep aangemaakt", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "Kan groep niet maken", Toast.LENGTH_LONG).show();
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
