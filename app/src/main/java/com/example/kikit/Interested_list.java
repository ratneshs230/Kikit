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
    List<String> comingIDs;
    LinearLayoutManager linearLayoutManager;
    User_model user_model;
    private FirebaseRecyclerAdapter adapter;
    String userName,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_list);
        Intent i=getIntent();
        storyKey=i.getStringExtra("StoryKey");
        uid=i.getStringExtra("UID");

        interestedList=new HashMap<>();


        recyclerView=findViewById(R.id.interestedRecycler);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        db = FirebaseDatabase.getInstance();

        story = new Story_model();
        reff = db.getReference().child("Joined");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG,"DAtasnapshot==>"+dataSnapshot+"");
                if(dataSnapshot.child("storyKey").equals(storyKey)){

                    String userId=dataSnapshot.child("UID").getValue().toString();
                    Log.w(TAG,"Users->"+userId);
                  //  fetch_category_wise(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void gerUserData(String userID) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User").child(userID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.w(TAG, "DATASNAPsHOT-->>" + dataSnapshot);

                if (dataSnapshot.child("name").getValue() != null) {
                    userName = dataSnapshot.child("name").getValue().toString();
                    image=dataSnapshot.child("profilePic_url").getValue().toString();
                }
                Log.w(TAG, "UserName--" + userName);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void fetch_category_wise(String userId) {
        user_model = new User_model();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("User").orderByChild("uid").equalTo(userId);

        FirebaseRecyclerOptions<User_model> options = new FirebaseRecyclerOptions.Builder<User_model>()
                .setQuery(query, User_model.class)
                .build();
            adapter = new FirebaseRecyclerAdapter<User_model, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Interested_list.ViewHolder holder, final int position, @NonNull final User_model model) {
                holder.setUsername(model.getName());


                holder.setUsername(model.getProfileUrl());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Interested_list.this,UserProfile.class);


                        intent.putExtra("uid", model.getUid());

                        intent.putExtra("image",model.getPhoto_url());
                        startActivity(intent);                    }
                });



            }


        };
        recyclerView.setAdapter(adapter);




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
      // adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // adapter.stopListening();
    }


}
