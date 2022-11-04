package com.example.cmput301f22t13.uilayer.recipestorage;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.cmput301f22t13.databinding.FragmentAddEditViewRecipeBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;

import java.util.ArrayList;

/**
 * This is the class for the Add, Edit, and View Recipe Fragment. It is a subclass of the {@link Fragment} class.
 * Class is responsible for deciding what will be displayed on the fragment and calling methods responsible for Recipe changes.
 * @version 1.0
 * Ongoing issue: Adding ingredient to existing recipe WORKS. Adding ingredient to new recipe does not work. Will be fixed in the final project.
 */

public class AddEditViewRecipeFragment extends Fragment {

    /**
     * This variable is responsible for data binding.
     */

    private FragmentAddEditViewRecipeBinding binding;

    /**
     * This variable is the listener for Recipe changes.
     */

    private OnRecipeItemChangedListener listener;

    /**
     * This variable holds the recipe that will be manipulated in this fragment by the user.
     */
    private RecipeItem recipe;

    /**
     * This variable holds the view.
     */
    private View view;

    /**
     * Variable is ingredients of the recipe in string form.
     */
    ArrayList<String> ingredientsOfRecipeList;


    /**
     * Variable is the adapter for ingredients list of recipe.
     */
    ArrayAdapter<String> ingredientsAdapter;

    /**
     * Variable is a list of ingredients.
     */
    ArrayList<IngredientItem> ingredients;

    public static final String RECIPE_PASSED = "recipe_passed";

