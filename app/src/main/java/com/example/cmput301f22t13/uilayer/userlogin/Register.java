package com.example.cmput301f22t13.uilayer.userlogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

/** UI layer for user Register it contains the following:
 * - EditTexts for getting users full name, email and password
 * - Button for register validation
 * - TextView to go the Login activity (in case user already has an account)
 * - ProgressBar to show that a particular action is being performed
 * - FirebaseAuth - authenticating Firebase and referencing current user
 * */
public class Register extends AppCompatActivity {
    private EditText userFullName, userEmail, userPassword;
    private Button registerBtn;
    private TextView loginBtn;
    private ProgressBar progressBar;
    private FireBaseDL fb = FireBaseDL.getFirebaseDL();
   // GoogleSignInOptions gso;
   // GoogleSignInClient gsc;
   // private ImageView googleSignUp;
  //  private Button googleSignOut;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        //Initializing objects used within the UI
        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn= findViewById(R.id.loginClick);
       // googleSignUp = findViewById(R.id.googleSignUp);
        //googleSignOut = findViewById(R.id.googleLogout);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.registerProgressBar);
        fstore = FirebaseFirestore.getInstance();

        //Google GSO & GSC setup
       // gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
      //  gsc = GoogleSignIn.getClient(this, gso);


        //User clicks the google sign up button
//        googleSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "Login button is clicked");
//                googleSignIn();
//            }
//        });




//        //google user signout
//        googleSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        finish();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Register.this, "cant sign out of google", Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", "cant sign out of google");
//                    }
//                });
//            }
//        });



        // Checks if user opening the app is a returning user - based on method in FireBaseDL
        fb.userReturning(new ResultListener() {
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
                fb.userRegister(email, password, "", new ResultListener() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(getApplicationContext(), IngredientStorageActivity.class));
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

//    private void googleSignIn() {
//        Log.d("TAG", "In google sign in activity");
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent,1000);
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d("TAG", "In Activity result activity");
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                authenticateWithGoogle(account);
//               // startActivity(new Intent(getApplicationContext(), IngredientStorageActivity.class));
//            } catch (ApiException e) {
//                Toast.makeText(this, "Google sign in unsuccessful", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }

//    private void authenticateWithGoogle(GoogleSignInAccount account) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
//
//        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    FirebaseUser user = auth.getCurrentUser();
//                    Intent intent = new Intent(getApplicationContext(), IngredientStorageActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(Register.this, "Authentication with google failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//    }
}