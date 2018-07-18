package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    public static ArrayList<comments_limited> arrayList=new ArrayList<>();
    CommentsLimitedAdapter commentsLimitedAdapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comments, container, false);
        CommentsPage activity = (CommentsPage) getActivity();
        NewsFeedStatus newsFeedStatus = activity.getMyData();


        Log.v("postDatOncommentfrag",newsFeedStatus.toString());


        recyclerView = view.findViewById(R.id.comments_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);




        arrayList=newsFeedStatus.getCommentsLimitedArrayList();
        Log.v("autname",newsFeedStatus.getAuthName());
        //Log.v("length",arrayList.size()+"");
        commentsLimitedAdapter=new CommentsLimitedAdapter(arrayList,this.getActivity());
        recyclerView.setAdapter(commentsLimitedAdapter);
        commentsLimitedAdapter.notifyDataSetChanged();



        return view;
//        Bundle extras = getArguments().getBundle("");
//        if (extras != null) {
//            newsFeedStatus = (NewsFeedStatus) getIntent().getSerializableExtra("PostDataClass");
//            //Log.v("PostDataOnCommetPage",newsFeedStatus.getFullDescription());
//            //Obtaining data
//        }


    }
}
