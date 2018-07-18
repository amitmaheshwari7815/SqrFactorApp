package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommentsLimitedAdapter extends RecyclerView.Adapter<CommentsLimitedAdapter.MyViewHolder> {

    private ArrayList<comments_limited> comments_limitedArrayList=new ArrayList<>();
    private Context context;
    private int flag=0;

    public CommentsLimitedAdapter(ArrayList<comments_limited> comments_limitedArrayList, Context context) {

        this.comments_limitedArrayList = comments_limitedArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_limited_adapter,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.commentText.setText(comments_limitedArrayList.get(position).getComment_limited_body());

        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (flag == 0) {

                    holder.buttonLike.setTextColor(context.getColor(R.color.sqr));
                    int result = comments_limitedArrayList.get(position).getLikeCount()+1;
                    holder.buttonLike.setText(result+" Like");
                    flag = 1;
                }
                else {
                    holder.buttonLike.setTextColor(context.getColor(R.color.gray));
                    int result = comments_limitedArrayList.get(position).getLikeCount();
                    holder.buttonLike.setText(result+" Like");
                    flag = 0;
                }

            }
        });
        holder.commenterUser.setText(comments_limitedArrayList.get(position).getCommentUserName());

        Glide.with(context).
                load(comments_limitedArrayList.get(position).getCommentUserPrfile())
                .centerCrop().into(holder.commenterProfile);


        String dtc = comments_limitedArrayList.get(position).getComment_limited_updated_at();
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
        holder.timeAgo.setText(days+" Days ago");




    }

    @Override
    public int getItemCount() {

        return comments_limitedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commentText,commenterUser,timeAgo;
        ImageView commenterProfile;
        Button buttonLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentText=(TextView)itemView.findViewById(R.id.commentBody);
            commenterUser=(TextView)itemView.findViewById(R.id.commenterUserName);
            timeAgo=(TextView)itemView.findViewById(R.id.timeAgo);
            commenterProfile=(ImageView)itemView.findViewById(R.id.commenterProfileImage);
            buttonLike=(Button)itemView.findViewById(R.id.commentLike);
        }
    }
}
