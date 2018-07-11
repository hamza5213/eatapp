package ModelClasses;

/**
 * Created by hamza on 10-Jul-18.
 */

public class RiderTask {
    String clientName;
    String clientId;
    double longitude;
    double latitude;
    double totalBill;
    String url;
    String address;
    String orderId;

    public RiderTask(String clientName, String clientId, double longitude, double latitude, double totalBill, String url, String address, String orderId) {
        this.clientName = clientName;
        this.clientId = clientId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.totalBill = totalBill;
        this.url = url;
        this.address = address;
        this.orderId = orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
