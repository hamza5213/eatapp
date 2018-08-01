package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;

import ModelClasses.OrderDItem;

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;

/**
 * Created by hamza on 04-Jul-18.
 */

public class FoodDisplayAdapter extends RecyclerView.Adapter<FoodDisplayAdapter.ViewHolder> {

    private final ArrayList<FoodItem> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;
    FirebaseStorage storage;
    ArrayList<OrderDItem> orderList;
    boolean ownerFlag;


    public FoodDisplayAdapter(ArrayList<FoodItem> items, Context context, OnListFragmentInteractionListener mListener,boolean ownerFlag) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
        this.ownerFlag=ownerFlag;
    }

    public FoodDisplayAdapter(ArrayList<FoodItem> items, Context context, OnListFragmentInteractionListener mListener, boolean ownerFlag, ArrayList<OrderDItem>orderList) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
        this.ownerFlag=ownerFlag;
        this.orderList=orderList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        if(ownerFlag)
        {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_food_display_item,parent,false);
            viewHolder=new OwnerViewHolder(view);
        }
        else {
             view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_item, parent, false);
             viewHolder=new ClientViewHolder(view);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bind(mValues.get(position),position);


    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public abstract class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(FoodItem foodItem,int position);

    }

    public class ClientViewHolder extends ViewHolder

    {
        public final View mView;
        public final ImageView mFoodPicture;
        public final TextView mFoodTitle;
        public final TextView mFoodDescription;
        public final TextView mFoodPrice;
        public final ImageView mFoodSpice;
        public final ImageButton mFoodAdd;
        public final ImageButton mFoodMinus;
        public final TextView mFoodQuantity;
        public FoodItem foodItem;


        public ClientViewHolder(View view) {
            super(view);
            mView = view;
            mFoodPicture= view.findViewById(R.id.food_display_Image);
            mFoodPrice = view.findViewById(R.id.food_display_price);
            mFoodTitle = view.findViewById(R.id.food_display_title);
            mFoodDescription=view.findViewById(R.id.food_display_description);
            mFoodSpice=view.findViewById(R.id.food_display_spiceLevel);
            mFoodAdd=view.findViewById(R.id.food_dispaly_add);
            mFoodMinus=view.findViewById(R.id.food_dispaly_minus);
            mFoodQuantity=view.findViewById(R.id.food_display_quantity);
        }

        @Override
        void bind(final FoodItem foodItem,  int position) {
            this.foodItem = foodItem;
            final int pos=position;
            mFoodTitle.setText(foodItem.getFoodItemTitle());
            mFoodDescription.setText(foodItem.getFoodItemDescription());
            mFoodPrice.setText("Rs."+String.valueOf(foodItem.getFoodItemPrice()));
            mFoodAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index=orderList.indexOf(new OrderDItem(pos));
                    if(index==-1)
                    {
                        orderList.add(new OrderDItem(mValues.get(pos),pos,1));
                        mFoodQuantity.setText("1");
                    }
                    else
                    {
                        int temp=orderList.get(index).getQuantity();
                        mFoodQuantity.setText(String.valueOf(temp+1));
                        orderList.get(index).setQuantity(temp+1);
                    }
                }
            });
            mFoodMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index=orderList.indexOf(new OrderDItem(pos));
                    if(index!=-1)
                    {
                        int temp=orderList.get(index).getQuantity();
                        temp--;
                        if(temp<=0)
                        {
                            orderList.remove(index);
                            mFoodQuantity.setText(String.valueOf(0));
                        }
                        else
                        {
                            orderList.get(index).setQuantity(temp);
                            mFoodQuantity.setText(String.valueOf(temp));
                        }
                    }

                }
            });
            String spicelevel=foodItem.getSpiceLevel();
            if(spicelevel.equals("High"))
            {
                mFoodSpice.setImageResource(R.drawable.fire);
            }
            else if(spicelevel.equals("Mild"))
            {
                mFoodSpice.setImageResource(R.drawable.drop);

            }
            else
            {
                mFoodSpice.setImageResource(R.drawable.leaf);
            }
            //holder.mFoodPicture.setImageResource(R.drawable.sandwich);
            StorageReference ref = storage.getReference().child("FoodPic/" + foodItem.getfID() + ".jpg");
            Glide.with(c.getApplicationContext()).load(ref).into(mFoodPicture);

        }

    }

    public class OwnerViewHolder extends ViewHolder
    {
        public final View mView;
        public final ImageView mFoodPicture;
        public final TextView mFoodTitle;
        public final TextView mFoodDescription;
        public final TextView mFoodPrice;
        public final ImageView mFoodSpice;
        public FoodItem foodItem;

        public OwnerViewHolder(View view) {
            super(view);
            mView = view;
            mFoodPicture= view.findViewById(R.id.food_display_Image);
            mFoodPrice = view.findViewById(R.id.food_display_price);
            mFoodTitle = view.findViewById(R.id.food_display_title);
            mFoodDescription=view.findViewById(R.id.food_display_description);
            mFoodSpice=view.findViewById(R.id.food_display_spiceLevel);
        }

        @Override
        void bind(FoodItem foodItem,int position) {

            this.foodItem = foodItem;
            mFoodTitle.setText(foodItem.getFoodItemTitle());
            mFoodDescription.setText(foodItem.getFoodItemDescription());
            mFoodPrice.setText("Rs."+String.valueOf(foodItem.getFoodItemPrice()));
           /* mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "owner");
                    mListener.onListFragmentInteraction(bundle, "foodItem", true);
                }
            });*/
            String spicelevel=foodItem.getSpiceLevel();
            if(spicelevel.equals("High"))
            {
                mFoodSpice.setImageResource(R.drawable.fire);
            }
            else if(spicelevel.equals("Mild"))
            {
                mFoodSpice.setImageResource(R.drawable.drop);

            }
            else
            {
                mFoodSpice.setImageResource(R.drawable.leaf);
            }
            StorageReference ref = storage.getReference().child("FoodPic/" + foodItem.getfID() + ".jpg");
            Glide.with(c.getApplicationContext()).load(ref).into(mFoodPicture);

        }
    }


}
