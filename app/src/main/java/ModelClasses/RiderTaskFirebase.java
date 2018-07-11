package ModelClasses;

/**
 * Created by hamza on 10-Jul-18.
 */

public class RiderTaskFirebase {
    String clientName;
    String clientId;
    double longitude;
    double latitude;
    double totalBill;
    String url;

    public RiderTaskFirebase() {
    }

    public RiderTaskFirebase(String clientName, String clientId, double longitude, double latitude, double totalBill, String url) {
        this.clientName = clientName;
        this.clientId = clientId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.totalBill = totalBill;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
