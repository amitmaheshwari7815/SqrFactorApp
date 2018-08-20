package com.hackerkernel.user.sqrfactor;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MessagesActivity extends ToolbarActivity {

    private ArrayList<ChatFriends> chatFriends = new ArrayList<>();
    //private ArrayList<ChatFriends> chatFriends = new ArrayList<>();
    private ChatAdapter chatAdapter;
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    public static String userProfile,userName;
    public static  int userId;
    public static FirebaseDatabase database;
    public static DatabaseReference ref;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        database= FirebaseDatabase.getInstance();

        ref = database.getReference();

        recycler = (RecyclerView)findViewById(R.id.msg_recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatFriends,this);
        recycler.setAdapter(chatAdapter);

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        //String token_id=FirebaseInstanceId.getInstance().getToken();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        if(savedInstanceState==null)
        {
            StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/message/"+userClass.getUserId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("ReponseFeed", response);
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject user=jsonObject.getJSONObject("user");

                                //CurrentLoginedUser currentLoginedUser=new CurrentLoginedUser(userId,userName,userProfile);
                                userProfile=user.getString("profile");
                                userName=user.getString("name");
                                userId=user.getInt("id");
                                HashMap<String,String> status=new HashMap<>();
                                status.put("isOnline","True");
                                status.put("time", ServerValue.TIMESTAMP.toString());
                                //IsOnline isOnline=new IsOnline("True",ServerValue.TIMESTAMP.toString());
                                ref.child("Status").child(userId+"").setValue(status);

                                JSONArray jsonArrayData = jsonObject.getJSONArray("friends");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    Log.v("Response",response);
                                    ChatFriends chatFriends1 = new ChatFriends(jsonArrayData.getJSONObject(i));
                                    chatFriends1.setIsOnline("False");
                                    chatFriends.add(chatFriends1);
                                }
                                chatAdapter.notifyDataSetChanged();
                                DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference().child("Status").child(userId+"");
                                IsOnline isOnline=new IsOnline("False",ServerValue.TIMESTAMP.toString());
                                presenceRef.onDisconnect().setValue(isOnline);
                                StatusLinstner();





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

        else {
            chatFriends=(ArrayList<ChatFriends>)savedInstanceState.getSerializable("Old");
            chatAdapter.notifyDataSetChanged();
//            DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference().child("Status").child(userId+"");
//            IsOnline isOnline=new IsOnline("False",ServerValue.TIMESTAMP.toString());
//            presenceRef.onDisconnect().setValue(isOnline);
            StatusLinstner();
        }




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Old", chatFriends);
    }

    public void StatusLinstner()
    {




        for(int i=0;i<chatFriends.size();i++)
        {
            final int finalI = i;
            ref.child("Status").child(chatFriends.get(i).getUserID()+"").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long timestamp ;
                    //String isOnline;
                    //HashMap<String, String> yourData = (HashMap<String, String>) dataSnapshot.getValue();
                    //for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    Log.v("DataSnapShot",dataSnapshot.getKey());

                    IsOnline isOnline=dataSnapshot.getValue(IsOnline.class);

                    if(isOnline!=null && isOnline.getIsOnline().equals("True"))
                    {
                        ChatFriends chatFriend= chatFriends.get(finalI);
                        chatFriend.setIsOnline("True");
                        chatFriends.set(finalI,chatFriend);
                        chatAdapter.notifyItemChanged(finalI);
                        //chatAdapter.notifyItemInserted(finalI);
                        //chatAdapter.notifyDataSetChanged();
                    }

                    else
                    {
                        ChatFriends chatFriend= chatFriends.get(finalI);
                        chatFriend.setIsOnline("False");
                        chatFriends.set(finalI,chatFriend);
                        //chatAdapter.notifyItemChanged(finalI);
                        //chatAdapter.notifyItemInserted(finalI);
                        chatAdapter.notifyItemChanged(finalI);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.v("OnStart","OnstartCllaed");
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        HashMap<String,String> status=new HashMap<>();
        status.put("isOnline","True");
        status.put("time", ServerValue.TIMESTAMP.toString());
        ref.child("Status").child(userClass.getUserId()+"").setValue(status);
    }

}
