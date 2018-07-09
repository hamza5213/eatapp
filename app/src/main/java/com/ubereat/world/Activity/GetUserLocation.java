package com.ubereat.world.Activity;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ModelClasses.FoodItem;
import ModelClasses.Order;
import ModelClasses.OrderFirebase;
import ModelClasses.OrderMetadata;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class GetUserLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location location;
    FloatingActionButton fab;
    ArrayList<FoodItem> foodItems;
    long totalBill;
    String authToken="3d8a264f7a584f93be5fbb79d6572f8f";
    String comments;
    long mLocTrackingInterval = 1000 * 60; // 5 sec
    float trackingDistance = 10f;
    LocationAccuracy trackingAccuracy;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab=findViewById(R.id.get_location_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location!=null)
                {

                   /* Intent nextIntent=new Intent();

                    nextIntent.putExtra("Longitude",location.getLongitude());
                    nextIntent.putExtra("Latitude",location.getLatitude());
                    startActivity(nextIntent);*/
                   onCompleteOrder();
                }
            }
        });

        trackingAccuracy = LocationAccuracy.HIGH;
        firebaseDatabase=FirebaseDatabase.getInstance();
        Intent temp= getIntent();
        foodItems=temp.getParcelableArrayListExtra("orderItems");
        totalBill=temp.getLongExtra("totalBill",10);
        comments="Please be on Time";





        // Add a marker in Sydney and move the camera


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(this).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location loc) {

                location=loc;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title("your Location")).showInfoWindow();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 16));
            }
        });
    }

    void onCompleteOrder()
    {
        AndroidNetworking.post("https://projectsapi.zoho.com/restapi/portal/tlxdml/projects/1265026000000020206/bugs/")
                .addHeaders("Authorization", authToken)
                .addBodyParameter("title","Order against UID :"+ FirebaseAuth.getInstance().getUid())
                .addBodyParameter("description",comments)
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
                            OrderFirebase orderFirebase=new OrderFirebase(foodItems,totalBill,location.getLongitude(),location.getLatitude(),url);
                            firebaseDatabase.getReference("Orders").child(key).setValue(orderFirebase);
                           // firebaseDatabase.getReference("UserOrder").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
                            firebaseDatabase.getReference("OrderMetadata").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(new OrderMetadata(getFoodName(),"waiting",totalBill));
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
        for(int i=0;i<foodItems.size();i++)
        {
            stringBuilder.append(foodItems.get(i).getFoodItemTitle()+", ");
        }
        return stringBuilder.toString();
    }


}
