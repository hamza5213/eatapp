package com.ubereat.world.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.OrderDetailAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.Order;
import ModelClasses.OrderFirebase;
import ModelClasses.RiderTaskFirebase;
import ModelClasses.User;
import ModelClasses.UserProfile;

public class OrderDetail extends AppCompatActivity implements OnListFragmentInteractionListener {

    Order order;
    final String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    FirebaseDatabase firebaseDatabase;
    TextView address;
    TextView clientName;
    TextView dateTime;
    TextView totalBill;
    TextView description;
    Spinner riderSpinner;
    OrderDisplayAdapter adapter;
    ArrayList<User>riders;
    ArrayAdapter<User>riderAdapter;
    User client;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderId=getIntent().getStringExtra("orderId");
        firebaseDatabase=FirebaseDatabase.getInstance();
        description=findViewById(R.id.order_detail_description);
        totalBill=findViewById(R.id.order_detail_TotalBill);
        dateTime=findViewById(R.id.order_detail_dateTime);
        clientName=findViewById(R.id.order_detail_UserName);
        address=findViewById(R.id.order_detail_address);
        riders=new ArrayList<>();
        riderSpinner=findViewById(R.id.order_details_rider_spinner);
        riderAdapter=new ArrayAdapter<User>(this, android.R.layout.simple_spinner_dropdown_item, riders);
        riderSpinner.setAdapter(riderAdapter);
        fetchOrder(orderId);


    }

    void fetchOrder(String key)
    {
        firebaseDatabase.getReference("Orders").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final OrderFirebase orderFirebase=dataSnapshot.getValue(OrderFirebase.class);
                AndroidNetworking.get(orderFirebase.getUrl())
                        .addHeaders("Authorization", authToken)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray bugs=response.getJSONArray("bugs");
                                    JSONObject object=bugs.getJSONObject(0);
                                    String description=object.getString("description");
                                    String uId=object.getString("title");
                                    uId=uId.substring(19);
                                    String dateTime=object.getString("created_time_format");
                                    order=new Order(orderFirebase.getFoodItems(),orderFirebase.getTotalBill(),orderFirebase.getLongitude(),orderFirebase.getLatitude(),uId,description,dateTime);
                                    fetchClient(uId);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void updateUI()
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_deatils_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDisplayAdapter(order.getFoodItems(),this, this);
        recyclerView.setAdapter(adapter);
        dateTime.setText(order.getTime());
        totalBill.setText("$"+String.valueOf(order.getTotalBill()));
        description.setText(order.getDescription());
        clientName.setText(client.getName());
        fetchRiders();
    }
    void fetchClient(final String uId)
    {
        firebaseDatabase.getReference("User").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                client=new User(uId,userProfile.getPhoneNumber(),userProfile.getName());
                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void fetchRiders()
    {
        firebaseDatabase.getReference("Riders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    if(snapshot.getValue(Boolean.class)) {

                        fetchRiderData(snapshot.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void fetchRiderData(String key)
    {
        firebaseDatabase.getReference().child("User").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                User user=new User(dataSnapshot.getKey(),userProfile.getPhoneNumber(),userProfile.getName());
                riders.add(user);
                riderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }
    public void onRiderSelected(View view)
    {
        AndroidNetworking.post("https://projectsapi.zoho.com/restapi/portal/tlxdml/projects/1265026000000020206/bugs/")
                .addHeaders("Authorization", authToken)
                .addBodyParameter("title",orderId)
                .addBodyParameter("description","129 Mall Road")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray bugs=response.getJSONArray("bugs");
                            JSONObject object=bugs.getJSONObject(0);
                            JSONObject link=object.getJSONObject("link");
                            JSONObject self=link.getJSONObject("self");
                            String url=self.getString("url");
                            String key=object.getString("id");
                            RiderTaskFirebase riderTaskFirebase =new RiderTaskFirebase(client.getName(),client.getUserId(),order.getLongitude(),order.getLatitude(),order.getTotalBill(),url);
                            firebaseDatabase.getReference("RiderTasks").child(((User)riderSpinner.getSelectedItem()).getUserId()).child(key).setValue(riderTaskFirebase);
                            firebaseDatabase.getReference("OwnerOrders").child("Pending").child(orderId).removeValue();
                            firebaseDatabase.getReference("OwnerOrders").child("OnTheWay").child(orderId).setValue(client.getUserId());
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("error");
                    }
                });

    }

}
