package com.azurehorsecreations.walmartshopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walmart_product);
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ProductInformationFetcher productInformationFetcher = new ProductInformationFetcher(this);
        productInformationFetcher.execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(PRODUCT, productAdapter.getItem(position));
        Product product = productAdapter.getItem(position);
        startActivity(intent);
    }

    public void handleResultData(Object object) {
        productList = (List<Product>) object;
        productAdapter = new ProductAdapter(WalmartProductActivity.this, productList);
        productAdapter.setClickListener(WalmartProductActivity.this);
        recyclerView.setAdapter(productAdapter);
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
