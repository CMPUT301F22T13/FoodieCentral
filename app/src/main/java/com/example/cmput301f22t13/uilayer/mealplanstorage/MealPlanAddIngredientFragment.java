package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanAddIngredientBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientListAdapter;

import java.util.ArrayList;

/**
 * {@link Fragment} to add an Ingredient to a meal plan
 */
public class MealPlanAddIngredientFragment extends Fragment {

    public static final String ARG_ITEM_LIST = "ARG_ITEM_LIST";

    private FragmentMealPlanAddIngredientBinding binding;

    private ArrayList<Item> items;
    private ArrayAdapter<IngredientItem> ingredientAdapter;
    private ArrayList<IngredientItem> ingredients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMealPlanAddIngredientBinding.inflate(inflater, container, false);
        items = (ArrayList<Item>) getArguments().getSerializable(ARG_ITEM_LIST);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredients = IngredientDL.getInstance().getIngredients();
        ingredientAdapter = new IngredientListAdapter(getActivity(), ingredients);
        binding.addIngredientMealPlanListview.setAdapter(ingredientAdapter);
        binding.addIngredientMealPlanListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedItemColors();
            }
        });

        binding.confirmAddIngredientMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSelectedIngredientsToArray();
                NavHostFragment.findNavController(MealPlanAddIngredientFragment.this).navigateUp();
            }
        });

    }

    private void addSelectedIngredientsToArray() {
        // iterate through checked items and add them to the item array if they are not already present
        SparseBooleanArray checkedItems = binding.addIngredientMealPlanListview.getCheckedItemPositions();
        for (int i = 0; i < binding.addIngredientMealPlanListview.getCount(); i++) {
            if (checkedItems.get(i, false)) {
                boolean contains = false;
                for (Item item : items) {
                    if (item.getHashId().equals(((Item) binding.addIngredientMealPlanListview.getItemAtPosition(i)).getHashId())) {
                        contains = true;
                    }
                }
                if (!contains) {
                    // create deep copy so that changes to the ingredient in meal plan do not affect
                    // the ingredients in the IngredientDL and Ingredient Storage
                    IngredientItem item = new IngredientItem((IngredientItem) binding.addIngredientMealPlanListview.getItemAtPosition(i));
                    item.setHashId(((IngredientItem) binding.addIngredientMealPlanListview.getItemAtPosition(i)).getHashId());
                    items.add(item);
                }
            }
        }
    }

    private void setSelectedItemColors() {
        // iterate through checked items and change colors of each row based on their selected state
        SparseBooleanArray checkedItems = binding.addIngredientMealPlanListview.getCheckedItemPositions();
        for (int i = 0; i < binding.addIngredientMealPlanListview.getCount(); i++) {
            View row = binding.addIngredientMealPlanListview.getChildAt(i);
            if (checkedItems.get(i)) {
                row.setBackgroundColor(Color.LTGRAY);
            }
            else {
                row.setBackgroundColor(Color.WHITE);
            }
        }
    }
}