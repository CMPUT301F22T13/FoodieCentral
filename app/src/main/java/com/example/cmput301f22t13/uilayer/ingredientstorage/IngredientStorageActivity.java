package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.os.Bundle;

import com.example.cmput301f22t13.databinding.ActivityIngredientStorageBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.storage.IngredientStorage;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

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

    private IngredientStorage ingredientStorage;
    private ArrayList<IngredientItem> testList;
    private IngredientListAdapter ingredientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIngredientStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ingredientStorage = new IngredientStorage();

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ingredient_storage);

        Bundle bundle = new Bundle();
        bundle.putSerializable("arraylist", ingredientStorage.getIngredients());
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
        if (!getIngredients().contains(ingredientItem)) {
            ingredientStorage.addIngredientToStorage(ingredientItem);
        }
    }

    @Override
    public void onDeletePressed(IngredientItem ingredientItem) {
        Log.d("IngredientStorage", "onDeletePressed");
        ingredientStorage.removeIngredientFromStorage(ingredientItem);
    }

    public ArrayList<IngredientItem> getIngredients() {
        return ingredientStorage.getIngredients();
    }
}