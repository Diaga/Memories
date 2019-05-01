package com.pika.memories;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class GpsTracker implements LocationListener {
    Context context;
    public GpsTracker(Context context){
        this.context=context;
    }

    public Location getLocation(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Persmission not provided",Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if(isGpsEnabled){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,5000,10,this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }else {
            Toast.makeText(context, "Enable GPS", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);

    }
}
