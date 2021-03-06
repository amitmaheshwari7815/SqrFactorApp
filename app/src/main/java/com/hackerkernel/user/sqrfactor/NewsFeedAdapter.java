package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsFeedStatus> newsFeedStatuses;
    private Context context;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userName;
    int commentsCount=0,likeCount=0;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 2;


    public NewsFeedAdapter(ArrayList<NewsFeedStatus> newsFeedStatuses, Context context) {
        this.newsFeedStatuses = newsFeedStatuses;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);

            return new HeaderViewHolder(itemView);
        }
        else if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_status_adapter, parent, false);
            return new MyViewHolder(itemView);
        }
        else return null;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("MyObject", "");
            final UserClass userClass = gson.fromJson(json, UserClass.class);

            Glide.with(context).load("https://archsqr.in/" + userClass.getProfile())
                    .into(headerHolder.ProfilePost);
            headerHolder.writePost.setText("Share your thoughts here...");
            headerHolder.writePost.setTextColor(R.color.gray);
            headerHolder.writePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent= new Intent(context,PostActivity.class);
                        context.startActivity(intent);
                }
            });
        } else if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            final NewsFeedStatus newsFeedStatus = newsFeedStatuses.get(position-1);

            int isAlreadyLiked = 0;

            SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("MyObject", "");
            final UserClass userClass = gson.fromJson(json, UserClass.class);
            commentsCount = Integer.parseInt(newsFeedStatus.getComments());


            if (newsFeedStatus.getType().equals("status")) {
                Log.v("status1", newsFeedStatus.getType());
                //holder.postTitle.setText(newsFeedStatus.getPostTitle());
                viewHolder.postTitle.setVisibility(View.GONE);
                viewHolder.postTag.setVisibility(View.GONE);
                viewHolder.authName.setText(newsFeedStatus.getUser_name_of_post());
                userName = newsFeedStatus.getUser_name_of_post();
                viewHolder.shortDescription.setText(newsFeedStatus.getFullDescription());
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getUserImageUrl())
                        .into(viewHolder.postImage);
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getAuthImageUrl())
                        .into(viewHolder.authImageUrl);
            } else if (newsFeedStatus.getType().equals("design")) {
                Log.v("status2", newsFeedStatus.getType());
                //holder.postTitle.setText(newsFeedStatus.getPostTitle());
                viewHolder.postTitle.setVisibility(View.VISIBLE);
                viewHolder.postTag.setVisibility(View.VISIBLE);
                viewHolder.authName.setText(newsFeedStatus.getUser_name_of_post());
                userName = newsFeedStatus.getUser_name_of_post();
                viewHolder.postTitle.setText(newsFeedStatus.getPostTitle());
                viewHolder.shortDescription.setText(newsFeedStatus.getShortDescription());
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getPostImage())
                        .into(viewHolder.postImage);
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getAuthImageUrl())
                        .into(viewHolder.authImageUrl);
            } else if (newsFeedStatus.getType().equals("article")) {
                Log.v("status2", newsFeedStatus.getType());
                viewHolder.postTitle.setVisibility(View.VISIBLE);
                viewHolder.postTag.setVisibility(View.VISIBLE);
                viewHolder.authName.setText(newsFeedStatus.getUser_name_of_post());
                viewHolder.postTitle.setText(newsFeedStatus.getPostTitle());
                userName = newsFeedStatus.getUser_name_of_post();
                viewHolder.shortDescription.setText(newsFeedStatus.getShortDescription());
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getPostImage())
                        .into(viewHolder.postImage);
                Glide.with(context).load("https://archsqr.in/" + newsFeedStatus.getAuthImageUrl())
                        .into(viewHolder.authImageUrl);
            }


            viewHolder.authName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userClass.getUserId() == newsFeedStatus.getUserId()) {
                        Intent intent = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("User_id", newsFeedStatus.getUserId());
                        intent.putExtra("ProfileUserName", newsFeedStatus.getUser_name_of_post());
                        intent.putExtra("ProfileUrl", newsFeedStatus.getUserImageUrl());
                        context.startActivity(intent);
                    }


                }
            });
            Glide.with(context).load("https://archsqr.in/" + userClass.getProfile())
                    .into(viewHolder.news_user_imageProfile);
            viewHolder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra("Post_Slug_ID", newsFeedStatus.getSlug());
                    context.startActivity(intent);
                }
            });
            String dtc = newsFeedStatus.getTime();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                Date past = format.parse(dtc);
                Date now = new Date();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days1 = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

                if (seconds < 60) {
                    viewHolder.time.setText(seconds + " sec ago");

                } else if (minutes < 60) {
                    viewHolder.time.setText(minutes + " min ago");
                } else if (hours < 24) {
                    viewHolder.time.setText(hours + " hours ago");
                } else {
                    viewHolder.time.setText(days1 + " days ago");
                }
            } catch (Exception j) {
                j.printStackTrace();
            }

            viewHolder.likelist.setText(newsFeedStatus.getLike() + " Like");

            viewHolder.comments.setText(commentsCount + " Comment");
            viewHolder.comments.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsPage.class);
                    intent.putExtra("PostSharedId", newsFeedStatus.getSharedId()); //second param is Serializable
                    context.startActivity(intent);

                }
            });
            if (userClass.getUserId() == newsFeedStatus.getUserId()) {
                viewHolder.news_post_menu.setVisibility(View.VISIBLE);
            }
            viewHolder.news_post_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pop = new PopupMenu(context, v);
                    pop.getMenuInflater().inflate(R.menu.delete_news_post_menu, pop.getMenu());
                    pop.show();

                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.fullView:
                                    Intent intent = new Intent(context, FullPostActivity.class);
                                    intent.putExtra("Post_Slug_ID", newsFeedStatus.getSlug());
                                    intent.putExtra("Post_ID", newsFeedStatus.getPostId());
                                    context.startActivity(intent);
                                    break;
                                case R.id.editPost:
                                    if (newsFeedStatus.getType().equals("design")) {
                                        Intent intent1 = new Intent(context, DesignActivity.class);
                                        intent1.putExtra("Post_Slug_ID", newsFeedStatus.getSlug());
                                        intent1.putExtra("Post_ID", newsFeedStatus.getPostId());
                                        context.startActivity(intent1);
                                    } else if (newsFeedStatus.getType().equals("article")) {
                                        Intent intent1 = new Intent(context, ArticleActivity.class);
                                        intent1.putExtra("Post_Slug_ID", newsFeedStatus.getSlug());
                                        intent1.putExtra("Post_ID", newsFeedStatus.getPostId());
                                        context.startActivity(intent1);
                                    } else if (newsFeedStatus.getType().equals("status")) {
                                        Intent intent1 = new Intent(context, StatusPostActivity.class);
                                        intent1.putExtra("Post_Slug_ID", newsFeedStatus.getSlug());
                                        intent1.putExtra("Post_ID", newsFeedStatus.getPostId());
                                        context.startActivity(intent1);
                                    }
                                    break;
                                case R.id.deletePost:
                                    newsFeedStatuses.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, 1);
                                    DeletePost(newsFeedStatus.getPostId() + "", newsFeedStatus.getSharedId() + "", newsFeedStatus.getIs_Shared());
                                    break;
                                case R.id.selectAsFeaturedPost:
                                    return true;

                            }
                            return true;
                        }
                    });
                }
            });

            for (int i = 0; i < newsFeedStatus.getAllLikesId().size(); i++) {
                if (userClass.getUserId() == newsFeedStatus.AllLikesId.get(i)) {
                    viewHolder.likelist.setTextColor(context.getResources().getColor(R.color.sqr));
                    isAlreadyLiked = 1;
                    viewHolder.like.setChecked(true);
                    ///flag=1;
                }
            }
            final int isAlreadyLikedFinal = isAlreadyLiked;

            for (int i = 0; i < newsFeedStatus.AllCommentId.size(); i++) {
                if (userClass.getUserId() == newsFeedStatus.AllCommentId.get(i)) {
                    viewHolder.comments.setTextColor(context.getResources().getColor(R.color.sqr));
                    viewHolder.commentCheckBox.setChecked(true);
                    //holder.co.setChecked(true);
                }
            }

            viewHolder.likelist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, LikeListActivity.class);
                    intent.putExtra("id", newsFeedStatus.getPostId());
                    context.startActivity(intent);
                }

            });
            viewHolder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                        int likeCount = Integer.parseInt(newsFeedStatus.getLike());
