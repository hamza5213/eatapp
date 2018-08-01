package ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hamza on 05-Jul-18.
 */

public class Order implements Parcelable {
    ArrayList<OrderDItem> orderItems;
    double totalBill;
    double longitude;
    double latitude;
    String uid;
    String description;
    String time;
    String address;

    public Order(ArrayList<OrderDItem> orderItems, double totalBill, double longitude, double latitude, String uid, String description, String time, String address) {
        this.orderItems = orderItems;
        this.totalBill = totalBill;
        this.longitude = longitude;
        this.latitude = latitude;
        this.uid = uid;
        this.description = description;
        this.time = time;
        this.address = address;
    }

    public ArrayList<OrderDItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderDItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.orderItems);
        dest.writeDouble(this.totalBill);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.uid);
        dest.writeString(this.description);
        dest.writeString(this.time);
        dest.writeString(this.address);
    }

    protected Order(Parcel in) {
        this.orderItems = in.createTypedArrayList(OrderDItem.CREATOR);
        this.totalBill = in.readDouble();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.uid = in.readString();
        this.description = in.readString();
        this.time = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
