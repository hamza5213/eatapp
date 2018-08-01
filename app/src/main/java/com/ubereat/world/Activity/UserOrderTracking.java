package com.ubereat.world.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ubereat.world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ModelClasses.LocationModel;
import ModelClasses.Order;

public class UserOrderTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseDatabase firebaseDatabase;
    String riderId;
    boolean isDirectionReceived;
    boolean isFirstFlag;
    Order order;
    ImageView markerImage;
    Marker riderMarker;
    LatLng destination;
    LatLng origin;
    private Timer mTimer1;
    TextView time;
    TextView dist;
    TextView timeLabel;
    TextView distLabel;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        riderId=getIntent().getStringExtra("riderId");
        order=getIntent().getParcelableExtra("order");
       // markerImage=findViewById(R.id.g_marker_image);
        isDirectionReceived=false;
        isFirstFlag=true;
        time=findViewById(R.id.user_order_tracking_mins);
        dist=findViewById(R.id.user_order_tracking_miles);
        timeLabel=findViewById(R.id.textmins);
        distLabel=findViewById(R.id.textmiles);
        destination=new LatLng(order.getLatitude(),order.getLongitude());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        firebaseDatabase.getReference("RiderLocation").child(riderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocationModel locationModel=dataSnapshot.getValue(LocationModel.class);
                if(isFirstFlag)
                {
                    isFirstFlag=false;
                    origin=new LatLng(locationModel.getLatitude(),locationModel.getLongitude());
                    getDirections();

                }
                else if(isDirectionReceived)
                {

                    origin=new LatLng(locationModel.getLatitude(),locationModel.getLongitude());
                    riderMarker.remove();
                    getMarkerBitmapFromView(new LatLng(locationModel.getLatitude(),locationModel.getLongitude()));
                }


              //  getMarkerBitmapFromView(new LatLng(locationModel.getLatitude(),locationModel.getLongitude()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


   void getMarkerBitmapFromView(LatLng l) {

       View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
       ImageView markerImageView = markerImage;
       //markerImageView.setImageResource(resId);
       customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
       customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
       customMarkerView.buildDrawingCache();
       Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
               Bitmap.Config.ARGB_8888);
       Canvas canvas = new Canvas(returnedBitmap);
       canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
       Drawable drawable = customMarkerView.getBackground();
       if (drawable != null)
           drawable.draw(canvas);
       customMarkerView.draw(canvas);
      riderMarker= mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap)).position(l).anchor(0.5f,0.5f));
      isDirectionReceived=true;
       //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 18));

   }

   void getDirections()
   {
       GoogleDirection.withServerKey("AIzaSyB-FU-RJZCuiRtZSQ_1GvPaiHmikafclBE")
               .from(origin)
               .to(destination)
               .avoid(AvoidType.FERRIES)
               .avoid(AvoidType.HIGHWAYS)
               .execute(new DirectionCallback() {
                   @Override
                   public void onDirectionSuccess(Direction direction, String rawBody) {
                       if(direction.isOK()) {
                           // Do something
                           Route route = direction.getRouteList().get(0);
                           mMap.addMarker(new MarkerOptions().position(destination).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))).showInfoWindow();
                           getMarkerBitmapFromView(origin);
                           ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                           mMap.addPolyline(DirectionConverter.createPolyline(UserOrderTracking.this,directionPositionList, 5, Color.rgb(	0, 131, 143)));
                           setCameraWithCoordinationBounds(route);


                       } else {
                           // Do something
                       }
                   }

                   @Override
                   public void onDirectionFailure(Throwable t) {
                       // Do something
                       System.out.println(t.getMessage());
                   }
               });
   }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        startTimer();
        //new async().execute();

    }

    ArrayList<String> getDuration()
    {
        try {

            StringBuilder response = new StringBuilder();

            String url1 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origin.latitude + "," + origin.longitude + "&destinations=" + destination.latitude + "," + destination.longitude + "&key=AIzaSyBknB7x4WpceoNpF1ykv0cJUeRJE7vqO-w";
            Log.d("URL", url1);
            URL url = new URL(url1);

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8192);
                String strLine = null;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }

            JSONObject json = new JSONObject(response.toString());
            JSONArray routeArray = json.getJSONArray("rows");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray newTempARr = routes.getJSONArray("elements");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
           // time.setText(timeOb.getString("text"));
            //System.out.println(distOb.getString("text"));
            ArrayList<String> s=new ArrayList<>();
            s.add(timeOb.getString("text"));
            s.add(distOb.getString("text"));
            return s;

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        new async().execute();
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 2000);
    }

    private void stopTimer(){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    public class async extends AsyncTask<Void,Void,ArrayList<String>>
    {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return getDuration();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
           time.setText(strings.get(0).substring(0,strings.get(0).indexOf(" ")));
           dist.setText(strings.get(1).substring(0,strings.get(1).indexOf(" ")));
           timeLabel.setText(strings.get(0).substring(strings.get(0).indexOf(" ")+1));
           distLabel.setText(strings.get(1).substring(strings.get(0).indexOf(" ")+1));
            
        }
    }


    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();

    }
}
