package ModelClasses;


import org.parceler.Parcel;

/**
 * Created by hamza on 02-Jul-18.
 */
@Parcel
public class FoodItem {
    String foodItemTitle;
    long foodItemPrice;
    String foodItemDescription;
    String spiceLevel;
    String fID;

    public FoodItem() {
    }

    public FoodItem(String foodItemTitle, long foodItemPrice, String foodItemDescription, String spiceLevel, String fID) {
        this.foodItemTitle = foodItemTitle;
        this.foodItemPrice = foodItemPrice;
        this.foodItemDescription = foodItemDescription;
        this.spiceLevel = spiceLevel;
        this.fID = fID;
    }

    public String getFoodItemTitle() {
        return foodItemTitle;
    }

    public void setFoodItemTitle(String foodItemTitle) {
        this.foodItemTitle = foodItemTitle;
    }

    public long getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(long foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public String getFoodItemDescription() {
        return foodItemDescription;
    }

    public void setFoodItemDescription(String foodItemDescription) {
        this.foodItemDescription = foodItemDescription;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }
}
