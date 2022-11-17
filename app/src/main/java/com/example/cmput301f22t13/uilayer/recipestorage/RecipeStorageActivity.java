package com.example.cmput301f22t13.uilayer.recipestorage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.ActivityRecipeStorageBinding;

import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;


import com.example.cmput301f22t13.domainlayer.storage.RecipeStorage;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This is the Activity class for the Recipe Storage. The class is a subclass of {@link AppCompatActivity} and implements {@link AddEditViewRecipeFragment.OnRecipeItemChangedListener} and {@link AddEditViewIngredientFragment.OnIngredientItemChangeListener}
 * The class inflates the layout and initializes the recipes data.
 *
 * @author Shiv Chopra
 * @version 1.0
 */
public class RecipeStorageActivity extends AppCompatActivity implements AddEditViewRecipeFragment.OnRecipeItemChangedListener, AddEditViewIngredientFragment.OnIngredientItemChangeListener {

    /**
     * This variable is for configuration of the app bar.
     */
    private AppBarConfiguration appBarConfiguration;

    /**
     * This variable is for data binding.
     */
    private ActivityRecipeStorageBinding binding;

    /**
     * This variable holds an array list of {@link RecipeItem} objects.
     */
    private ArrayList<RecipeItem> recipeDataList;

    public static final String RECIPE = "recipe";

    /**
     * Stores recipe selected.
     */
    private RecipeItem recipeSelected;

    private BottomNavigationView bottomNavigationView;

    private RecipeDL recipeDL = RecipeDL.getInstance();

    /**
     * This method performs initialization of all fragments.
     * Also initializes the array list for {@link RecipeItem} objects.
     * @param savedInstanceState Of type {@link Bundle}
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecipeItem sampleRecipe = new RecipeItem();
        sampleRecipe.setTitle("Sample Recipe");
        sampleRecipe.setPrepTime(120);
        sampleRecipe.setServings(3);
        sampleRecipe.setComments("This is a sample recipe");
        sampleRecipe.setCategory("Dessert");

        IngredientItem item1 = new IngredientItem();
        item1.setName("Eggs");

        IngredientItem item2 = new IngredientItem();
        item2.setName("Batter");

        sampleRecipe.addIngredient(item1);
        sampleRecipe.addIngredient(item2);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.recipes:
                        return true;
                    case R.id.ingredientStorage:
                        Intent intentIngr = new Intent(RecipeStorageActivity.this, IngredientStorageActivity.class);
                        startActivity(intentIngr);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.recipes);

        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_storage);
        Bundle bundle = new Bundle();
        bundle.putSerializable("init_recipes", recipeDL.getRecipes());
        navController.setGraph(R.navigation.nav_recipestorage_to_viewrecipe, bundle);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    /**
     * This method is called to navigate up within application's activity hierarchy from the action bar.
     * @return Returns a {@link Boolean}. True if navigation was successful. False otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_storage);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /**
     *  Implementation method for OnRecipeItemChangedListener.
     *  Method adds provided {@link RecipeItem} object to the recipe list.
     * @param recipe The {@link RecipeItem} to be added to the list.
     */
    @Override
    public void onAddDonePressed(RecipeItem recipe) {
        recipeDL.recipeFirebaseAddEdit(recipe);
    }

    /**
     * Method replaces the recipe from the recipe list provided in the first parameter to the recipe in the second parameter.
     * Implementation for method in OnRecipeItemChangedListener interface.
     * @param oldRecipe The recipe to be replaced.
     * @param newRecipe The new recipe that will take the oldRecipe's place.
     */
    @Override
    public void changeRecipe(RecipeItem oldRecipe, RecipeItem newRecipe) {
        recipeDL.getRecipes().set(recipeDL.getRecipes().indexOf(oldRecipe), newRecipe);
    }

    /**
     * Method deletes the provided {@link RecipeItem}
     * Implementation for method in OnRecipeItemChangedListener interface.
     * @param recipe The recipe to be deleted.
     */
    @Override
    public void onDeletePressed(RecipeItem recipe) {
        recipeDL.recipeFirebaseDelete(recipe);

    }

    /**
     * Method saves the recipe selected.
     * @param recipe Of type {@link RecipeItem}
     */
    public void recipeSelected(RecipeItem recipe) {
        this.recipeSelected = recipe;
    }

    /**
     * Method adds the ingredient provided to the recipe selected.
     * @param ingredientItem Of type {@link IngredientItem}
     */
    @Override
    public void onDonePressed(IngredientItem ingredientItem) {
        if (recipeSelected != null && !recipeSelected.getIngredients().contains(ingredientItem)) {
            recipeSelected.addIngredient(ingredientItem);
        }
        // For now
        recipeDL.recipeFirebaseAddEdit(this.recipeSelected);

        Log.d("RecipeActivity", "OnDonePressed");
    }

    @Override
    public void onDeletePressed(IngredientItem ingredientItem) {
    }
}
