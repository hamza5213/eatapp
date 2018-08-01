package ModelClasses;

/**
 * Created by hamza on 26-Jul-18.
 */

public class Notification {
    String reciverId;
    String status;
    String message;

    public Notification() {
    }

    public Notification(String reciverId, String status, String message) {
        this.reciverId = reciverId;
        this.status = status;
        this.message = message;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
