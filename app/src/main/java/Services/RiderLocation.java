package Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ModelClasses.LocationModel;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

/**
 * Created by hamza on 10-Jul-18.
 */

public class RiderLocation extends IntentService {
    public RiderLocation() {
        super("Rider Location");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RiderLocation").child(FirebaseAuth.getInstance().getUid());
        final SharedPreferences[] sharedPreferences = new SharedPreferences[1];
        long mLocTrackingInterval = 0; // 5 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(getApplicationContext()).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                LocationModel locationModel = new LocationModel(location.getLongitude(), location.getLatitude());
                sharedPreferences[0] = getSharedPreferences("riderLocation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences[0].edit();
                editor.putString("longitude", String.valueOf(location.getLongitude()));
                editor.putString("latitude", String.valueOf(location.getLatitude()));
                editor.commit();
                databaseReference.setValue(locationModel);
            }

        });
    }
}
