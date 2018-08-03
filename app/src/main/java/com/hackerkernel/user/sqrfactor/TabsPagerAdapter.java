package com.hackerkernel.user.sqrfactor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new ContributorsFragment();
                break;
            case 1:
                fragment = new ContributorsFragment();
                break;

            case 2:
                fragment = new ContributorsFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}

