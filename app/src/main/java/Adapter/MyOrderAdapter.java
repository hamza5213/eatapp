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
import ss.com.bannerslider.Slider;

/**
 * Created by hamza on 06-Jul-18.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private final ArrayList<OrderMetadata> mValues;
    ArrayList<String> ordersIds;
    Context c;
    OnListFragmentInteractionListener mListener;
    boolean ownerFlag=true;

    public MyOrderAdapter(ArrayList<OrderMetadata> items, Context context, OnListFragmentInteractionListener mListener,ArrayList<String>ordersIds,boolean flag) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        this.ordersIds=ordersIds;
        ownerFlag=flag;
    }
    public MyOrderAdapter(ArrayList<OrderMetadata> items, Context context, OnListFragmentInteractionListener mListener,ArrayList<String>ordersIds) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        this.ordersIds=ordersIds;
    }


    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new MyOrderAdapter.ViewHolder(view,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0&&!ownerFlag)
            return R.layout.image_slider;
        return R.layout.my_order_item;
    }

    @Override
    public void onBindViewHolder(final MyOrderAdapter.ViewHolder holder, final int position) {
        if(position==0) {
            if (!ownerFlag) {
                holder.slider.setAdapter(new MainSliderAdapter());
                return;
            }
        }
                holder.orderMetadata = mValues.get(position);
                holder.mOrderId.setText(ordersIds.get(position));
                holder.mOrderPrice.setText("Rs" + String.valueOf(mValues.get(position).getTotalBill()));
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        mListener.onListFragmentInteraction(bundle, "foodItem", true);
                    }
                });
                holder.mOrderStatus.setText(mValues.get(position).getStatus());
                String status = mValues.get(position).getStatus();
                if (status.equals("Pending")) {
                    holder.mOrderStausImage.setImageResource(R.drawable.pending1);
                } else if (status.equals("Confirmed")) {
                    holder.mOrderStausImage.setImageResource(R.drawable.confirmed);
                } else if (status.equals("On The Way")) {
                    holder.mOrderStausImage.setImageResource(R.drawable.on_the_way);
                }
                String address = mValues.get(position).getAddress();
                if (address.length() > 70) {
                    address = address.substring(0, 60) + "...";
                }
                holder.mOrderAddress.setText(address);
                holder.mRiderName.setText(mValues.get(position).getRiderName());



    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final TextView mOrderId;
        public final TextView mOrderPrice;
        public final TextView mOrderStatus;
        public final TextView mOrderAddress;
        public final ImageView mOrderStausImage;
        public final TextView mRiderName;
        public final Slider slider;
        public OrderMetadata orderMetadata;


        public ViewHolder(View view ,int position) {
            super(view);
            mView = view;
            if(position==R.layout.image_slider&&!ownerFlag)
            {
                slider=view.findViewById(R.id.my_orders_image_slider);
                mOrderId =null;
                mOrderPrice = null;
                mOrderStatus = null;
                mOrderAddress=null;
                mOrderStausImage=null;
                mRiderName=null;
            }
            else
            {
                mOrderId = view.findViewById(R.id.my_order_item_orderid);
                mOrderPrice = view.findViewById(R.id.my_order_item_totalBill);
                mOrderStatus = view.findViewById(R.id.my_order_item_status);
                mOrderAddress=view.findViewById(R.id.my_order_item_address);
                mOrderStausImage=view.findViewById(R.id.my_order_item_status_image);
                mRiderName=view.findViewById(R.id.my_order_item_riderName);
                slider=null;
            }

        }
    }


}
