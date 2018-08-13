package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmployeeMemberDetails extends AppCompatActivity {
Toolbar toolbar;
private EditText firstName,lastName,role,phoneNumber,aadhaarId,email;
private Button employeeAddbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_member_details);

        toolbar = (Toolbar) findViewById(R.id.employee_details_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        firstName =(EditText)findViewById(R.id.employee_firstName_text);
        lastName =(EditText)findViewById(R.id.employee_firstLast_text);
        role =(EditText)findViewById(R.id.employee_role_text);
        phoneNumber =(EditText)findViewById(R.id.employee_number_text);
        aadhaarId =(EditText)findViewById(R.id.employee_aadhaar_text);
        email =(EditText)findViewById(R.id.employee_email_text);
        employeeAddbtn =(Button)findViewById(R.id.employee_add_button);


    }
}
