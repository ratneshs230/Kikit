package com.example.kikit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class profileBuild {
    String Uid,Name;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,storageRef;
    FirebaseStorage firebaseStorage;
    File Profile_pic;

    public profileBuild() {
    }

    User_model user_model;
    String TAG="Profile_Build";

    public void display(String uid){
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("User");
        user_model=new User_model();

        Query query=databaseReference.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        user_model=ds.getValue(User_model.class);
                        Log.w(TAG,"USER_MODEL=>"+user_model);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





}
