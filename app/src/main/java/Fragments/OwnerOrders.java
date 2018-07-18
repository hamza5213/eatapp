package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubereat.world.Activity.OrderDetail;
import com.ubereat.world.R;

import java.util.ArrayList;

import Adapter.MyOrderAdapter;
import Interfaces.OnListFragmentInteractionListener;
import ModelClasses.OrderMetadata;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 */
public class OwnerOrders extends Fragment implements OnListFragmentInteractionListener {
    FirebaseDatabase firebaseDatabase;
    ArrayList<OrderMetadata> orderMetadataArrayList;
    ArrayList<String> orderIds;
    OnListFragmentInteractionListener mListener;
    Context context;
    MyOrderAdapter adapter;

    public OwnerOrders() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_owner_orders, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        orderMetadataArrayList=new ArrayList<>();
        orderIds=new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ownerOrder_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyOrderAdapter(orderMetadataArrayList,context, this);
        recyclerView.setAdapter(adapter);
        fetchUserOrdersMetaData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // mListener=(OnListFragmentInteractionListener)context;
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    void fetchUserOrdersMetaData()
    {
        firebaseDatabase.getReference("OwnerOrders").child("Pending").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


               if(dataSnapshot.getValue(Object.class)!=null)
               {
                   fetchMetaData(dataSnapshot.getValue(String.class),dataSnapshot.getKey());
               }

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

    void fetchMetaData(String uid,String oid)
    {
        firebaseDatabase.getReference("OrderMetadata").child(uid).child(oid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderMetadataArrayList.add(dataSnapshot.getValue(OrderMetadata.class));
                orderIds.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        int position=details.getInt("position");
        Intent i = new Intent(context,OrderDetail.class);
        i.putExtra("orderId",orderIds.get(position));
        i.putExtra("ownerFlag",true);
        i.putExtra("status",orderMetadataArrayList.get(position).getStatus());
        context.startActivity(i);
    }
}
