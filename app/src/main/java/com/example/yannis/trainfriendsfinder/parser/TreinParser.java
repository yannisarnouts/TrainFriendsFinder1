package com.example.yannis.trainfriendsfinder.parser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yannis.trainfriendsfinder.MainActivity;
import com.example.yannis.trainfriendsfinder.R;
import com.example.yannis.trainfriendsfinder.Trains;
import com.example.yannis.trainfriendsfinder.model.Trein;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yannis on 11/06/2018.
 */

public class TreinParser extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    private Trains activity;
    public ArrayList<Trein> countries = null;
    public URL url;
    public String station;
    public TreinParser(Trains activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity.getContext());
        pDialog.setTitle("Data");
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    public Void doInBackground(Void... params) {
        try {
            if(station == null){
                station = "antwerpen";
            }
            url = new URL("https://api.irail.be/liveboard/?station=" + station + "&fast=true");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), "utf-8");

            int eventType = parser.getEventType();
            Trein trein = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        countries = new ArrayList();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("departure")) {
                            trein = new Trein();
                        } else if (trein != null) {
                            if (name.equals("station")) {
                                trein.setstation(parser.nextText());
                            } else if (name.equals("vehicle")) {
                                trein.setvehicle(parser.nextText());
                            } else if (name.equals("platform")){
                                trein.setId(parser.nextText());
                            } else if (name.equals("time")){
                                trein.setTime(parser.getAttributeValue(0));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("departure") && trein != null) {
                            countries.add(trein);
                        }
                }
                eventType = parser.next();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pDialog.dismiss();
        activity.UpdateUI(countries);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeUrl(String url){
        onPreExecute();
        station = url;
        doInBackground();
        activity.UpdateUI(countries);
        //return station;
    }
}
