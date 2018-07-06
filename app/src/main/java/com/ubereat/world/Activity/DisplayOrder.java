package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.ubereat.world.R;
import java.util.ArrayList;

import Adapter.FoodDisplayAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;

public class DisplayOrder extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<FoodItem>foodItems;
    OrderDisplayAdapter adapter;
    long totalBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);
        foodItems=getIntent().getParcelableArrayListExtra("orderList");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.display_order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDisplayAdapter(foodItems,this, this);
        recyclerView.setAdapter(adapter);
        totalBill=0;
        for(int i=0;i<foodItems.size();i++)
        {
            totalBill+=foodItems.get(i).getFoodItemPrice();
        }
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        FoodItem  foodItem= details.getParcelable("orderItem");
        foodItems.remove(foodItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.order_display_cart)
        {
            Intent intent=new Intent(this,GetUserLocation.class);
            intent.putParcelableArrayListExtra("orderItems",foodItems);
            intent.putExtra("totalBill",totalBill);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
