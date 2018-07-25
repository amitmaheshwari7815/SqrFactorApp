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
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView morebtn,coverImage,userProfileImage;
    private TextView userName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    private Button followbtn,messagebtn;
    private ArrayList<UserProfileClass> userProfileClassArrayList = new ArrayList<>();
    private ArrayList<UserFollowClass>userFollowClassArrayList = new ArrayList<>();
    private TextView userBlueprint, userPortfolio, userFollowers, userFollowing;
    LinearLayoutManager layoutManager;
    UserProfileAdapter userProfileAdapter;
    RecyclerView recyclerView;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        messagebtn =(Button)findViewById(R.id.user_messagebtn);
        coverImage =(ImageView)findViewById(R.id.user_coverImage);
        userProfileImage =(ImageView)findViewById(R.id.user_image);
        userName = (TextView)findViewById(R.id.user_name);
        followCnt = (TextView)findViewById(R.id.user_followers);
        followingCnt = (TextView)findViewById(R.id.user_following);
        portfolioCnt = (TextView)findViewById(R.id.user_portfolio);
        bluePrintCnt = (TextView)findViewById(R.id.user_blueprint);

        recyclerView = findViewById(R.id.user_profile_recycler);
//        progressBar = rootView.findViewById(R.id.progress_bar_status);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        userProfileAdapter = new UserProfileAdapter(this,userProfileClassArrayList);
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
                Intent i = new Intent(UserProfileActivity.this, BlueprintActivity.class);
                startActivity(i);
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
        followbtn =(Button)findViewById(R.id.user_followButton);

        RequestQueue requestQueue0 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/follow_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                            userFollowClassArrayList.add(userFollowClass);
                            followbtn.setText(userFollowClass.getMessage());
                            
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
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZmMTliZGU4NTZmODZiMTZiMDUxYjk2OGI3YTk5ZmRlYzM1MDk2YjBhNzJiN2YyMWQ1MGVkNmU2NDMwMzQ0ZTIxZGE5YWZiMDkxZGU5MzZjIn0.eyJhdWQiOiIzIiwianRpIjoiZmYxOWJkZTg1NmY4NmIxNmIwNTFiOTY4YjdhOTlmZGVjMzUwOTZiMGE3MmI3ZjIxZDUwZWQ2ZTY0MzAzNDRlMjFkYTlhZmIwOTFkZTkzNmMiLCJpYXQiOjE1MzI0Mjg5NjYsIm5iZiI6MTUzMjQyODk2NiwiZXhwIjoxNTYzOTY0OTY2LCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.TiGb0ea0wLkAVp9AstLs2EmH3RD8wORc-HjxeTXiQ_RRNAMUOcmUoV8E7g9SwdMjywztsZbYOekY_fQD0xnpY26gkaxSaaDVeZKYrnN-WM--zWWzHKpi9wUp9QUnVBh36Ah1Akqu-hwhCFKTplF7ZMS1NM9LKxOyNd2uwFsv-neScT2vQFvGYgxla2tmzqo9tzpXXPzFEn1uLQvg4lsr-EDZs_9CvB_Bef-WyLRZfKtJTEzbcd4FvviQdZCVYuzHkI-VixlMoBRAEWqMWBnUI0xBepOqC4gfv7s4Qs03M0vhu18rTXJ-nCfjhg3XA7wdYKBXe9sFNNTcTe7A0rnWIKRsIH6dQxipw0HqtXFOR-kgG6p0fFgY30oo_P8OSp8RwYNVYw118jsXKh24YR5twXyvXAeZoi-iNbU8qpJNVp1cWTRIUvbtosSt9ct8lj70zKpuyFRZgfT_FEfdW32UQ_6d9VC_7NjlyDFlmZazCfYOgEYsdOPmU1xTy4xGnEyNgj0jCbi_BNt7KnefER2i41leombzq14d8wqkdm_num53EPWTOxlaXRF85jOrjKv5a6WEFYOzTCHSXWeIIcYfYFnOdqsWXeSSlNZ4xD1DITObUbUl30lGkkNSY0v5yePFL9-hNVByGY7LrMH4TNFiWn5-g0MAIo5DOHK8gHAmWJg");

                return params;
            }

        };

        //Adding request to the queue
        requestQueue0.add(stringRequest);


        followbtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/follow_user",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(s);
                                    UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                                    userFollowClassArrayList.add(userFollowClass);
                                    followbtn.setText(userFollowClass.getMessage());
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
                        params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZmMTliZGU4NTZmODZiMTZiMDUxYjk2OGI3YTk5ZmRlYzM1MDk2YjBhNzJiN2YyMWQ1MGVkNmU2NDMwMzQ0ZTIxZGE5YWZiMDkxZGU5MzZjIn0.eyJhdWQiOiIzIiwianRpIjoiZmYxOWJkZTg1NmY4NmIxNmIwNTFiOTY4YjdhOTlmZGVjMzUwOTZiMGE3MmI3ZjIxZDUwZWQ2ZTY0MzAzNDRlMjFkYTlhZmIwOTFkZTkzNmMiLCJpYXQiOjE1MzI0Mjg5NjYsIm5iZiI6MTUzMjQyODk2NiwiZXhwIjoxNTYzOTY0OTY2LCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.TiGb0ea0wLkAVp9AstLs2EmH3RD8wORc-HjxeTXiQ_RRNAMUOcmUoV8E7g9SwdMjywztsZbYOekY_fQD0xnpY26gkaxSaaDVeZKYrnN-WM--zWWzHKpi9wUp9QUnVBh36Ah1Akqu-hwhCFKTplF7ZMS1NM9LKxOyNd2uwFsv-neScT2vQFvGYgxla2tmzqo9tzpXXPzFEn1uLQvg4lsr-EDZs_9CvB_Bef-WyLRZfKtJTEzbcd4FvviQdZCVYuzHkI-VixlMoBRAEWqMWBnUI0xBepOqC4gfv7s4Qs03M0vhu18rTXJ-nCfjhg3XA7wdYKBXe9sFNNTcTe7A0rnWIKRsIH6dQxipw0HqtXFOR-kgG6p0fFgY30oo_P8OSp8RwYNVYw118jsXKh24YR5twXyvXAeZoi-iNbU8qpJNVp1cWTRIUvbtosSt9ct8lj70zKpuyFRZgfT_FEfdW32UQ_6d9VC_7NjlyDFlmZazCfYOgEYsdOPmU1xTy4xGnEyNgj0jCbi_BNt7KnefER2i41leombzq14d8wqkdm_num53EPWTOxlaXRF85jOrjKv5a6WEFYOzTCHSXWeIIcYfYFnOdqsWXeSSlNZ4xD1DITObUbUl30lGkkNSY0v5yePFL9-hNVByGY7LrMH4TNFiWn5-g0MAIo5DOHK8gHAmWJg");

                        return params;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        //Adding parameters
