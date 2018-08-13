package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity{
Toolbar toolbar;
private EditText oldPassword,newPassword,confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = (Toolbar) findViewById(R.id.change_password_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        oldPassword =(EditText)findViewById(R.id.old_password_text);
        newPassword =(EditText)findViewById(R.id.new_password_text);
        confirmPassword =(EditText)findViewById(R.id.confirm_password_text);
    }
}
