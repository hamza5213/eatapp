package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.Order;
import ModelClasses.RiderTask;
import ModelClasses.RiderTaskFirebase;
import Services.RiderLocation;

public class RiderMainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<RiderTask> riderTasks;
    FirebaseDatabase firebaseDatabase;
    final String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);
        Intent locationService=new Intent(this, RiderLocation.class);
        startService(locationService);
        fetchRiderTask();
    }

    void fetchRiderTask()
    {
        firebaseDatabase.getReference("RiderTasks").child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final RiderTaskFirebase riderTaskFirebase=dataSnapshot.getValue(RiderTaskFirebase.class);
                AndroidNetworking.get(riderTaskFirebase.getUrl())
                        .addHeaders("Authorization", authToken)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray bugs=response.getJSONArray("bugs");
                                    JSONObject object=bugs.getJSONObject(0);
                                    String address=object.getString("description");
                                    String orderId=object.getString("title");
                                    RiderTask riderTask;
                                    riderTasks.add(new RiderTask(riderTaskFirebase.getClientName(),riderTaskFirebase.getClientId(),riderTaskFirebase.getLongitude(),riderTaskFirebase.getLatitude(),riderTaskFirebase.getTotalBill(),riderTaskFirebase.getUrl(),address,orderId) );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
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
        DatabaseReference orderRef= firebaseDatabase.getReference("OrderMetadata").child(riderTasks.get(position).getClientId()).child(riderTasks.get(position).getOrderId());
        orderRef.child("status").setValue("On The Way");
        orderRef.child("riderId").setValue(FirebaseAuth.getInstance().getUid());
    }
}
