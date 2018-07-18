package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatWithAFriendActivityAdapter extends RecyclerView.Adapter<ChatWithAFriendActivityAdapter.MyViewHolder> {

    private ArrayList<MessageClass> messageClassArrayList;
    private Context context;
    private int friendId;
    private String friendProfileUrl,friendName;

    public ChatWithAFriendActivityAdapter(ArrayList<MessageClass> messageClassArrayList, Context context,int friendId,String friendProfileUrl,String friendName) {
        this.messageClassArrayList = messageClassArrayList;
        this.context = context;
        this.friendId=friendId;
        this.friendProfileUrl=friendProfileUrl;
        this.friendName=friendName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_a_friend_activity_adapter, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            MessageClass messageClass=messageClassArrayList.get(position);
            int fromId=messageClass.getUserFrom();
            if(fromId==friendId)
            {
                holder.frndName.setText(friendName);
                Glide.with(context).load("https://archsqr.in/"+friendProfileUrl)
                        .into(holder.frndProfile);

            }
            else
            {
                holder.frndName.setText(MessagesActivity.userName);
                Glide.with(context).load("https://archsqr.in/"+MessagesActivity.userProfile)
                        .into(holder.frndProfile);

            }

            holder.chatMessage.setText(messageClassArrayList.get(position).getChat());
            holder.chatTime.setText(messageClassArrayList.get(position).getUpdatedAt());


    }

    @Override
    public int getItemCount() {
        return messageClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView frndProfile;
        TextView frndName,chatMessage,chatTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            frndProfile =(ImageView)itemView.findViewById(R.id.chat_frnd_profile);
            frndName =(TextView) itemView.findViewById(R.id.chat_frnd_name);
            chatMessage =(TextView)itemView.findViewById(R.id.chat_message);
            chatTime=(TextView)itemView.findViewById(R.id.chat_time);
        }
    }
}
