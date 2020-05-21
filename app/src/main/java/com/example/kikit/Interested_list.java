package com.example.kikit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    String storyKey,uid;
    FirebaseDatabase db;
    Story_model story;
    DatabaseReference reff,reference;
    Map<String,String> interestedList;
    String TAG="Interested";
    LinearLayoutManager linearLayoutManager;
    User_model user_model;
    private FirebaseRecyclerAdapter adapter;
    String userName,image;
    List<String> id;
Story_model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_list);
        Intent i=getIntent();
        uid=i.getStringExtra("uid");
        storyKey=i.getStringExtra("StoryKey");
        Log.w(TAG, "storykey==>" + storyKey + "");

        interestedList=new HashMap<>();
        id=new ArrayList<String>();

        recyclerView=findViewById(R.id.interestedRecycler);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        db = FirebaseDatabase.getInstance();
            model=new Story_model();
        reference=db.getReference().child("User");
        reff = db.getReference().child("Joined_Events");

        fetch();
    }

    public void fetch() {
        user_model = new User_model();
        Log.w(TAG, "FetchFunction==>");

        reff.orderByChild("storyKey").equalTo(storyKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG,"Ddatasnapshot=>"+dataSnapshot);
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                id.add(ds.child("UID").getValue(String.class));

                Log.w(TAG,"ID-=>"+id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for(int i=0;i<id.size();i++){
            Log.w(TAG,"ID"+i+"=>"+id.get(i));

            reference.orderByChild("uid").equalTo(id.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.w(TAG,"USER__Ddatasnapshot=>"+dataSnapshot);

                    user_model=dataSnapshot.getValue(User_model.class);
                    Log.w(TAG,"User_Model=>"+user_model);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Log.w(TAG,"User_Model__outside=>"+user_model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView username;
        public ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            username = itemView.findViewById(R.id.eventName);
            userImage=itemView.findViewById(R.id.event_img);

        }

        public LinearLayout getRoot() {
            return root;
        }

        public void setRoot(LinearLayout root) {
            this.root = root;
        }


        public void setUsername(String string) {
            username.setText(string);
        }

        public void setUserImage(String image){

            Picasso.get().load(image).into(userImage);
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



}
