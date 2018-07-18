package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ubereat.world.R;

import Adapter.ViewPagerAdapter;
import Interfaces.OnListFragmentInteractionListener;

public class OwnerScreen extends AppCompatActivity implements OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_screen);
        SmartTabLayout smartTabLayout=findViewById(R.id.owner_screen_tabLayout);
        ViewPager viewPager=findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        //smartTabLayout.setDistributeEvenly(true);
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

        if(details.getString("flag").equals("foodDisplay"))
        {

        }
        else
        {
            if(details.getString("order_type").equals("pending"))
            {
                startActivity(new Intent(this,OrderDetail.class));
            }
            else
            {

            }
        }
    }
}
