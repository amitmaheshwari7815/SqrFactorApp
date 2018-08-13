package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArchitectureFirmsAdapter extends RecyclerView.Adapter<ArchitectureFirmsAdapter.MyViewAdapter> {

    private ArrayList<ArchitectureFirmClass> architectureFirmClassArrayList;
    private Context context;

    public ArchitectureFirmsAdapter(ArrayList<ArchitectureFirmClass> architectureFirmsClassArrayList, Context context) {
        this.architectureFirmClassArrayList = architectureFirmsClassArrayList;
        this.context = context;
    }
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.architecture_firms_adapter, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {
        ArchitectureFirmClass architectureFirmClass = architectureFirmClassArrayList.get(position);
        holder.name.setText(architectureFirmClass.getName());
        Glide.with(context).load("https://archsqr.in/" + architectureFirmClass.getProfile())
                .into(holder.profileImage);


    }

    @Override
    public int getItemCount() {
        return architectureFirmClassArrayList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView name,firm;
        ImageView profileImage;

        public MyViewAdapter(View itemView) {
            super(itemView);

            profileImage=(ImageView) itemView.findViewById(R.id.architectureFirm_image);
            name=(TextView)itemView.findViewById(R.id.architectureFirm_name);
            firm =(TextView)itemView.findViewById(R.id.architectureFirm_firm);

        }
    }
}
