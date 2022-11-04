package com.example.cmput301f22t13.datalayer;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//TODO Figure out where login activity is created and how it would refrence values from FirebaseDL


/** FireBaseDL initiaties connection with Firebase and handles user login,signup and forget password functionalities
 * */

public class FireBaseDL {



    public static FireBaseDL getInstance( ) {
        if (firebasedl == null) {
            firebasedl = new FireBaseDL();
            fstore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
        }
        return firebasedl;
    }

    private static FireBaseDL firebasedl = null;


    protected static FirebaseFirestore fstore;
    protected static FirebaseAuth auth;


    private FireBaseDL() {
    }



    /** Authenticates user(Signs in user) into Firestore email and password authentication
     * @Input: String email - Users email
     *         String password - Users password
     * @return*/
    //TODO passwords passed in should be encrypted/hashed (also dehashed and decrypted) for proper security posture - V2

    public Task<AuthResult> userSignIn(String email, String password){
        //User signs in email and password - adding onComplete to signify valid task completion
        return auth.signInWithEmailAndPassword(email,password);
    }

    /** Sends email to valid user if password is forgotten
     * @Input: String email - Users email for password reset to be sent
     *
     * @return*/

    public Task<Void> userForgotPassword(String email) {
        //user inputs email for password reset prompt to be sent - adding onSuccessListener & onFailureListener to signify valid
        //task completion
        return auth.sendPasswordResetEmail(email);
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
     * @return*/

    //TODO passwords passed in should be encrypted/hashed (also dehashed and decrypted) for proper security posture - V2
    public void userRegister(String email, String password, String name) {
        //User enters email and password for registration - adding onComplete to signify valid task completion
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Storing in firebase - get user id and create a document
                    String userId = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("Users").document(userId);
                    //Creating user data using hash map
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", name);
                    user.put("Email", email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "onSuccess: user profile is created in firebase with uid " + userId);
                        }
                    });
                    //Send to ingredient activity
                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                } else {
                    // Toast.makeText(Register.this, "User not created - error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    /** Logging users out of application
     * */
    public void userLogout(){
        auth.signOut();
    }

};