package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
        EditText email,password;
    String mailId, pass;
    TextView registerhere;
    Button login_btn;
        FirebaseAuth mAuth;
        String TAG="LOGIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        registerhere=findViewById(R.id.registerhere);

        registerhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new  Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });


        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:{
                mailId = email.getText().toString().trim();
                pass = password.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(mailId,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"Login Successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(Login.this,Homepage.class);
                            intent.putExtra("UID",user.getUid());
                            startActivity(intent);
                            login_btn.setEnabled(false);
                        }
                        else{

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();                }
                    }
                });

            }
        }
    }




}
