package Fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubereat.world.Activity.AddFoodActivity;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.FoodDisplayAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.FoodItem;
import ModelClasses.FoodItemFirebase;


/**
 * Created by hamza on 09-Jul-18.
 */

public class OwnerFoodDisplay extends Fragment {
    DatabaseReference foodItemRef;
    ArrayList<FoodItem> foodItems;
    String acessToken="3d8a264f7a584f93be5fbb79d6572f8f";
    FoodDisplayAdapter adapter;
    ArrayList<FoodItem> orderList;
    OnListFragmentInteractionListener mListener;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.owner_food_display_fragment, container, false);
        foodItems=new ArrayList<>();
        orderList=new ArrayList<>();
        fetchFoodItems();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ownerFoodDisplay_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FoodDisplayAdapter(foodItems, context, mListener,false);
        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.owner_food_display_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context,AddFoodActivity.class));
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        AndroidNetworking.initialize(context);
        mListener=(OnListFragmentInteractionListener)context;
    }
    void fetchFoodItems()
    {
        foodItemRef= FirebaseDatabase.getInstance().getReference().child("FoodItems");
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



}
