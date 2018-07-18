package ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hamza on 17-Jul-18.
 */

public class OrderDItem implements Parcelable {
    FoodItem foodItem;
    int position;
    int quantity;

    public OrderDItem(FoodItem foodItem, int position, int quantity) {
        this.foodItem = foodItem;
        this.position = position;
        this.quantity = quantity;
    }

    public OrderDItem() {
    }

    public OrderDItem(int position) {
        this.position = position;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OrderDItem)
        {
            return position==((OrderDItem)obj).position;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.foodItem, flags);
        dest.writeInt(this.position);
        dest.writeInt(this.quantity);
    }

    protected OrderDItem(Parcel in) {
        this.foodItem = in.readParcelable(FoodItem.class.getClassLoader());
        this.position = in.readInt();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<OrderDItem> CREATOR = new Parcelable.Creator<OrderDItem>() {
        @Override
        public OrderDItem createFromParcel(Parcel source) {
            return new OrderDItem(source);
        }

        @Override
        public OrderDItem[] newArray(int size) {
            return new OrderDItem[size];
        }
    };
}
