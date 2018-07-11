package com.ubereat.world.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ubereat.world.R;

import Interfaces.OnListFragmentInteractionListener;

public class OwnerScreen extends AppCompatActivity implements OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_screen);
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

        if(details.getString("flag").equals("owner"))
        {
            Log.d("foo","bar");
        }
    }
}
