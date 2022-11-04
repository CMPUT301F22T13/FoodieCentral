package com.example.cmput301f22t13.datalayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmput301f22t13.MyApplication;
import com.example.cmput301f22t13.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//Register user code engulfed with UI logic, refer to xml for UI template



public class Register extends AppCompatActivity {

    EditText userFullname, userEmail, userPassword;
    Button regsiterBtn;
    TextView loginBtn;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseFirestore fstore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initilizers
        userFullname = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        regsiterBtn = findViewById(R.id.registerBtn);
        loginBtn= findViewById(R.id.loginClick);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        //Instantiate firebase firestore
        fstore = FirebaseFirestore.getInstance();


//        //Check if user is a returning user and send to main activity
//        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        //When register is clicked
        regsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userFullname.getText().toString();
                String email = userEmail.getText().toString();
                String password  = userPassword.getText().toString();
                //Email field empty
                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Email is required");
                    return;
                }
                //Password field is empty
                if(TextUtils.isEmpty(password)){
                    userPassword.setError("Password is required");
                }
                //Password length
                if(password.length() < 6){
                    userPassword.setError("Password must be greater than 6 characters");
                }

                //setting progress bar - to show user something is happening
                progressBar.setVisibility(View.VISIBLE);


                FireBaseDL.getFirebaseDL().userRegister(email, password, "", new ResultListener() {
                    @Override
                    public void onSuccess() {
                            progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                    }
                });
//                //register the user in Firebase
//                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
//                            //Storing in firebase - get user id and create a document
//                            userId = auth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fstore.collection("Users").document(userId);
//                            //Creating user data using hash map
//                            Map<String,Object> user = new HashMap<>();
//                            user.put("Name" , name);
//                            user.put("Email", email);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d("TAG", "onSuccess: user profile is created in firebase with uid " + userId);
//                                }
//                            });
//                            //Send to ingredient activity
//                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        }else{
//                            Toast.makeText(Register.this, "User not created - error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
            }
        });

        //Login button on click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}