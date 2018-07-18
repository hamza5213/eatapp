package com.ubereat.world.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mukesh.OtpView;
import com.ubereat.world.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;

import ModelClasses.UserProfile;
import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;


public class UserProfileActivity extends AppCompatActivity implements IPickResult {

    ImageView profilePic;
    EditText Name;
    FirebaseStorage storage;
    DatabaseReference userProfileRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    ToggleSwitch toggleSwitch;
    OtpView otpView;
    String riderCode;
    String ownerCode;
    TextView pinTitle;
    int pos;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profilePic=findViewById(R.id.UserProfileImage);
        Name=findViewById(R.id.UserProfileName);
        toggleSwitch=findViewById(R.id.user_profile_toggle);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        otpView=findViewById(R.id.otp_view);
        pinTitle=findViewById(R.id.user_profile_pin);
        coordinatorLayout=findViewById(R.id.user_profile_coordinatorLayout);
        userProfileRef=firebaseDatabase.getReference().child("User").child(firebaseUser.getUid());
        storage=FirebaseStorage.getInstance();
        toggleSwitch.setCheckedTogglePosition(0);
        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    pos=1;
                    otpView.setVisibility(View.VISIBLE);
                    pinTitle.setVisibility(View.VISIBLE);
                }
                else
                {
                    pos=0;
                    otpView.setVisibility(View.GONE);
                    pinTitle.setVisibility(View.GONE);
                }
            }
        });
        fetchCode();
    }

    void fetchCode()
    {
        firebaseDatabase.getReference("Code").child("Rider").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                riderCode=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseDatabase.getReference("Code").child("Owner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ownerCode=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onSaveClick(View view)
    {
        if(pos==0) {
            UploadPicture(0);
        }
        else
        {
            String pin=otpView.getOTP();
            if(pin.equals(ownerCode))
            {
                UploadPicture(1);
            }
            else if(pin.equals(riderCode))
            {
                UploadPicture(2);
            }
            else
            {
                Snackbar.make(coordinatorLayout, "Incorrect Pin Enter.", Snackbar.LENGTH_LONG).show();
            }

        }

    }
    public void onPicClick(View view)
    {
        PickSetup setup = new PickSetup().setSystemDialog(true);
        PickImageDialog.build(setup).show(this);
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            profilePic.setImageBitmap(pickResult.getBitmap());
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void UploadPicture(final int userFlag)
    {
        StorageReference profilePicRef = storage.getReference().child("UserDP/"+firebaseUser.getUid()+".jpg");

        profilePic.setDrawingCacheEnabled(true);
        profilePic.buildDrawingCache();
        Bitmap bitmap = profilePic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UserProfileActivity.this, "Failed To Upload Profile Pic", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
               // setResult(1);
                //finish();
                UserProfile userProfile;
                if(userFlag==2) {
                     userProfile = new UserProfile(Name.getText().toString(), firebaseUser.getPhoneNumber(), "rider");
                     firebaseDatabase.getReference("Riders").child(firebaseUser.getUid()).setValue(true);

                }
                else if (userFlag==1)
                {
                    userProfile = new UserProfile(Name.getText().toString(), firebaseUser.getPhoneNumber(), "owner");
                }
                else
                {
                     userProfile = new UserProfile(Name.getText().toString(), firebaseUser.getPhoneNumber(), "consumer");
                }
                userProfileRef.setValue(userProfile);
                startActivity(new Intent(UserProfileActivity.this, MyOrders.class));
                finish();

            }
        });
    }
}
