package com.azurehorsecreations.walmartshopper;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/*
 * ProductDetailPagerActivity displays the product details in a swipeable viewer
 */

public class ProductDetailPagerActivity extends AppCompatActivity {
    private static final String PRODUCT = "PRODUCT";
    private Product product;
    private Product[] productArray;
    FragmentPagerAdapter adapterViewPager;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        Parcelable[] parcelableProductArray = getIntent().getParcelableArrayExtra(PRODUCT);
        productArray = new Product[parcelableProductArray.length];
        for (int i=0; i<parcelableProductArray.length; ++i) {
            productArray[i] = (Product) parcelableProductArray[i];
        }
        fragments = new ArrayList<>();
        for (int i=0; i<productArray.length; ++i) {
            fragments.add(ProductDetailFragment.newInstance(productArray[i]));
        }
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new ProductDetailPagerAdapter(getSupportFragmentManager(), fragments);
        vpPager.setAdapter(adapterViewPager);
    }
}
