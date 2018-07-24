package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import static android.content.Context.MODE_PRIVATE;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder> {
    private ArrayList<NewsFeedStatus> newsFeedStatuses;
    private Context context;
    int flag = 0;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    public NewsFeedAdapter(ArrayList<NewsFeedStatus> newsFeedStatuses, Context context) {
        this.newsFeedStatuses = newsFeedStatuses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_status_adapter,parent,false);

        return new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final NewsFeedStatus newsFeedStatus=newsFeedStatuses.get(position);
        holder.authName.setText(newsFeedStatus.getAuthName());
        holder.time.setText(newsFeedStatus.getTime());
        holder.postTitle.setText(newsFeedStatus.getPostTitle());
        holder.shortDescription.setText(newsFeedStatus.getShortDescription());
        Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                .into(holder.authImageUrl);
        Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                .into(holder.postImage);

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
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff/(24*60*60*1000);

        holder.time.setText(days+ " Days ago");
        //holder.fullDescription.setText(newsFeedStatus.);
        holder.likelist.setText(newsFeedStatus.getLike()+" Like");

        holder.comments.setText(newsFeedStatus.getComments()+" Comment");
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsPage.class);
                intent.putExtra("PostDataClass",newsFeedStatuses.get(position)); //second param is Serializable
                context.startActivity(intent);

            }
        });

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
        return newsFeedStatuses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         ImageView userImageUrl,authImageUrl,postImage,commentProfileImageUrl;
         TextView authName,time,postTitle,shortDescription,fullDescription,comments,share,
                 commentUserName,commentTime,commentDescription,commentLike,writeComment;
         ImageButton like;
         Button likelist,commentPostbtn;


         TextView postId,userId;

        public MyViewHolder(View itemView) {
            super(itemView);

            userImageUrl=(ImageView)itemView.findViewById(R.id.newsProfileImage);
            authImageUrl=(ImageView)itemView.findViewById(R.id.news_auth_image);
            postImage=(ImageView)itemView.findViewById(R.id.news_post_image);
            commentProfileImageUrl=(ImageView)itemView.findViewById(R.id.news_comment_image);
            authName=(TextView)itemView.findViewById(R.id.news_auth_name);
            time=(TextView)itemView.findViewById(R.id.news_post_time);
            postTitle=(TextView)itemView.findViewById(R.id.news_post_title);
            shortDescription=(TextView)itemView.findViewById(R.id.news_short_Descrip);
            fullDescription=(TextView)itemView.findViewById(R.id.news_full_Descrip);
            like=(ImageButton) itemView.findViewById(R.id.news_post_like);
            comments=(TextView)itemView.findViewById(R.id.news_comment);
            share=(TextView)itemView.findViewById(R.id.news_share);
            commentUserName=(TextView)itemView.findViewById(R.id.news_comment_name);
            commentTime=(TextView)itemView.findViewById(R.id.news_comment_time);
            commentDescription=(TextView)itemView.findViewById(R.id.news_comment_descrip);
            commentLike=(TextView)itemView.findViewById(R.id.news_commnent_like);
            likelist =(Button) itemView.findViewById(R.id.news_post_likeList);
            writeComment =(TextView)itemView.findViewById(R.id.news_user_commnentEdit);
            commentPostbtn =(Button)itemView.findViewById(R.id.news_comment_post);
            commentPostbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike",s);
                                    writeComment.setText("");

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

                            params.put("commentable_id",newsFeedStatuses.get(getAdapterPosition()).getSharedId()+"");
//
                            params.put("comment_text",writeComment.getText().toString());

                            //returning parameters
                            return params;
                        }
                    };

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }
            });
            likelist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopup();
                }

            });

            like.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    if (flag == 0) {
                       likelist.setTextColor(context.getColor(R.color.sqr));
                        int result = Integer.parseInt(newsFeedStatuses.get(getAdapterPosition()).getLike())+1;
                       likelist.setText(result+" Like");
                        database= FirebaseDatabase.getInstance();
                        ref = database.getReference();
                        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = mPrefs.getString("MyObject", "");
                        UserClass userClass = gson.fromJson(json, UserClass.class);
                        NotificationClass notificationClass=new NotificationClass(userClass.getUserId(),userClass.getProfile(),newsFeedStatuses.get(getAdapterPosition()).getPostId(),newsFeedStatuses.get(getAdapterPosition()).getPostTitle(),newsFeedStatuses.get(getAdapterPosition()).getShortDescription(),"Like");
                        ref.child("Notifications").child(newsFeedStatuses.get(getAdapterPosition()).getUserId()+"").setValue(notificationClass);

                        flag = 1;
                    }
                    else {
                       likelist.setTextColor(context.getColor(R.color.gray));
                        int result = Integer.parseInt(newsFeedStatuses.get(getAdapterPosition()).getLike());
                      likelist.setText(result+" Like");
                        flag = 0;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike",s);

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
                            //Adding parameters
//                 params.put("image_value",image);
                            params.put("likeable_id",newsFeedStatuses.get(getAdapterPosition()).getSharedId()+"");
                            params.put("likeable_type","users_post_share");
//                            params.put("user_id",newsFeedStatuses.get(getAdapterPosition()).getUserId()+"");
                            //returning parameters
                            return params;
                        }
                    };

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }


            });
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
            commentLike.setOnClickListener(new View.OnClickListener() {

            int flag = 0;
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (flag == 0) {
                    commentLike.setTextColor(context.getColor(R.color.sqr));
                    flag = 1;
                }
                else {
                    commentLike.setTextColor(context.getColor(R.color.gray));
                    flag = 0;
                    }

                context.startActivity(new Intent(context,CommentsPage.class));

            }

        });
        }
    }
    private void shareIt() {
    //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"SqrFactor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }
    public void showPopup(){
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.post_likes_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        RecyclerView recyclerView = promptsView.findViewById(R.id.like_recycler);
        // show it
        alertDialog.show();

    }
}
