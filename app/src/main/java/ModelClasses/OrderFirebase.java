package ModelClasses;

import java.util.ArrayList;

/**
 * Created by hamza on 05-Jul-18.
 */

public class OrderFirebase {
    ArrayList<FoodItem> foodItems;
    double totalBill;
    double longitude;
    double latitude;
    String url;

    public OrderFirebase(ArrayList<FoodItem> foodItems, double totalBill, double longitude, double latitude, String url) {
        this.foodItems = foodItems;
        this.totalBill = totalBill;
        this.longitude = longitude;
        this.latitude = latitude;
        this.url = url;
    }

    public OrderFirebase() {
    }

    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
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
}
