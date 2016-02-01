package service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class UpadateUserLocation extends Service {
    ParseUser user;
    private ParseGeoPoint geoPoint = new ParseGeoPoint();
    private double lat, longg;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                //your code here
                lat = location.getLatitude();
                longg = location.getLongitude();
                Log.e("lat", "" + lat + " " + longg);
                if (user != null) {

                    geoPoint.setLatitude(lat);
                    geoPoint.setLongitude(longg);
                    user.put("location", geoPoint);
                    user.saveInBackground();
                }

            }
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private LocationManager locationManager;
    private ParseUser currentUser;

    public UpadateUserLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        user = ParseUser.getCurrentUser();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            user = ParseUser.getCurrentUser();
            currentUser = user;
        }

            return super.onStartCommand(intent, flags, startId);

    }
}
