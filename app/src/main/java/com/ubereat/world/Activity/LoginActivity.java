package com.ubereat.world.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.ubereat.world.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(this,AddFoodActivity.class));
        }
        else
        {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.PhoneBuilder().build()
                            ))
                            .build(),
                    123);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123&&resultCode==RESULT_OK)
        {
            startActivity(new Intent(this,AddFoodActivity.class));
        }

    }

    public void onShowClick(View view )
    {
        startActivity(new Intent(this,FoodDisplayActivity.class));
    }
}
