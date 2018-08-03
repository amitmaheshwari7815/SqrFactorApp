package com.hackerkernel.user.sqrfactor;

import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TopContributors extends AppCompatActivity {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_contributors);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.top_tabs);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        createTabs();


    }

    private void createTabs() {

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tabs_name, null);
        TextView iv_tab1 = tabOne.findViewById(R.id.iv_tab);
        iv_tab1.setText("Architecture Colleges");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tabs_name, null);
        TextView iv_tab2 = tabTwo.findViewById(R.id.iv_tab);
        iv_tab2.setText("Top Contributors");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tabs_name, null);
        TextView iv_tab3 = tabThree.findViewById(R.id.iv_tab);
        iv_tab3.setText("Architecture Firms");
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
}
