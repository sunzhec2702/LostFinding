package com.example.darren.lostfinding;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by darrens on 10/21/15.
 */
public class PositionUpdate implements LocationListener {
    private double lat = 0;
    private double lng = 0;
    private LocationManager locMan;
    public Location loc;
    private boolean isGPSEnabled, isNetworkEnabled, canGetLocation;

    PositionUpdate(Context activity) {
        locMan = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, this);
        isGPSEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            this.canGetLocation = true;
            if (isNetworkEnabled) {
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Log.d("Network", "Network Enabled");
                if (locMan != null) {
                    loc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (loc != null) {
                        lat = loc.getLatitude();
                        lng = loc.getLongitude();
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (loc == null) {
                    locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Log.d("GPS", "GPS Enabled");
                    if (locMan != null) {
                        loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            lat = loc.getLatitude();
                            lng = loc.getLongitude();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("D", "Location changed");
        loc = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("D", provider + " changed!");
        locMan.requestLocationUpdates(provider, 0, 0, this);
        loc = locMan.getLastKnownLocation(provider);
        lat = loc.getLatitude();
        lng = loc.getLongitude();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("D", provider + " enabled!");
        locMan.requestLocationUpdates(provider, 0, 0, this);
        loc = locMan.getLastKnownLocation(provider);
        if (loc != null) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("D", provider + " disabled!");
    }

    public Location getLoc() {
        return loc;
    }
}
