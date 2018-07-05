package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ubereat.world.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import Adapter.FoodDisplayAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;

public class DisplayOrder extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<FoodItem>foodItems;
    OrderDisplayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);
       // foodItems=getIntent().getParcelableArrayListExtra("orderList");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.display_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDisplayAdapter(foodItems,this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        FoodItem  foodItem= Parcels.unwrap(details.getParcelable("orderItem"));
        foodItems.remove(foodItem);
        adapter.notifyDataSetChanged();
    }
}
