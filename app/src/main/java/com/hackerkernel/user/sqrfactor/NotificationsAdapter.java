package com.hackerkernel.user.sqrfactor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    String text;

    public NotificationsAdapter(String s) {
        text = s;
    }

    @NonNull

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_layout, parent, false);

        ViewHolder v = new ViewHolder(ll);
        return v;

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        holder.notification.setText(Html.fromHtml(text));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView notification;

        public ViewHolder(View itemView) {

            super(itemView);

            notification = (TextView)itemView.findViewById(R.id.notification_line);

        }
    }

}
