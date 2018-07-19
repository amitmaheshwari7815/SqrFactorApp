package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatWithAFriendActivity extends AppCompatActivity {
    int id;
    String friendProfile, friendName;
    private ArrayList<MessageClass> messageClassArrayList = new ArrayList<>();
    private ChatWithAFriendActivityAdapter chatWithAFriendActivityAdapter;
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private TextView friendNametext;
    private EditText messageToSend;
    private ImageButton sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_afriend);

        friendNametext = (TextView) findViewById(R.id.friendName);
        messageToSend = (EditText) findViewById(R.id.messageToSend);
        sendMessageButton = (ImageButton) findViewById(R.id.sendButton);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("FriendId");
        friendProfile = intent.getExtras().getString("FriendProfileUrl");
        friendName = intent.getExtras().getString("FriendName");
        friendNametext.setText(friendName);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        chatWithAFriendActivityAdapter = new ChatWithAFriendActivityAdapter(messageClassArrayList, this, id, friendProfile, friendName);
        recycler.setAdapter(chatWithAFriendActivityAdapter);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/myallMSG/getChat/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("chats");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                //Log.v("Response",response);
                                MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                messageClassArrayList.add(messageClass);
                            }
                            Collections.reverse(messageClassArrayList);
                            chatWithAFriendActivityAdapter.notifyDataSetChanged();


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


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageToServer();
            }
        });


    }



    public  void SendMessageToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/chat/sendChat"
                ,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        MessageClass messageClass=
                                new MessageClass(messageClassArrayList.get(messageClassArrayList.size()-1).getMessageId()+1,MessagesActivity.userId,id,messageToSend.getText().toString(),"1",formatter.format(date),formatter.format(date));
                        Collections.reverse(messageClassArrayList);
                        messageClassArrayList.add(messageClass);
                        Collections.reverse(messageClassArrayList);
                        messageToSend.setText("");
                        chatWithAFriendActivityAdapter.notifyDataSetChanged();


                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",MessagesActivity.userId+"");
                params.put("friendId",id+"" );
                params.put("message",messageToSend.getText().toString() );

                return params;
            }

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
