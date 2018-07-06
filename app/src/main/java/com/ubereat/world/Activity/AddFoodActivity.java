package com.ubereat.world.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ubereat.world.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import ModelClasses.FoodItemFirebase;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class AddFoodActivity extends AppCompatActivity implements IPickResult {

    ImageButton high;
    ImageButton medium;
    ImageButton low;
    EditText description;
    EditText title;
    EditText price;
    Bitmap foodBitmap;
    ImageView foodImage;
    String spiceLevel=null;
    String acessToken="3d8a264f7a584f93be5fbb79d6572f8f";
    DatabaseReference foodItemRef;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        storage=FirebaseStorage.getInstance();
        high=findViewById(R.id.spice_high);
        medium=findViewById(R.id.spice_medium);
        low=findViewById(R.id.spice_low);
        foodImage=findViewById(R.id.foodPic);
        description=findViewById(R.id.add_food_description);
        title=findViewById(R.id.add_Food_title);
        price=findViewById(R.id.add_food_price);
        AndroidNetworking.initialize(getApplicationContext());
        foodItemRef= FirebaseDatabase.getInstance().getReference().child("FoodItems");

    }
    public void onHighSpiceClick(View view)
    {
        high.setImageResource(R.drawable.fire_checked);
        medium.setImageResource(R.drawable.drop);
        low.setImageResource(R.drawable.leaf);
        spiceLevel="High";

    }
    public void onMediumSpiceClick(View view)
    {
        high.setImageResource(R.drawable.fire);
        medium.setImageResource(R.drawable.drop_checked);
        low.setImageResource(R.drawable.leaf);
        spiceLevel="Mild";
    }
    public void onLowSpiceClick(View view)
    {
        high.setImageResource(R.drawable.fire);
        medium.setImageResource(R.drawable.drop);
        low.setImageResource(R.drawable.leaf_checked);
        spiceLevel="Low";

    }
    public void onDoneClick(View view)
    {
        String pTitle = title.getText().toString();
        String pDescription = description.getText().toString();
        final String pPrice = price.getText().toString();
        if (spiceLevel == null || TextUtils.isEmpty(pTitle) || TextUtils.isEmpty(pDescription) || TextUtils.isEmpty(pPrice)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout_id),"Kindly fill the required fields", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
      //  final String key=foodItemRef.push().getKey();
        AndroidNetworking.post("https://projectsapi.zoho.com/restapi/portal/tlxdml/projects/1265026000000020206/bugs/")
            .addHeaders("Authorization", acessToken)
            .addBodyParameter("title",pTitle)
            .addBodyParameter("description",pDescription)
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
                        FoodItemFirebase foodItemFirebase=new FoodItemFirebase(Long.parseLong(pPrice),spiceLevel,url);
                        foodItemRef.child(key).setValue(foodItemFirebase);
                        UploadPicture(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onError(ANError error) {
                    System.out.println("error");
                }
            });
    }
    public void onPicClick(View view)
    {
        PickSetup setup = new PickSetup().setSystemDialog(true);
        PickImageDialog.build(setup).show(this);
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            foodImage.setImageBitmap(pickResult.getBitmap());
            foodBitmap=pickResult.getBitmap();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void UploadPicture(String key)
    {
        StorageReference foodPicRef = storage.getReference().child("FoodPic/"+key+".jpg");
        if(foodBitmap==null) {
            foodImage.setDrawingCacheEnabled(true);
            foodImage.buildDrawingCache();
            foodBitmap = foodImage.getDrawingCache();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foodBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = foodPicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddFoodActivity.this, "Failed To Upload Profile Pic", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                finish();
            }
        });
    }




}
