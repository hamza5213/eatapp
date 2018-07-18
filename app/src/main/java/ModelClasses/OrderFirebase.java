package ModelClasses;

import java.util.ArrayList;

/**
 * Created by hamza on 05-Jul-18.
 */

public class OrderFirebase {
    ArrayList<OrderDItem> orderDItems;
    double totalBill;
    String address;
    double longitude;
    double latitude;
    String url;

    public OrderFirebase(ArrayList<OrderDItem> orderDItems, double totalBill, String address, double longitude, double latitude, String url) {
        this.orderDItems = orderDItems;
        this.totalBill = totalBill;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.url = url;
    }

    public OrderFirebase() {
    }

    public ArrayList<OrderDItem> getOrderDItems() {
        return orderDItems;
    }

    public void setOrderDItems(ArrayList<OrderDItem> orderDItems) {
        this.orderDItems = orderDItems;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
