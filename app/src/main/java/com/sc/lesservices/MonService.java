package com.sc.lesservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MonService extends Service {

    private IBinder myBinder = new MyActivityBinder();
    private ServiceCallbacks serviceCallbacks;

    public double latitude;
    public double longitude;
    public LocationManager locationManager = null;
    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            MonService.this.latitude = location.getLatitude();
            MonService.this.longitude = location.getLongitude();

            if (serviceCallbacks != null) {
                serviceCallbacks.updateActivityUI();
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    public MonService() {
    }

    @Override
    public void onCreate() {

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(this.getClass().getName(),"flags=" + flags + " StartId ="+ startId);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
    }

    public class MyActivityBinder extends Binder {
        MonService getMonService() {
            return MonService.this;
        }
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

}
