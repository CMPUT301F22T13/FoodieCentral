package com.example.cmput301f22t13.uilayer.recipestorage;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddEditIngredientForRecipeBinding;
import com.example.cmput301f22t13.databinding.FragmentAddEditViewRecipeBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is the class for the Add and Edit Recipe Fragment. It is a subclass of the {@link Fragment} class.
 * Class is responsible for deciding what will be displayed on the fragment and calling methods responsible for Recipe changes.
 *
 * @author Shiv Chopra
 * @version 3.0
 */

public class AddEditIngredientForRecipe extends Fragment {


    /**
     * This variable is responsible for data binding.
     */

    private FragmentAddEditIngredientForRecipeBinding binding;

    /**
     * This variable is the listener for Recipe changes.
     */

    /**
     * This variable holds the recipe that will be manipulated in this fragment by the user.
     */
    private IngredientItem ingredient;

    /**
     * Variable is ingredients of the recipe in string form.
     */
    private ArrayList<String> ingredientsOfRecipeList;


    /**
     * Variable is the Uri of the recipe image
     */
    private Uri selectedImageUri;

    /**
     * Variable is the adapter for ingredients list of recipe.
     */
    private ArrayAdapter<String> ingredientsAdapter;

    /**
     * Variable is a list of ingredients.
     */
    private ArrayList<IngredientItem> ingredients;


    public static final String INGREDIENT_PASSED = "recipe_passed";


    /**
     * Called to have the fragment instantiate its UI view. Also sets the recipe passed.
     *
     * @param inflater           Of type {@link LayoutInflater}
     * @param container          Of type {@link ViewGroup}
     * @param savedInstanceState Of type {@link Bundle}
     * @return Returns a {@link View}
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAddEditIngredientForRecipeBinding.inflate(inflater, container, false);

        try {
            ingredient = (IngredientItem) getArguments().getSerializable(INGREDIENT_PASSED);
        } catch (NullPointerException e) {
            ingredient = null;
        }
        return binding.getRoot();

    }

    /**
     * Called after onCreateView.
     * This method is responsible for deciding what views will be displayed depending if an existing recipe has been passed or not.
     * This method sets up onClickListener for the views.
     * Calls methods to create {@link RecipeItem} and calls methods from {@link RecipeStorageActivity} responsible for manipulation of Recipe list.
     *
     * @param view               Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ingredient != null) {
            // Set EditText text to current values of recipe attributes.
            binding.editIngredientNameForRecipe.setText(ingredient.getName());
            // binding.editIngredientQuantityForRecipe.setText(ingredient.getAmount());

        }
        binding.saveIngredientForRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientItem newIngredient = new IngredientItem();
                newIngredient.setName(binding.editIngredientNameForRecipe.getText().toString());
                newIngredient.setDescription(binding.editIngredientDescriptionForRecipe.getText().toString());
                newIngredient.setAmount(Integer.parseInt(binding.editIngredientQuantityForRecipe.getText().toString()));
                newIngredient.setUnit(binding.editIngredientUnitForRecipe.getText().toString());
                ((RecipeStorageActivity) getActivity()).onDonePressed(newIngredient);
                NavHostFragment.findNavController(AddEditIngredientForRecipe.this).popBackStack(R.id.addEditViewRecipeFragment, false);

            }
        });
    }

        /**
         * Called when the view has been detached from the fragment.
         */
        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
}

    /**
     * Method creates a new {@link RecipeItem} with the attributes specified in the EditText fields.
     * @return Returns the newly created {@link RecipeItem}
     */
   /* public RecipeItem createNewRecipe() {
        RecipeItem newRecipe;
        if (recipe == null)
            newRecipe = new RecipeItem();
        else
            newRecipe = recipe;

        // Set title typed in by user.
        newRecipe.setTitle(binding.recipeNameEdit.getText().toString());

        // Empty title will default to Untitled Recipe.
        if (newRecipe.getTitle().equals("")) {
            newRecipe.setTitle("Untitled Recipe");
        }

        // Setting servings attribute.
        // Illegal value will set servings to 0.
        try {
            newRecipe.setServings(Integer.parseInt(String.valueOf(binding.servingsEdit.getText())));
        } catch (NumberFormatException e) {
            newRecipe.setServings(0);
        }

        // Set Category attribute.
        newRecipe.setCategory(binding.categoryEdit.getText().toString());

        // Set Comments attribute.
        newRecipe.setComments(binding.commentsEdit.getText().toString());

        // Set Preparation Time attribute.
        // Illegal value will set preparation time to 0.
        try {
            newRecipe.setPrepTime(Integer.parseInt(String.valueOf(binding.preparationTimeEdit.getText())));
        } catch (NumberFormatException x) {
            newRecipe.setPrepTime(0);
        }

        if (recipe != null)
            newRecipe.setIngredients(recipe.getIngredients());

        // Set Image
        if (selectedImageUri != null) {
            newRecipe.setPhoto(selectedImageUri.toString());
        }

        return newRecipe;
    }*/

    /**
     * Sets the Recipe Image.
     * @param imageUri Of type {@link Uri}
     */

    /**
     * Interface of the listener for {@link RecipeItem} changes.
     */

