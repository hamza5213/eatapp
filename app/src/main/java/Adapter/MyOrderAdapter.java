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

import org.w3c.dom.Text;

import java.util.ArrayList;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.OrderMetadata;

/**
 * Created by hamza on 06-Jul-18.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private final ArrayList<OrderMetadata> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;

    public MyOrderAdapter(ArrayList<OrderMetadata> items, Context context, OnListFragmentInteractionListener mListener) {
        mValues = items;
        c = context;
        this.mListener = mListener;
    }

    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_item, parent, false);
        return new MyOrderAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyOrderAdapter.ViewHolder holder, final int position) {
        holder.orderMetadata = mValues.get(position);
        holder.mOrderTitle.setText(mValues.get(position).getFoodNames());
        holder.mOrderPrice.setText("$"+String.valueOf(mValues.get(position).getTotalBill()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                mListener.onListFragmentInteraction(bundle, "foodItem", true);
            }
        });
        holder.mOrderStaus.setText(mValues.get(position).getStatus());
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final TextView mOrderTitle;
        public final TextView mOrderPrice;
        public final TextView mOrderStaus;
        public OrderMetadata orderMetadata;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderTitle = view.findViewById(R.id.my_order_item_title);
            mOrderPrice = view.findViewById(R.id.my_order_bill);
            mOrderStaus = view.findViewById(R.id.my_order_status);

        }
    }


}
