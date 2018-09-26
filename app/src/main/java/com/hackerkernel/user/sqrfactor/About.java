package com.hackerkernel.user.sqrfactor;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

public class About extends AppCompatActivity {

    TextView aboutName,aboutFollowers,aboutFollowing;
    TextView email,phoneNumber;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        aboutName = findViewById(R.id.about_name);
        aboutFollowers = findViewById(R.id.about_followers);
        aboutFollowing = findViewById(R.id.about_following);
        email = findViewById(R.id.about_email_text);
        phoneNumber = findViewById(R.id.about_phone_number_text);


        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        UserData userData= gson1.fromJson(json1, UserData.class);

        aboutName.setText(userClass.getName());
        aboutFollowers.setText(userClass.getFollowerCount());
        aboutFollowing.setText(userClass.getFollowingCount());
        phoneNumber.setText(userClass.getMobile());
        email.setText(userClass.getEmail());

    }
}
