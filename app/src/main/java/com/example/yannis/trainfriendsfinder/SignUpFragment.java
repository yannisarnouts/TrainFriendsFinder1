package com.example.yannis.trainfriendsfinder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yannis.trainfriendsfinder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends android.app.Fragment {
    EditText txtUsername, txtPassword, txtNaam, txtGroep;
    Button btnSignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference dbref;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtUsername = getView().findViewById(R.id.editTextEmail);
        txtPassword = getView().findViewById(R.id.editTextPassword);
        txtGroep = getView().findViewById(R.id.editTextGroep);
        txtNaam = getView().findViewById(R.id.editTextNaam);
        btnSignUp = getView().findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser();
            }
        });
        dbref = FirebaseDatabase.getInstance().getReference().child("Groepen");
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(getActivity(), "U bent al ingelogd", Toast.LENGTH_LONG).show();
        }
    }

    private void registeruser(){
        final String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        final String naam = txtNaam.getText().toString();
        final String groep = txtGroep.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(getActivity(), "Gelieve email in te vullen", Toast.LENGTH_LONG).show();
            txtUsername.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            Toast.makeText(getActivity(), "Gelieve een geldig email adres te geven", Toast.LENGTH_LONG).show();
        }
        if(password.isEmpty()){
            Toast.makeText(getActivity(), "Gelieve wachtwoord in te vullen", Toast.LENGTH_LONG).show();
            txtPassword.requestFocus();
        }
        if(password.length() < 6){
            Toast.makeText(getActivity(), "Wachtwoord is te kort (min 6 tekens)", Toast.LENGTH_LONG).show();
            txtPassword.requestFocus();
        }
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(naam, username, groep);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Gebruiker succesvol geregistreerd" , Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(), "Registreren mislukt", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    FirebaseDatabase.getInstance().getReference("Groepen").child(groep).setValue(groep);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getActivity(), "Dit email adres is al in gebruik", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "Registreren mislukt", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
