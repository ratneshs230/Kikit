package com.example.kikit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StoryDisplay extends AppCompatActivity {
        TextView title,desc,date,host,home,track;
        DatabaseReference reff,fer;
        String TAG="StoryDisplay";
        FirebaseDatabase firebaseDatabase;
        Button join;
        Story_model story_model;
        String storyKey;
    Display_Story display_story;
        FirebaseAuth mAuth;
    Display_Story model;
    String Display_Title,Display_desc,Display_date,Display_host;
    String current_user;
    String referencesString,story_reference;
    String key,uid,image;
    FirebaseUser firebaseUser;
    ImageView display_storyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_display);
try {
    Intent i = getIntent();


    uid = i.getStringExtra("uid");
    image = i.getStringExtra("image");
    Display_Title = i.getStringExtra("title");
    Display_desc = i.getStringExtra("desc");
    Display_date = i.getStringExtra("date");
    storyKey = i.getStringExtra("StoryKey");
    Log.w(TAG, "Story_key" + storyKey);
    Display_host = i.getStringExtra("userName");




    track=findViewById(R.id.track);
    title = findViewById(R.id.title);
    desc = findViewById(R.id.story_desc);
    date = findViewById(R.id.story_date);
    host = findViewById(R.id.story_host);
    home = findViewById(R.id.home);
    display_storyImage = findViewById(R.id.display_storyImage);
    join = findViewById(R.id.join);


    firebaseDatabase = FirebaseDatabase.getInstance();

    reff = firebaseDatabase.getReference();
    story_model = new Story_model();

    display_story = new Display_Story();

    Log.w(TAG, "REFF=>" + reff);

    title.setText(Display_Title);
    desc.setText(Display_desc);
    host.setText(Display_host);
    date.setText(Display_date);
    Picasso.get().load(image).into(display_storyImage);

    fetch_story();

    track.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(StoryDisplay.this,MapsActivity.class);
            startActivity(intent);
        }
    });
    host.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StoryDisplay.this, UserProfile.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
    });

    join.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mAuth = FirebaseAuth.getInstance();
                firebaseUser = mAuth.getCurrentUser();
                current_user = firebaseUser.getDisplayName();

                Map<String, Object> joiningObject = new HashMap<>();

                joiningObject.put("uid", firebaseUser.getUid() );
                joiningObject.put("joining event",Display_Title);


                reff.child("Story").child("coming").updateChildren(joiningObject);
                    Toast.makeText(getApplicationContext(),"Activity Joined Succesfully",Toast.LENGTH_LONG).show();
                //Intent intent=new Intent(StoryDisplay.this,MapsActivity.class);
                //startActivity(intent);
            } catch (Exception e) {
                Log.w(TAG, "ERROR=>" + e);
            }
        }
    });


}catch (Exception e){
    e.printStackTrace();
}

    }
    public void fetch_story() {
            story_model=new Story_model();
        Log.w(TAG, "Fetch FUnction");
        Query query=reff.child("Story").orderByChild("Story_key");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for(DataSnapshot ds:dataSnapshot.getChildren()){
                    story_model =dataSnapshot.getValue(Story_model.class);
                    if(ds.child("story_image").equals(storyKey)){

                        String image=ds.child("story_image").getValue(String.class);

    //                    Picasso.get().load(image).into(display_storyImage);
                        Log.w(TAG,"StoryModel--==>>"+image);

                    }
                Log.w(TAG,"Display_story--==>>"+model);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public static class Display_Story {
        public String story_Name;
        public String story_desc;
        public String story_date;
        public String story_host;

        public Uri getStory_image() {
            return story_image;
        }

        public void setStory_image(Uri story_image) {
            this.story_image = story_image;
        }

        public Uri story_image;

        public String getStory_category() {
            return story_category;
        }

        public void setStory_category(String story_category) {
            this.story_category = story_category;
        }

        public String story_category;


        public Display_Story(String story_Name, String story_desc, String story_date, String story_host,String story_category) {
            this.story_Name = story_Name;
            this.story_desc = story_desc;
            this.story_date = story_date;
            this.story_host = story_host;
            this.story_category=story_category;
        }

        public Display_Story() {
        }

        public String getStory_Name() {
            return story_Name;
        }

        public void setStory_Name(String story_Name) {
            this.story_Name = story_Name;
        }

        public String getStory_desc() {
            return story_desc;
        }

        public void setStory_desc(String story_desc) {
            this.story_desc = story_desc;
        }

        public String getStory_date() {
            return story_date;
        }

        public void setStory_date(String story_date) {
            this.story_date = story_date;
        }

        public String getStory_host() {
            return story_host;
        }

        public void setStory_host(String story_host) {
            this.story_host = story_host;
        }
    }
}