//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                        viewHolder.likelist.setTextColor(context.getResources().getColor(R.color.sqr));
                        if (isAlreadyLikedFinal == 1)
                            viewHolder.likelist.setText(likeCount + " Like");
                        else {
                            likeCount = likeCount + 1;
                            viewHolder.likelist.setText(likeCount + " Like");
                        }

                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference();
                        SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = mPrefs.getString("MyObject", "");
                        UserClass userClass = gson.fromJson(json, UserClass.class);

                        Log.v("daattataatat", userClass.getUserId() + " " + userClass.getProfile() + " ");
                        if (newsFeedStatus.getType().equals("status") && userClass.getUserId() != newsFeedStatus.getUserId()) {
                            PushNotificationClass pushNotificationClass;
                            from_user fromUser;
                            post post1 = new post(newsFeedStatus.getFullDescription(), newsFeedStatus.getSlug(), "post Title", newsFeedStatus.getType(), newsFeedStatus.getPostId());
                            if (userClass.getName() != "null") {
                                fromUser = new from_user(userClass.getEmail(), userClass.getName(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                                pushNotificationClass = new PushNotificationClass(userClass.getName() + " liked your status ", new Date().getTime(), fromUser, post1, "like_post");
                            } else {
                                fromUser = new from_user(userClass.getEmail(), userClass.getFirst_name() + " " + userClass.getLast_name(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                                pushNotificationClass = new PushNotificationClass(userClass.getFirst_name() + " " + userClass.getLast_name() + " liked your status ", new Date().getTime(), fromUser, post1, "like_post");
                            }

                            String key = ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").push().getKey();
                            ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").child(key).setValue(pushNotificationClass);
                            Map<String, String> unred = new HashMap<>();
                            unred.put("unread", key);
                            ref.child("notification").child(newsFeedStatus.getUserId() + "").child("unread").child(key).setValue(unred);
                        } else if (userClass.getUserId() != newsFeedStatus.getUserId()) {
                            from_user fromUser = new from_user(userClass.getEmail(), userClass.getName(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                            post post1 = new post(newsFeedStatus.getShortDescription(), newsFeedStatus.getSlug(), newsFeedStatus.getPostTitle(), newsFeedStatus.getType(), newsFeedStatus.getPostId());
                            PushNotificationClass pushNotificationClass;
                            if (userClass.getName().equals("null")) {
                                pushNotificationClass = new PushNotificationClass(userClass.getUser_name() + " liked your article ", new Date().getTime(), fromUser, post1, "like_post");
                            } else {
                                pushNotificationClass = new PushNotificationClass(userClass.getName() + " liked your article ", new Date().getTime(), fromUser, post1, "like_post");
                            }

                            //=new PushNotificationClass(userClass.getUser_name()+" liked your article",new Date().getTime(),fromUser,post1,"like_post");
                            String key = ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").push().getKey();
                            ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").child(key).setValue(pushNotificationClass);
                            Map<String, String> unred = new HashMap<>();
                            unred.put("unread", key);
                            ref.child("notification").child(newsFeedStatus.getUserId() + "").child("unread").child(key).setValue(unred);

                        }
                    } else {

                        if (isAlreadyLikedFinal == 1) {
                            Log.v("isAlreadyLiked1", isAlreadyLikedFinal + " ");
                            viewHolder.likelist.setTextColor(context.getResources().getColor(R.color.gray));
                            int likeCount1 = Integer.parseInt(newsFeedStatus.getLike());
                            Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                            likeCount1 = likeCount1 - 1;
                            viewHolder.likelist.setText(likeCount1 + " Like");
                        } else {
                            Log.v("isAlreadyLiked2", isAlreadyLikedFinal + " ");
                            Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                            viewHolder.likelist.setTextColor(context.getResources().getColor(R.color.gray));
                            viewHolder.likelist.setText(newsFeedStatus.getLike() + " Like");
                        }


                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike", s);


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

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

                            params.put("likeable_id", newsFeedStatus.getSharedId() + "");
                            params.put("likeable_type", "users_post_share");
//
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }


            });

            viewHolder.commentPostbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike", s);

                                    viewHolder.commentTime.setText("0 minutes ago");
                                    viewHolder.commentDescription.setText(viewHolder.writeComment.getText().toString());
                                    Glide.with(context).load("https://archsqr.in/" + userClass.getProfile())
                                            .into(viewHolder.commentProfileImageUrl);
                                    viewHolder.commentUserName.setText(userClass.getUser_name());
                                    viewHolder.news_comment_card.setVisibility(View.VISIBLE);
                                    database = FirebaseDatabase.getInstance();
                                    ref = database.getReference();
                                    SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
                                    Gson gson = new Gson();
                                    String json = mPrefs.getString("MyObject", "");
                                    UserClass userClass = gson.fromJson(json, UserClass.class);

                                    if (userClass.getUserId() != newsFeedStatus.getUserId()) {
                                        from_user fromUser;
                                        post post1 = new post(newsFeedStatus.getFullDescription(), newsFeedStatus.getSlug(), newsFeedStatus.getPostTitle(), newsFeedStatus.getType(), newsFeedStatus.getPostId());
                                        PushNotificationClass pushNotificationClass;
                                        if (userClass.getName() != "null") {
                                            fromUser = new from_user(userClass.getEmail(), userClass.getName(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                                            pushNotificationClass = new PushNotificationClass(userClass.getName() + " liked your status ", new Date().getTime(), fromUser, post1, "like_post");
                                        } else {
                                            fromUser = new from_user(userClass.getEmail(), userClass.getFirst_name() + " " + userClass.getLast_name(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                                            pushNotificationClass = new PushNotificationClass(userClass.getFirst_name() + " " + userClass.getLast_name() + " liked your status ", new Date().getTime(), fromUser, post1, "like_post");
                                        }

                                        String key = ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").push().getKey();
                                        ref.child("notification").child(newsFeedStatus.getUserId() + "").child("all").child(key).setValue(pushNotificationClass);
                                        Map<String, String> unred = new HashMap<>();
                                        unred.put("unread", key);
                                        ref.child("notification").child(newsFeedStatus.getUserId() + "").child("unread").child(key).setValue(unred);
                                    }


                                    viewHolder.writeComment.setText("");
                                    commentsCount = commentsCount + 1;
                                    viewHolder.comments.setText(commentsCount + " Comment");

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

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

                            params.put("commentable_id", newsFeedStatus.getSharedId() + "");
                            params.put("comment_text", viewHolder.writeComment.getText().toString());

                            //returning parameters
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            });
        }
    }
    public void DeletePost(final String  user_post_id, final String  id, final String is_shared) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/delete_post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(context, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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
                params.put("users_post_id",user_post_id+"");
                params.put("id",id+"");
                params.put("is_shared",is_shared+"");
//
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return newsFeedStatuses.size() +1;
    }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            EditText writePost;
            ImageView ProfilePost;

            public HeaderViewHolder(View view) {
                super(view);
                writePost = (EditText)view.findViewById(R.id.news_editPost_status);
                ProfilePost = (ImageView)view.findViewById(R.id.profile_image_status);
            }
        }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageUrl,authImageUrl,postImage,commentProfileImageUrl,news_user_imageProfile,news_post_menu;;
        TextView authName,time,postTitle,postTag,shortDescription,fullDescription,comments,share,commentPostbtn,
                commentUserName,commentTime,commentDescription;
        ImageButton commentLike;
        TextView likelist;
        CheckBox like,commentCheckBox;
        EditText writeComment;
        View view;
        CardView news_comment_card;
        TextView postId,userId;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("MyObject", "");
            final UserClass userClass = gson.fromJson(json, UserClass.class);
            news_user_imageProfile=(ImageView)itemView.findViewById(R.id.news_user_imageProfile);
            userImageUrl=(ImageView)itemView.findViewById(R.id.newsProfileImage);
            authImageUrl=(ImageView)itemView.findViewById(R.id.news_auth_image);
            postImage=(ImageView)itemView.findViewById(R.id.news_post_image);
            news_post_menu =(ImageView)itemView.findViewById(R.id.news_post_menu);

            news_comment_card=(CardView)itemView.findViewById(R.id.news_comment_card);
            commentCheckBox=itemView.findViewById(R.id.commentCheckBox);
            commentProfileImageUrl=(ImageView)itemView.findViewById(R.id.news_comment_image);
            authName=(TextView)itemView.findViewById(R.id.news_auth_name);
            time=(TextView)itemView.findViewById(R.id.news_post_time);
            postTitle=(TextView)itemView.findViewById(R.id.news_post_title);
            postTag =(TextView)itemView.findViewById(R.id.news_post_tag);
            shortDescription=(TextView)itemView.findViewById(R.id.news_short_Descrip);
//            fullDescription=(TextView)itemView.findViewById(R.id.news_full_Descrip);
            like=(CheckBox)itemView.findViewById(R.id.news_post_like);
            comments=(TextView)itemView.findViewById(R.id.news_comment);
            share=(TextView)itemView.findViewById(R.id.news_share);
            commentUserName=(TextView)itemView.findViewById(R.id.news_comment_name);
            commentTime=(TextView)itemView.findViewById(R.id.news_comment_time);
            commentDescription=(TextView)itemView.findViewById(R.id.news_comment_descrip);
            commentLike=(ImageButton)itemView.findViewById(R.id.news_commnent_like);
            likelist =(TextView) itemView.findViewById(R.id.news_post_likeList);
            writeComment =(EditText)itemView.findViewById(R.id.news_user_commnentEdit);
            commentPostbtn = (TextView) itemView.findViewById(R.id.news_comment_post);





            share.setOnClickListener(new View.OnClickListener() {

                int flag = 0;
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    if (flag == 0) {
                        share.setTextColor(context.getColor(R.color.sqr));
                        flag = 1;
                    }
                    else {
                        share.setTextColor(context.getColor(R.color.gray));
                        flag = 0;
                    }
                    shareIt();
                }
            });

        }
    }
    @Override
    public long getItemId(int position) {
        return  position;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"SqrFactor");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }

}