package com.example.cmput301f22t13.datalayer;

import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

//TODO Figure out where login activity is created and how it would refrence values from FirebaseDL


/** FireBaseDL initiaties connection with Firebase and handles user login,signup and forget password functionalities
 * */

public abstract class FireBaseDL {
    FirebaseAuth auth;
    FirebaseFirestore fstore;


    public FireBaseDL() {
        auth = FirebaseAuth.getInstance();
    }

    /** Authenticates user(Signs in user) into Firestore email and password authentication
     * @Input: String email - Users email
     *         String password - Users password
     * */
    //TODO passwords passed in should be encrypted/hashed (also dehashed and decrypted) for proper security posture - V2

    public void userSignIn(String email, String password){
        //User signs in email and password - adding onComplete to signify valid task completion
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "User authentication is successful");
                } else {
                    Log.d("TAG", "User authentication is unsuccessful");
                }
            }
        });
    }

    /** Sends email to valid user if password is forgotten
     * @Input: String email - Users email for password reset to be sent
     *
     * */

    public void userForgotPassword(String email) {
        //user inputs email for password reset prompt to be sent - adding onSuccessListener & onFailureListener to signify valid
        //task completion
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "Sent email for forgot password");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error in forget password");
            }
        });
    }

    /** Checks if user that opens the app is an already established user in DB
     * */
    public void userReturning() {
        //checks if current user is an already existing user
        if(auth.getCurrentUser()!=null) {
            Log.d("TAG", "This is a returning user");
        }
        else {
            Log.d("TAG", "Not a returning user");
        }
    }


    /** Registers new users in Firestore
     * @Input: String email - User email for registering user,
     *         String password - User password for registering user,
     *         String name - User name for registering user
     * */

    //TODO passwords passed in should be encrypted/hashed (also dehashed and decrypted) for proper security posture - V2
    public void userRegister(String email, String password, String name) {
        //User enters email and password for registration - adding onComplete to signify valid task completion
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG", "User created");
                    //Passing user id to store unique documents for each user in FireStore
                    String userId = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("Users").document(userId);

                    //Storing user name and email in Firestore
                    Map<String,Object> user = new HashMap<>();
                    user.put("Name" , name);
                    user.put("Email", email);

                    //Pushing changes to Firestore - adding onSuccessListener & onFailureListener to signify valid task completion
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "onSuccess: user profile is created in firebase with uid " + userId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "User profile not created" );
                        }
                    });
                }
            }
        });
    };

    /** Logging users out of application
     * */
    public void userLogout(){
        FirebaseAuth.getInstance().signOut();
    }

};