package com.example.cmput301f22t13.uilayer.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/** UI layer for user Register it contains the following:
 * - EditTexts for getting users full name, email and password
 * - Button for register validation
 * - TextView to go the Login activity (in case user already has an account)
 * - ProgressBar to show that a particular action is being performed
 * - FirebaseAuth - authenticating Firebase and referencing current user
 * */

public class Register extends AppCompatActivity {

    EditText userFullName, userEmail, userPassword;
    Button registerBtn;
    TextView loginBtn;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseFirestore fstore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing objects used within the UI
        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn= findViewById(R.id.loginClick);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fstore = FirebaseFirestore.getInstance();


        // Checks if user opening the app is a returning user - based on method in FireBaseDL
        FireBaseDL.getFirebaseDL().userReturning(new ResultListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(Register.this, "Congrats you have logged in again", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Register.this, "Not logged in", Toast.LENGTH_SHORT).show();
            }
        });


        //User clicks the register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets String value for name, email and password
                String name = userFullName.getText().toString();
                String email = userEmail.getText().toString();
                String password  = userPassword.getText().toString();

                //Error checking

                //Empty email passed
                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Email is required");
                    return;
                }
                //Empty Password passed
                if(TextUtils.isEmpty(password)){
                    userPassword.setError("Password is required");
                }
                //Password length is less than 6 characters
                if(password.length() < 6){
                    userPassword.setError("Password must be greater than 6 characters");
                }

                //Progress bar - shows user activity is happening
                progressBar.setVisibility(View.VISIBLE);

                // Registers user based on method in FireBaseDL
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
            }
        });

        //Login button clicked - Login activity intent
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}