package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference location_reference,myreff;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    String TAG="MapsActivity";
    FirebaseAuth mAuth;
    String uid,Name;

    String myId;
    FirebaseUser user;
    Button share;
    Location_Model location_model;
     LatLng latlng;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            Intent i=getIntent();
            uid=i.getStringExtra("UID");
            Name=i.getStringExtra("Name");


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mAuth = FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
            if(user!=null){
                myId=user.getUid();
                Log.w(TAG,"MYUID=>"+myId);
            }
            else{
                Log.w(TAG,"Logged OUT");
            }


            location_model=new Location_Model();


            firebaseDatabase = FirebaseDatabase.getInstance();
            myreff=firebaseDatabase.getReference("User").child(myId);
            location_reference = firebaseDatabase.getReference().child("User").child(uid).child("Location_Data");

   }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            mMap=googleMap;

            location_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.w(TAG,"Location_Datasnapshot=>"+dataSnapshot);
                    location_model=dataSnapshot.getValue(Location_Model.class);
                    if(dataSnapshot.child("mLatitiude").getValue()!=null && dataSnapshot.child("mLongitude").getValue()!=null)
                   latlng=new LatLng((Double) dataSnapshot.child("mLatitiude").getValue(),(Double)dataSnapshot.child("mLongitude").getValue());
                    Log.w(TAG,"LATLNG=>"+latlng);
                    Log.w(TAG,"Name=>"+Name);


                    mMap.addMarker(new
                            MarkerOptions().position(latlng).title(Name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,11));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });








}
catch (Exception e)
{
    e.printStackTrace();
}
    }

}
