package com.azurehorsecreations.walmartshopper;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String productId;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private String price;
    private String productImage;
    private double reviewRating;
    private int reviewCount;
    private boolean inStock;
    private Bitmap productImageBitmap;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Bitmap getProductImageBitmap() {
        return productImageBitmap;
    }

    public void setProductImageBitmap(Bitmap productImageBitmap) {
        this.productImageBitmap = productImageBitmap;
    }

    private Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        price = in.readString();
        productImage = in.readString();
        reviewRating = in.readDouble();
        reviewCount = in.readInt();
        inStock = in.readInt() == 1;
    }

    public Product() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(productId);
        out.writeString(productName);
        out.writeString(shortDescription);
        out.writeString(longDescription);
        out.writeString(price);
        out.writeString(productImage);
        out.writeDouble(reviewRating);
        out.writeInt(reviewCount);
        out.writeInt(inStock ? 1 : 0);
    }

    public static final Creator<Product> CREATOR
            = new Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
