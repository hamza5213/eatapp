package ModelClasses;

import java.util.ArrayList;

/**
 * Created by hamza on 05-Jul-18.
 */

public class Order {
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
}
