package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class All_Users extends AppCompatActivity {
    FirebaseRecyclerAdapter adapter;
String TAG="ALL User";
RecyclerView user_recycler;
User_model model;
LinearLayoutManager linearLayoutManager;
FirebaseUser user;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__users);

        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
    user_recycler = findViewById(R.id.userRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        user_recycler.setLayoutManager(linearLayoutManager);
        user_recycler.setHasFixedSize(true);
        fetch();

    }

    private void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference().child("User");
        FirebaseRecyclerOptions<User_model> options = new FirebaseRecyclerOptions.Builder<User_model>()
                .setQuery(query, User_model.class)
                .build();
        Log.w(TAG,"query=>"+query);
        adapter = new FirebaseRecyclerAdapter<User_model, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final User_model model) {

                holder.setEventname(model.getName());
                 holder.setEvent_img(model.getProfileUrl());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(All_Users.this,UserProfile.class);

                        intent.putExtra("uid", model.getUid());
                        startActivity(intent);
                    }
                });

            }
        };
        user_recycler.setAdapter(adapter);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView eventname, eventdesc, eventhost;
        public ImageView event_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            eventname = itemView.findViewById(R.id.eventName);
            event_img=itemView.findViewById(R.id.event_img);

        }

        public LinearLayout getRoot() {
            return root;
        }

        public void setRoot(LinearLayout root) {
            this.root = root;
        }


        public void setEventname(String string) {
            eventname.setText(string);
        }


        public void setEventdesc(String string) {
            eventdesc.setText(string);
        }


        public void setEventhost(String string) {
            eventdesc.setText(string);
        }
        public void setEvent_img(String image){

            Picasso.get().load(image).into(event_img);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
