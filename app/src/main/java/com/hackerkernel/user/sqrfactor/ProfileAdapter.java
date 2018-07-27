package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private ArrayList<ProfileClass> profileClassArrayList;
    private Context context;
    int flag = 0;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    public ProfileAdapter(ArrayList<ProfileClass> profileClasses, Context context) {
        this.profileClassArrayList = profileClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ProfileClass profileClass = profileClassArrayList.get(position);
        holder.userName.setText(profileClass.getUser_name());
        //holder.postTime.setText(newsFeedStatus.getTime());
        holder.postShortDescription.setText(profileClass.getShort_description());
        // holder.shortDescription.setText(newsFeedStatus.getShortDescription());
        Glide.with(context).load("https://archsqr.in/" + profileClass.getProfile())
                .into(holder.userProfile);
        Glide.with(context).load("https://archsqr.in/" + profileClass.getBanner_image())
                .into(holder.postBannerImage);

        String dtc = profileClass.getPost_time();
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
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        holder.postTime.setText(days + " Days ago");
        //holder.fullDescription.setText(newsFeedStatus.);
        holder.buttonLikeList.setText(profileClass.getLike() + " Like");
        holder.buttonComment.setText(profileClass.getComment() + " Comment");
    }

    @Override
    public int getItemCount() {
        return profileClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, postTime, postShortDescription, postDescription;
        EditText userComment;
        ImageView usercommentProfile, userProfile, postBannerImage;
        Button buttonLikeList, buttonComment, buttonShare, commentpost;
        ImageButton buttonLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            postBannerImage = (ImageView) itemView.findViewById(R.id.user_post_image);
            userName = (TextView) itemView.findViewById(R.id.userprofle_name);
            postTime = (TextView) itemView.findViewById(R.id.user_post_time);
            postShortDescription = (TextView) itemView.findViewById(R.id.user_post_short_descriptions);
            usercommentProfile = (ImageView) itemView.findViewById(R.id.user_profileImage);
            userProfile = (ImageView) itemView.findViewById(R.id.userprofile_image);
            buttonLike = (ImageButton) itemView.findViewById(R.id.user_post_like);
            buttonLikeList = (Button) itemView.findViewById(R.id.user_post_likeList);
            buttonComment = (Button) itemView.findViewById(R.id.user_post_comment);
            buttonShare = (Button) itemView.findViewById(R.id.user_post_share);
            commentpost = (Button) itemView.findViewById(R.id.user_post_button);
            userComment = (EditText) itemView.findViewById(R.id.user_write_comment);
            buttonLikeList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });

            buttonLike.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    if (flag == 0) {
                        buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                        int result = Integer.parseInt(profileClassArrayList.get(getAdapterPosition()).getLike()) + 1;
                        buttonLikeList.setText(result + " Like");
                        flag = 1;
                    } else {
                        buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        int result = Integer.parseInt(profileClassArrayList.get(getAdapterPosition()).getLike());
                        buttonLikeList.setText(result + " Like");
                        flag = 0;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike", s);

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
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Accept", "application/json");
                            params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZmMTliZGU4NTZmODZiMTZiMDUxYjk2OGI3YTk5ZmRlYzM1MDk2YjBhNzJiN2YyMWQ1MGVkNmU2NDMwMzQ0ZTIxZGE5YWZiMDkxZGU5MzZjIn0.eyJhdWQiOiIzIiwianRpIjoiZmYxOWJkZTg1NmY4NmIxNmIwNTFiOTY4YjdhOTlmZGVjMzUwOTZiMGE3MmI3ZjIxZDUwZWQ2ZTY0MzAzNDRlMjFkYTlhZmIwOTFkZTkzNmMiLCJpYXQiOjE1MzI0Mjg5NjYsIm5iZiI6MTUzMjQyODk2NiwiZXhwIjoxNTYzOTY0OTY2LCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.TiGb0ea0wLkAVp9AstLs2EmH3RD8wORc-HjxeTXiQ_RRNAMUOcmUoV8E7g9SwdMjywztsZbYOekY_fQD0xnpY26gkaxSaaDVeZKYrnN-WM--zWWzHKpi9wUp9QUnVBh36Ah1Akqu-hwhCFKTplF7ZMS1NM9LKxOyNd2uwFsv-neScT2vQFvGYgxla2tmzqo9tzpXXPzFEn1uLQvg4lsr-EDZs_9CvB_Bef-WyLRZfKtJTEzbcd4FvviQdZCVYuzHkI-VixlMoBRAEWqMWBnUI0xBepOqC4gfv7s4Qs03M0vhu18rTXJ-nCfjhg3XA7wdYKBXe9sFNNTcTe7A0rnWIKRsIH6dQxipw0HqtXFOR-kgG6p0fFgY30oo_P8OSp8RwYNVYw118jsXKh24YR5twXyvXAeZoi-iNbU8qpJNVp1cWTRIUvbtosSt9ct8lj70zKpuyFRZgfT_FEfdW32UQ_6d9VC_7NjlyDFlmZazCfYOgEYsdOPmU1xTy4xGnEyNgj0jCbi_BNt7KnefER2i41leombzq14d8wqkdm_num53EPWTOxlaXRF85jOrjKv5a6WEFYOzTCHSXWeIIcYfYFnOdqsWXeSSlNZ4xD1DITObUbUl30lGkkNSY0v5yePFL9-hNVByGY7LrMH4TNFiWn5-g0MAIo5DOHK8gHAmWJg");

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            //Adding parameters
//                 params.put("image_value",image);
                            params.put("likeable_id", profileClassArrayList.get(getAdapterPosition()).getSharedId() + "");
//                        params.put("likeable_type","user_post_share");
                            params.put("user_id", profileClassArrayList.get(getAdapterPosition()).getUser_id() + "");

                            //returning parameters
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