    /**
     * Called when when the fragment is first attached to its context.
     * @param context The context of type {@link Context}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeItemChangedListener) {
            listener = (OnRecipeItemChangedListener) context;
        } else {
            throw new RuntimeException(context.toString() + "LISTENER NOT IMPLEMENTED.");
        }
    }

    /**
     * Called to have the fragment instantiate its UI view.
     * @param inflater Of type {@link LayoutInflater}
     * @param container Of type {@link ViewGroup}
     * @param savedInstanceState Of type {@link Bundle}
     * @return Returns a {@link View}
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddEditViewRecipeBinding.inflate(inflater, container, false);
        try {
            recipe = (RecipeItem) getArguments().getSerializable(RECIPE_PASSED);
        } catch (NullPointerException n) {
            recipe = null;
        }

        return binding.getRoot();

    }

    /**
     * Called after onCreateView.
     * This method is responsible for deciding what views will be displayed depending if an existing recipe has been passed or not.
     * This method sets up onClickListener for the views.
     * Calls methods to create {@link RecipeItem} and calls methods from {@link RecipeStorageActivity} responsible for manipulation of Recipe list.
     * @param view Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setTitle("My new title");
        // Set visibility of buttons for View Recipe page.
        ingredientsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);;
        ingredientsOfRecipeList = new ArrayList<String>();
        ingredients = new ArrayList<IngredientItem>();
        binding.listOfIngredients.setAdapter(ingredientsAdapter);

        binding.editButton.setVisibility(View.VISIBLE);
        binding.deleteButton.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.GONE);
        binding.addIngredientToRecipe.setVisibility(View.GONE);
        binding.deleteIngredientFromRecipe.setVisibility(View.GONE);

        // This code should run when an existing recipe has been passed to the fragment.
        if (recipe != null) {
            listener.recipeSelected(recipe);
            // Set EditText text to current values of recipe attributes.
            binding.recipeNameEdit.setText(recipe.getTitle());
            binding.servingsEdit.setText(String.valueOf(recipe.getServings()));
            binding.preparationTimeEdit.setText(String.valueOf(recipe.getPrepTime()));
            binding.categoryEdit.setText(recipe.getCategory());
            binding.commentsEdit.setText(recipe.getComments());

            // Set ingredients of recipe.
            ArrayList<IngredientItem> ingredients = recipe.getIngredients();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                ingredientsOfRecipeList.add(ingredients.get(i).getName());
                ingredientsAdapter.add(ingredients.get(i).getName());
                ingredientsAdapter.notifyDataSetChanged();
            }

            // As we are currently in View Recipe mode, all EditText should be disabled.
            binding.recipeNameEdit.setEnabled(false);
            binding.servingsEdit.setEnabled(false);
            binding.preparationTimeEdit.setEnabled(false);
            binding.categoryEdit.setEnabled(false);
            binding.commentsEdit.setEnabled(false);

            // Sets up OnClickListener for the Delete Button.
            binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Calls onDeletePressed to delete the recipe.
                    listener.onDeletePressed(recipe);
                    // Navigates back to Recipes page after deletion.
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this).navigateUp();
                }
            });

            // Sets up OnClickListener for Edit button.
            binding.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.addIngredientToRecipe.setVisibility(View.VISIBLE);
                    binding.deleteIngredientFromRecipe.setVisibility(View.VISIBLE);
                    binding.recipeNameEdit.setEnabled(true);
                    binding.servingsEdit.setEnabled(true);
                    binding.preparationTimeEdit.setEnabled(true);
                    binding.categoryEdit.setEnabled(true);
                    binding.commentsEdit.setEnabled(true);
                    binding.editButton.setVisibility(View.GONE);
                    binding.deleteButton.setVisibility(View.GONE);
                    binding.saveButton.setVisibility(View.VISIBLE);
                }
            });

            ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        binding.recipeImageView.setImageURI(result.getData().getData());
                    } else {
                        Log.d("AddEditViewRecipe", String.valueOf(result.getResultCode()));
                    }
                }
            });
            binding.recipeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
                }
            });

            // OnClickListener for adding ingredient to a recipe.
            binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                            .navigate(R.id.action_addEditViewRecipeFragment_to_ingredient_storage_nav_graph);
                    ingredientsAdapter.notifyDataSetChanged();
                    RecipeItem newRecipe = createNewRecipe();
                    listener.changeRecipe(recipe, newRecipe);
                }
            });

            // OnClickListener for deleting a selected ingredient.
            binding.listOfIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    binding.deleteIngredientFromRecipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ingredientsAdapter.remove(recipe.getIngredients().get(i).getName());
                            recipe.deleteIngredient(i);
                            ingredientsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            // onClickListener for the save button.
            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = createNewRecipe();
                    listener.changeRecipe(recipe, newRecipe);
                }
            });
        }
        else {
            recipe = new RecipeItem();
            listener.recipeSelected(recipe);

            binding.recipeNameEdit.setEnabled(true);
            binding.servingsEdit.setEnabled(true);
            binding.preparationTimeEdit.setEnabled(true);
            binding.categoryEdit.setEnabled(true);
            binding.commentsEdit.setEnabled(true);
            binding.editButton.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.addIngredientToRecipe.setVisibility(View.VISIBLE);
            binding.deleteIngredientFromRecipe.setVisibility(View.GONE);
            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = createNewRecipe();
                    listener.onAddDonePressed(newRecipe);
                }
            });

            binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                            .navigate(R.id.action_addEditViewRecipeFragment_to_ingredient_storage_nav_graph);
                    ingredientsAdapter.notifyDataSetChanged();
                }
            });
            }
        }

    /**
     * Called when the view has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Method creates a new {@link RecipeItem} with the attributes specified in the EditText fields.
     * @return Returns the newly created {@link RecipeItem}
     */
    public RecipeItem createNewRecipe() {
        RecipeItem newRecipe = new RecipeItem();

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

        newRecipe.setIngredients(recipe.getIngredients());

        binding.saveButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.deleteButton.setVisibility(View.VISIBLE);
        binding.recipeNameEdit.setEnabled(false);
        binding.preparationTimeEdit.setEnabled(false);
        binding.servingsEdit.setEnabled(false);
        binding.categoryEdit.setEnabled(false);
        binding.commentsEdit.setEnabled(false);
        binding.listOfIngredients.setEnabled(false);

        return newRecipe;
    }

    /**
     * Interface of the listener for {@link RecipeItem} changes.
     */
    public interface OnRecipeItemChangedListener {
        void onAddDonePressed(RecipeItem recipe);
        void onDeletePressed(RecipeItem recipe);
        void changeRecipe(RecipeItem oldRecipe, RecipeItem newRecipe);
        void onDonePressed(IngredientItem ingredientItem);
        void recipeSelected(RecipeItem recipe);
    }

}