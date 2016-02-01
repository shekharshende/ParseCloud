package com.parse.starter.dummy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.parse.starter.R;

import java.util.ArrayList;

import fragment.Map;
import modelclass.PGeoPoint;
import service.GetUserLocationService;


public class MapActivity extends AppCompatActivity {

    private Intent intent;
    private PGeoPoint pGeoPoint=new PGeoPoint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Double latt = getIntent().getDoubleExtra("latt", -1);
//        Double longg = getIntent().getDoubleExtra("longg", -1);
        String user=getIntent().getStringExtra("user");
        ArrayList userlist=getIntent().getStringArrayListExtra("users");
       intent = new Intent(MapActivity.this, GetUserLocationService.class);
        intent.putStringArrayListExtra("users",userlist);
        intent.putExtra("user", user);
        startService(intent);
        Bundle bundle = new Bundle();
        Map map = new Map();
//        bundle.putDouble("latt", latt);
        bundle.putParcelableArrayList("latlong", getIntent().getParcelableArrayListExtra("com.parse.starter.modelclass.PGeoPoint"));
//        bundle.putDouble("longg", longg);
        map.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.view, map).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intent!=null) {
            stopService(intent);
        }
    }
}
