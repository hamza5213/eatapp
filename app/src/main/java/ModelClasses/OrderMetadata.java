package ModelClasses;

/**
 * Created by hamza on 06-Jul-18.
 */

public class OrderMetadata {
    String foodNames;
    String status;
    long totalBill;
    String riderId;

    public OrderMetadata(String foodNames, String status, long totalBill, String riderId) {
        this.foodNames = foodNames;
        this.status = status;
        this.totalBill = totalBill;
        this.riderId = riderId;
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
}
