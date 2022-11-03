package com.example.cmput301f22t13.uilayer.recipestorage;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;

public class AddEditViewRecipeFragment extends Fragment {

    private FragmentAddEditViewRecipeBinding binding;
    private OnRecipeItemChangedListener listener;

    private RecipeItem recipe;
    private View view;
    String[] spinner_values = {"Breakfast", "Lunch", "Dinner", "Dessert"};
    ArrayAdapter<String> adapter;

    public static final String RECIPE_PASSED = "recipe_passed";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeItemChangedListener) {
            listener = (OnRecipeItemChangedListener) context;
        } else {
            throw new RuntimeException(context.toString() + "LISTENER NOT IMPLEMENTED.");
        }
    }

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editButton.setVisibility(View.VISIBLE);
        binding.deleteButton.setVisibility(View.VISIBLE);
        binding.saveButton.setVisibility(View.GONE);

        if (recipe != null) {
            binding.saveButton.setVisibility(View.GONE);

            binding.recipeNameEdit.setText(recipe.getTitle());
            binding.servingsEdit.setText(String.valueOf(recipe.getServings()));
            binding.preparationTimeEdit.setText(String.valueOf(recipe.getPrepTime()));
            String categoryString = recipe.getCategory();
            /*try {
                int spinnerPosition = adapter.getPosition(categoryString);
                binding.categoryEdit.setSelection(spinnerPosition);
            } catch (NullPointerException y) {
                binding.categoryEdit.setSelection(1);*/
            //}
            binding.commentsEdit.setText(recipe.getComments());

            binding.recipeNameEdit.setInputType(InputType.TYPE_NULL);
            binding.preparationTimeEdit.setInputType(InputType.TYPE_NULL);
            binding.servingsEdit.setInputType(InputType.TYPE_NULL);

            binding.recipeNameEdit.setEnabled(false);
            binding.servingsEdit.setEnabled(false);
            binding.preparationTimeEdit.setEnabled(false);
            binding.categoryEdit.setEnabled(false);
            binding.commentsEdit.setEnabled(false);

            binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDeletePressed(recipe);
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this).navigateUp();
                }
            });

            binding.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.editButton.setVisibility(View.GONE);
                    binding.deleteButton.setVisibility(View.GONE);
                    binding.saveButton.setVisibility(View.VISIBLE);
                    binding.recipeNameEdit.setEnabled(true);
                    binding.servingsEdit.setEnabled(true);
                    binding.preparationTimeEdit.setEnabled(true);
                    binding.categoryEdit.setEnabled(true);
                    binding.commentsEdit.setEnabled(true);
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

         /*   binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                    *//*NavHostFragment.findNavController(AddEditViewRecipeFragment.this)});*//*
            });*/

            //TODO ADD INGREDIENTS TO A RECIPE
            binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                            .navigate(R.id.action_addEditViewRecipeFragment_to_ingredient_storage_nav_graph);
                }
            });

            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = createNewRecipe();
                    listener.changeRecipe(recipe, newRecipe);
                }
            });
        }
        else {
            binding.recipeNameEdit.setEnabled(true);
            binding.servingsEdit.setEnabled(true);
            binding.preparationTimeEdit.setEnabled(true);
            binding.categoryEdit.setEnabled(true);
            binding.commentsEdit.setEnabled(true);
            binding.editButton.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.GONE);
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = createNewRecipe();
                    listener.onAddDonePressed(newRecipe);
                }
            });


        }
        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                        .navigate(R.id.action_First2Fragment_to_Second2Fragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

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
        //newRecipe.setCategory(binding.categoryEdit.getSelectedItem().toString());

        // Set Comments attribute.
        newRecipe.setComments(binding.commentsEdit.getText().toString());

        // Set Preparation Time attribute.
        // Illegal value will set preparation time to 0.
        try {
            newRecipe.setPrepTime(Integer.parseInt(String.valueOf(binding.preparationTimeEdit.getText())));
        } catch (NumberFormatException x) {
            newRecipe.setPrepTime(0);
        }

        binding.saveButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.VISIBLE);
        binding.deleteButton.setVisibility(View.VISIBLE);
        binding.recipeNameEdit.setEnabled(false);
        binding.preparationTimeEdit.setEnabled(false);
        binding.servingsEdit.setEnabled(false);
        binding.categoryEdit.setEnabled(false);
        binding.commentsEdit.setEnabled(false);

        return newRecipe;
    }

    public interface OnRecipeItemChangedListener {
        void onAddDonePressed(RecipeItem recipe);
        void onDeletePressed(RecipeItem recipe);
        void changeRecipe(RecipeItem oldRecipe, RecipeItem newRecipe);
    }

}