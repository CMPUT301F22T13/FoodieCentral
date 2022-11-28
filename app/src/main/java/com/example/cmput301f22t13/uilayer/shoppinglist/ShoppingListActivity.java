package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.ActivityShoppingListBinding;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;
import com.example.cmput301f22t13.uilayer.mealplanstorage.MealPlanActivity;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityShoppingListBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shoppingList:
                        return true;
                    case R.id.ingredientStorage:
                        Intent ingredientsIntent = new Intent(ShoppingListActivity.this, IngredientStorageActivity.class);
                        startActivity(ingredientsIntent);
                        return true;
                    case R.id.recipes:
                        Intent recipeIntent = new Intent(ShoppingListActivity.this, RecipeStorageActivity.class);
                        startActivity(recipeIntent);
                        return true;
                    case R.id.mealPlanning:
                        Intent mealPlanningIntent = new Intent(ShoppingListActivity.this, MealPlanActivity.class);
                        startActivity(mealPlanningIntent);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.shoppingList);

        setSupportActionBar(binding.toolbar);

        ArrayList<CountedIngredient> countedIngredients = new ArrayList<>();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_shopping_list);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ShoppingListMainFragment.ARG_COUNTED_INGREDIENT_LIST, countedIngredients);
        navController.setGraph(R.navigation.shopping_list_nav_graph, bundle);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}