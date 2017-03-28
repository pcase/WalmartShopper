package com.azurehorsecreations.walmartshopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalmartProductActivity extends AppCompatActivity implements CallbackReceiver, ProductAdapter.ItemClickListener {
    private static final String PRODUCT = "PRODUCT";
    private static final String TAG = "WalmartProductActivity";
    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EndlessRecyclerViewScrollListener scrollListener;

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
            int page = 1;
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore() page=" + page);
                ProductInformationFetcher productInformationFetcher = new ProductInformationFetcher(WalmartProductActivity.this, page);
                productInformationFetcher.execute();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        ProductInformationFetcher productInformationFetcher = new ProductInformationFetcher(this);
        productInformationFetcher.execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(PRODUCT, productAdapter.getItem(position));
        startActivity(intent);
    }

    public void handleResultData(Object object) {
        int size;
        if (productAdapter != null) {
            size = productAdapter.getItemCount();
        } else {
            size = 0;
        }
        final int currentSize = size;
        productList = (List<Product>) object;
        productAdapter = new ProductAdapter(WalmartProductActivity.this, productList);
        productAdapter.setClickListener(WalmartProductActivity.this);
        recyclerView.setAdapter(productAdapter);
        productAdapter.notifyItemRangeInserted(currentSize, productList.size() - 1);
        scrollListener.resetState();
        // This crashes
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                productAdapter.notifyItemRangeInserted(currentSize, productList.size() - 1);
//                scrollListener.resetState();
//            }
//        });
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
