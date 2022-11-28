package com.example.cmput301f22t13.datalayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/** FireBaseDL initiates connection with Firebase and handles user login,signup and forget password functionalities
 * */

public class FireBaseDL {
    protected FirebaseAuth auth;
    protected FirebaseFirestore fstore;

    /**
     * Gets or creates current instance of the firebase DL
     */
    public FireBaseDL() {
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

    protected void addToFireBase(Map<String, Object> fbobject, DocumentReference doc) {
        doc.set(fbobject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "firebaseAdd works as wanted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "firebaseAdd does not work");
            }
        });
    }

    protected void deleteFromFireBase(DocumentReference doc) {
        doc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "item successfully deleted from Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "item not deleted");
            }
        });
    }

}

