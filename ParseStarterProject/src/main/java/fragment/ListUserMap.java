package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.starter.R;

import service.GetUserLocationService;


public class ListUserMap extends MapFragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GoogleMap googleMap;
    Geocoder gcd;
    Double latt, longg;
    MarkerOptions marker;
    private LatLng latLng;
    private LatLng latLngForCamera;
    private CameraPosition cameraPosition;
    private Marker marker1;
    private String user;
    // TODO: Rename and change types of parameters


    public ListUserMap() {
        // Required empty public constructor
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap=mgoogleMap;
        latLng = new LatLng(latt, longg);
        latLngForCamera = new LatLng(21.0000, 78.0000);
        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20) // Sets the zoom
                .build();
        mgoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker = new MarkerOptions().position(latLng);
        marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map));
        marker.alpha(1);
        marker1= mgoogleMap.addMarker(marker);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        latt = getArguments().getDouble("latt");
        Log.e("MapLocation", "" + latt);
        longg = getArguments().getDouble("longg");
        user=getArguments().getString("user");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_list, container, false);
//        return v;
        return super.onCreateView(inflater, container,
                savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getView().setBackgroundColor(Color.WHITE);
        getMapAsync(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,
                new IntentFilter("location"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleMap != null) {
            googleMap.clear();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menu1 = menu.getItem(0);
        menu1.setVisible(false);
    }

    private BroadcastReceiver messageReceiver;

    {
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Double latt = intent.getDoubleExtra("lat", 0);
                Double longg = intent.getDoubleExtra("longg", 0);
                Log.e("BroadcastReceiver::", "" + latt + longg);
                if (latt != 0 && longg != 0) {
                    LatLng latLng = new LatLng(latt, longg);
                    if (marker1 != null && googleMap != null)
                        animateMarker(marker1, latLng, googleMap, false);
                }
            }

        };
    }


    public void animateMarker(final Marker marker, final LatLng toPosition,GoogleMap mGoogleMapObject,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMapObject.getProjection();
        android.graphics.Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}