//                 params.put("image_value",image);
                        params.put("to_user","106");
//                            params.put("user_id",newsFeedStatuses.get(getAdapterPosition()).getUserId()+"");
                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue1.add(stringRequest);
            }


        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/detail/hackerkernel",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MorenewsFeedFromServer", response);
                        Toast.makeText(UserProfileActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            UserProfileClass userProfileClass = new UserProfileClass(jsonObject);
                                userProfileClassArrayList.add(userProfileClass);
                                followCnt.setText(userProfileClass.getFollowerscnt());
                                followingCnt.setText(userProfileClass.getFollowingcnt());
                                portfolioCnt.setText(userProfileClass.getPortfoliocnt());
                                bluePrintCnt.setText(userProfileClass.getBluePrintcnt());
                                userName.setText(userProfileClass.getUser_name());
                                Glide.with(getApplicationContext()).load("https://archsqr.in/"+userProfileClass.getProfile())
                                    .into(userProfileImage);

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
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZmMTliZGU4NTZmODZiMTZiMDUxYjk2OGI3YTk5ZmRlYzM1MDk2YjBhNzJiN2YyMWQ1MGVkNmU2NDMwMzQ0ZTIxZGE5YWZiMDkxZGU5MzZjIn0.eyJhdWQiOiIzIiwianRpIjoiZmYxOWJkZTg1NmY4NmIxNmIwNTFiOTY4YjdhOTlmZGVjMzUwOTZiMGE3MmI3ZjIxZDUwZWQ2ZTY0MzAzNDRlMjFkYTlhZmIwOTFkZTkzNmMiLCJpYXQiOjE1MzI0Mjg5NjYsIm5iZiI6MTUzMjQyODk2NiwiZXhwIjoxNTYzOTY0OTY2LCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.TiGb0ea0wLkAVp9AstLs2EmH3RD8wORc-HjxeTXiQ_RRNAMUOcmUoV8E7g9SwdMjywztsZbYOekY_fQD0xnpY26gkaxSaaDVeZKYrnN-WM--zWWzHKpi9wUp9QUnVBh36Ah1Akqu-hwhCFKTplF7ZMS1NM9LKxOyNd2uwFsv-neScT2vQFvGYgxla2tmzqo9tzpXXPzFEn1uLQvg4lsr-EDZs_9CvB_Bef-WyLRZfKtJTEzbcd4FvviQdZCVYuzHkI-VixlMoBRAEWqMWBnUI0xBepOqC4gfv7s4Qs03M0vhu18rTXJ-nCfjhg3XA7wdYKBXe9sFNNTcTe7A0rnWIKRsIH6dQxipw0HqtXFOR-kgG6p0fFgY30oo_P8OSp8RwYNVYw118jsXKh24YR5twXyvXAeZoi-iNbU8qpJNVp1cWTRIUvbtosSt9ct8lj70zKpuyFRZgfT_FEfdW32UQ_6d9VC_7NjlyDFlmZazCfYOgEYsdOPmU1xTy4xGnEyNgj0jCbi_BNt7KnefER2i41leombzq14d8wqkdm_num53EPWTOxlaXRF85jOrjKv5a6WEFYOzTCHSXWeIIcYfYFnOdqsWXeSSlNZ4xD1DITObUbUl30lGkkNSY0v5yePFL9-hNVByGY7LrMH4TNFiWn5-g0MAIo5DOHK8gHAmWJg");

                return params;
            }

        };

        requestQueue.add(myReq);
    }
}

