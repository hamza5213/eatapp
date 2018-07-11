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
import android.widget.TextView;

import com.ubereat.world.R;
import java.util.ArrayList;

import Adapter.FoodDisplayAdapter;
import Adapter.OrderDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;

public class DisplayOrder extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<FoodItem>foodItems;
    OrderDisplayAdapter adapter;
    EditText instructions;
    TextView totalBillView;

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
        instructions=findViewById(R.id.order_disaply_instruction);
        totalBillView=findViewById(R.id.order_display_totalBill);
        totalBillView.setText("$"+String.valueOf(totalBill));
        findViewById(R.id.order_display_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mInstruction=instructions.getText().toString();
                if(mInstruction!="")
                {
                    Intent i=new Intent(DisplayOrder.this,GetUserLocation.class);
                    i.putParcelableArrayListExtra("orderItems",foodItems);
                    i.putExtra("totalBill",totalBill);
                    i.putExtra("instruction",mInstruction);
                    startActivity(i);
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


}
