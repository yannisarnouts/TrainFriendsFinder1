package com.example.yannis.trainfriendsfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yannis.trainfriendsfinder.model.Melding;
import com.example.yannis.trainfriendsfinder.R;

import java.util.ArrayList;

/**
 * Created by yannis on 23/08/2018.
 */

public class MeldingAdapter extends ArrayAdapter<String> {
    public MeldingAdapter(Context context, ArrayList<String> meldingen) {
        super(context, -1, meldingen);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        String melding = getItem(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_melding, parent, false);

        }
        TextView naam = view.findViewById(R.id.naamMelding);
        TextView bestem = view.findViewById(R.id.bestemmingMelding);
        String nm = melding.split(":'")[0];
        String best = melding.split(":'")[1];
        String best1 = best.replace("'", "");
        String best2 = best1.replace("T", "om ");
        String best3 = best2.replace("}", "").replace(":00", "u");
        naam.setText(nm);
        bestem.setText(best3);
        return view;
    }
}
