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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reff;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    String locationKey;
    DatabaseReference locationReff;
    String TAG="MapsActivity";
    FirebaseAuth mAuth;
    private Double Maplongitude, Maplatitude;
    String uid;
    LocationModel locationModel;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            Intent i=getIntent();
            uid=i.getStringExtra("UID");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mAuth = FirebaseAuth.getInstance();
            locationModel = new LocationModel();
             firebaseDatabase = FirebaseDatabase.getInstance();

            reff = firebaseDatabase.getReference("User").child(uid);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        saveOnDB();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveOnDB(){
        final Double[] longitude = new Double[1];
        final Double[] latitude = new Double[1];
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            latitude[0] = location.getLatitude();
                            longitude[0] = location.getLongitude();
                            Map<String,Double> DBlocation=new HashMap<>();
                            DBlocation.put("Latitude", latitude[0]);
                            DBlocation.put("Longitude", longitude[0]);
                            Log.w(TAG, "Location=>" + latitude[0] + "/" + longitude[0]);
                            reff.child("Location").setValue(DBlocation);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"FAIled to read last location ");
            }
        });

    }
    private void fetchLocation() {
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            locationModel=dataSnapshot.child("Location").getValue(LocationModel.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
try{
    fetchLocation();

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
    LatLng latLng = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
    googleMap.addMarker(markerOptions);



                  // Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);

}
catch (Exception e)
{
    e.printStackTrace();
}
    }

    public class LocationModel{
        Double Latitude;
        Double Longitude;

        public Double getLatitude() {
            return Latitude;
        }

        public void setLatitude(Double latitude) {
            Latitude = latitude;
        }

        public Double getLongitude() {
            return Longitude;
        }

        public void setLongitude(Double longitude) {
            Longitude = longitude;
        }

        public LocationModel(Double latitude, Double longitude) {
            Latitude = latitude;
            Longitude = longitude;
        }

        public LocationModel() {
        }
    }

}
