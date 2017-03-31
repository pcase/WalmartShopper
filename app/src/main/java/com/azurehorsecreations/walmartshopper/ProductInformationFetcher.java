package com.azurehorsecreations.walmartshopper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/*
 * ProductInformationFetcher downloads a page of product information
 */

public class ProductInformationFetcher extends AsyncTask<Void, Void, List<Product>> {
    private static final String TAG = "ProductInfoFetcher";
    private static final String API_KEY = "e0a4274f-45b6-405b-839e-1096222be4fc";
    private static final String BASE_URL = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1";
    private static final String WALMART_PRODUCTS = "walmartproducts";
    private static final String PAGE_SIZE = "30";
    private Context mContext;
    private ProgressBar progressBar;
    private int mPageNumber;

    public ProductInformationFetcher(Context context) {
        mContext = context;
        mPageNumber = 1;
    }

    public ProductInformationFetcher(Context context, int pageNumber) {
        mContext = context;
        mPageNumber = pageNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = (ProgressBar) ((Activity) mContext).findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Product> doInBackground(Void... params) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String productJsonStr = null;

        try {
            URL url = new URL(BASE_URL + "/" + WALMART_PRODUCTS + "/" + API_KEY + "/" + mPageNumber + "/" + PAGE_SIZE);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            productJsonStr = buffer.toString();
            List<Product> productList = parseResultData(productJsonStr);
            inputStream.close();

            for (Product product : productList) {
                try {
                    InputStream in = new java.net.URL(product.getProductImage()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    product.setProductImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e(TAG + ":" + "Error", e.getMessage());
                    e.printStackTrace();
                }
            }

            return productList;
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(List<Product> result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);
        ((CallbackReceiver)mContext).handleResultData(result);
    }

    private List<Product> parseResultData(String result) {
        List<Product> productList = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray jArray = jObject.getJSONArray("products");
            for (int i=0; i < jArray.length(); i++) {
                try {
                    Product product = new Product();
                    JSONObject item = jArray.getJSONObject(i);
                    if (item.has("productId")) {
                        product.setProductId(item.getString("productId"));
                    }
                    if (item.has("productName")) {
                        product.setProductName(item.getString("productName"));
                    }
                    if (item.has("shortDescription")) {
                        product.setShortDescription(item.getString("shortDescription"));
                    }
                    if (item.has("longDescription")) {
                        product.setLongDescription(item.getString("longDescription"));
                    }
                    if (item.has("price")) {
                        product.setPrice(item.getString("price"));
                    }
                    if (item.has("productImage")) {
                        product.setProductImage(item.getString("productImage"));
                    }
                    if (item.has("reviewRating")) {
                        product.setReviewRating(item.getDouble("reviewRating"));
                    }
                    if (item.has("reviewCount")) {
                        product.setReviewCount(item.getInt("reviewCount"));
                    }
                    if (item.has("inStock")) {
                        product.setInStock(item.getBoolean("inStock"));
                    }
                    productList.add(product);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return productList;
    }
}


