package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    Uri photoUrl;
    String date;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        try {
            Intent i=getIntent();
            userName=i.getStringExtra("userName");

            mAuth = FirebaseAuth.getInstance();
            uid = mAuth.getUid();
            user_model = new User_model();
            thisUser = FirebaseAuth.getInstance().getCurrentUser();
            storageReference = FirebaseStorage.getInstance().getReference();
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




            story = new Story_model();
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
                    store_data();
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



    private void save_image(Uri imageUri) {


    }


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this
                , this
                , Calendar.getInstance().get(Calendar.YEAR)
                , Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void store_data() {

        final String[] path = new String[1];

        story=new Story_model();


        StorageReference fileref=storageReference.child(uid)
                .child("Activity");
        db = FirebaseDatabase.getInstance();

        reff =  db.getReference().child("Story");
        DatabaseReference reference;
        reference=reff.child(uid).push();
        pushKey=reference.getKey();
        story.setStory_key(pushKey);



        final DatabaseReference story_reference=reff.child("Story/"
                +uid);

        Log.w(TAG,"Story_reference=>"+story_reference);


        final Intent intent = new Intent(HostActivity.this, StoryDisplay.class);

        if(imageUri!=null) {
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

                            Log.w(TAG,"URI=====>>>"+uri);
                            Log.w(TAG,"Path=====>>>"+path[0]);

                            intent.putExtra("image",path[0]);


                            story_reference.updateChildren(imageObject);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HostActivity.this, "Error Uploading File", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

                final String store_title = host_title.getText().toString();
                final String store_desc = host_desc.getText().toString();
                final String store_Date = host_date.getText().toString();

                            try {

                        save_image(imageUri);
                            String key=story_reference.getKey();
                            Log.w(TAG,"Testing||referenceKey"+key);
                            Log.w(TAG,"Testing||referenceKey.push()"+story_reference.push().getKey());
                        story.setStory_Name(store_title);
                        story.setStory_desc(store_desc);
                        story.setStory_date(store_Date);
                        story.setStory_category(Story_category);
                        story.setStory_host(userName);
                        story.setUID(mAuth.getCurrentUser().getUid());
                        story.setStory_key(pushKey);
                        story_reference.setValue(story);


                        Toast.makeText(HostActivity.this, "Event Hosted Successfully", Toast.LENGTH_LONG).show();

                        intent.putExtra("title", store_title);
                        intent.putExtra("desc", store_desc);
                        intent.putExtra("date", store_Date);
                        intent.putExtra("username", userName);
                        intent.putExtra("uid", uid);
                        intent.putExtra("StoryKey",pushKey);

                        startActivity(intent);
                    }
                    catch (Exception e)
                    {e.printStackTrace();}
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