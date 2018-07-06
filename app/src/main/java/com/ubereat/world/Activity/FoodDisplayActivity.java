package com.ubereat.world.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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

import Adapter.FoodDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.FoodItemFirebase;

public class FoodDisplayActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    DatabaseReference foodItemRef;
    ArrayList<FoodItem> foodItems;
    String acessToken="3d8a264f7a584f93be5fbb79d6572f8f";
    FoodDisplayAdapter adapter;
    ArrayList<FoodItem> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        foodItems=new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        orderList=new ArrayList<>();
        fetchFoodItems();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onCartClick();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.food_display_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodDisplayAdapter(foodItems, this, this);
        recyclerView.setAdapter(adapter);
    }

    void fetchFoodItems()
    {
        foodItemRef=FirebaseDatabase.getInstance().getReference().child("FoodItems");
        foodItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FoodItemFirebase foodItemFirebase=dataSnapshot.getValue(FoodItemFirebase.class);
                System.out.println(foodItemFirebase.getPrice());

                AndroidNetworking.get(foodItemFirebase.getUrl())
                .addHeaders("Authorization", acessToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray bugs=response.getJSONArray("bugs");
                            JSONObject object=bugs.getJSONObject(0);
                            String foodTitle=object.getString("title");
                            String foodDescription=object.getString("description");
                            String fid=object.getString("id");
                            foodItems.add(new FoodItem(foodTitle,foodItemFirebase.getPrice(),foodDescription,foodItemFirebase.getSpiceLevel(),fid));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getMessage();

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

        FoodItem foodItem= details.getParcelable("FoodItem");
        orderList.add(foodItem);

    }

    public void onCartClick()
    {
        Intent i= new Intent(this,DisplayOrder.class);
        i.putParcelableArrayListExtra("orderList",orderList);
        startActivity(i);
    }
}
