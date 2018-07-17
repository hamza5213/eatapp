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

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.Order;
import ModelClasses.OrderDItem;

/**
 * Created by hamza on 04-Jul-18.
 */

public class OrderDisplayAdapter extends RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder>{

    private final ArrayList<OrderDItem> mValues;
            Context c;
            OnListFragmentInteractionListener mListener;
            FirebaseStorage storage;

    public OrderDisplayAdapter(ArrayList<OrderDItem> items,Context context,OnListFragmentInteractionListener mListener){
            mValues=items;
            c=context;
            this.mListener=mListener;
            storage=FirebaseStorage.getInstance();
            }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view= LayoutInflater.from(parent.getContext())
            .inflate(R.layout.order_item,parent,false);
            return new ViewHolder(view);
            }


    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position){
            holder.orderItem=mValues.get(position);
            holder.mFoodTitle.setText(mValues.get(position).getFoodItem().getFoodItemTitle());
            holder.mFoodDescription.setText(mValues.get(position).getFoodItem().getFoodItemDescription());
            holder.mFoodPrice.setText("$"+String.valueOf(mValues.get(position).getFoodItem().getFoodItemPrice()));
            holder.mFoodCount.setText(String.valueOf(mValues.get(position).getQuantity()));
            StorageReference ref=storage.getReference().child("FoodPic/"+mValues.get(position).getFoodItem().getfID()+".jpg");
            Glide.with(c.getApplicationContext()).load(ref).into(holder.mFoodPicture);
            }


    @Override
    public int getItemCount(){
            return mValues.size();
            }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final ImageView mFoodPicture;
        public final TextView mFoodTitle;
        public final TextView mFoodDescription;
        public final TextView mFoodPrice;
        public final TextView mFoodCount;
        public OrderDItem orderItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFoodPicture= view.findViewById(R.id.order_detail_foodImage);
            mFoodTitle = view.findViewById(R.id.order_detail_title);
            mFoodDescription=view.findViewById(R.id.order_detail_description);
            mFoodPrice=view.findViewById(R.id.order_details_price);
            mFoodCount=view.findViewById(R.id.order_detail_quantity);
        }
    }

}
