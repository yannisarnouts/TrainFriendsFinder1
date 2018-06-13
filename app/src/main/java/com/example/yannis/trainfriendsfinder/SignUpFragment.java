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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends android.app.Fragment {
    EditText txtUsername, txtPassword;
    Button btnSignUp;
    private FirebaseAuth mAuth;
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
        btnSignUp = getView().findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser();
            }
        });
    }

    private void registeruser(){
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
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
                    Toast.makeText(getActivity(), "Gebruiker succesvol geregistreerd", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Registreren mislukt", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
