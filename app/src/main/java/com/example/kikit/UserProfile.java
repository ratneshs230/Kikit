package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserProfile extends AppCompatActivity  {

    FirebaseUser user;
    ImageView profile_pic;

    String TAG="USER_PROFILE";
    String uid,name,image;
    Button Signout;
    DatabaseReference database;
    TextView userName,track;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        try {
            Intent i = getIntent();
            uid = i.getStringExtra("uid");
            userName = findViewById(R.id.user);
            Log.w(TAG, "UID Recieved=>" + uid);
            profile_pic = findViewById(R.id.profile_picture);



            user = FirebaseAuth.getInstance().getCurrentUser();

            Signout = findViewById(R.id.signout);
            track = findViewById(R.id.track);
            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserProfile.this, MapsActivity.class);
                    intent.putExtra("UID", uid);
                    startActivity(intent);
                }
            });

            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(UserProfile.this, AuthSelect.class);
                    startActivity(intent);
                }
            });

            database = FirebaseDatabase.getInstance().getReference("User").child(uid);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.w(TAG, "DATASNAPsHOT-->>" + dataSnapshot);

                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                        image = dataSnapshot.child("photo_url").getValue().toString();
                        userName.setText(name);
                        Picasso.get().load(image).into(profile_pic);
                    }
                    Log.w(TAG, "UserName--" + name);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }catch (Exception e){
                        e.printStackTrace();
                    }


    }


}
