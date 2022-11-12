package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.content.Intent;
import android.os.Bundle;

import com.example.cmput301f22t13.databinding.ActivityIngredientStorageBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

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

        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ingredientStorage:
                        return false;
                    case R.id.recipes:
                        Intent intent = new Intent(IngredientStorageActivity.this, RecipeStorageActivity.class);
                        startActivity(intent);
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

    @Override
    public void onDonePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDonePressed");
        ingredientDL.ingredientFirebaseAddEdit(ingredientItem);
    }

    @Override
    public void onDeletePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDeletePressed");
        ingredientDL.ingredientFirebaseDelete(ingredientItem);
    }

}