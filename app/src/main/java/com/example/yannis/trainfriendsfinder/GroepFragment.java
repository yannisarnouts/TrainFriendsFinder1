package com.example.yannis.trainfriendsfinder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yannis.trainfriendsfinder.adapter.GroepAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroepFragment extends android.app.Fragment {
    private DatabaseReference dbref;
    private ListView lvGroep;
    private ArrayList<String> usernames = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    private AdView mAdView;
    public GroepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groep, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        lvGroep = getView().findViewById(R.id.groepList);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        mAdView = getView().findViewById(R.id.adViewGroep);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //final ArrayAdapter<String> groepAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usernames);
        final GroepAdapter groepAdapter = new GroepAdapter(getActivity(), usernames);
        //getContext
        lvGroep.setAdapter(groepAdapter);

    dbref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                String value = s.child("groepId").getValue(String.class);
                String naam = s.child("naam").getValue(String.class);
                try{
                    if (value.equals(dataSnapshot.child(uid).child("groepId").getValue(String.class))) {
                        usernames.add(naam.toUpperCase());
                        groepAdapter.notifyDataSetChanged();
                    }
                }catch (NullPointerException ne){
                    Toast.makeText(getActivity(), "Maak een groep of voeg jezelf aan een groep toe!", Toast.LENGTH_LONG).show();
                    //getcontext
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
