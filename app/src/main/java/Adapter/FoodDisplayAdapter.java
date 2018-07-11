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
    ArrayList<Boolean> marked;
    boolean ownerFlag;


    public FoodDisplayAdapter(ArrayList<FoodItem> items, Context context, OnListFragmentInteractionListener mListener,boolean ownerFlag) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
        this.ownerFlag=ownerFlag;
    }

    public FoodDisplayAdapter(ArrayList<FoodItem> items, Context context, OnListFragmentInteractionListener mListener,boolean ownerFlag,ArrayList<Boolean>marked) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
        this.ownerFlag=ownerFlag;
        this.marked=marked;
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
        }

        @Override
        void bind(final FoodItem foodItem,  int position) {
            this.foodItem = foodItem;
            final int pos=position;
            mFoodTitle.setText(foodItem.getFoodItemTitle());
            mFoodDescription.setText(foodItem.getFoodItemDescription());
            mFoodPrice.setText("$"+String.valueOf(foodItem.getFoodItemPrice()));
            mFoodAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", pos);
                    if(!marked.get(pos)) {
                        marked.set(pos,true);
                        mFoodAdd.setImageResource(R.drawable.baseline_indeterminate_check_box_black_36dp);

                    }
                    else
                    {
                        marked.set(pos,false);
                        mFoodAdd.setImageResource(R.drawable.ic_add_circle_white_24dp);
                    }
                    mListener.onListFragmentInteraction(bundle, "foodItem", true);
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
            mFoodPrice.setText("$"+String.valueOf(foodItem.getFoodItemPrice()));
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "owner");
                    mListener.onListFragmentInteraction(bundle, "foodItem", true);
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
            StorageReference ref = storage.getReference().child("FoodPic/" + foodItem.getfID() + ".jpg");
            Glide.with(c.getApplicationContext()).load(ref).into(mFoodPicture);

        }
    }


}
