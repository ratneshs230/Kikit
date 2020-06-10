package com.example.kikit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class StoryDisplay extends AppCompatActivity {
    TextView title, desc, date, host, home;
    DatabaseReference reff, reference, joinedReference, location_reference;
    String TAG = "StoryDisplay";
    FirebaseDatabase db;
    Button join, coming;

    String storyKey;
    FirebaseAuth mAuth;

    String current_user, uid,from;
    ImageView display_storyImage;
    Uri image;
    Story_model story;
    Map<String, Object> joinedObject;
    FirebaseUser user;
    Location_Model locationModel;
    CoordinatorLayout coordinatorLayout;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_display);
        try {
            Intent i = getIntent();
            from=i.getStringExtra("From");
            storyKey = i.getStringExtra("StoryKey");
            SharedPreferences no_pref=getSharedPreferences("Event_preference",MODE_PRIVATE);
            uid=no_pref.getString("Uid","");
            Log.w(TAG, "Story_key+UID" + storyKey + "<<-->>"+uid);


            joinedObject = new HashMap<>();
            joinedObject.put("UID", uid);
            joinedObject.put("storyKey", storyKey);

            title = findViewById(R.id.title);
            desc = findViewById(R.id.story_desc);
            date = findViewById(R.id.story_date);
            host = findViewById(R.id.story_host);
            home = findViewById(R.id.home);
            coming = findViewById(R.id.coming);
            display_storyImage = findViewById(R.id.display_storyImage);
            join = findViewById(R.id.join);
            coordinatorLayout=findViewById(R.id.coordinator_layout);
            db = FirebaseDatabase.getInstance();

            story = new Story_model();
            reff = db.getReference().child("Story");
            location_reference = db.getReference().child("User").child(uid).child("Location_Data");

            joinedReference = db.getReference().child("Joined_Events");

                fetch_story(storyKey);

                    coming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StoryDisplay.this, Interested_list.class);
                    intent.putExtra("StoryKey", storyKey);
                    intent.putExtra("uid", uid);

                    startActivity(intent);
                }
            });

            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        joinedReference.push().setValue(joinedObject);

                        Toast.makeText(getApplicationContext(), "Activity Joined Succesfully", Toast.LENGTH_LONG).show();
                        showSnackbar();
                        join.setEnabled(false);
                    } catch (Exception e) {
                        Log.w(TAG, "ERROR=>" + e);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showSnackbar() {
        Snackbar snackbar=Snackbar.make(coordinatorLayout,"Share your location",Snackbar.LENGTH_INDEFINITE)
                .setAction("Share", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(
                                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(StoryDisplay.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            return;
                        }
                        currentLocation();
                    }
                });
        snackbar.show();
    }

    public void fetch_story(String storyKey) {
        Log.w(TAG, "Fetch FUnction"+storyKey);
        reff.child(storyKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "DATASNAPsHOT-->>" + dataSnapshot);
                story = dataSnapshot.getValue(Story_model.class);


                Log.w(TAG, "" + dataSnapshot);
                Log.w(TAG, "" + story.getStory_Name());
                title.setText(story.getStory_Name());
                desc.setText(story.getStory_desc());
                host.setText(story.getStory_host());
                date.setText(story.getStory_date());
                Picasso.get().load(story.getStory_image()).into(display_storyImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void currentLocation()    {
locationModel=new Location_Model();
        Log.w(TAG,"CurrentLocation Reached");
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(StoryDisplay.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(StoryDisplay.this)
                                .removeLocationUpdates(this);
                        if(locationResult!=null && locationResult.getLocations().size()>0){
                            int latestLocationIndex=locationResult.getLocations().size()-1;
                            locationModel.setmLatitiude(locationResult.getLocations().get(latestLocationIndex).getLatitude());
                            locationModel.setmLongitude(locationResult.getLocations().get(latestLocationIndex).getLongitude());
                            Log.w(TAG,"LocationModel=>"+locationModel);
                            Log.w(TAG,"LocationValues=>"+locationResult.getLocations().get(latestLocationIndex).getLongitude());
                            location_reference.setValue(locationModel);

                        }
                    }
                }, Looper.getMainLooper());

}

}