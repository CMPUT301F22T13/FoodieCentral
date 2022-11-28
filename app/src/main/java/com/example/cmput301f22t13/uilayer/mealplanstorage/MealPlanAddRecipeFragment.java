package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanAddRecipeBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.datalayer.MealPlanDL;
import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientListAdapter;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeListArrayAdapter;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;

import java.util.ArrayList;

/**
 * {@link Fragment} to add a recipe to a meal plan
 */
public class MealPlanAddRecipeFragment extends Fragment {

    // argument that contains the list of items in the meal plan
    public static final String ARG_ITEM_LIST = "ARG_ITEM_LIST";

    private FragmentMealPlanAddRecipeBinding binding;

    private ArrayList<Item> items;
    private ArrayAdapter<RecipeItem> recipeAdapter;
    private ArrayList<RecipeItem> recipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealPlanAddRecipeBinding.inflate(inflater, container, false);
        items = (ArrayList<Item>) getArguments().getSerializable(ARG_ITEM_LIST);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipes = RecipeDL.getInstance().getStorage();
        recipeAdapter = new RecipeListArrayAdapter(getActivity(), recipes);
        binding.addRecipeMealPlanListview.setAdapter(recipeAdapter);
        binding.addRecipeMealPlanListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedItemColors();
            }
        });

        MealPlanDL.getInstance().listener = new ResultListener() {
            @Override
            public void onSuccess() {
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        binding.confirmAddRecipeMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSelectedRecipesToArray();
                NavHostFragment.findNavController(MealPlanAddRecipeFragment.this).navigateUp();
            }
        });
    }

    private void addSelectedRecipesToArray() {
        // iterate through checked items and add them to the item array if they are not already present
        SparseBooleanArray checkedItems = binding.addRecipeMealPlanListview.getCheckedItemPositions();
        for (int i = 0; i < binding.addRecipeMealPlanListview.getCount(); i++) {
            if (checkedItems.get(i, false)) {
                if (!items.contains((RecipeItem) binding.addRecipeMealPlanListview.getItemAtPosition(i))) {
                    // create deep copy so that changes to the ingredient in meal plan do not affect
                    // the ingredients in the IngredientDL and Ingredient Storage
                    items.add(new RecipeItem((RecipeItem) binding.addRecipeMealPlanListview.getItemAtPosition(i)));
                }
            }
        }
    }

    private void setSelectedItemColors() {
        // iterate through checked items and change colors of each row based on their selected state
        /*SparseBooleanArray checkedItems = binding.addRecipeMealPlanListview.getCheckedItemPositions();
        for (int i = 0; i < binding.addRecipeMealPlanListview.getCount(); i++) {
            View row = binding.addRecipeMealPlanListview.getChildAt(i);
            if (checkedItems.get(i)) {
                row.setBackgroundColor(Color.LTGRAY);
            }
            else {
                row.setBackgroundColor(Color.WHITE);
            }
        }*/
    }
}