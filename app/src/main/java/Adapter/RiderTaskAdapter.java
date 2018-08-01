package Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;

import java.util.ArrayList;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.Notification;
import ModelClasses.OrderMetadata;
import ModelClasses.RiderTask;
import at.markushi.ui.CircleButton;

/**
 * Created by hamza on 10-Jul-18.
 */

public class RiderTaskAdapter extends RecyclerView.Adapter<RiderTaskAdapter.ViewHolder> {

    private final ArrayList<RiderTask> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;

    public RiderTaskAdapter(ArrayList<RiderTask> items,Context context,OnListFragmentInteractionListener mListener){
        mValues=items;
        c=context;
        this.mListener=mListener;
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    @Override
    public RiderTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rider_task_item,parent,false);
        return new RiderTaskAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RiderTaskAdapter.ViewHolder holder, int pos){
        final int position=pos;
        holder.riderTask=mValues.get(position);
        holder.mAddress.setText(mValues.get(position).getAddress());
        holder.mClientName.setText(mValues.get(position).getClientName());
        holder.mTotalBill.setText(String.valueOf(mValues.get(position).getTotalBill()));
        holder.mOrderId.setText(mValues.get(position).getOrderId());
        holder.startRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr="+String.valueOf(mValues.get(position).getLatitude())+","+String.valueOf(mValues.get(position).getLongitude())));
                //intent.setComponent(new ComponentName("com.google.android.apps.maps","com.google.android.maps.MapsActivity"));
                c.startActivity(intent);
            }
        });
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference uoRef= firebaseDatabase.getReference("OrderMetadata").child(mValues.get(position).getClientId()).child(mValues.get(position).getOrderId());
                uoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OrderMetadata orderMetadata=dataSnapshot.getValue(OrderMetadata.class);
                        uoRef.removeValue();
                        firebaseDatabase.getReference("UserOrderHistory").child(mValues.get(position).getClientId()).child(mValues.get(position).getOrderId()).setValue(orderMetadata);
                        firebaseDatabase.getReference("OwnerOrders").child("OnTheWay").child(mValues.get(position).getOrderId()).removeValue();
                        firebaseDatabase.getReference("OwnerOrders").child("History").child(mValues.get(position).getOrderId()).setValue(mValues.get(position).getClientId());
                        firebaseDatabase.getReference("Notification").push().setValue(new Notification(mValues.get(position).getClientId(),"Order Received","Rider has Deliver Your Order"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        public final CircleButton startRide;
        public final CircleButton done;
        RiderTask riderTask;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderId=view.findViewById(R.id.rider_task_item_orderId);
            mClientName=view.findViewById(R.id.rider_task_item_name);
            mAddress=view.findViewById(R.id.rider_task_item_address);
            mTotalBill=view.findViewById(R.id.rider_task_item_total_bill);
            startRide=view.findViewById(R.id.rider_task_start_ride);
            done=view.findViewById(R.id.rider_task_done);
        }
    }

}
