package com.hackerkernel.user.sqrfactor;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
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

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MessagesActivity extends ToolbarActivity {

    private ArrayList<ChatFriends> chatFriends = new ArrayList<>();
    private ChatAdapter chatAdapter;
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    public static String userProfile,userName;
    public static int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recycler = (RecyclerView)findViewById(R.id.msg_recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatFriends,this);
        recycler.setAdapter(chatAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), layoutManager.getOrientation());
        recycler.addItemDecoration(decoration);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/message/105",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject user=jsonObject.getJSONObject("user");
                            userProfile=user.getString("profile");
                            userName=user.getString("name");
                            userId=user.getInt("id");

                            JSONArray jsonArrayData = jsonObject.getJSONArray("friends");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response",response);
                                ChatFriends chatFriends1 = new ChatFriends(jsonArrayData.getJSONObject(i));
                                chatFriends.add(chatFriends1);
                            }
                            chatAdapter.notifyDataSetChanged();


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



//        PusherOptions options = new PusherOptions();
//        options.setCluster("ap2");
//        Pusher pusher = new Pusher("f3973acd596886c25549", options);
//
//        Channel channel = pusher.subscribe("my-channel");
//
//        channel.bind("my-event", new SubscriptionEventListener() {
//                    @Override
//                    public void onEvent(String channelName, String eventName, final String data) {
//                        System.out.println(data);
//                        Log.v("Data", data);
//
//
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                try {
//                                    final JSONObject jsonObject = new JSONObject(data);
//                                    name.setText(jsonObject.getString("name"));
//                                    message.setText(jsonObject.getString("message"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//
//
//                    }
//                });
//
//
//        pusher.connect();
//
//    }
    }

    }


