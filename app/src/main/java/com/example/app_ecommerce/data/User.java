package com.example.app_ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private Integer id;

    @SerializedName("fname")
    @ColumnInfo(name = "fname")
    private String fname;

    @SerializedName("lname")
    @ColumnInfo(name = "lname")
    private String lname;

    @SerializedName("mobile")
    @ColumnInfo(name = "mobile")
    private String mobile;

    @SerializedName("email")
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("password")
    @ColumnInfo(name = "password")
    private String password;

    @SerializedName("area")
    @ColumnInfo(name = "area")
    private String area;

    @SerializedName("address")
    @ColumnInfo(name = "address")
    private String address;

    @SerializedName("state")
    @ColumnInfo(name = "state")
    private String state;

    @SerializedName("city")
    @ColumnInfo(name = "city")
    private String city;

    @SerializedName("images")
    @ColumnInfo(name = "images")
    private String images;

    @SerializedName("token")
    @ColumnInfo(name = "token")
    private String token;

    @SerializedName("reset_code")
    @ColumnInfo(name = "reset_code")
    private String reset_code;

    @SerializedName("firebase_token")
    @ColumnInfo(name = "firebase_token")
    private String firebase_token;

    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    private String gender;

    // Default constructor
    public User() {}

    // Constructor with parameters
    public User(Integer id, String fname, String lname, String mobile, String email, String password,
                String area, String address, String state, String city, String images, String token,
                String reset_code, String firebase_token, String gender) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.area = area;
        this.address = address;
        this.state = state;
        this.city = city;
        this.images = images;
        this.token = token;
        this.reset_code = reset_code;
        this.firebase_token = firebase_token;
        this.gender = gender;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(area);
        dest.writeString(address);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(images);
        dest.writeString(token);
        dest.writeString(reset_code);
        dest.writeString(firebase_token);
        dest.writeString(gender);
    }

    protected User(Parcel in) {
        id = (Integer) in.readValue(Integer.class.getClassLoader());
        fname = in.readString();
        lname = in.readString();
        mobile = in.readString();
        email = in.readString();
        password = in.readString();
        area = in.readString();
        address = in.readString();
        state = in.readString();
        city = in.readString();
        images = in.readString();
        token = in.readString();
        reset_code = in.readString();
        firebase_token = in.readString();
        gender = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getReset_code() {
        return reset_code;
    }

    public void setReset_code(String reset_code) {
        this.reset_code = reset_code;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
