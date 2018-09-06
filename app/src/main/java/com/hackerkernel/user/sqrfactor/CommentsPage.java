package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentsPage extends ToolbarActivity {
    private EditText commentBody;
    private ImageView authImageUrl, userImageUrl, postImage;
    private LinearLayoutManager layoutManager;
    private RecyclerView comentPageRecyclerView;
    private TextView userName, time, postDescription, commentWrite, comments, share,sendButton,
            commentUserName, commentTime, commentDescription, commentLike;
    private ArrayList<comments_limited> comments_limitedArrayList = new ArrayList<>();
    private NewsFeedStatus newsFeedStatus;
    private ProfileClass1 profileClass1;
    private FullPost fullPost;
    private Button postbtn, likelist;
    private String isShared;
    private ImageButton like;
    private CommentsLimitedAdapter commentsLimitedAdapter;
    private int flag = 0, commentable_id,postId,shared_id,user_id;
    private static final String TAG = CommentsPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_page);


        Bundle extras = getIntent().getExtras();
        String activity = extras.getString("Activity");
        if (extras != null && (activity.equals("From_News_Adapter")||activity.equals("From_User_Profile_Adapter"))) {
            newsFeedStatus = (NewsFeedStatus) getIntent().getSerializableExtra("PostDataClass");
            commentable_id=newsFeedStatus.getSharedId();
            postId=newsFeedStatus.getPostId();
            isShared=newsFeedStatus.getIs_Shared();
            user_id=newsFeedStatus.getUserId();
            comments_limitedArrayList=newsFeedStatus.getCommentsLimitedArrayList();
        }
        if (extras != null && activity.equals("From_Profile_Adapter")) {
            profileClass1 = (ProfileClass1) getIntent().getSerializableExtra("PostDataClass");
            commentable_id=profileClass1.getSharedId();
            postId=profileClass1.getPostId();
            isShared=profileClass1.getIs_Shared();
            user_id=profileClass1.getUserId();
            comments_limitedArrayList=profileClass1.getCommentsLimitedArrayList();
        }
        if (extras != null && activity.equals("From_Full_PostActivity")) {
            fullPost = (FullPost) getIntent().getSerializableExtra("PostDataClass");
            commentable_id = fullPost.getShared_id();
            postId = fullPost.getId();
            user_id = fullPost.getUser_id();
            isShared = fullPost.getIs_shared();
            comments_limitedArrayList = fullPost.getCommentsLimitedArrayList();
        }
        if (extras != null && activity.equals("From_UserProfle_Adapter")) {
            newsFeedStatus = (NewsFeedStatus) getIntent().getSerializableExtra("PostDataClass");
            commentable_id=newsFeedStatus.getSharedId();
            postId=newsFeedStatus.getPostId();
            isShared=newsFeedStatus.getIs_Shared();
            user_id=newsFeedStatus.getUserId();
            comments_limitedArrayList=newsFeedStatus.getCommentsLimitedArrayList();
        }

        comentPageRecyclerView = (RecyclerView) findViewById(R.id.comentPageRecyclerView);

        //Toast.makeText(this,newsFeedStatus.getCommentsLimitedArrayList().get(0).getBody(),Toast.LENGTH_LONG).show();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comentPageRecyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(layoutManager);
        //Log.v("cokmentData",newsFeedStatus.getCommentsLimitedArrayList().get(0).getBody());

        commentsLimitedAdapter = new CommentsLimitedAdapter(comments_limitedArrayList,this,user_id,commentable_id,postId,isShared);
        comentPageRecyclerView.setAdapter(commentsLimitedAdapter);
        sendButton = (TextView) findViewById(R.id.sendButton);
        commentBody = (EditText) findViewById(R.id.cmmentToSend);

        final DragToClose dragToClose = findViewById(R.id.comment_drag_to_close);
        dragToClose.setDragListener(new DragListener() {
            @Override
            public void onStartDraggingView() {
                Log.d(TAG, "onStartDraggingView()");
            }

            @Override
            public void onViewCosed() {
                Log.d(TAG, "onViewCosed()");
            }
        });


        SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        userImageUrl = (ImageView)findViewById(R.id.commentPage_profile);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(userImageUrl);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(commentBody.getText().toString()))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    comments_limited commentsLimited=new comments_limited(userClass.getUserId(),userClass.getFirst_name(),userClass.getLast_name(),userClass
                            .getProfile(),userClass.getUser_name(),commentBody.getText().toString(),formatter.format(date),0);
                    comments_limitedArrayList.add(commentsLimited);
                    commentsLimitedAdapter.notifyItemInserted(comments_limitedArrayList.size());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            comentPageRecyclerView.smoothScrollToPosition(comments_limitedArrayList.size()-1);
                        }
                    }, 1);

                    sendCommentToServer();

                }

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void sendCommentToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(CommentsPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
                        commentBody.setText("");

//                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + TokenClass.Token);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("commentable_id", commentable_id + "");
                params.put("comment_text", commentBody.getText().toString());

                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public NewsFeedStatus getMyData() {
        return newsFeedStatus;
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SqrFactor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }


    public void UselessData() {
        authImageUrl = findViewById(R.id.commentPage_post_userprofile);
        //userImageUrl = findViewById(R.id.commentPage_post_profile);
        postImage = findViewById(R.id.commentPage_post);

        Glide.with(this)
                .load("https://archsqr.in/" + newsFeedStatus.getPostImage())
                .centerCrop()
                .into(postImage);

        Glide.with(this).load("https://archsqr.in/" + newsFeedStatus.getAuthImageUrl()).centerCrop().into(authImageUrl);

        userName = findViewById(R.id.commentPage_post_userName);
        userName.setText(newsFeedStatus.getUser_name_of_post());

        time = findViewById(R.id.commentPage_post_time);

        String dtc = newsFeedStatus.getTime();
        Log.v("dtc", dtc);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
        Log.v("sdf1", sdf1.toString());
        Log.v("sdf2", sdf2.toLocalizedPattern());
        Date date = null;
        try {
            date = sdf1.parse(dtc);
            String newDate = sdf2.format(date);
            Log.v("date", date + "");
            System.out.println(newDate);
            Log.e("Date", newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);
        time.setText(days + " Days ago");

        postDescription = findViewById(R.id.commentPage_post_text);
        postDescription.setText(newsFeedStatus.getShortDescription());


        like = findViewById(R.id.commentPage_post_like);
        likelist = findViewById(R.id.commentPage_post_likeList);
        likelist.setText(newsFeedStatus.getLike() + " Likes");
        like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (flag == 0) {

                    likelist.setTextColor(getApplicationContext().getColor(R.color.sqr));
                    like.setBackgroundColor(getApplicationContext().getColor(R.color.sqr));
                    int result = Integer.parseInt(newsFeedStatus.getLike()) + 1;
                    likelist.setText(result + " Like");
                    flag = 1;
                } else {
                    likelist.setTextColor(getApplicationContext().getColor(R.color.gray));
                    like.setBackgroundColor(getApplicationContext().getColor(R.color.gray));
                    int result = Integer.parseInt(newsFeedStatus.getLike());
                    likelist.setText(result + " Like");
                    flag = 0;
                }


            }
        });


        comments = findViewById(R.id.commentPage_post_comment);
        comments.setText(newsFeedStatus.getComments() + " Comments");

        share = findViewById(R.id.commentPage_post_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share your post here
                shareIt();
            }
        });

        commentWrite = findViewById(R.id.commentPage_post_writeComment);


        postbtn = findViewById(R.id.commentPage_post_postButton);

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post you commetn here using volley

            }

        });

    }
}