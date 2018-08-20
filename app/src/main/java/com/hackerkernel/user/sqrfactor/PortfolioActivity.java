package com.hackerkernel.user.sqrfactor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<PortfolioClass> portfolioArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView_portfolio);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        PortfolioAdapter portfolioAdapter = new PortfolioAdapter(portfolioArrayList,this);
//
        PortfolioClass portfolioClass = new PortfolioClass("url","Architectural wonders of the...","Amit","5","4");
        portfolioArrayList.add(portfolioClass);
//
        recyclerView.setAdapter(portfolioAdapter);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/profile/follow/sqrfactor?action=followers",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("MorenewsFeedFromServer", response);
//                        Toast.makeText(PortfolioActivity.this, response, Toast.LENGTH_LONG).show();
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray follows=jsonObject.getJSONArray("follows");
//                            for(int i=0;i<follows.length();i++)
//                            {
//                                FollowerClass followerClass=new FollowerClass(follows.getJSONObject(i));
//                                followerClassArrayList.add(followerClass);
//                            }
//
//                            followersAdapter.notifyDataSetChanged();
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/json");
//                params.put("Authorization", "Bearer "+TokenClass.Token);
//
//                return params;
//            }
//
//        };
//
//        requestQueue.add(myReq);

    }
}
