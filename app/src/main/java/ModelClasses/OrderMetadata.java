package ModelClasses;

/**
 * Created by hamza on 06-Jul-18.
 */

public class OrderMetadata {
    String foodNames;
    String status;
    long totalBill;
    String riderId;
    String riderName;
    String address;

    public OrderMetadata(String foodNames, String status, long totalBill, String riderId, String riderName, String address) {
        this.foodNames = foodNames;
        this.status = status;
        this.totalBill = totalBill;
        this.riderId = riderId;
        this.riderName = riderName;
        this.address = address;
    }

    public OrderMetadata() {
    }

    public String getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(String foodNames) {
        this.foodNames = foodNames;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(long totalBill) {
        this.totalBill = totalBill;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }
}
