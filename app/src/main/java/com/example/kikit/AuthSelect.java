package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthSelect extends AppCompatActivity implements View.OnClickListener {
    Button login,register;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_select);
        login=findViewById(R.id.login_btn);
        register=findViewById(R.id.register_btn);
        firebaseAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
          if(user!=null){
              Intent intent=new Intent(AuthSelect.this,Homepage.class);
              startActivity(intent);
              finish();
          }
            }
        };

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn: {
                Intent intent = new Intent(AuthSelect.this, Login.class);
                startActivity(intent);
                break;
            }
            case R.id.register_btn: {
                Intent intent = new Intent(AuthSelect.this, Register.class);
                startActivity(intent);
                break;
            }
        }
    }
}
