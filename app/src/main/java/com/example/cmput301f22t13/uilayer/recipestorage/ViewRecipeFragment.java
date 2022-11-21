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
import com.example.cmput301f22t13.databinding.FragmentAddEditViewRecipeBinding;
import com.example.cmput301f22t13.databinding.FragmentViewRecipeBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is the class for the Add, Edit, and View Recipe Fragment. It is a subclass of the {@link Fragment} class.
 * Class is responsible for deciding what will be displayed on the fragment and calling methods responsible for Recipe changes.
 *
 * @author Shiv Chopra
 * @version 2.0
 */

public class ViewRecipeFragment extends Fragment {

    /**
     * This variable is responsible for data binding.
     */

    private FragmentViewRecipeBinding binding;

    /**
     * This variable is the listener for Recipe changes.
     */

    private OnRecipeItemChangedListener listener;

    /**
     * This variable holds the recipe that will be manipulated in this fragment by the user.
     */
    private RecipeItem recipe;

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

    private BottomNavigationView navigationView;


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
     * Called to have the fragment instantiate its UI view. Also sets the recipe passed.
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
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false);

        recipe = (RecipeItem) getArguments().getSerializable(RECIPE_PASSED);

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

        // Initializing ingredients adapter.
        ingredientsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);;
        ingredientsOfRecipeList = new ArrayList<String>();
        ingredients = new ArrayList<IngredientItem>();
        binding.viewfragIngredientsOfRecipe.setAdapter(ingredientsAdapter);
        ingredients = recipe.getIngredients();

        ingredientsAdapter.clear();
        // Set ingredients of recipe.
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientsAdapter.add(ingredients.get(i).getName());
            ingredientsAdapter.notifyDataSetChanged();
        }
        ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri image = result.getData().getData();
                    getActivity().getContentResolver().takePersistableUriPermission(image, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    setRecipeImage(image);
                }
                else {
                    Log.d("AddEditViewRecipe", String.valueOf(result.getResultCode()));
                }
            }
        });


        binding.viewfragEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddEditViewRecipeFragment.RECIPE_PASSED, recipe);
                NavHostFragment.findNavController(ViewRecipeFragment.this)
                        .navigate(R.id.view_frag_to_edit_frag, bundle);
            }
        });
        /*binding.viewfragRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.saveButton.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
                }
            }
        });*/

        // This code should run when an existing recipe has been passed to the fragment.
        //if (!recipe.getTitle().equals("")) {
        listener.recipeSelected(recipe);
        // Set EditText text to current values of recipe attributes.
        binding.viewfragRecipeName.setText(recipe.getTitle());
        binding.viewfragServingsAmount.setText(String.valueOf(recipe.getServings()));
        binding.viewfragPreptimeAmount.setText(String.valueOf(recipe.getPrepTime()));
        binding.viewfragCategoryAmount.setText(recipe.getCategory());
        binding.viewfragCommentsAmount.setText(recipe.getComments());
        setRecipeImage(Uri.parse(recipe.getPhoto()));

        /*binding.viewfragEditButton.setVisibility(View.VISIBLE);
        binding.deleteButton.setVisibility(View.VISIBLE);*/

        // As we are currently in View Recipe mode, all EditText should be disabled.
        /*binding.recipeNameEdit.setEnabled(false);
        binding.servingsEdit.setEnabled(false);
        binding.preparationTimeEdit.setEnabled(false);
        binding.categoryEdit.setEnabled(false);
        binding.commentsEdit.setEnabled(false);*/

        // Sets up OnClickListener for the Delete Button.
        binding.viewfragDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calls onDeletePressed to delete the recipe.
                listener.onDeletePressed(recipe);
                // Navigates back to Recipes page after deletion.
                NavHostFragment.findNavController(ViewRecipeFragment.this).navigateUp();
            }
        });

        // Sets up OnClickListener for Edit button.
       /* binding.viewfragEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*binding.addIngredientToRecipe.setVisibility(View.VISIBLE);
                binding.deleteIngredientFromRecipe.setVisibility(View.VISIBLE);
                binding.recipeNameEdit.setEnabled(true);
                binding.servingsEdit.setEnabled(true);
                binding.preparationTimeEdit.setEnabled(true);
                binding.categoryEdit.setEnabled(true);
                binding.commentsEdit.setEnabled(true);
                binding.editButton.setVisibility(View.GONE);
                binding.deleteButton.setVisibility(View.GONE);
                binding.saveButton.setVisibility(View.VISIBLE);*//*
            }
        });*/

        // OnClickListener for adding ingredient to a recipe.
        /*binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                        .navigate(R.id.action_addEditViewRecipeFragment_to_ingredient_storage_nav_graph);
                //TODO ingredientsAdapter.notifyDataSetChanged();
                RecipeItem newRecipe = createNewRecipe();
                listener.changeRecipe(recipe, newRecipe);
            }
        });*/

        // OnClickListener for deleting a selected ingredient.
        /*binding.viewfragIngredientsOfRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });*/

        // onClickListener for the save button.
        /*binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeItem newRecipe = createNewRecipe();
                listener.onAddDonePressed(newRecipe);
                binding.addIngredientToRecipe.setVisibility(View.GONE);
                binding.deleteIngredientFromRecipe.setVisibility(View.GONE);
            }
        });*/
    }
/*        else {

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
                NavHostFragment.findNavController(AddEditViewRecipeFragment.this).navigateUp();

            }
        });

        binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                        .navigate(R.id.action_addEditViewRecipeFragment_to_ingredient_storage_nav_graph);
            }
        });
    }*/

    /**
     * Called when the view has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

 /*  *//* *//**//**
     * Method creates a new {@link RecipeItem} with the attributes specified in the EditText fields.
     * @return Returns the newly created {@link RecipeItem}
     *//*
    public RecipeItem createNewRecipe() {
        RecipeItem newRecipe;
        if (recipe == null)
            newRecipe = new RecipeItem();
        else
            newRecipe = recipe;

        // Set title typed in by user.
        newRecipe.setTitle(binding.viewfragRecipeName.getText().toString());

        // Empty title will default to Untitled Recipe.
        if (newRecipe.getTitle().equals("")) {
            newRecipe.setTitle("Untitled Recipe");
        }

        // Setting servings attribute.
        // Illegal value will set servings to 0.
        try {
            newRecipe.setServings(Integer.parseInt(String.valueOf(binding.viewfragServings.getText())));
        } catch (NumberFormatException e) {
            newRecipe.setServings(0);
        }

        // Set Category attribute.
        newRecipe.setCategory(binding.viewfragCategoryAmount.getText().toString());

        // Set Comments attribute.
        newRecipe.setComments(binding.viewfragComments.getText().toString());

        // Set Preparation Time attribute.
        // Illegal value will set preparation time to 0.
        try {
            newRecipe.setPrepTime(Integer.parseInt(String.valueOf(binding.viewfragPreptimeAmount.getText())));
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
    }
*/
    /**
     * Sets the Recipe Image.
     * @param imageUri Of type {@link Uri}
     */
    private void setRecipeImage(Uri imageUri) {
        // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        try {
            selectedImageUri = imageUri;
            final InputStream imageStream;
            imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            binding.viewfragRecipeImage.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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