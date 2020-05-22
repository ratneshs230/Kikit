package com.example.kikit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Interested_list extends AppCompatActivity {
    RecyclerView recyclerView;
    String storyKey, uid;
    FirebaseDatabase db;
    Story_model story;
    DatabaseReference reff, reference;
    Map<String, String> interestedList;
    String TAG = "Interested";
    LinearLayoutManager linearLayoutManager;
    User_model user_model;
    List<String> user_namesList ;
    List<String> user_imageList,uidList;
    RecyclerView.Adapter mAdapter;
    String userName, image;
    List<String> id;
    Story_model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_list);
        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        storyKey = i.getStringExtra("StoryKey");
        Log.w(TAG, "storykey==>" + storyKey + "");

        interestedList = new HashMap<>();
        id = new ArrayList<>();
        user_imageList= new ArrayList<>();
        user_namesList= new ArrayList<>();
        uidList=new ArrayList<>();
        recyclerView = findViewById(R.id.interestedRecycler);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        db = FirebaseDatabase.getInstance();
        model = new Story_model();
        reference = db.getReference().child("User");
        reff = db.getReference().child("Joined_Events");

        fetch();
      /*  for(int j=0;j<user_namesList.size();j++){
            Log.w(TAG, "FOR___UserNameList=>" + user_namesList.get(j));
            Log.w(TAG, "FOR___UserImageList=>" + user_namesList.get(j));


        }*/


        recyclerView.setAdapter(mAdapter);


    }

    public void fetch() {
        user_model = new User_model();

        Log.w(TAG, "FetchFunction==>");
        user_model = new User_model();
        reff.orderByChild("storyKey").equalTo(storyKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    id.add(ds.child("UID").getValue(String.class));

                    Log.w(TAG, "ID-=>" + id);
                }

                for (int i = 0; i < id.size(); i++) {
                    Log.w(TAG, "ID" + i + "=>" + id.get(i));

                    reference.orderByChild("uid").equalTo(id.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //dataSnapshot.child("User").getValue(User_model.class);
                            //User_Firebase message = dataSnapshot.getValue(User_Firebase.class);

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                               // user_model =ds.getValue(User_model.class);
                                String name = (String) ds.child("name").getValue();
                                String email = (String) ds.child("email").getValue();
                                String photo_url = (String) ds.child("profilePic_string").getValue();
                                String uid = (String) ds.child("uid").getValue();
                                String user_key = (String) ds.child("user_key").getValue();
                                Log.w(TAG, "ID-=>" + id);


                                user_model.setName(name);
                                user_model.setEmail(email);
                                user_model.setUid(uid);
                                user_model.setProfilePic_string(photo_url);
                                user_model.setUser_key(user_key);

                            }
                            uidList.add(user_model.getUid());
                            user_namesList.add(user_model.getName());
                            user_imageList.add(user_model.getProfilePic_string());//<---------------------PASS THISS LIST IN RECUCLER VIEW
                            Log.w(TAG, "FIrst___UserNameList=>" + user_namesList);
                            showDAta(user_namesList,user_imageList,uidList);

                             }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void showDAta(List<String> user_namesList, List<String> user_imageList,List<String> uidList) {
        mAdapter=new Adapter_class(Interested_list.this,user_namesList,user_imageList,uidList);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}
