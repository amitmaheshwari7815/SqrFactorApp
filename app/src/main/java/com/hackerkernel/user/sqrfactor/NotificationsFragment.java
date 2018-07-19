package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationsFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_notifications, container, false);

        String s = getContext().getResources().getString(R.string.noti_string);
        //text.setText(Html.fromHtml(s));
        //text2.setText(Html.fromHtml(s));

        recycler = (RecyclerView)view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        adapter = new NotificationsAdapter(s);
        recycler.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);

        return view;

    }

}
