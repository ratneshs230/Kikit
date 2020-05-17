package com.example.kikit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView title, desc, date, host, home, track;
    DatabaseReference reff, reference,joinedReference;
    String TAG = "StoryDisplay";
    FirebaseDatabase db;
    Button join,coming;

    String storyKey;
    FirebaseAuth mAuth;
    String Display_Title, Display_desc, Display_date, Display_host;
    String current_user, uid;
    FirebaseUser firebaseUser;
    ImageView display_storyImage;
    String image;
    Story_model story;
    Map<String, Object> joinedObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_display);
        try {
            Intent i = getIntent();


            uid = i.getStringExtra("uid");
            storyKey = i.getStringExtra("StoryKey");
            Log.w(TAG, "Story_key+UID" + storyKey + "<<-->>");
            joinedObject = new HashMap<>();
            joinedObject.put("UID",uid);
            joinedObject.put("storyKey",storyKey);


            title = findViewById(R.id.title);
            desc = findViewById(R.id.story_desc);
            date = findViewById(R.id.story_date);
            host = findViewById(R.id.story_host);
            home = findViewById(R.id.home);
            coming=findViewById(R.id.coming);
            display_storyImage = findViewById(R.id.display_storyImage);
            join = findViewById(R.id.join);


            db = FirebaseDatabase.getInstance();

            story = new Story_model();
            reff = db.getReference().child("Story");


            joinedReference=db.getReference().child("Joined");
            reference = reff.child(storyKey);


            fetch_story();

            title.setText(story.getStory_Name());
            desc.setText(story.getStory_desc());
            host.setText(story.getStory_host());
            date.setText(story.getStory_date());
            Picasso.get().load(story.getStory_image()).into(display_storyImage);
            host.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StoryDisplay.this, UserProfile.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            });
            coming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(StoryDisplay.this,All_Users.class);
                    intent.putExtra("StoryKey",storyKey);
                    intent.putExtra("UID",uid);
                    startActivity(intent);
                }
            });
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Toast.makeText(getApplicationContext(), "Activity Joined Succesfully", Toast.LENGTH_LONG).show();


                        joinedReference.push().setValue(joinedObject);
                        //Intent intent=new Intent(StoryDisplay.this,MapsActivity.class);
                        //startActivity(intent);
                    } catch (Exception e) {
                        Log.w(TAG, "ERROR=>" + e);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fetch_story() {
        Log.w(TAG, "Fetch FUnction");
        reference.addValueEventListener(new ValueEventListener() {
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


}