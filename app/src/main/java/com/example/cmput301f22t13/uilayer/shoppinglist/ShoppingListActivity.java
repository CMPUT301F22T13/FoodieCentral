package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityShoppingListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        IngredientItem item1 = new IngredientItem();
        item1.setName("Apple");
        IngredientItem item2 = new IngredientItem();
        item2.setName("Pear");
        CountedIngredient countedIngredient1 = new CountedIngredient(item1, 6);
        CountedIngredient countedIngredient2 = new CountedIngredient(item2, 12);

        ArrayList<CountedIngredient> countedIngredients = new ArrayList<>();
        countedIngredients.add(countedIngredient1);
        countedIngredients.add(countedIngredient2);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_shopping_list);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ShoppingListMainFragment.ARG_COUNTED_INGREDIENT_LIST, countedIngredients);
        navController.setGraph(R.navigation.shopping_list_nav_graph, bundle);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
