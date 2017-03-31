package com.azurehorsecreations.walmartshopper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.InputStream;

/*
 * ProductDetailFragment is a fragment for the product detail page
 */

public class ProductDetailFragment extends Fragment {
    private static final String TAG = "ProductDetFrag";
    private Product product;
    private TextView productId;
    private TextView productName;
    private TextView shortDescription;
    private TextView longDescription;
    private TextView price;
    private TextView reviewRating;
    private TextView reviewCount;
    private TextView inStock;
    private ImageView productImage;
    private ProgressBar progressBar;

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragmentFirst = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString("productId", product.getProductId());
        args.putString("productName", product.getProductName());
        args.putString("shortDescription", product.getShortDescription());
        args.putString("longDescription", product.getLongDescription());
        args.putString("price", product.getPrice());
        args.putString("productImage", product.getProductImage());
        args.putDouble("reviewRating", product.getReviewRating());
        args.putInt("reviewCount", product.getReviewCount());
        args.putInt("inStock", product.isInStock() ? 1 : 0);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = new Product();
        product.setProductId(getArguments().getString("productId", ""));
        product.setProductName(getArguments().getString("productName", ""));
        product.setShortDescription(getArguments().getString("shortDescription", ""));
        product.setLongDescription(getArguments().getString("longDescription", ""));
        product.setPrice(getArguments().getString("price", ""));
        product.setProductImage(getArguments().getString("productImage", ""));
        product.setReviewRating(getArguments().getDouble("reviewRating", 0));
        product.setReviewCount(getArguments().getInt("reviewCount", 0));
        product.setInStock(getArguments().getInt("inStock", 0) == 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_product_detail, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        productId = (TextView) view.findViewById(R.id.product_Id);
        productName = (TextView) view.findViewById(R.id.product_name);
        shortDescription = (TextView) view.findViewById(R.id.short_description);
        longDescription = (TextView) view.findViewById(R.id.long_description);
        price = (TextView) view.findViewById(R.id.price);
        reviewRating = (TextView) view.findViewById(R.id.review_rating);
        reviewCount = (TextView) view.findViewById(R.id.review_count);
        inStock = (TextView) view.findViewById(R.id.in_stock);
        productImage = (ImageView) view.findViewById(R.id.product_image);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (product.getProductId() != null) {
                productId.setText((Html.fromHtml(product.getProductId(), Html.FROM_HTML_MODE_LEGACY)));
            }
            if (product.getProductName() != null) {
                productName.setText((Html.fromHtml(product.getProductName(), Html.FROM_HTML_MODE_LEGACY)));
            }
            if (product.getShortDescription() != null) {
                shortDescription.setText(Html.fromHtml(product.getShortDescription(), Html.FROM_HTML_MODE_LEGACY));
            }
            if (product.getLongDescription() != null) {
                longDescription.setText(Html.fromHtml(product.getLongDescription(), Html.FROM_HTML_MODE_LEGACY));
            }
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
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e(TAG + ": Error", e.getMessage());
                e.printStackTrace();
            };
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(View.INVISIBLE);
            bmImage.setImageBitmap(result);
        }
    }
}
