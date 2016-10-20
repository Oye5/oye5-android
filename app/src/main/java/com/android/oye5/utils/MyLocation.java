package com.android.oye5.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.oye5.preferences.AppPreference;

public class MyLocation {
    private Context mContext;
    private LocationManager mLocationManager;
    Location mLastLocation = null;
    String mCurrentProvider;

    public MyLocation(Context context){
        this.mContext = context;
        setupLocationListeners();
    }

    private void setupLocationListeners(){
        mLocationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);

        try {
            mLastLocation = mLocationManager


                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mLastLocation == null) {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mCurrentProvider = LocationManager.GPS_PROVIDER;
            } else {
                mCurrentProvider = LocationManager.NETWORK_PROVIDER;
            }
        }catch(Exception e){}
    }

    private void releaseLocationListeners(){
        if (mLocationManager == null) return;

        try {
            mLocationManager.removeUpdates(mGpsLocationListener);
            mLocationManager.removeUpdates(mNetworkLocationListener);
        }catch(Exception e){}

        Log.e(getClass().getName(), "Location Listeners released!");
    }

    public void setLocationListners(){
        if (mLocationManager == null) return;

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mGpsLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mNetworkLocationListener);
        }catch(Exception e){}

        Log.e(getClass().getName(), "Location Listeners setup!");
    }

    /** Listener for GPS Location **/
    private final LocationListener mGpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mLastLocation == null) {
                mLastLocation = location;
            } else {
                if (Utils.isBetterLocation(location, mLastLocation)) {
                    Log.d(getClass().getName(), "Location Acquired: " + location.toString());
                    mLastLocation = location;
                }
            }

            updateLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {
            mCurrentProvider = LocationManager.NETWORK_PROVIDER;
            Log.e(getClass().getName(), "Gps disabled");
        }

        public void onProviderEnabled(String provider) {
            mCurrentProvider = LocationManager.GPS_PROVIDER;
            Log.e(getClass().getName(), "Gps enabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    /**
     * Listener for Network Location
     */
    private final LocationListener mNetworkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mLastLocation == null) {
                mLastLocation = location;
            } else {
                if (Utils.isBetterLocation(location, mLastLocation)) {
                    Log.d(getClass().getName(), "Location Acquired: " + location.toString());
                    mLastLocation = location;
                }
            }

            updateLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private void updateLocation(){
        Log.d(getClass().getName(), "Location Updated: " + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());
        AppPreference.setLocation(mContext, mLastLocation.getLatitude(), mLastLocation.getLongitude());

        //releaseLocationListeners();
    }
}