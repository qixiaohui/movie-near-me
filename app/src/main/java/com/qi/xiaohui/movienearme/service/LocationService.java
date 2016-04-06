package com.qi.xiaohui.movienearme.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TQi on 4/6/16.
 */
public class LocationService {
    private static LocationService instance;
    private LocationManager locationManager;
    private Location location;

    public static LocationService getLocationService(Context context){
        if(instance == null){
            instance = new LocationService(context);
        }
        return instance;
    }

    private LocationService(Context context){
        init(context);
    }

    @TargetApi(23)
    private void init(Context context){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public List<?> getLocations(){
        if(location != null) {
            List<Double> geo = new ArrayList<>();
            geo.add(location.getLatitude());
            geo.add(location.getLongitude());
            return geo;
        }else{
            return new ArrayList<>();
        }
    }
}
