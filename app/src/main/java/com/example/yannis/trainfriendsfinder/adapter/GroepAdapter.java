package com.example.yannis.trainfriendsfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yannis.trainfriendsfinder.R;

import java.util.ArrayList;

/**
 * Created by yannis on 28/08/2018.
 */

public class GroepAdapter extends ArrayAdapter<String> {
    public GroepAdapter(Context context, ArrayList<String> leden) {
        super(context, -1, leden);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        String lid = getItem(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_groupmember, parent, false);

        }
        TextView naam = view.findViewById(R.id.txtmember);
        naam.setText(lid);
        return view;
    }
}
