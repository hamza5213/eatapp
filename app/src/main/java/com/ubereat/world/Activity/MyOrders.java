package com.ubereat.world.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ubereat.world.R;

import java.util.ArrayList;

import Adapter.MyOrderAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.OrderMetadata;
import Utility.PicassoImageLoadingService;
import ss.com.bannerslider.Slider;

public class MyOrders extends AppCompatActivity implements OnListFragmentInteractionListener {

    FirebaseDatabase firebaseDatabase;
    ArrayList<OrderMetadata> orderMetadataArrayList;
    MyOrderAdapter adapter;
    ArrayList<String>orderIDs;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        this.getActionBar().setTitle("Current Orders");
        Slider.init(new PicassoImageLoadingService(this));
        orderIDs=new ArrayList<>();
        orderMetadataArrayList=new ArrayList<>();
        orderIDs.add("12");
        orderMetadataArrayList.add(new OrderMetadata());
        firebaseDatabase=FirebaseDatabase.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyOrders.this,FoodDisplayActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyOrderAdapter(orderMetadataArrayList,this, this,orderIDs,false);
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

                int index=orderIDs.indexOf(dataSnapshot.getKey());
                orderMetadataArrayList.set(index,dataSnapshot.getValue(OrderMetadata.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                int index=orderIDs.indexOf(key);
                orderIDs.remove(index);
                orderMetadataArrayList.remove(index);
                adapter.notifyDataSetChanged();

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
        Intent intent=new Intent(this,OrderDetail.class);
        intent.putExtra("orderId",orderIDs.get(position));
        intent.putExtra("ownerFlag",false);
        intent.putExtra("status",orderMetadataArrayList.get(position).getStatus());
        intent.putExtra("riderId",orderMetadataArrayList.get(position).getRiderId());
        startActivity(intent);

        /*if(orderMetadataArrayList.get(position).getStatus().equals("On The Way"))
        {
            Intent i =new Intent(this,UserOrderTracking.class);
            i.putExtra("riderId",orderMetadataArrayList.get(position).getRiderId());
            startActivity(i);

        }
        else
        {
            DisplayAlert("Please wait as your Order is in progress");
        }*/



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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.main_menu_signOut)
        {

            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("FCM_InstanceID").child(FirebaseAuth.getInstance().getUid());
            dR.removeValue();
            SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            Intent i=new Intent(this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            FirebaseAuth.getInstance().signOut();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
