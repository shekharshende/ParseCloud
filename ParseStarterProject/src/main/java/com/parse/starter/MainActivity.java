/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password, email, referalCode;
    Button login;
    int ref;
    CheckBox admin;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.txtUserName);
        password = (EditText) findViewById(R.id.txtPass);
        email = (EditText) findViewById(R.id.txtEmail);
        referalCode = (EditText) findViewById(R.id.edtReferal);
        admin = (CheckBox) findViewById(R.id.chkAdmin);
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isAdmin = isChecked;
                if (isChecked == true)
                    referalCode.setVisibility(View.GONE);
                else
                    referalCode.setVisibility(View.VISIBLE);
            }
        });
        login = (Button) findViewById(R.id.btnAddUser);
        login.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    @Override
    public void onClick(View v) {
        final String user = username.getText().toString();
        String pass = password.getText().toString();
        final String userMail = email.getText().toString();
        String refCode=referalCode.getText().toString();
        if(!refCode.equals(""))
        {
            ref= Integer.parseInt(refCode);
        }
        if (username.getText().length() > 0 && password.getText().length() > 0) {
            final ParseUser newUser = new ParseUser();
            newUser.setUsername(user);
            newUser.setPassword(pass);
            newUser.setEmail(userMail);
            newUser.put("isAdmin", isAdmin);
            if(isAdmin==false)
            {
                newUser.put("otp",ref);
            }

                      newUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ParseUser.logOut();


    }
}
