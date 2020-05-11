package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class
Register extends AppCompatActivity implements OnClickListener {
        EditText name, email,password;
        Button register_btn;
        FirebaseAuth mAuth;
        FirebaseDatabase db;
        DatabaseReference reff;
    FirebaseUser user;
    TextView loginHere;

    String mailId, pass,Name,pushKey;
    ImageView profilePic;
    User_model user_model;
    String TAG="LOGIN_ACTIVITY";
    StorageReference storageReference;
    Boolean register_status=false;
    Uri imageUri;
    Uri photoUrl;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode==1000){
                if(resultCode== Activity.RESULT_OK){
                    imageUri=data.getData();
                    profilePic.setImageURI(imageUri);
                    Log.w(TAG,"OnSelectionImgUrl=>"+imageUri);

            }
        }}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
                try {
                 name = findViewById(R.id.name);
                    email = findViewById(R.id.email);
                    password = findViewById(R.id.pass);
                    profilePic = findViewById(R.id.profile_pic);
                    register_btn = findViewById(R.id.register_btn);
                loginHere=findViewById(R.id.loginhere);
                    mAuth = FirebaseAuth.getInstance();
                    user_model = new User_model();

                    storageReference = FirebaseStorage.getInstance().getReference();

                    loginHere.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Register.this,Login.class);
                            startActivity(intent);
                        }
                    });

                    register_btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            email_Register();

                        }
                    });

                    profilePic.setOnClickListener(this);




                }catch (Exception e){
                e.printStackTrace();
            }
                    }


        private void email_Register() {
            mailId = email.getText().toString();
            pass = password.getText().toString();
            Name = name.getText().toString();
            Log.w(TAG,"email_RegisterImgUrl=>"+imageUri);
            final String[] uid = new String[1];
            mAuth.createUserWithEmailAndPassword(mailId,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Log.d(TAG,"User Creation Successful");

                       try {
                          user=FirebaseAuth.getInstance().getCurrentUser();
                            uid[0] =user.getUid();

                            if(Name!=null && mailId!=null && pass!=null && uid[0] !=null && imageUri!=null && user!=null) {
                                user_data_save(Name, mailId, pass, uid[0], imageUri, user);
                                register_status=true;

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please enter all entities including Image",Toast.LENGTH_LONG).show();
                            }

                       }
                       catch (Exception e){
                           Log.w(TAG,"Exception in user=>"+e);
                       }

                    }
                    else{

                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();                }
                }
            });

        }
/*private void getDownloadUrl(StorageReference storageReference){
        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setUserProfileImage(uri);
                    }
                });
}*/
private void setUserProfileImage(Uri uri){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Register.this,"Image Updated.",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Profile image failed to download", Toast.LENGTH_SHORT).show();
            }
        });
}


    private void user_data_save(String name, String email, final String password, final String uid, Uri image, FirebaseUser user) {
        final StorageReference fileref=storageReference.child("profile Images").child(uid+"jpeg");
        db = FirebaseDatabase.getInstance();
        reff = db.getReference().child("User");
        DatabaseReference reference;
        reference=reff.child(uid).push();
        pushKey=reference.getKey();
        user_model.setUser_key(pushKey);



        setUserProfileImage(image);

        fileref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.w(TAG,"image Uploaded Successfully");
                Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl();
                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String path=uri.toString();
                    reff.child(uid).child(user_model.getUser_key()).child("profilePic_url").setValue(path);
                        user_model.setPhoto_url(uri);
                    }

                });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,"Error Uploading File",Toast.LENGTH_LONG).show();
            }
        });

        user_model.setName(name);
        user_model.setEmail(mailId);
        user_model.setPassword(password);
        user_model.setUid(uid);




        reference.setValue(user_model);


        Intent intent = new Intent(Register.this, Homepage.class);
        intent.putExtra("UID", uid);
        intent.putExtra("userName",user_model.getName());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.profile_pic: {

                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
                break;
                    }
        }

    }
}
