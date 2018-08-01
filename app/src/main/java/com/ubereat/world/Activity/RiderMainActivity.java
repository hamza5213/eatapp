package com.ubereat.world.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

import Adapter.OrderDisplayAdapter;
import Adapter.RiderTaskAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.RiderTask;
import ModelClasses.RiderTaskFirebase;
import com.ubereat.world.Activity.Services.RiderService;

public class RiderMainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<RiderTask> riderTasks;
    FirebaseDatabase firebaseDatabase;
    final String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    RiderTaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);
        Intent locationService=new Intent(this, RiderService.class);
        startService(locationService);
        firebaseDatabase=FirebaseDatabase.getInstance();
        riderTasks=new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rider_task_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RiderTaskAdapter(riderTasks,this, this);
        recyclerView.setAdapter(adapter);
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
                                    adapter.notifyDataSetChanged();
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

                RiderTask riderTask=dataSnapshot.getValue(RiderTask.class);
                int index=riderTasks.indexOf(riderTask);
                riderTasks.remove(index);
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
       // DatabaseReference orderRef= firebaseDatabase.getReference("OrderMetadata").child(riderTasks.get(position).getClientId()).child(riderTasks.get(position).getOrderId());
       // orderRef.child("status").setValue("On The Way");
        //orderRef.child("riderId").setValue(FirebaseAuth.getInstance().getUid());
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?&daddr="+String.valueOf(riderTasks.get(position).getLatitude())+","+String.valueOf(riderTasks.get(position).getLongitude())));
        //intent.setComponent(new ComponentName("com.google.android.apps.maps","com.google.android.maps.MapsActivity"));
        startActivity(intent);
    }

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
