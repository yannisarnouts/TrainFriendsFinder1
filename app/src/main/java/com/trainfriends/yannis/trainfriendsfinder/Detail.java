package com.trainfriends.yannis.trainfriendsfinder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail extends android.app.Fragment {
    TextView tv_naam;
    public static String naamWaarde;

    public Detail() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Detail newInstance(String naam, String tijd) {
        Detail fragment = new Detail();
        naamWaarde = naam;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        tv_naam = view.findViewById(R.id.tv_naam);

        updateViews(naamWaarde);
        return view;
    }

    public void updateViews(String name) {
        tv_naam.setText(name);
    }

}
