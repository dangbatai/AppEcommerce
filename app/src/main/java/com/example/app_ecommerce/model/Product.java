package com.example.app_ecommerce.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app_ecommerce.BR;
import com.example.app_ecommerce.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product extends BaseObservable implements Serializable  {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("discountPercentage")
    @Expose
    private Double discountPercentage;

    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("stock")
    @Expose
    private Integer stock;

    @SerializedName("tags")
    @Expose
    private List<String> tags;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("weight")
    @Expose
    private Integer weight;

    @SerializedName("warrantyInformation")
    @Expose
    private String warrantyInformation;

    @SerializedName("shippingInformation")
    @Expose
    private String shippingInformation;

    @SerializedName("availabilityStatus")
    @Expose
    private String availabilityStatus;

    @SerializedName("returnPolicy")
    @Expose
    private String returnPolicy;

    @SerializedName("minimumOrderQuantity")
    @Expose
    private Integer minimumOrderQuantity;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public Product() {
    }

    public Product(Integer id, String title, String description, Double price, Double discountPercentage, Double rating, Integer stock, String brand, String category, String thumbnail, String warrantyInformation, String shippingInformation, String availabilityStatus, String returnPolicy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.thumbnail = thumbnail;
        this.warrantyInformation = warrantyInformation;
        this.shippingInformation = shippingInformation;
        this.availabilityStatus = availabilityStatus;
        this.returnPolicy = returnPolicy;
    }

    @BindingAdapter({"thumbnail"})
    public static void loadThumbnail(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.star)
                            .error(R.drawable.star))
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.star);
        }
    }

    @SerializedName("images")
    @Expose
    private List<String> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Bindable
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Bindable
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
        notifyPropertyChanged(BR.brand);
    }

    @Bindable
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Bindable
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String getWarrantyInformation() {
        return warrantyInformation;
    }

    public void setWarrantyInformation(String warrantyInformation) {
        this.warrantyInformation = warrantyInformation;
        notifyPropertyChanged(BR.warrantyInformation);
    }

    @Bindable
    public String getShippingInformation() {
        return shippingInformation;
    }

    public void setShippingInformation(String shippingInformation) {
        this.shippingInformation = shippingInformation;
        notifyPropertyChanged(BR.shippingInformation);
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Bindable
    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
        notifyPropertyChanged(BR.returnPolicy);
    }

    public Integer getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(Integer minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }
}
