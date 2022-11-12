package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Intent;
import android.os.Bundle;

import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.databinding.ActivityMealPlanBinding;

import com.example.cmput301f22t13.R;

public class MealPlanActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMealPlanBinding binding;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMealPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ingredientStorage:
                        Intent ingrIntent = new Intent(MealPlanActivity.this, IngredientStorageActivity.class);
                        startActivity(ingrIntent);
                        return true;
                    case R.id.recipes:
                        Intent recipeIntent = new Intent(MealPlanActivity.this, RecipeStorageActivity.class);
                        startActivity(recipeIntent);
                        return true;
                    case R.id.mealPlanning:
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.mealPlanning);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_meal_plan);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_meal_plan);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}