package com.example.app_ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.app_ecommerce.model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "favorite", primaryKeys = {"id", "userId"})
public class Favorite implements Parcelable {


    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private Integer id;

    @NonNull
    @SerializedName("userId")
    @Expose
    private Integer userId;
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
    private String tags;

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

    @SerializedName("isChecked")
    @Expose
    @ColumnInfo(name = "isChecked")
    private boolean isChecked;
    @SerializedName("images")
    @Expose
    private String images;

    // Default constructor
    public Favorite() {}

    // Constructor with parameters
    public Favorite(@NonNull Integer id, String title, String description, String category, Double price, Double discountPercentage,
                    Double rating, Integer stock, String brand,
                    String warrantyInformation, String shippingInformation,
                    String returnPolicy, Integer minimumOrderQuantity, String thumbnail, String images,boolean isChecked,@NonNull int userID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.warrantyInformation = warrantyInformation;
        this.shippingInformation = shippingInformation;
        this.returnPolicy = returnPolicy;
        this.minimumOrderQuantity = minimumOrderQuantity;
        this.thumbnail = thumbnail;
        this.images=images;
        this.isChecked = isChecked;
        this.userId=userID;
    }
    public Favorite(@NonNull Integer id,@NonNull Integer UserId, String title, String description, String category, Double price, Double discountPercentage,
                    Double rating, Integer stock, String brand,
                    String warrantyInformation, String shippingInformation,
                    String returnPolicy, Integer minimumOrderQuantity, String thumbnail, String images,boolean isChecked) {
        this.id = id;
        this.userId=UserId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.warrantyInformation = warrantyInformation;
        this.shippingInformation = shippingInformation;
        this.returnPolicy = returnPolicy;
        this.minimumOrderQuantity = minimumOrderQuantity;
        this.thumbnail = thumbnail;
        this.images=images;
        this.isChecked = isChecked;
    }

    protected Favorite(Parcel in) {
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
        description = in.readString();
        category = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            discountPercentage = null;
        } else {
            discountPercentage = in.readDouble();
        }
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        if (in.readByte() == 0) {
            stock = null;
        } else {
            stock = in.readInt();
        }
        tags = in.readString();
        brand = in.readString();
        sku = in.readString();
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = in.readInt();
        }
        warrantyInformation = in.readString();
        shippingInformation = in.readString();
        availabilityStatus = in.readString();
        returnPolicy = in.readString();
        if (in.readByte() == 0) {
            minimumOrderQuantity = null;
        } else {
            minimumOrderQuantity = in.readInt();
        }
        thumbnail = in.readString();
        isChecked = in.readByte() != 0;
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
        dest.writeString(description);
        dest.writeString(category);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (discountPercentage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(discountPercentage);
        }
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        if (stock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stock);
        }
        dest.writeString(tags);
        dest.writeString(brand);
        dest.writeString(sku);
        if (weight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(weight);
        }
        dest.writeString(warrantyInformation);
        dest.writeString(shippingInformation);
        dest.writeString(availabilityStatus);
        dest.writeString(returnPolicy);
        if (minimumOrderQuantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minimumOrderQuantity);
        }
        dest.writeString(thumbnail);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    // Getters and setters


    @NonNull
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Integer userId) {
        this.userId = userId;
    }

    public void setUserID(Integer userID) {
        this.userId = userID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
