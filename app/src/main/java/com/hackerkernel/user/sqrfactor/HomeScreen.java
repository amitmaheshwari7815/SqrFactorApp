package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeScreen extends ToolbarActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN","sqr");
        TokenClass.Token=token;
        TokenClass tokenClass=new TokenClass(token);
        Log.v("Token1",token);

        //getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new TrophyFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new NewsFeedFragment()).commit();


        tabLayout = (TabLayout)findViewById(R.id.tabs);

        //tabLayout.setupWithViewPager(pager);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.news_feed2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.msg));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_black_24dp));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profilepic));

        /*tabLayout.getTabAt(0).setIcon(R.drawable.trophy_filled);
        tabLayout.getTabAt(1).setIcon(R.drawable.msg);
        tabLayout.getTabAt(2).setIcon(R.drawable.notification);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:

                    case 1:
                        tab.setIcon(R.drawable.envelope_filled);
                        Intent i1 = new Intent(HomeScreen.this, MessagesActivity.class);
                        startActivity(i1);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notification_filled);
                        Intent i2 = new Intent(HomeScreen.this, NotificationsActivity.class);
                        startActivity(i2);
                        break;


                    case 3:
                        new ModalSheet().show(getSupportFragmentManager(), "");
                        //Intent i4 = new Intent(HomeScreen.this, MessagesActivity.class);
                        //startActivity(i4);
                        break;

                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:

                        break;

                    case 1:
                        tab.setIcon(R.drawable.msg);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notification);
                        break;


                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {



            }
        });
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_profile) {
                    Intent i = new Intent(HomeScreen.this, ProfileActivity.class);
                    //i.putExtra("Activity", "1");
                    startActivity(i);

                }
                if (id == R.id.navigation_credits){
                    Intent j = new Intent(HomeScreen.this,Credits.class);
                    //j.putExtra("Activity", "2");
                    startActivity(j);

                }
                if (id == R.id.navigation_settings){
                    Intent intent = new Intent(HomeScreen.this,Settings.class);
                    //intent.putExtra("Activity", "3");
                    startActivity(intent);

                }
                menuItem.setChecked(false);
                drawerLayout.closeDrawers();
                Toast.makeText(HomeScreen.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //navigationView.getMenu().getItem(0).setCheckable(false);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
        //LinearLayout ll = (LinearLayout)tabLayout.getChildAt(0);
        //ll.getChildAt(tabLayout.getSelectedTabPosition()).setSelected(false);
        //ll.getChildAt(0).setSelected(true);
    }

    /*private class SectionsPagerAdapter extends FragmentStatePagerAdapter{


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0:
                    return new TrophyFragment();

                case 1:
                    return new MessagesFragment();

                case 2:
                    return new TrophyFragment();

            }

            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        /*@Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){

                case 0:
                    return "Status";

                case 1:
                    return "Status";

                case 2:
                    return "Status";

                case 3:
                    return "Status";

            }

            return null;

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawers();
        else
            super.onBackPressed();
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
