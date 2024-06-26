package com.example.app_ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

@Entity(tableName = "cart", primaryKeys = {"id", "userId"})
public class Cart implements Parcelable {
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#.00");

    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private Integer id;
    @NonNull
    @SerializedName("userId")
    @ColumnInfo(name = "userId")
    private Integer userId;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("discountPercentage")
    @ColumnInfo(name = "discountPercentage")
    private Double discountPercentage;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("price")
    @ColumnInfo(name = "price")
    private Double price;

    @SerializedName("rating")
    @ColumnInfo(name = "rating")
    private Double rating;

    @SerializedName("stock")
    @ColumnInfo(name = "stock")
    private Integer stock;

    @SerializedName("brand")
    @ColumnInfo(name = "brand")
    private String brand;

    @SerializedName("tags")
    private String tags;

    @SerializedName("sku")
    private String sku;

    @SerializedName("weight")
    private Integer weight;

    @SerializedName("warrantyInformation")
    private String warrantyInformation;

    @SerializedName("shippingInformation")
    private String shippingInformation;

    @SerializedName("availabilityStatus")
    private String availabilityStatus;

    @SerializedName("returnPolicy")
    private String returnPolicy;

    @SerializedName("minimumOrderQuantity")
    private Integer minimumOrderQuantity;

    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @SerializedName("isAddedToCart")
    @ColumnInfo(name = "isAddedToCart")
    private boolean isAddedToCart;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @SerializedName("category")
    private String category;
    @SerializedName("images")
    @Expose
    private String images;

    // Default constructor
    public Cart() {}

    // Constructor with parameters
    public Cart( @NonNull Integer id,  String title, Double discountPercentage, String description, Double price, Double rating, Integer stock, String brand, String warrantyInformation, String shippingInformation, String returnPolicy, Integer minimumOrderQuantity, String thumbnail, boolean isAddedToCart, int quantity, String category,String images,@NonNull int userId) {
        this.id = id;

        this.title = title;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.price = formatPrice(price);
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.warrantyInformation = warrantyInformation;
        this.shippingInformation = shippingInformation;
        this.returnPolicy = returnPolicy;
        this.minimumOrderQuantity = minimumOrderQuantity;
        this.thumbnail = thumbnail;
        this.isAddedToCart = isAddedToCart;
        this.quantity = quantity;
        this.category = category;
        this.images=images;
         this.userId=userId;
    }
    public Cart( @NonNull Integer id,@NonNull Integer UserId , String title, Double discountPercentage, String description, Double price, Double rating, Integer stock, String brand, String warrantyInformation, String shippingInformation, String returnPolicy, Integer minimumOrderQuantity, String thumbnail, boolean isAddedToCart, int quantity, String category) {
        this.id = id;
        this.userId=UserId;
        this.title = title;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.price = formatPrice(price);
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.warrantyInformation = warrantyInformation;
        this.shippingInformation = shippingInformation;
        this.returnPolicy = returnPolicy;
        this.minimumOrderQuantity = minimumOrderQuantity;
        this.thumbnail = thumbnail;
        this.isAddedToCart = isAddedToCart;
        this.quantity = quantity;
        this.category = category;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(title);
        dest.writeDouble(discountPercentage != null ? discountPercentage : 0);
        dest.writeString(description);
        dest.writeDouble(price != null ? price : 0);
        dest.writeDouble(rating != null ? rating : 0);
        dest.writeInt(stock != null ? stock : 0);
        dest.writeString(brand);
        dest.writeString(tags);
        dest.writeString(sku);
        dest.writeInt(weight != null ? weight : 0);
        dest.writeString(warrantyInformation);
        dest.writeString(shippingInformation);
        dest.writeString(availabilityStatus);
        dest.writeString(returnPolicy);
        dest.writeInt(minimumOrderQuantity != null ? minimumOrderQuantity : 0);
        dest.writeString(thumbnail);
        dest.writeByte((byte) (isAddedToCart ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeString(category);
    }

    protected Cart(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        title = in.readString();
        discountPercentage = in.readDouble();
        description = in.readString();
        price = in.readDouble();
        rating = in.readDouble();
        stock = in.readInt();
        brand = in.readString();
        tags = in.readString();
        sku = in.readString();
        weight = in.readInt();
        warrantyInformation = in.readString();
        shippingInformation = in.readString();
        availabilityStatus = in.readString();
        returnPolicy = in.readString();
        minimumOrderQuantity = in.readInt();
        thumbnail = in.readString();
        isAddedToCart = in.readByte() != 0;
        quantity = in.readInt();
        category = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Cart> CREATOR = new Parcelable.Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = formatPrice(price);
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getWarrantyInformation() {
        return warrantyInformation;
    }

    public void setWarrantyInformation(String warrantyInformation) {
        this.warrantyInformation = warrantyInformation;
    }

    public String getShippingInformation() {
        return shippingInformation;
    }

    public void setShippingInformation(String shippingInformation) {
        this.shippingInformation = shippingInformation;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    public Integer getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(Integer minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private Double formatPrice(Double price) {
        return Double.valueOf(PRICE_FORMAT.format(price));
    }
}
