package com.trainfriends.yannis.trainfriendsfinder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trainfriends.yannis.trainfriendsfinder.adapter.MeldingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeldingenFragment extends android.app.Fragment {
    private DatabaseReference dbref;
    private ListView lvMeldingen;
    private ArrayList<String> meldingen = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    DatabaseReference dbrefUser;
    String groepId;

    public MeldingenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meldingen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbref = FirebaseDatabase.getInstance().getReference().child("Notifications");
        lvMeldingen = getView().findViewById(R.id.lvMeldingen);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        dbrefUser = FirebaseDatabase.getInstance().getReference().child("Users");
        final MeldingAdapter meldingAdapter = new MeldingAdapter(getActivity(), meldingen);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, meldingen); //getContext
        lvMeldingen.setAdapter(meldingAdapter);

        dbrefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groepId= dataSnapshot.child(uid).child("groepId").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    String value = s.child("to").getValue(String.class);
                    String naam = s.child("message").getValue(String.class);
                    try {
                        if (value.equals(groepId)) {
                            meldingen.add(naam);
                            meldingAdapter.notifyDataSetChanged();
                        }
                    } catch (NullPointerException ne){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
