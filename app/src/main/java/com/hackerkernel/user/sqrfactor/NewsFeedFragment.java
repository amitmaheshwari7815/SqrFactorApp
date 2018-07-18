package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFeedFragment extends Fragment {

    TabLayout tabLayout;
    public static int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View)inflater.inflate(R.layout.fragment_news_feed, container, false);


//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("PREF_NAME",getActivity().MODE_PRIVATE);
//        String token = sharedPreferences.getString("TOKEN","sqr");
//        Log.v("Token2",token);

        getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();

        tabLayout = (TabLayout)view.findViewById(R.id.tabs2);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.status)
                .setText("Status"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.design)
                .setText("Design"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.article)
                .setText("Article"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition()==0)
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();

                if (tab.getPosition()==1) {
//                    getChildFragmentManager().beginTransaction().replace(R.id.fragment, new DesignFragment()).addToBackStack(null).commit();
                    Intent intent = new Intent(getActivity().getApplicationContext(), DesignActivity.class);
                    getActivity().startActivity(intent);
                }

                if (tab.getPosition()==2) {
//                    getChildFragmentManager().beginTransaction().replace(R.id.fragment, new DesignFragment()).addToBackStack(null).commit();
                    Intent i = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
                    getActivity().startActivity(i);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
