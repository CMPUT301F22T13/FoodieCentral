package com.example.cmput301f22t13.datalayer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


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

}

