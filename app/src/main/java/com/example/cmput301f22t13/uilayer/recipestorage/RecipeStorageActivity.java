package com.example.cmput301f22t13.uilayer.recipestorage;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.ActivityRecipeStorageBinding;

import com.example.cmput301f22t13.domainlayer.item.RecipeItem;


import com.example.cmput301f22t13.domainlayer.storage.RecipeStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeStorageActivity extends AppCompatActivity implements AddEditViewRecipeFragment.OnRecipeItemChangedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRecipeStorageBinding binding;
    private RecipeStorage recipeStorage;
    private ArrayList<RecipeItem> recipeDataList;

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
        //addButton = findViewById(R.id.add_button);
        recipeDataList = new ArrayList<RecipeItem>();
        recipeDataList.add(sampleRecipe);

        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_storage);
        Bundle bundle = new Bundle();
        bundle.putSerializable("init_recipes", recipeDataList);
        navController.setGraph(R.navigation.nav_recipestorage_to_viewrecipe, bundle);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe_storage);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onAddDonePressed(RecipeItem recipe) {
        recipeDataList.add(recipe);
    }

    @Override
    public void changeRecipe(RecipeItem oldRecipe, RecipeItem newRecipe) {
        recipeDataList.set(recipeDataList.indexOf(oldRecipe), newRecipe);
    }
    @Override
    public void onDeletePressed(RecipeItem recipe) {
        recipeDataList.remove(recipe);

    }
}