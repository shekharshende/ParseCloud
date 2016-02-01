package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import service.UpadateUserLocation;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private Button login;
    private EditText userName, userPass;
    private ParseGeoPoint geoPoint = new ParseGeoPoint();
    private boolean isenabled;
    private LocationManager locationManager;
    private ParseUser currentuser;

    private double lat, longg;
ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     user=    ParseUser.getCurrentUser();
       if( user!= null) {
           boolean isAdmin = ParseUser.getCurrentUser().getBoolean("isAdmin");
           if (isAdmin == true) {

               Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
               intent.putExtra("referalCode", ParseUser.getCurrentUser().getInt("referalCode"));
               startActivity(intent);
               finish();

//
           } else {
               Toast.makeText(LoginActivity.this, "Thanks for logging!!!", Toast.LENGTH_LONG).show();
               Intent intent = new Intent(LoginActivity.this, UpadateUserLocation.class);
               startService(intent);

           }
       }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       boolean isProviderEnabled= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isProviderEnabled)
            buildAlertMessageNoGps();
        userName = (EditText) findViewById(R.id.edtUserName);
        userPass = (EditText) findViewById(R.id.edtPass);
        register = (TextView) findViewById(R.id.tvRegister);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        register.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String name = userName.getText().toString();
                String pass = userPass.getText().toString();
                ParseUser.logInInBackground(name, pass, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            boolean isAdmin = user.getBoolean("isAdmin");
                            if (isAdmin == true) {
                                ParseQuery<ParseObject> parseObjectParseQuery=ParseQuery.getQuery("AdminOTP");
                                        parseObjectParseQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser());
                                parseObjectParseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        int referalCode= (int) object.get("otp");
                                        SharedPreferences sharedPreferences=getSharedPreferences("ReferalCode",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putInt("referalCode", referalCode).commit();
                                        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                                        intent.putExtra("referalCode",  object.getInt("otp"));

                                        startActivity(intent);
                                        finish();
                                    }
                                });


//
                            } else {
                                Toast.makeText(LoginActivity.this, "Thanks for logging!!!", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LoginActivity.this, UpadateUserLocation.class);
                                startService(intent);
                                finish();


                            }
//                            }
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
                break;
            case R.id.tvRegister:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText("");
        userPass.setText("");
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
