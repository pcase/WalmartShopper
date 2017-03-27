package com.azurehorsecreations.walmartshopper;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String PRODUCT = "PRODUCT";
    Product product;
    TextView productId;
    TextView productName;
    TextView shortDescription;
    TextView longDescription;
    TextView price;
    TextView reviewRating;
    TextView reviewCount;
    TextView inStock;
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walmart_product_detail);
        product = getIntent().getParcelableExtra(PRODUCT);
        productId = (TextView) findViewById(R.id.product_Id);
        productName = (TextView) findViewById(R.id.product_name);
        shortDescription = (TextView) findViewById(R.id.short_description);
        longDescription = (TextView) findViewById(R.id.long_description);
        price = (TextView) findViewById(R.id.price);
        reviewRating = (TextView) findViewById(R.id.review_rating);
        reviewCount = (TextView) findViewById(R.id.review_count);
        inStock = (TextView) findViewById(R.id.in_stock);
        productImage = (ImageView) findViewById(R.id.product_image);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            productId.setText((Html.fromHtml(product.getProductId(), Html.FROM_HTML_MODE_LEGACY)));
            productName.setText((Html.fromHtml(product.getProductName(), Html.FROM_HTML_MODE_LEGACY)));
            shortDescription.setText(Html.fromHtml(product.getShortDescription(), Html.FROM_HTML_MODE_LEGACY));
            longDescription.setText(Html.fromHtml(product.getLongDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            if (product.getProductId() != null) {
                productId.setText((Html.fromHtml(product.getProductId())));
            }
            if (product.getProductName() != null) {
                productName.setText((Html.fromHtml(product.getProductName())));
            }
            if (product.getShortDescription() != null) {
                shortDescription.setText(Html.fromHtml(product.getShortDescription()));
            }
            if (product.getLongDescription() != null) {
                longDescription.setText(Html.fromHtml(product.getLongDescription()));
            }
        }
        if (product.getPrice() != null) {
            price.setText(product.getPrice());
        }

        reviewRating.setText(String.valueOf(product.getReviewRating()));
        reviewCount.setText(String.valueOf(product.getReviewCount()));
        if (product.isInStock()) {
            inStock.setText(R.string.yes);
        } else {
            inStock.setText(R.string.no);
        }
        new DownloadImageTask(productImage).execute(product.getProductImage());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            };
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
