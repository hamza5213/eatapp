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
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;
import Utility.MyAppGlideModule;
import java.util.ArrayList;

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

    public FoodDisplayAdapter(ArrayList<FoodItem> items, Context context, OnListFragmentInteractionListener mListener) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.foodItem = mValues.get(position);
        holder.mFoodTitle.setText(mValues.get(position).getFoodItemTitle());
        holder.mFoodDescription.setText(mValues.get(position).getFoodItemDescription());
        holder.mFoodPrice.setText("$"+String.valueOf(mValues.get(position).getFoodItemPrice()));
        holder.mFoodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("FoodItem", mValues.get(position));
                mListener.onListFragmentInteraction(bundle, "foodItem", true);
            }
        });
        String spicelevel=mValues.get(position).getSpiceLevel();
        if(spicelevel.equals("High"))
        {
            holder.mFoodSpice.setImageResource(R.drawable.fire);
        }
        else if(spicelevel.equals("Mild"))
        {
            holder.mFoodSpice.setImageResource(R.drawable.drop);

        }
        else
        {
            holder.mFoodSpice.setImageResource(R.drawable.leaf);
        }
        //holder.mFoodPicture.setImageResource(R.drawable.sandwich);
        StorageReference ref = storage.getReference().child("FoodPic/" + mValues.get(position).getfID() + ".jpg");
        Glide.with(c.getApplicationContext()).load(ref).into(holder.mFoodPicture);


    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final ImageView mFoodPicture;
        public final TextView mFoodTitle;
        public final TextView mFoodDescription;
        public final TextView mFoodPrice;
        public final ImageView mFoodSpice;
        public final ImageButton mFoodAdd;
        public FoodItem foodItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFoodPicture= view.findViewById(R.id.food_display_Image);
            mFoodPrice = view.findViewById(R.id.food_display_price);
            mFoodTitle = view.findViewById(R.id.food_display_title);
            mFoodDescription=view.findViewById(R.id.food_display_description);
            mFoodSpice=view.findViewById(R.id.food_display_spiceLevel);
            mFoodAdd=view.findViewById(R.id.food_dispaly_add);
        }

    }
}
