package com.sima.javainstagramclone.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sima.javainstagramclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

   private  ActivityMainBinding binding;
   private FirebaseAuth Auth;
    String kMail;
    String kPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Auth= FirebaseAuth.getInstance();

        FirebaseUser user=Auth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void Signİn(View view){
        kMail=binding.editTextTextEmailAddress.getText().toString();
        kPass=binding.editTextNumberPassword.getText().toString();

      if(kPass.equals(" ")|| kPass.equals(" ")){
          Toast.makeText(MainActivity.this,"field cannot be left blank!",Toast.LENGTH_LONG).show();
      }
      else{
          Auth.signInWithEmailAndPassword(kMail,kPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
              @Override
              public void onSuccess(AuthResult authResult) {
                  Intent intent=new Intent(MainActivity.this, FeedActivity.class);
                  startActivity(intent);
                  finish();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
              }
          });
      }
    }
    public void SignUp(View view){

        kMail=binding.editTextTextEmailAddress.getText().toString();
        kPass=binding.editTextNumberPassword.getText().toString();

        if(kPass.equals(" ") || kMail.equals(" ")){
            Toast.makeText(MainActivity.this,"field cannot be left blank!",Toast.LENGTH_LONG).show();
        }else{
            Auth.createUserWithEmailAndPassword(kMail,kPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_LONG).show();
                    startActivity(intent);

                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }


    }


}

