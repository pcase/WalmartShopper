package com.azurehorsecreations.walmartshopper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/*
 * ProductDetailPagerAdapter handles product detail fragment data
 */

public class ProductDetailPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ProductDetailPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
