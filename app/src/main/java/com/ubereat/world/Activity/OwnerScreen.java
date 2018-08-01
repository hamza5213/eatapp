package com.ubereat.world.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
