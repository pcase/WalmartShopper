package com.azurehorsecreations.walmartshopper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Product> mProductList;
    private Context mContext;

    public ProductAdapter(Context context, List<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.mProductList = products;
        this.mContext = context;
    }

    public Product getItem(int id) {
        return mProductList.get(id);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mProductList.get(position).getProductName();
        holder.productTextView.setText(name);
        Bitmap bitmap = mProductList.get(position).getProductImageBitmap();
        holder.productImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productTextView;
        private ImageView productImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            productTextView = (TextView) itemView.findViewById(R.id.product_text);
            productImageView = (ImageView) itemView.findViewById(R.id.product_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
