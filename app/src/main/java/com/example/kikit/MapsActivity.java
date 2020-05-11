package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Double longitude, latitude;
    LocationModel locationModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mAuth = FirebaseAuth.getInstance();
            locationModel = new LocationModel();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            firebaseDatabase = FirebaseDatabase.getInstance();

            reff = firebaseDatabase.getReference("User").child(mAuth.getCurrentUser().getUid());

        }catch (Exception e){
            e.printStackTrace();
        }



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

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    Log.w(TAG,"LocationListener=>"+latitude+"/"+longitude);
                    locationModel.setLatitude(latitude);
                    locationModel.setLongitude(longitude);
                    reff.child("Location").push().setValue(locationModel);
                    locationKey=reff.child("Location").push().getKey();
                    locationReff=reff.child("Locaition").child(locationKey);
                    locationReff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                locationModel = dataSnapshot.getValue(LocationModel.class);
                                Log.w(TAG, "DAtaSnapshot=>" + dataSnapshot);
                                Log.w(TAG, "Longitude=>" + locationModel.getLongitude());
                                Log.w(TAG, "LAtitude=>" + locationModel.getLatitude());


                                LatLng latLng = new LatLng(locationModel.getLatitude(), locationModel.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try{
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save_location(){
        try {
            reff.child("Location").child("Latitude").push().setValue(latitude);
            reff.child("Location").child("Longitude").push().setValue(longitude);
        }catch (Exception e){
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
