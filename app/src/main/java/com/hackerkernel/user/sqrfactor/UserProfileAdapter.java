package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {
    private Context context;
    int flag = 0;
    private int flag1 = 0;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userName;
    private ArrayList<NewsFeedStatus> userProfileClassArrayList;
    int result;

    public UserProfileAdapter(Context context,ArrayList<NewsFeedStatus> userProfileClasses) {

        this.context = context;
        this.userProfileClassArrayList=userProfileClasses;
    }

    @NonNull
    @Override
    public UserProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_adapter,parent,false);

        return new UserProfileAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final UserProfileAdapter.MyViewHolder holder, final int position) {

        //UserProfileClass userProfileClass=userProfileClassArrayList.get(position);
        final NewsFeedStatus newsFeedStatus=userProfileClassArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        result = Integer.parseInt(newsFeedStatus.getComments());

        if(newsFeedStatus.getType().equals("status"))
        {
            Log.v("status1",newsFeedStatus.getType());
            //holder.postTitle.setText(newsFeedStatus.getPostTitle());
            holder.userName.setText(newsFeedStatus.getUser_name_of_post());
            //userName=newsFeedStatus.getUser_name_of_post();
            holder.postShortDescription.setText(newsFeedStatus.getFullDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getUserImageUrl())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(newsFeedStatus.getType().equals("design"))
        {
            Log.v("status2",newsFeedStatus.getType());
            //holder.postTitle.setText(newsFeedStatus.getPostTitle());
            holder.userName.setText(newsFeedStatus.getUser_name_of_post());
            //userName=newsFeedStatus.getUser_name_of_post();
            holder.user_post_title.setText(newsFeedStatus.getPostTitle());
            holder.postShortDescription.setText(newsFeedStatus.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(newsFeedStatus.getType().equals("article"))
        {
            Log.v("status2",newsFeedStatus.getType());
            holder.userName.setText(newsFeedStatus.getUser_name_of_post());
            //userName=newsFeedStatus.getUser_name_of_post();
            holder.user_post_title.setText(newsFeedStatus.getPostTitle());
            holder.postShortDescription.setText(newsFeedStatus.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        Glide.with(context).load("https://archsqr.in/"+userClass.getProfile())
                .into( holder.usercommentProfile);

        holder.postBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",newsFeedStatus.getSlug());
                context.startActivity(intent);
            }
        });
        String dtc = newsFeedStatus.getTime();
        //Log.v("dtc",dtc);
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
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff/(24*60*60*1000);

        holder.postTime.setText(days+ " Days ago");
        //holder.fullDescription.setText(newsFeedStatus.);
        holder.buttonLikeList.setText(newsFeedStatus.getLike()+" Like");
        holder.buttonComment.setText(newsFeedStatus.getComments()+" Comment");
        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"comment button",Toast.LENGTH_LONG).show();
                if(flag1 == 0) {
                    holder.buttonComment.setTextColor(context.getColor(R.color.sqr));
                    flag1 = 1;

                }
                else {
                    holder.buttonComment.setTextColor(context.getColor(R.color.gray));
                    flag1 = 0;
                }

                //context.startActivity(new Intent(context,CommentsPage.class));
                Intent intent = new Intent(context, CommentsPage.class);
                intent.putExtra("Activity","From_UserProfle_Adapter");
                intent.putExtra("PostDataClass",userProfileClassArrayList.get(position)); //second param is Serializable
                context.startActivity(intent);

            }
        });
        if(userClass.getUserId()==newsFeedStatus.getUserId())
        {
            holder.user_post_menu.setVisibility(View.VISIBLE);
        }
        holder.user_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.delete_news_post_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.fullView:
                                Intent intent=new Intent(context,FullPostActivity.class);
                                intent.putExtra("Post_Slug_ID",newsFeedStatus.getSlug());
                                context.startActivity(intent);
                                break;
                            case R.id.editPost:
                                if(newsFeedStatus.getType().equals("design"))
                                {
                                    context.startActivity(new Intent(context,DesignActivity.class));
                                }
                                else if(newsFeedStatus.getType().equals("article"))
                                {
                                    context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                else if(newsFeedStatus.getType().equals("status"))
                                {
                                    context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                break;
                            case R.id.deletePost:
                                userProfileClassArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, 1);
                                DeletePost(newsFeedStatus.getPostId()+"",newsFeedStatus.getSharedId()+"",newsFeedStatus.getIs_Shared());
                                break;
                            case R.id.selectAsFeaturedPost:
                                return true;

                        }
                        return true;
                    }
                });
            }
        });
        holder.commentpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);

                                holder.commentTime.setText("0 minutes ago");
                                holder.commentDescription.setText(holder.userComment.getText().toString());
                                Glide.with(context).load("https://archsqr.in/"+userClass.getProfile())
                                        .into(holder.usercommentProfile);
                                holder.commentUserName.setText(userClass.getUser_name());
                                holder.comment_card.setVisibility(View.VISIBLE);
                                database= FirebaseDatabase.getInstance();
                                ref = database.getReference();
                                SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = mPrefs.getString("MyObject", "");
                                UserClass userClass = gson.fromJson(json, UserClass.class);
                                PushNotificationClass notificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),newsFeedStatus.getPostId(),newsFeedStatus.getPostTitle(),newsFeedStatus.getShortDescription(),"Commented",userClass.getUser_name()+" commented on your post");
                                ref.child("Notifications").child(newsFeedStatus.getUserId()+"").setValue(notificationClass);
                                holder.userComment.setText("");
                                result=result+1;
                                holder.buttonComment.setText(result+" Comment");

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

                        params.put("commentable_id",newsFeedStatus.getSharedId()+"");
                        params.put("comment_text",holder.userComment.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });
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
        return userProfileClassArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName,postTime,postShortDescription,postDescription,user_post_title,buttonLikeList,commentpost;
        EditText userComment;
        TextView commentUserName,commentTime,commentDescription;
        ImageView usercommentProfile,userProfile,postBannerImage,user_post_menu,userComment_menu;
        Button buttonComment,buttonShare;
        CheckBox buttonLike;
        CardView comment_card;
        ImageButton commentLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            user_post_title=itemView.findViewById(R.id.user_post_title);
            postBannerImage=(ImageView) itemView.findViewById(R.id.user_post_image);
            userName=(TextView)itemView.findViewById(R.id.userprofle_name);
            postTime=(TextView)itemView.findViewById(R.id.user_post_time);
            postShortDescription=(TextView)itemView.findViewById(R.id.user_post_short_descriptions);
            usercommentProfile=(ImageView)itemView.findViewById(R.id.user_profileImage);
            userProfile = (ImageView) itemView.findViewById(R.id.userprofile_image);
            buttonLike=(CheckBox)itemView.findViewById(R.id.user_post_like);
            buttonLikeList=(TextView)itemView.findViewById(R.id.user_post_likeList);
            buttonComment=(Button)itemView.findViewById(R.id.user_post_comment);
            buttonShare=(Button)itemView.findViewById(R.id.user_post_share);
            commentpost=(TextView)itemView.findViewById(R.id.user_post_button);
            userComment = (EditText)itemView.findViewById(R.id.user_write_comment);
            user_post_menu = (ImageView)itemView.findViewById(R.id.user_post_menu);

            userComment_menu = (ImageView)itemView.findViewById(R.id.user_comment_menu);
            commentTime=(TextView)itemView.findViewById(R.id.news_comment_time);
            commentDescription=(TextView)itemView.findViewById(R.id.user_comment);
            commentLike=(ImageButton)itemView.findViewById(R.id.user_post_likecomment);
            comment_card = (CardView)itemView.findViewById(R.id.userProfile_commentCardView);

            buttonLikeList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });

            commentpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike",s);
                                    userComment.setText("");


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

                            params.put("commentable_id",userProfileClassArrayList.get(getAdapterPosition()).getSharedId()+"");
