package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Interfaces.OnListFragmentInteractionListener;
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
            .inflate(R.layout.order_detail_item,parent,false);
            return new ViewHolder(view);
            }


    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position){
            holder.orderItem=mValues.get(position);
            holder.mFoodTitle.setText(mValues.get(position).getFoodItem().getFoodItemTitle());
            holder.mFoodDescription.setText(mValues.get(position).getFoodItem().getFoodItemDescription());
            holder.mFoodPrice.setText("Rs. "+String.valueOf(mValues.get(position).getFoodItem().getFoodItemPrice()));
            holder.mFoodCount.setText(String.valueOf(mValues.get(position).getQuantity())+"x");
            holder.mTotalPrice.setText("Rs. "+String.valueOf(mValues.get(position).getQuantity()*mValues.get(position).getFoodItem().getFoodItemPrice()));
            }


    @Override
    public int getItemCount(){
            return mValues.size();
            }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final TextView mFoodTitle;
        public final TextView mFoodDescription;
        public final TextView mFoodPrice;
        public final TextView mTotalPrice;
        public final TextView mFoodCount;
        public OrderDItem orderItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFoodTitle = view.findViewById(R.id.order_detail_item_title);
            mFoodDescription=view.findViewById(R.id.order_detail_item_description);
            mFoodPrice=view.findViewById(R.id.order_detail_item_price);
            mFoodCount=view.findViewById(R.id.order_detail_item_quantity);
            mTotalPrice=view.findViewById(R.id.order_detail_item_totalPrice);
        }
    }

}
