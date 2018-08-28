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
import android.widget.TextView;
import android.widget.Toast;

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
public class ProfileFragment extends android.app.Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView txtNaam;
    DatabaseReference dbref;
    ListView lvMijnRitten;
    String uid;
    private ArrayList<String> treinen = new ArrayList<>();


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        txtNaam = getView().findViewById(R.id.txtNaam);
        lvMijnRitten = getView().findViewById(R.id.lvMijnRitten);
        dbref = FirebaseDatabase.getInstance().getReference().child("Notifications");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        txtNaam.setText(user.getEmail());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, treinen);
        //getContext()
        lvMijnRitten.setAdapter(arrayAdapter);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    String trein = s.child("message").getValue(String.class);
                    String from = s.child("from").getValue(String.class);
                    try {
                        if (from.equals(uid)) {
                            treinen.add(trein.replace('}', ' '));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"ERROR",Toast.LENGTH_SHORT).show(); //getContext
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
