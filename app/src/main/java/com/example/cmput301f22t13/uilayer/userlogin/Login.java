package com.example.cmput301f22t13.uilayer.userlogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.datalayer.LoginDL;
import com.example.cmput301f22t13.datalayer.MealPlanDL;
import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.shoppinglist.ShoppingListActivity;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/** UI layer for user Login it contains the following:
 * - EditTexts for getting user email and password
 * - Button for login validate
 * - TextViews to go the Register activity (in case user doesn't have an account) or forgot password steps
 * - ProgressBar to show that a perticular action is being performed
 * - FirebaseAuth - authenticating Firebase and referencing current user
 * */

//TODO couple issues with the login UI - progress bar and toast visibility
public class Login extends AppCompatActivity {
    private EditText loginEmail,loginPassword;
    private Button loginBtn;
    private TextView createBtn, forgotPasswordBtn;
    private ProgressBar loginProgressBar;
    private ImageView googleLogIn;
   // GoogleSignInOptions gso;
    //GoogleSignInClient gsc;
    private LoginDL loginDL = LoginDL.getInstance();
    private Button logoutBtn;

////    @Override
////    protected void onStart() {
////        super.onStart();
////
////        FirebaseUser user = auth.getCurrentUser();
////        if(user !=null){
////            Intent intent = new Intent(getApplicationContext(), IngredientStorageActivity.class);
////            startActivity(intent);
////        }
//    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        //Initializing objects used within the UI
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        logoutBtn = findViewById(R.id.menuShare);
        createBtn = findViewById(R.id.createClick);
        forgotPasswordBtn = findViewById(R.id.forgetPasswordBtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);
       // googleLogIn = findViewById(R.id.googleSignIn);

        //Google firebase authentication
       // gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       // gsc = GoogleSignIn.getClient(this,gso);




       //User clicks the login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets String value for email and password
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();


                //Error checking

                //Empty email passed
                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is required");
                    return;
                }
                //Empty Password passed
                if(TextUtils.isEmpty(password)){
                    loginPassword.setError("Password is required");
                }
                //Password length is less than 6 characters
                if(password.length() < 6){
                    loginPassword.setError("Password must be greater than 6 characters");
                }

                //Progress visible - issue with this
                loginProgressBar.setVisibility(View.INVISIBLE);

                //Authenticates user based on method in FireBaseDL
                loginDL.userSignIn(email,password, new ResultListener() {
                    @Override
                    public void onSuccess() {
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        initalizeDB();
                        startActivity(new Intent(getApplicationContext(), IngredientStorageActivity.class));
                    }
                    @Override
                    public void onFailure(Exception e) {
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Couldn't authenticate you - please create a profile if you don't have one already!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        //Create button clicked - Register activity intent
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });



        //Forgot password clicked - reset password dialog along side reset email conditon
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password? ");
                passwordResetDialog.setMessage("Enter your email to recieve reset link");
                passwordResetDialog.setView(resetEmail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get email and send reset link
                        String email = resetEmail.getText().toString();

                        //Authenticates user based on method in FireBaseDL
                        loginDL.userForgotPassword(email, new ResultListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("TAG", "Email has been sent ");
                                Toast.makeText(Login.this, "Reset Link has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(Login.this, "Password reset Unsuccessful" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }) ;
            passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close diaglog
                    }
                });

                passwordResetDialog.create().show();
            }
        });


    }

    private void initalizeDB() {
        IngredientDL.getInstance();
        RecipeDL.getInstance();
        MealPlanDL.getInstance();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
//        // If you don't have res/menu, just create a directory named "menu" inside res
//        getMenuInflater().inflate(R.menu.mymenu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    // handle button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.menuShare) {
//            // do something here
//
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

}