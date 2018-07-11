package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;

import java.util.ArrayList;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.RiderTask;

/**
 * Created by hamza on 10-Jul-18.
 */

public class RiderTaskAdapter extends RecyclerView.Adapter<RiderTaskAdapter.ViewHolder> {

    private final ArrayList<RiderTask> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;

    public RiderTaskAdapter(ArrayList<RiderTask> items,Context context,OnListFragmentInteractionListener mListener){
        mValues=items;
        c=context;
        this.mListener=mListener;
    }

    @Override
    public RiderTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rider_task_item,parent,false);
        return new RiderTaskAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RiderTaskAdapter.ViewHolder holder, final int position){
        holder.riderTask=mValues.get(position);
        holder.mAddress.setText(mValues.get(position).getAddress());
        holder.mClientName.setText(mValues.get(position).getClientName());
        holder.mTotalBill.setText(String.valueOf(mValues.get(position).getTotalBill()));
        holder.mOrderId.setText(mValues.get(position).getOrderId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                mListener.onListFragmentInteraction(bundle, "foodItem", true);
            }
        });
    }


    @Override
    public int getItemCount(){
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final TextView mOrderId;
        public final TextView mClientName;
        public final TextView mAddress;
        public final TextView mTotalBill;
        RiderTask riderTask;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderId=view.findViewById(R.id.rider_task_item_orderId);
            mClientName=view.findViewById(R.id.rider_task_item_name);
            mAddress=view.findViewById(R.id.rider_task_item_address);
            mTotalBill=view.findViewById(R.id.rider_task_item_total_bill);
        }
    }

}
