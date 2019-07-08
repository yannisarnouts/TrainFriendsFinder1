package com.trainfriends.yannis.trainfriendsfinder;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.concurrent.Executor;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends android.app.Fragment {
    //SignInButton signInButton;
    //Button signOutButton;
    TextView statusTextView;
    //GoogleApiClient mGoogleApiClient;
    public static final String TAG = "SignInFragment";
    public static final int RC_SIGN_IN = 9001;

    EditText txtlogin;
    EditText txtpassw;
    FirebaseAuth mAuth;
    Button btnLogin;
    FirebaseUser user;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton fbloginButton;

    public LoginFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), " " + loginResult, Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "ERROR !!! :  " + error);
                Toast.makeText(getActivity(), " " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initialiseView() {
        //signInButton = getView().findViewById(R.id.sign_in_button);
        //signOutButton = getView().findViewById(R.id.signOutButton);
        callbackManager = CallbackManager.Factory.create();
        btnLogin = getView().findViewById(R.id.btnlogin);
        txtlogin = getView().findViewById(R.id.loginemail);
        txtpassw = getView().findViewById(R.id.loginpassword);
        mAuth = FirebaseAuth.getInstance();
        fbloginButton = (LoginButton) getView().findViewById(R.id.fblogin_button);
        fbloginButton.setReadPermissions(Arrays.asList(EMAIL));
        fbloginButton.setFragment(this);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Toast.makeText(getActivity(), "Logged in!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent restartIntent = getActivity().getPackageManager() // getContext
                                    .getLaunchIntentForPackage(getActivity().getPackageName());
                            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(restartIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(getActivity(), user.getEmail(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), data + " ", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void userLogin() {
        final String email = txtlogin.getText().toString();
        String pass = txtpassw.getText().toString();
        final MainActivity mainActivity = new MainActivity();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "U bent ingelogd!", Toast.LENGTH_LONG).show();
                    user = mAuth.getCurrentUser();
                    mainActivity.updateNav();
                    Intent restartIntent = getActivity().getPackageManager() // getContext
                            .getLaunchIntentForPackage(getActivity().getPackageName());
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(restartIntent);
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
