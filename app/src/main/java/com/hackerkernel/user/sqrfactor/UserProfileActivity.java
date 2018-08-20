package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView morebtn,coverImage,userProfileImage;
    private TextView userName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    private Button followbtn,messagebtn;
    private ArrayList<NewsFeedStatus> userProfileClassArrayList = new ArrayList<>();
    private ArrayList<PostDataClass> userFollowClassList = new ArrayList<>();
    private TextView userBlueprint, userPortfolio, userFollowers, userFollowing;
    LinearLayoutManager layoutManager;
    UserProfileAdapter userProfileAdapter;
    RecyclerView recyclerView;
    boolean flag = false;
    private String profileNameOfUser;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        user_id = intent.getIntExtra("User_id",0);
        profileNameOfUser = intent.getStringExtra("ProfileUserName");
        Toast.makeText(getApplicationContext(),user_id+" "+profileNameOfUser,Toast.LENGTH_LONG).show();

        messagebtn = (Button) findViewById(R.id.user_messagebtn);
        coverImage = (ImageView) findViewById(R.id.user_coverImage);
        userProfileImage = (ImageView) findViewById(R.id.user_image);
        userName = (TextView) findViewById(R.id.user_name);
        followCnt = (TextView) findViewById(R.id.user_followers);
        followingCnt = (TextView) findViewById(R.id.user_following);
        portfolioCnt = (TextView) findViewById(R.id.user_portfolio);
        bluePrintCnt = (TextView) findViewById(R.id.user_blueprint);

        recyclerView = findViewById(R.id.user_profile_recycler);
//        progressBar = rootView.findViewById(R.id.progress_bar_status);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        userProfileAdapter = new UserProfileAdapter(this, userProfileClassArrayList);
        recyclerView.setAdapter(userProfileAdapter);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        morebtn = (ImageView) findViewById(R.id.user_morebtn);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

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

        userBlueprint = (TextView) findViewById(R.id.user_blueprintClick);
        userPortfolio = (TextView) findViewById(R.id.user_portfolioClick);
        userFollowers = (TextView) findViewById(R.id.user_followersClick);
        userFollowing = (TextView) findViewById(R.id.user_followingClick);





        userBlueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(UserProfileActivity.this, BlueprintActivity.class);
                //startActivity(i);
                LoadData();

            }
        });

        userPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, PortfolioActivity.class);
                startActivity(i);
            }
        });

        userFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, FollowersActivity.class);
                startActivity(i);
            }
        });

        userFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, FollowingActivity.class);
                startActivity(i);
            }
        });
        followbtn = (Button) findViewById(R.id.user_followButton);
        followbtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                FollowMethod();
            }


        });
        FollowMethod();
        LoadData();

    }

    public void FollowMethod()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/follow_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                            flag = userFollowClass.isReturnType();
                            if (flag == false) {
                                Log.v("follow",flag+"");
                                followbtn.setText("Follow");
                                flag = true;
                            }
                            else {
                                Log.v("following",flag+"");
                                followbtn.setText("Following");
                                flag = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("to_user",user_id+"");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void LoadData()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/profile/detail/corrupted_desires",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MorenewsFeedFromServer", response);
                        Toast.makeText(UserProfileActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            if(jsonObject.has("message"))
//                            {
//                                if(jsonObject.getString("message").equals("No posts found"));
//                                return;
//                            }
                            UserProfileClass userProfileClass = new UserProfileClass(jsonObject);
                            userProfileClassArrayList.addAll(userProfileClass.getPostDataClassArrayList());
                            followCnt.setText(userProfileClass.getFollowerscnt());
                            followingCnt.setText(userProfileClass.getFollowingcnt());
                            portfolioCnt.setText(userProfileClass.getPortfoliocnt());
                            bluePrintCnt.setText(userProfileClass.getBluePrintcnt());
                            userName.setText(userProfileClass.getUser_name());
                            Glide.with(getApplicationContext()).load("https://archsqr.in/"+userProfileClass.getProfile())
                                    .into(userProfileImage);

                            userProfileAdapter.notifyDataSetChanged();
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