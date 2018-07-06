package ModelClasses;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hamza on 02-Jul-18.
 */
public class FoodItem implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodItemTitle);
        dest.writeLong(this.foodItemPrice);
        dest.writeString(this.foodItemDescription);
        dest.writeString(this.spiceLevel);
        dest.writeString(this.fID);
    }

    protected FoodItem(Parcel in) {
        this.foodItemTitle = in.readString();
        this.foodItemPrice = in.readLong();
        this.foodItemDescription = in.readString();
        this.spiceLevel = in.readString();
        this.fID = in.readString();
    }

    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel source) {
            return new FoodItem(source);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
