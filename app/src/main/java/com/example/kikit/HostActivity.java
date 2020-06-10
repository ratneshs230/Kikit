package com.example.kikit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,DatePickerDialog.OnDateSetListener,View.OnClickListener {
    EditText host_title, host_desc;
    TextView host_date;
    Button Submit;
    String TAG = "HostEventPAge";
    FirebaseDatabase db;
    private String uid, choice;
    DatabaseReference reff;
    private String Story_category,pushKey;
    private Spinner spinner;
    private String[] category = new String[]{"Events", "Trips", "Dineouts", "Sports"};
    Story_model story;
    public String userName;
    FirebaseAuth mAuth;
    User_model user_model;
    Uri imageUri;
    FirebaseUser thisUser;
    ImageView uploadImage;
    String date;
    StorageReference storageReference, fileref;
    DatabaseReference reference;
    Intent intent;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        try {
            Intent i=getIntent();
            uid = i.getStringExtra("UID");
            Log.w(TAG, "UID RECIEVED-=>" + uid);


            mAuth = FirebaseAuth.getInstance();
            thisUser = mAuth.getCurrentUser();
            userName = thisUser.getDisplayName();
            user_model = new User_model();

            thisUser = FirebaseAuth.getInstance().getCurrentUser();
            storageReference = FirebaseStorage.getInstance().getReference();
            db = FirebaseDatabase.getInstance();

            story = new Story_model();
            reff = db.getReference().child("Story");

            reference = reff.push();
            pushKey = reference.getKey();
            story.setStory_key(pushKey);




            progress=findViewById(R.id.progressBar);
            uploadImage=findViewById(R.id.activity_image);
            host_date = findViewById(R.id.Host_date);
            host_desc = findViewById(R.id.Host_desc);
            host_title = findViewById(R.id.Host_title);
            spinner = findViewById(R.id.spinner1);


            ArrayAdapter<String> adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, category);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            host_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();

                }
            });
            spinner.setAdapter(adapter);

            fileref = storageReference.child(uid)
                    .child("Activity").child(System.currentTimeMillis() + "");


            Log.w(TAG, uid);


            Log.w(TAG, "USerNAme=>" + userName);
            Submit = findViewById(R.id.submit_btn);
            spinner.setOnItemSelectedListener(this);

            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGallery, 1000);
                }
            });
            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.setVisibility(View.VISIBLE);
                    store_data();

                    progress.setVisibility(View.GONE);
                    startActivity(intent);
                    Submit.setEnabled(false);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri=data.getData();
                uploadImage.setImageURI(imageUri);

            }
        }}
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void save_image() {


    }


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this
                , this
                , Calendar.getInstance().get(Calendar.YEAR)
                , Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public void store_data() {
        SharedPreferences pref=getSharedPreferences("Event_preference",MODE_PRIVATE);;
        final SharedPreferences.Editor editor=pref.edit();


        intent = new Intent(HostActivity.this, StoryDisplay.class);

        final String store_title = host_title.getText().toString();
        final String store_desc = host_desc.getText().toString();
        final String store_Date = host_date.getText().toString();
        if (!store_title.isEmpty() && !store_Date.isEmpty()) {
            final String[] path = new String[1];

            try {


                fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.w(TAG, "image Uploaded Successfully");
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                        downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                path[0] = uri.toString();

                                Map<String, Object> imageObject = new HashMap<>();
                                imageObject.put("story_image", path[0]);


                                story.setStory_image(path[0]);

                                Log.w(TAG, "URI=====>>>" + uri);
                                Log.w(TAG, "Path=====>>>" + path[0]);

                                intent.putExtra("image", path[0]);

                                editor.putString("Image",path[0]);

                                reference.updateChildren(imageObject);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Image linking failed");
                                Toast.makeText(HostActivity.this, "Error Uploading File", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });


                Log.w(TAG, "Testing||referenceKey" + pushKey);
                story.setStory_Name(store_title);
                story.setStory_desc(store_desc);
                story.setStory_date(store_Date);
                story.setStory_category(Story_category);
                story.setStory_host(userName);
                story.setUID(uid);
                story.setStory_key(pushKey);
                reference.setValue(story);
                editor.putString("Name",store_title);
                editor.putString("Desc",store_desc);
                editor.putString("Date",store_Date);
                editor.putString("Host",userName);
                editor.apply();


                Toast.makeText(HostActivity.this, "Event Hosted Successfully", Toast.LENGTH_LONG).show();
            intent.putExtra("From","host");
                intent.putExtra("StoryKey",pushKey);
                intent.putExtra("UID", uid);



            } catch (Exception e) {e.printStackTrace();}
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0: {
                Story_category = "Events";
                break;
            }
            case 1: {
                Story_category = "Trips";
                break;
            }
            case 2: {
                Story_category = "Dineouts";
                break;
            }
            case 3: {
                Story_category = "Sports";
                break;
            }
            default: {
                Story_category = "Events";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date=dayOfMonth+"/"+month+"/"+year;
        host_date.setText(date);
    }

    @Override
    public void onClick(View v) {

    }
}