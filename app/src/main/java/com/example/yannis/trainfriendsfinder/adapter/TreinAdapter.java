package com.example.yannis.trainfriendsfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yannis.trainfriendsfinder.R;
import com.example.yannis.trainfriendsfinder.model.Trein;

import java.util.ArrayList;

/**
 * Created by yannis on 11/06/2018.
 */

public class TreinAdapter extends ArrayAdapter<Trein> {

    public TreinAdapter(Context context, ArrayList<Trein> treins) {
        super(context, -1, treins);

    }
    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {

        View view = convertView;
        Trein trein = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_trains, parent, false);
        }


        TextView naam = (TextView) view
                .findViewById(R.id.tv_naam);
        naam.setText(trein.getstation());

        TextView tijd = view.findViewById(R.id.tv_tijd);
        tijd.setText(trein.getTime());

        ImageView ig = view.findViewById(R.id.iv_afbeelding);
        ig.setImageResource(R.drawable.trein);


        return view;

    }

    public interface OnClickListener {
        public void onClick(Trein trein);
    }

}
