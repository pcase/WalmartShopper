package com.azurehorsecreations.walmartshopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
 * WalmartProductActivity fetches and displays product names and images in a list
 */

public class WalmartProductActivity extends AppCompatActivity implements CallbackReceiver, ProductAdapter.ItemClickListener {
    private static final String TAG = "WalmartProductActivity";
    private static final String PRODUCT = "PRODUCT";
    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EndlessRecyclerViewScrollListener scrollListener;
    private int pageToLoad = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walmart_product);
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(gridLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ++pageToLoad;
                ProductInformationFetcher productInformationFetcher = new ProductInformationFetcher(WalmartProductActivity.this, pageToLoad);
                productInformationFetcher.execute();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        ProductInformationFetcher productInformationFetcher = new ProductInformationFetcher(this);
        productInformationFetcher.execute();
    }

    // Display the product's detail page
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProductDetailPagerActivity.class);
        Product[] productArrayToSend = new Product[productList.size()];
        int listIndexFromPosition = position;
        for (int i=0; i<productList.size()-position; ++i) {
            productArrayToSend[i] = productList.get(listIndexFromPosition);
            ++listIndexFromPosition;
        }
        int listIndexToPosition = 0;
        for (int i = productList.size()-position; i<productList.size(); ++i) {
            productArrayToSend[i] = productList.get(listIndexToPosition);
            ++listIndexToPosition;
        }
        intent.putExtra(PRODUCT, productArrayToSend);
        startActivity(intent);
    }

    // Update the product page with loaded data
    public void handleResultData(Object object) {
        int size;
        if (productAdapter != null) {
            size = productAdapter.getItemCount();
        } else {
            size = 0;
        }
        final int currentSize = size;
        List<Product> newProductList = (List<Product>) object;
        productList.addAll(newProductList);
        productAdapter = new ProductAdapter(WalmartProductActivity.this, productList);
        productAdapter.setClickListener(WalmartProductActivity.this);
        recyclerView.setAdapter(productAdapter);
        productAdapter.notifyItemRangeInserted(currentSize, productList.size() - 1);
        scrollListener.resetState();
        runOnUiThread(new Runnable() {
            public void run() {
                if ((productList == null) || (productList != null && productList.size() == 0)) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}
