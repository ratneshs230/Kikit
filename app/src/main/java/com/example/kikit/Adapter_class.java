package com.example.kikit;

import android.net.Uri;
import android.util.Log;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_class extends RecyclerView.Adapter<Adapter_class.ViewHolder> {
    String TAG="Adapter_class";
    List<String> d1;
    Context context;
    List<String> img;
    List<String> uid;

    public Adapter_class(Context context,List<String> d1,  List<String> img,List<String> uid) {
        this.d1 = d1;
        this.context = context;
        this.img = img;
        this.uid=uid;
    }

    @NonNull
    @Override
    public Adapter_class.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater inflater= LayoutInflater.from(parent.getContext());

        View view =inflater.inflate(R.layout.cardview, parent, false);
        ViewHolder av=new ViewHolder(view);
        return  av;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.w(TAG,"bindview Reached");
        String name=d1.get(position);
        String image=img.get(position);
        holder.eventname.setText(name);
        Picasso.get().load(image).into(holder.event_img);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(context,UserProfile.class);
                    intent.putExtra("uid",uid.get(position));
                    context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.w(TAG,"GetItemCount=>"+d1.size());
        return d1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public TextView eventname;
        public ImageView event_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.root);
            eventname = itemView.findViewById(R.id.eventName);
            event_img=itemView.findViewById(R.id.event_img);        }

    }
}
