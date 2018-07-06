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

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;

/**
 * Created by hamza on 04-Jul-18.
 */

public class OrderDisplayAdapter extends RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder>{

    private final ArrayList<FoodItem> mValues;
            Context c;
            OnListFragmentInteractionListener mListener;
            FirebaseStorage storage;

    public OrderDisplayAdapter(ArrayList<FoodItem> items,Context context,OnListFragmentInteractionListener mListener){
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
            holder.foodItem=mValues.get(position);
            holder.mFoodTitle.setText(mValues.get(position).getFoodItemTitle());
            holder.mFoodDescription.setText(mValues.get(position).getFoodItemDescription());
            StorageReference ref=storage.getReference().child("FoodPic/"+mValues.get(position).getfID()+".jpg");
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
        public FoodItem foodItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFoodPicture= view.findViewById(R.id.order_detail_foodImage);
            mFoodTitle = view.findViewById(R.id.order_detail_title);
            mFoodDescription=view.findViewById(R.id.order_detail_description);
        }
    }

}
