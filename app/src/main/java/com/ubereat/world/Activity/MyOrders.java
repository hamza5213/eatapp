package com.ubereat.world.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ubereat.world.R;

import java.util.ArrayList;

import Adapter.MyOrderAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.OrderMetadata;

public class MyOrders extends AppCompatActivity implements OnListFragmentInteractionListener {

    FirebaseDatabase firebaseDatabase;
    ArrayList<OrderMetadata> orderMetadataArrayList;
    MyOrderAdapter adapter;
    ArrayList<String>orderIDs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderIDs=new ArrayList<>();
        orderMetadataArrayList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyOrders.this,FoodDisplayActivity.class));
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyOrderAdapter(orderMetadataArrayList,this, this);
        recyclerView.setAdapter(adapter);
        fetchUserOrdersMetaData();
    }

    void fetchUserOrdersMetaData()
    {
        firebaseDatabase.getReference("OrderMetadata").child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                orderMetadataArrayList.add(dataSnapshot.getValue(OrderMetadata.class));
                orderIDs.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

        int position=details.getInt("position");
        if(orderMetadataArrayList.get(position).equals("On the way"))
        {

        }
        else
        {
            DisplayAlert("Please wait as your Order is in progress");
        }


    }

    void DisplayAlert(String Message)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Not Ready Yet")
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
}
