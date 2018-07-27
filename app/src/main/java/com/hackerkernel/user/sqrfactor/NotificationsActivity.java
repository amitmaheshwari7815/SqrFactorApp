package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsActivity extends ToolbarActivity {

    //TextView text, text2;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NotificationClass> notificationsClassArrayList = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        String s = getApplicationContext().getResources().getString(R.string.noti_string);
        //text.setText(Html.fromHtml(s));
        //text2.setText(Html.fromHtml(s));

        recycler = (RecyclerView)findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(this);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        notificationsAdapter = new NotificationsAdapter(notificationsClassArrayList,this);
        recycler.setAdapter(notificationsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("notifications");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response",response);
                                NotificationClass notificationsClass = new NotificationClass(jsonArrayData.getJSONObject(i));
                                notificationsClassArrayList.add(notificationsClass);
                            }
                            notificationsAdapter.notifyDataSetChanged();


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
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg2Mjc1NzgxMmIzMmM3MWY5NGRhMWM3MDEzZGNjYTYzMzA0ODIzNzYyMTFmNzAwMGI0MTQxYjY2MDEzMGUyYTAzZjJjZTY1NTkwYTUxZjY3In0.eyJhdWQiOiIzIiwianRpIjoiODYyNzU3ODEyYjMyYzcxZjk0ZGExYzcwMTNkY2NhNjMzMDQ4MjM3NjIxMWY3MDAwYjQxNDFiNjYwMTMwZTJhMDNmMmNlNjU1OTBhNTFmNjciLCJpYXQiOjE1MzIwODMyNTksIm5iZiI6MTUzMjA4MzI1OSwiZXhwIjoxNTYzNjE5MjU5LCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.o61HxX3vrr-O8UfzSfNy5J2qIisywW8EodRcDbGEY896kjjkYQN3xWBmiP2zaxlDeloZjtlYtYa_1AYKhXreOlhB5SmE3xeXulKQFVPRtlfzfEWtoGvhhBfggqnhq0qa3zlWX6nty7Eu0qyAwtCPPrKEEGPi3TDcVI7OWXC0v-_drkP7fqiVxH02AThSeH4XeDbkvw7bNkZHFgvZesbkrTvWqW0v4UAA7VcZw_n6icVW_Ce2xYLsxrpmVKtLxTWp0731qU5KVB861R5Xzh4Wbew7SoipjjZiyY9ZWqegUoSZi-oMgS5YIuGKMqmj8u9Hy7Ug7O4bmC_Mu4Mkhtz7Pi3bt6oCA1tgjnqkkeRRJriV9JnBm-DliwCCnyKYijQjzKYeV_vo62FkzGAViBUOLkU0bgD3cLfy314ipvRAd_OasRQv9riRtH50FkKkqz7vqI1WDrDciE1Tnz2HbdxjHHeOixvZZrOqMqghY87dsG4fPJ_gNFuml4yCllfBy-o2la13D2OOmryeEs3WVNHi0MatS4srfPg1AzxUXD5sPS0AT_jvso9Cz5DtWtyw0K_eRQJW-p-pmlta9gkC7E0zRenwm4wG7Jc76aiUJOzVVbBaBzSsDsdQOExjlp7AW_kzIzVI8gM1eXMdVsAaXqXo_774IBgAjKve4So4VL5RnUU");

                return params;
            }

        };

        requestQueue.add(myReq);

    }

}

