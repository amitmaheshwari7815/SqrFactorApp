package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends ToolbarActivity {
    private ArrayList<ProfileClass> profileClassList = new ArrayList<>();
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
        profileName =(TextView)findViewById(R.id.profile_profile_name);
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
                Intent i = new Intent(ProfileActivity.this, BlueprintActivity.class);
                startActivity(i);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/detail/sqrfactor",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MorenewsFeedFromServer", response);
                        Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ProfileClass profileClass = new ProfileClass(jsonObject);
                            profileClassList.add(profileClass);
                            followCnt.setText(profileClass.getFollowerscnt());
                            followingCnt.setText(profileClass.getFollowingcnt());
                            portfolioCnt.setText(profileClass.getPortfoliocnt());
                            bluePrintCnt.setText(profileClass.getBluePrintcnt());
                            profileName.setText(profileClass.getUser_name());
                            Glide.with(getApplicationContext()).load("https://archsqr.in/"+profileClass.getProfile())
                                    .into(profileImage);

                            profileAdapter.notifyDataSetChanged();
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
