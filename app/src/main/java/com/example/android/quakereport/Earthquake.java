package com.example.android.quakereport;

import java.util.Date;

import static com.example.android.quakereport.R.id.date;

/**
 * Created by Katary on 11/21/2017.
 */

public class Earthquake {
    private String mLocation;
    private double mMagnitude;
    private long mTime;
    private String mUrl;

    public Earthquake(String location, double magnitude, long time, String url){
        mLocation = location;
        mMagnitude = magnitude;
        mTime = time;
        mUrl = url;
    }

    public String getLocation(){
        return mLocation;
    }

    public double getMagnitude(){
        return mMagnitude;
    }

    public long getTime(){
        return mTime;
    }
    public String getUrl(){
        return mUrl;
    }

}
