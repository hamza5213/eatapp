package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.FoodDisplayAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.OrderDItem;
import ModelClasses.OrderFirebase;
import ModelClasses.OrderMetadata;

public class DisplayOrder extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<OrderDItem>orderItems;
    OrderDisplayAdapter adapter;
    EditText instructions;
    TextView totalBillView;
    static int PLACE_PICKER_REQUEST = 1;
    long totalBill;
    String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    TextView location;
    LatLng loc;
    ImageButton addLocation;
    FirebaseDatabase firebaseDatabase;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);
        orderItems=getIntent().getParcelableArrayListExtra("orderList");
        location=findViewById(R.id.order_display_location);
        addLocation=findViewById(R.id.order_display_addLocation);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.display_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDisplayAdapter(orderItems,this, this);
        recyclerView.setAdapter(adapter);
        totalBill=0;
        firebaseDatabase=FirebaseDatabase.getInstance();
        for(int i=0;i<orderItems.size();i++)
        {
            totalBill+=(orderItems.get(i).getFoodItem().getFoodItemPrice()*orderItems.get(i).getQuantity());
        }
        instructions=findViewById(R.id.order_disaply_instruction);
        totalBillView=findViewById(R.id.order_display_totalBill);
        totalBillView.setText("$"+String.valueOf(totalBill));
        findViewById(R.id.order_display_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mInstruction=instructions.getText().toString();
                if(mInstruction!="")
                {
                    onCompleteOrder();
                    /*Intent i=new Intent(DisplayOrder.this,GetUserLocation.class);
                    i.putParcelableArrayListExtra("orderItems",foodItems);
                    i.putExtra("totalBill",totalBill);
                    i.putExtra("instruction",mInstruction);
                    startActivity(i);*/
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        /*FoodItem  foodItem= details.getParcelable("orderItem");
        foodItems.remove(foodItem);
        adapter.notifyDataSetChanged();*/
    }

    public void addLocation(View view)
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                location.setText(place.getAddress());
                address=place.getAddress().toString();
                loc = place.getLatLng();
                addLocation.setImageResource(R.drawable.baseline_edit_location_white_48dp);
            }
        }
    }


    void onCompleteOrder()
    {
        String mInstruction=instructions.getText().toString();
        AndroidNetworking.post("https://projectsapi.zoho.com/restapi/portal/tlxdml/projects/1265026000000020206/bugs/")
                .addHeaders("Authorization", authToken)
                .addBodyParameter("title","Order against UID :"+ FirebaseAuth.getInstance().getUid())
                .addBodyParameter("description",mInstruction)
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
                            OrderFirebase orderFirebase=new OrderFirebase(orderItems,totalBill,address,loc.longitude,loc.latitude,url);
                            firebaseDatabase.getReference("Orders").child(key).setValue(orderFirebase);
                            // firebaseDatabase.getReference("UserOrder").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
                            firebaseDatabase.getReference("OrderMetadata").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(new OrderMetadata(getFoodName(),"waiting",totalBill,""));
                            firebaseDatabase.getReference("OwnerOrders").child("Pending").child(key).setValue(FirebaseAuth.getInstance().getUid());
                            Intent intent=new Intent(DisplayOrder.this,MyOrders.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    String getFoodName()
    {
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<orderItems.size();i++)
        {
            stringBuilder.append(orderItems.get(i).getFoodItem().getFoodItemTitle()+", ");
        }
        stringBuilder.replace(stringBuilder.lastIndexOf(","),stringBuilder.length(),".");
        return stringBuilder.toString();
    }





}
