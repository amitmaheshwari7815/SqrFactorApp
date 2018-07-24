package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationsActivity extends ToolbarActivity {

    //TextView text, text2;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;

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

        //text = (TextView)findViewById(R.id.text);
        //text2 = (TextView)findViewById(R.id.text2);

        String s = getApplicationContext().getResources().getString(R.string.noti_string);
        //text.setText(Html.fromHtml(s));
        //text2.setText(Html.fromHtml(s));

        recycler = (RecyclerView)findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(this);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        adapter = new NotificationsAdapter(s);
        recycler.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(),linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);





        }

}

