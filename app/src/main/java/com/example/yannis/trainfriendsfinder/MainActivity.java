package com.example.yannis.trainfriendsfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    View headerView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView menuNaam;
    ImageView imgProfiel;
    MenuItem mijnProfiel, newGroep, meldingen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.app.Fragment fragment = null;
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragment = new Trains();
        if(fragmentManager != null){
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.Fragment fragment = null;
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragment = new Trains();
                if(fragmentManager != null){
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
                }
                Snackbar.make(view, "Refreshed", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        //NU FRAGMENT
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        menuNaam = headerView.findViewById(R.id.menuNaam);
        imgProfiel = headerView.findViewById(R.id.imageView);
        updateNav();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.trainsButton) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.Fragment fragment = null;
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.trainsButton) {
            fragment = new Trains();
        } else if (id == R.id.nav_groep && user != null) {
            fragment = new GroepFragment();
        } else if (id == R.id.meldingen && user != null) {
            fragment = new MeldingenFragment();
        }  else if(id == R.id.login){
            fragment = new LoginFragment();

        } else if(id == R.id.signUp){
            fragment = new SignUpFragment();
        }
        else if(id == R.id.profiel && user != null){
            fragment = new ProfileFragment();
        }else if(id == R.id.loguit){
            FirebaseAuth.getInstance().signOut();
            //finish();
            Intent restartIntent = getPackageManager() //getContext
                    .getLaunchIntentForPackage(getPackageName());
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(restartIntent);
            fragment = new LoginFragment();
        }else if(id == R.id.nav_send){
            Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            fragment = new Trains();
        }
        else if(id == R.id.creategroup && user != null){
            fragment = new CreateGroepFragment();
        }else if(id == R.id.nav_share){
            String textToShare = "Download onze app TrainFriends (http://www.google.com) voor meer info." +"\n" + "Of bezoek onze website: www.trainfriends.be";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_SUBJECT, "TrainFriends");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(textToShare));
            startActivity(intent);
            fragment = new Trains();
        }
        else{
            fragment = new LoginFragment();
        }
        if(fragmentManager != null){
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNav(){
        if(user != null){
            menuNaam.setText(user.getEmail());
            imgProfiel.setImageURI(user.getPhotoUrl());
        }
    }
}