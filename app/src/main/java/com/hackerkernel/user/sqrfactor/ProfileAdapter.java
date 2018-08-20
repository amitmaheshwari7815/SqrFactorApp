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
    private ArrayList<ProfileClass1> profileClassArrayList;
    private Context context;
    private int flag = 0;
    private String userName;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    public ProfileAdapter(ArrayList<ProfileClass1> profileClasses, Context context) {
        this.profileClassArrayList = profileClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProfileClass1 profileClass = profileClassArrayList.get(position);
        //final NewsFeedStatus profileClass=profileClasses.get(position);


        // holder.time.setText(profileClass.getTime());
        //Log.v("status",profileClass.getType());
//

//        holder.time.setText(profileClass.getTime());
//        Log.v("status",profileClass.getType());
        if(profileClass.getType().equals("status"))
        {
            Log.v("status1",profileClass.getType());
            //holder.postTitle.setText(profileClass.getPostTitle());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postShortDescription.setText(profileClass.getFullDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getUserImageUrl())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(profileClass.getType().equals("design"))
        {
            Log.v("status2",profileClass.getType());
            //holder.postTitle.setText(profileClass.getPostTitle());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(profileClass.getType().equals("article"))
        {
            Log.v("status2",profileClass.getType());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                Log.v("Data",profileClass.getUserId()+" "+userName+" "+profileClass.getPostId());
                intent.putExtra("User_id",profileClass.getUserId());
                intent.putExtra("ProfileUserName",userName);
                context.startActivity(intent);

            }
        });

        String dtc = profileClass.getTime();
        // Log.v("dtc",dtc);
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
        //holder.fullDescription.setText(profileClass.);
        holder.buttonLikeList.setText(profileClass.getLike()+" Like");

        holder.buttonComment.setText(profileClass.getComments()+" Comment");
//        holder.comments.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"comment button",Toast.LENGTH_LONG).show();
//                if(flag1 == 0) {
//                    holder.comments.setTextColor(context.getColor(R.color.sqr));
//                    flag1 = 1;
//                    database= FirebaseDatabase.getInstance();
//                    ref = database.getReference();
//                    SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
//                    Gson gson = new Gson();
//                    String json = mPrefs.getString("MyObject", "");
//                    UserClass userClass = gson.fromJson(json, UserClass.class);
//                    PushNotificationClass notificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),profileClass.getPostId(),profileClass.getPostTitle(),profileClass.getShortDescription(),"Commented",userClass.getUser_name()+" commented on your post");
//                    ref.child("Notifications").child(profileClass.getUserId()+"").setValue(notificationClass);
//
//                }
//                else {
//                    holder.comments.setTextColor(context.getColor(R.color.gray));
//                    flag1 = 0;
//                }
//
//                //context.startActivity(new Intent(context,CommentsPage.class));
//                Intent intent = new Intent(context, CommentsPage.class);
//                intent.putExtra("PostDataClass",profileClasses.get(position)); //second param is Serializable
//                context.startActivity(intent);
//
//
//
//
//            }
//        });

//        holder.share.setText(profileClass.getShare());
//        holder.commentUserName.setText(profileClass.getCommentUserName());
//        holder.commentTime.setText(profileClass.getCommentTime());
//        holder.commentDescription.setText(profileClass.getCommentDescription());
//        holder.commentLike.setText(profileClass.getCommentLike());
//        holder.postId.setText(profileClass.getPostId());
        //      holder.userId.setText(profileClass.getUserId());
    }

    @Override
    public int getItemCount() {
        return profileClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, postTime, postShortDescription, postDescription,postTitle;
        EditText userComment;
        ImageView usercommentProfile, userProfile, postBannerImage;
        Button buttonLikeList, buttonComment, buttonShare, commentpost;
        ImageButton buttonLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            postTitle=(TextView)itemView.findViewById(R.id.user_post_title);
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
//            buttonLikeList.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//
//            });

//            buttonLike.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.M)
//                @Override
//                public void onClick(View v) {
//
//                    if (flag == 0) {
//                        buttonLikeList.setTextColor(context.getColor(R.color.sqr));
//                        int result = Integer.parseInt(profileClassArrayList.get(getAdapterPosition()).getLike()) + 1;
//                        buttonLikeList.setText(result + " Like");
//                        flag = 1;
//                    } else {
//                        buttonLikeList.setTextColor(context.getColor(R.color.gray));
//                        int result = Integer.parseInt(profileClassArrayList.get(getAdapterPosition()).getLike());
//                        buttonLikeList.setText(result + " Like");
//                        flag = 0;
//                    }
//                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String s) {
//                                    Log.v("ResponseLike", s);
//
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError volleyError) {
//
//                                }
//                            }) {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Accept", "application/json");
//                            params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjVkNWNmZDNhODYxYTBlNzU5MTk4NGU3MGQyNDdkYWYwYzU2MWEwNmMxMGU2MTMxNmU0ZTcyZTBhYjBmMjZlYWJhOWIwNDVkZWEzOWRkNTYwIn0.eyJhdWQiOiIzIiwianRpIjoiNWQ1Y2ZkM2E4NjFhMGU3NTkxOTg0ZTcwZDI0N2RhZjBjNTYxYTA2YzEwZTYxMzE2ZTRlNzJlMGFiMGYyNmVhYmE5YjA0NWRlYTM5ZGQ1NjAiLCJpYXQiOjE1MzQxNDUyNDMsIm5iZiI6MTUzNDE0NTI0MywiZXhwIjoxNTY1NjgxMjQzLCJzdWIiOiIxMDUiLCJzY29wZXMiOltdfQ.DTLGdolrOb68d2JR3W6kQ0O92eIP2Cj0OoJtbTduQwUb5TL8nszup_Q7keSkzD-_OrRSffzbTqVNLf8lucGeMqCBO5Fwb8MiiiSxCcGOlzvEmOen-MNYNb-f8KmGxjvW0c3vszD8a9C7OK5VnlXhPM7wFJr_NRlbl2Q_c3dHXQc4PLEhA6FE5oB7H_s7OtgKZ6AbLzEqNxbOCSZ7n5nhy2P5OHOuIIvsVEFA9BM-hWG_s6BNbMeSqAHwj3HRzrIpFHGXI37eU00ZKU641HlllWmlNPw4ou4T1MWW2YIFLt81xMe7xRpfK2-NdvfZCSk2j3fRd19vjKJddzATsSD_RWoH5Ste9oJ5_XADNP1eNjMUhVZDqIe8KBAtclvPDTUeIlWgUh1Rw797NT0uwlZ_7CHIabHYchsD78rcqi600wTcSlYhjC1778elk0skSFuaa9Mubj8z7eut-pUqpb0I4mCKFq84sMmJDF5k2bD-4MgX1oe3CRBALG3uZ7emAJb4NN4V6f5aDUI7slB3YSQhg9qg_Dpglt851aYZwGKg0VXbHktIwVWjhimoEP73DsZ29-3cDCY5mqV5Ev4UUL5HlrjikTfJHkuyXG4poQM4VW-eSkYlcpbd3e6wUxjOLxCJDfYu_nGkaL3OtRe1-qd6buQqVoQrgQfGK1ifpGBOfvc");
//
//                            return params;
//                        }
//
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<>();
//                            //Adding parameters
////                 params.put("image_value",image);
//                            params.put("likeable_id", profileClassArrayList.get(getAdapterPosition()).getSharedId() + "");
////                        params.put("likeable_type","user_post_share");
//                            params.put("user_id", profileClassArrayList.get(getAdapterPosition()).getUserId()+ "");
//
//                            //returning parameters
//                            return params;
//                        }
//                    };
//
//                    //Adding request to the queue
//                    requestQueue.add(stringRequest);
//                }
//
//
//            });
        }
    }
}
