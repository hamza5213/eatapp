package com.ubereat.world.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.w3c.dom.Text;

import Adapter.OrderDetailAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.Order;
import ModelClasses.OrderFirebase;

public class OrderDetail extends AppCompatActivity implements OnListFragmentInteractionListener {

    Order order;
    final String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    FirebaseDatabase firebaseDatabase;
    TextView address;
    TextView clientName;
    TextView dateTime;
    TextView totalBill;
    TextView description;
    OrderDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        String orderId=getIntent().getStringExtra("orderId");
        fetchOrder(orderId);
        firebaseDatabase=FirebaseDatabase.getInstance();
        description=findViewById(R.id.order_detail_description);
        totalBill=findViewById(R.id.order_detail_TotalBill);
        dateTime=findViewById(R.id.order_detail_dateTime);
        clientName=findViewById(R.id.order_detail_UserName);
        address=findViewById(R.id.order_detail_address);

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
                                    updateUI();
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.display_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(order.getFoodItems(),this, this);
        recyclerView.setAdapter(adapter);
        dateTime.setText(order.getTime());
        totalBill.setText("$"+String.valueOf(order.getTotalBill()));
        description.setText(order.getDescription());
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }
}
