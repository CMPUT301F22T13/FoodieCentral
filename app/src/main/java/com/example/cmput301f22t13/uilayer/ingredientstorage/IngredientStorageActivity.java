package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.cmput301f22t13.databinding.ActivityIngredientStorageBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.mealplanstorage.MealPlanActivity;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.uilayer.shoppinglist.ShoppingListActivity;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

/** Activity to handle ingredient storage,
 * utilizes 2 fragments for displaying ingredients and adding and editing ingredients
 *
 * */
public class IngredientStorageActivity extends AppCompatActivity implements AddEditViewIngredientFragment.OnIngredientItemChangeListener {
    private IngredientDL ingredientDL = IngredientDL.getInstance();
    private AppBarConfiguration appBarConfiguration;
    private ActivityIngredientStorageBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIngredientStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IngredientDL.getInstance().populateOnStartup();

//        IngredientItem item1 = new IngredientItem();
//        item1.setName("Apple");
//        item1.setDescription("This is an apple");
//        ingredientDL.ingredientStorage.add(item1);
//        IngredientItem item2 = new IngredientItem();
//        item2.setName("Pear");
//        item2.setDescription("This is a pear");
//        ingredientDL.ingredientStorage.add(item1);
        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ingredientStorage:
                        return true;
                    case R.id.recipes:
                        Intent recipesIntent = new Intent(IngredientStorageActivity.this, RecipeStorageActivity.class);
                        startActivity(recipesIntent);
                        return true;
                    case R.id.shoppingList:
                        Intent shoppingListIntent = new Intent(IngredientStorageActivity.this, ShoppingListActivity.class);
                        startActivity(shoppingListIntent);
                        return true;
                    case R.id.mealPlanning:
                        Intent mealPlanIntent = new Intent(IngredientStorageActivity.this, MealPlanActivity.class);
                        startActivity(mealPlanIntent);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.ingredientStorage);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ingredient_storage);

        Bundle bundle = new Bundle();
        bundle.putSerializable(IngredientStorageMainFragment.ARG_INGREDIENT_LIST, ingredientDL.ingredientStorage);
        navController.setGraph(R.navigation.ingredient_storage_nav_graph, bundle);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ingredient_storage);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDonePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDonePressed");
        ingredientDL.firebaseAddEdit(ingredientItem);
    }

    @Override
    public void onDeletePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDeletePressed");
        ingredientDL.firebaseDelete(ingredientItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return  true;
    }


    /** onOptionsItemSelected - handles on click events with menu items
     *
     * */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.menuLogout){
            logoutUser();
            return true;

        }
        return false;
    }

    /** logoutUser - signs current user out and sends user to the login page
     *
     * */

    private void logoutUser() {

        //Normal user logout
        Log.d("TAG", "logoutUser: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        IngredientDL.getInstance().deRegisterListener();
        super.onDestroy();
    }
}