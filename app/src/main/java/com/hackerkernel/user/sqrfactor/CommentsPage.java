package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentsPage extends ToolbarActivity {
    ImageView authImageUrl,userImageUrl,postImage;
    TextView userName,time,postDescription,commentWrite,comments,share,
            commentUserName,commentTime,commentDescription,commentLike;
    NewsFeedStatus newsFeedStatus;
    Button postbtn,likelist;
    ImageButton like;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_page);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             newsFeedStatus = (NewsFeedStatus) getIntent().getSerializableExtra("PostDataClass");
            //Log.v("PostDataOnCommetPage",newsFeedStatus.getFullDescription());
            //Obtaining data
        }
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        authImageUrl = findViewById(R.id.commentPage_post_userprofile);
        //userImageUrl = findViewById(R.id.commentPage_post_profile);
        postImage = findViewById(R.id.commentPage_post);

        Glide.with(this)
                .load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                .centerCrop()
                .into(postImage);

        Glide.with(this).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl()).centerCrop().into(authImageUrl);

        userName = findViewById(R.id.commentPage_post_userName);

        time = findViewById(R.id.commentPage_post_time);

        String dtc = newsFeedStatus.getTime();
        Log.v("dtc",dtc);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM",Locale.ENGLISH);
        Log.v("sdf1",sdf1.toString());
        Log.v("sdf2",sdf2.toLocalizedPattern());
        Date date = null;
        try{
            date = sdf1.parse(dtc);
            String newDate = sdf2.format(date);
            Log.v("date",date+"");
            System.out.println(newDate);
            Log.e("Date",newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff/(24*60*60*1000);
        time.setText(days+" Days ago");

        postDescription = findViewById(R.id.commentPage_post_text);
        postDescription.setText(newsFeedStatus.getCommentDescription());


        like = findViewById(R.id.commentPage_post_like);
        likelist =findViewById(R.id.commentPage_post_likeList);
        likelist.setText(newsFeedStatus.getLike()+" Likes");
        like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (flag == 0) {

                    likelist.setTextColor(getApplicationContext().getColor(R.color.sqr));
                    like.setBackgroundColor(getApplicationContext().getColor(R.color.sqr));
                    int result = Integer.parseInt(newsFeedStatus.getLike())+1;
                    likelist.setText(result+" Like");
                    flag = 1;
                }
                else {
                    likelist.setTextColor(getApplicationContext().getColor(R.color.gray));
                    like.setBackgroundColor(getApplicationContext().getColor(R.color.gray));
                    int result = Integer.parseInt(newsFeedStatus.getLike());
                    likelist.setText(result+" Like");
                    flag = 0;
                }



            }
        });


        comments = findViewById(R.id.commentPage_post_comment);
        comments.setText(newsFeedStatus.getComments()+" Comments");

        share = findViewById(R.id.commentPage_post_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share your post here
                shareIt();
            }
        });

        commentWrite=findViewById(R.id.commentPage_post_writeComment);



        postbtn = findViewById(R.id.commentPage_post_postButton);

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post you commetn here using volley

                RequestQueue requestQueue = Volley.newRequestQueue(CommentsPage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);
                                commentWrite.setText("");

//                        //Showing toast message of the response
//                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
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
                        params.put("Authorization", "Bearer " +TokenClass.Token);

                        return params;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();

                        params.put("commentable_id",newsFeedStatus.getCommentsLimitedArrayList().get(0).getComment_limited_commentable_id()+"");
//
                        params.put("comment_text",commentWrite.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }

        });







        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        Bundle bundle = new Bundle();
//        bundle.putSerializable("DataCommentsLimited", newsFeedStatus);
//// set Fragmentclass Arguments
//        CommentsFragment fragobj = new CommentsFragment();
//        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.commfrag, new CommentsFragment()).commit();

    }
    public NewsFeedStatus getMyData() {
        return newsFeedStatus;
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"SqrFactor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }
}
