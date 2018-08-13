package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

public class CompanyFirmDetails extends AppCompatActivity {
    Toolbar toolbar;
    private EditText yearInService,firmSize,serviceOffered,assetsServed,cityServed,awardName,projectName;
    private Button saveAllChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_firm_details);

        toolbar = (Toolbar) findViewById(R.id.company_firm_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        yearInService= (EditText)findViewById(R.id.years_in_service_text);
        firmSize=(EditText)findViewById(R.id.firm_size_text);
        serviceOffered=(EditText)findViewById(R.id.service_offered_text);
        assetsServed=(EditText)findViewById(R.id.assets_served_text);
        cityServed=(EditText)findViewById(R.id.city_served_text);
        awardName=(EditText)findViewById(R.id.award_name_text);
        projectName=(EditText)findViewById(R.id.project_name_text);
        saveAllChanges=(Button) findViewById(R.id.save_changes);
    }
}
