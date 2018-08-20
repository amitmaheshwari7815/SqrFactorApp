package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {
    private Context context;
    int flag = 0;
    private ArrayList<NewsFeedStatus> userProfileClassArrayList;
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
    public void onBindViewHolder(@NonNull UserProfileAdapter.MyViewHolder holder, int position) {

        //UserProfileClass userProfileClass=userProfileClassArrayList.get(position);
        NewsFeedStatus newsFeedStatus=userProfileClassArrayList.get(position);

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
            //holder.postTitle.setText(newsFeedStatus.getPostTitle());
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
            //holder.postTitle.setText(newsFeedStatus.getPostTitle());
            holder.postShortDescription.setText(newsFeedStatus.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.userProfile);
        }
//            if(newsFeedStatus.getAuthName().equals("null"))
//            {
//                holder.userName.setText(postDataClass.getFirstName()+" "+postDataClass.getLastName());
//            }
//
//            else {
//                holder.userName.setText(postDataClass.getUserName());
//            }
        //holder.postTime.setText(newsFeedStatus.getTime());
        //holder.postShortDescription.setText(postDataClass.getShort_description());
        // holder.shortDescription.setText(newsFeedStatus.getShortDescription());
//            Glide.with(context).load("https://archsqr.in/"+postDataClass.getUserProfile())
//                    .into(holder.userProfile);
//            Glide.with(context).load("https://archsqr.in/"+postDataClass.getBanner_image())
//                    .into(holder.postBannerImage);

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
//        holder.share.setText(newsFeedStatus.getShare());
//        holder.commentUserName.setText(newsFeedStatus.getCommentUserName());
//        holder.commentTime.setText(newsFeedStatus.getCommentTime());
//        holder.commentDescription.setText(newsFeedStatus.getCommentDescription());
//        holder.commentLike.setText(newsFeedStatus.getCommentLike());
//        holder.postId.setText(newsFeedStatus.getPostId());
        //      holder.userId.setText(newsFeedStatus.getUserId());
    }

    @Override
    public int getItemCount() {
        return userProfileClassArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName,postTime,postShortDescription,postDescription;
        EditText userComment;
        ImageView usercommentProfile,userProfile,postBannerImage;
        Button buttonLikeList,buttonComment,buttonShare,commentpost;
        ImageButton buttonLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            postBannerImage=(ImageView) itemView.findViewById(R.id.user_post_image);
            userName=(TextView)itemView.findViewById(R.id.userprofle_name);
            postTime=(TextView)itemView.findViewById(R.id.user_post_time);
            postShortDescription=(TextView)itemView.findViewById(R.id.user_post_short_descriptions);
            usercommentProfile=(ImageView)itemView.findViewById(R.id.user_profileImage);
            userProfile = (ImageView) itemView.findViewById(R.id.userprofile_image);
            buttonLike=(ImageButton)itemView.findViewById(R.id.user_post_like);
            buttonLikeList=(Button)itemView.findViewById(R.id.user_post_likeList);
            buttonComment=(Button)itemView.findViewById(R.id.user_post_comment);
            buttonShare=(Button)itemView.findViewById(R.id.user_post_share);
            commentpost=(Button)itemView.findViewById(R.id.user_post_button);
            userComment = (EditText)itemView.findViewById(R.id.user_write_comment);
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

