package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfessionalActivity extends AppCompatActivity {

    private Button add,remove,add1,remove2,save;
    private boolean stDate=false;
    private boolean edDate=false;
    private LinearLayout newForm,newForm1;
    private TextInputLayout startDate,endDate;
    private Boolean isClicked=false;
    private Boolean isClicked1=false;
    private EditText startDateText,endDateText,COA,yearSinceService,role,companyCollege,salary;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);


        toolbar = (Toolbar) findViewById(R.id.professional_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);


        COA = (EditText)findViewById(R.id.COARgistration);
        yearSinceService = (EditText)findViewById(R.id.YearsSinceServiceText);
        role = (EditText)findViewById(R.id.RoleText);
        companyCollege = (EditText)findViewById(R.id.CompanyText);
        add=(Button)findViewById(R.id.Add);
        remove=(Button)findViewById(R.id.Remove);
        add1 =(Button)findViewById(R.id.Add1);
        remove2=(Button)findViewById(R.id.Remove2);
        save =(Button)findViewById(R.id.save_professinal);
        startDate= (TextInputLayout) findViewById(R.id.StartDate);
        endDate=(TextInputLayout) findViewById(R.id.EndDate);

        newForm=(LinearLayout) findViewById(R.id.linear);
        newForm1=(LinearLayout) findViewById(R.id.linear2);

        startDateText= (EditText)findViewById(R.id.StartDateText);
        endDateText=(EditText)findViewById(R.id.EndDateText);
        newForm.setVisibility(View.GONE);
        newForm1.setVisibility(View.GONE);
        //add.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked)
                {
                    newForm.setVisibility(View.VISIBLE);
                    isClicked=true;
                }

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm!=null && isClicked)
                {
                        newForm.setVisibility(View.GONE);
                        isClicked=false;
                }
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked1)
                {
                    newForm1.setVisibility(View.VISIBLE);
                    isClicked1=true;
                }

            }
        });

        remove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm1!=null && isClicked1)
                {
                    newForm1.setVisibility(View.GONE);
                    isClicked1=false;
                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        startDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stDate=true;
                new DatePickerDialog(ProfessionalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

       endDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edDate=true;
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfessionalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserData","");
        UserData userData = gson.fromJson(json,UserData.class);
        if (userData.getCoa_registration() != null){
            COA.setText(userData.getCoa_registration());
        }
        if (userData.getYears_since_service() != null){
            yearSinceService.setText(userData.getYears_since_service());
        }
        if (userData.getRole() != null){
            role.setText(userData.getRole());
        }
        if (userData.getCompany_firm_or_college_university() != null){
            companyCollege.setText(userData.getCompany_firm_or_college_university());
        }
        if (userData.getSalary_stripend_other_details() != null){
            salary.setText(userData.getSalary_stripend_other_details());
        }
        if (userData.getSalary_stripend_other_details() != null){
            startDateText.setText(userData.getSalary_stripend_other_details());
        }
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(stDate==true)
        startDateText.setText(sdf.format(myCalendar.getTime()));
        if(edDate==true)
            endDateText.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
