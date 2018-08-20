package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends ToolbarActivity {
    private ArrayList<ProfileClass1> profileClassList = new ArrayList<>();
    private ImageView displayImage;
    private ImageView camera;// button
    public ImageButton mRemoveButton;
    private ProfileAdapter profileAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TabLayout tabLayout1;
    ImageView morebtn, btn,coverImage,profileImage,profileStatusImage;
    private TextView profileName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    Button btnSubmit,editProfile;
    EditText writePost;
    Bitmap bitmap;
    boolean flag = false;
    LinearLayoutManager layoutManager;
    TextView blueprint, portfolio, followers, following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.profile_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        profileAdapter = new ProfileAdapter(profileClassList,this);
        recyclerView.setAdapter(profileAdapter);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        camera = (ImageView)findViewById(R.id.profile_profile_camera);
        displayImage = (ImageView)findViewById(R.id.profile_upload_image);
        btnSubmit = (Button)findViewById(R.id.profile_profile_postbtn);
        writePost = (EditText)findViewById(R.id.profile_profile_write_post);
        editProfile = (Button)findViewById(R.id.profile_editprofile);
        coverImage = (ImageView) findViewById(R.id.profile_cover_image);


        profileImage = (ImageView) findViewById(R.id.profile_profile_image);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);
        profileName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());

        profileStatusImage = (ImageView) findViewById(R.id.profile_status_image);
        followCnt = (TextView) findViewById(R.id.profile_followerscnt);
        followingCnt = (TextView) findViewById(R.id.profile_followingcnt);
        portfolioCnt = (TextView) findViewById(R.id.profile_portfoliocnt);
        bluePrintCnt = (TextView) findViewById(R.id.profile_blueprintcnt);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Settings.class);
                intent.putExtra("portFolioCount",portfolioCnt.getText().toString());
                intent.putExtra("followingCount",followingCnt.getText().toString());
                intent.putExtra("followersCount",followCnt.getText().toString());
                intent.putExtra("bluePrintCount",bluePrintCnt.getText().toString());
                startActivity(intent);
            }
        });

        btn = (ImageView)findViewById(R.id.profile_morebtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.home_menu, pop.getMenu());
                pop.show();
            }
        });

        morebtn = (ImageView)findViewById(R.id.profile_about_morebtn);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.about:
                                Intent i = new Intent(getApplicationContext(), About.class);
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        blueprint = (TextView)findViewById(R.id.profile_blueprintClick);
        portfolio = (TextView)findViewById(R.id.profile_portfolioClick);
        followers = (TextView)findViewById(R.id.profile_followersClick);
        following = (TextView)findViewById(R.id.profile_followingClick);

        blueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ProfileActivity.this, BlueprintActivity.class);
//                startActivity(i);
                LoadData();
            }
        });

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PortfolioActivity.class);
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowersActivity.class);
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowingActivity.class);
                startActivity(i);
            }
        });

        tabLayout1 = (TabLayout)findViewById(R.id.tabs2);

        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.status)
                .setText("Status"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.design)
                .setText("Design"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.article)
                .setText("Article"));

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 1:
                        Intent i2 = new Intent(getApplicationContext(), DesignActivity.class);
                        startActivity(i2);
                        break;

                    case 2:
                        Intent i3 = new Intent(getApplicationContext(), ArticleActivity.class);
                        startActivity(i3);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LoadData();


    }

    public void LoadData(){

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/profile/detail/"+userClass.getUser_name(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            followCnt.setText(jsonObject.getString("followerCnt"));
                            followingCnt.setText(jsonObject.getString("followingCnt"));
                            bluePrintCnt.setText(jsonObject.getString("blueprintCnt"));
                            portfolioCnt.setText(jsonObject.getString("portfolioCnt"));

                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            if (jsonPost!=null)
                            {
                                JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                    profileClassList.add(profileClass1);
                                }
                                profileAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

        };


        requestQueue.add(myReq);


    }

}