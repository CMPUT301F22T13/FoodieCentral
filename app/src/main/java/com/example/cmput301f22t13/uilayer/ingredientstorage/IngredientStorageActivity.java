package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.content.Intent;
import android.os.Bundle;

import com.example.cmput301f22t13.databinding.ActivityIngredientStorageBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.storage.IngredientStorage;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientStorageActivity extends AppCompatActivity implements AddEditViewIngredientFragment.OnIngredientItemChangeListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityIngredientStorageBinding binding;

    private ArrayList<IngredientItem> testList;

    private ArrayAdapter<IngredientItem> ingredientListAdapter;
    private ListView ingredientListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIngredientStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IngredientItem item1 = new IngredientItem();
        item1.setName("Apple");
        item1.setDescription("This is an apple");
        IngredientStorage.getInstance().addIngredientToStorage(item1);
        IngredientItem item2 = new IngredientItem();
        item2.setName("Pear");
        item2.setDescription("This is a pear");
        IngredientStorage.getInstance().addIngredientToStorage(item2);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ingredient_storage);

        navController.setGraph(R.navigation.ingredient_storage_nav_graph);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientStorageActivity.this, RecipeStorageActivity.class);
                startActivity(intent);
            }
        });

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
        if (!getIngredients().contains(ingredientItem)) {
            IngredientStorage.getInstance().addIngredientToStorage(ingredientItem);
        }
    }

    @Override
    public void onDeletePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDeletePressed");
        IngredientStorage.getInstance().removeIngredientFromStorage(ingredientItem);
    }

    public ArrayList<IngredientItem> getIngredients() {
        return IngredientStorage.getInstance().getIngredients();
    }
}