//
                            params.put("comment_text",userComment.getText().toString());

                            //returning parameters
                            return params;
                        }
                    };

                    //Adding request to the queue
                    requestQueue.add(stringRequest);

                }
            });

            buttonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsPage.class);
                    intent.putExtra("Activity","From_User_Profile_Adapter");
                    intent.putExtra("PostDataClass",userProfileClassArrayList.get(getAdapterPosition())); //second param is Serializable
                    context.startActivity(intent);
                }
            });
            buttonLike.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    if (flag == 0) {
                        buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                        int result = Integer.parseInt(userProfileClassArrayList.get(getAdapterPosition()).getLike()+1);
                        buttonLikeList.setText(result+" Like");
                        flag = 1;
                    }
                    else {
                        buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        int result = Integer.parseInt(userProfileClassArrayList.get(getAdapterPosition()).getLike());
                        buttonLikeList.setText(result+" Like");
                        flag = 0;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike",s);
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
                            params.put("Authorization", "Bearer "+TokenClass.Token);
                            return params;
                        }
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //Adding parameters
//                 params.put("image_value",image);
                            params.put("likeable_id",userProfileClassArrayList.get(getAdapterPosition()).getSharedId()+"");
//                        params.put("likeable_type","user_post_share");
                            params.put("user_id",userProfileClassArrayList.get(getAdapterPosition()).getUserId()+"");


                            return params;
                        }
                    };

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }


            });
        }
    }
}