package ModelClasses;

/**
 * Created by hamza on 02-Jul-18.
 */

public class FoodItemFirebase {
    long price;
    String spiceLevel;
    String url;

    public FoodItemFirebase() {
    }

    public FoodItemFirebase(long price, String spiceLevel, String url) {
        this.price = price;
        this.spiceLevel = spiceLevel;
        this.url = url;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
