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
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
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
     * Variable is ingredients of the recipe in string form.
     */
    private ArrayList<String> ingredientsOfRecipeList;


    /**
     * Variable is the Bitmap of the recipe image
     */

    private Bitmap selectedImage;

    /**
     * Variable is the adapter for ingredients list of recipe.
     */
    private ArrayAdapter<String> ingredientsAdapter;

    /**
     * Variable is a list of ingredients.
     */
    private ArrayList<IngredientItem> ingredients;


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
        binding = FragmentAddEditViewRecipeBinding.inflate(inflater, container, false);

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
        binding.listOfIngredients.setAdapter(ingredientsAdapter);
        ingredients = recipe.getIngredients();

        ingredientsAdapter.clear();

        // Set ingredients of recipe.
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientsAdapter.add(ingredients.get(i).getName());
            ingredientsAdapter.notifyDataSetChanged();
        }

        // Select image launcher
        ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    setRecipeImage((Bitmap) result.getData().getExtras().get("data"));
                }
            }
        });
        binding.recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this means that the edit button has been pressed
                // we only want to launch this intent if we have clicked the edit button prior to this
                // when the edit button is clicked, the done visibility turns visible so we know if it has been pressed
                if (binding.saveButton.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    selectImageLauncher.launch(Intent.createChooser(intent, "Take photo"));
                }
            }
        });

        // This code should run when an existing recipe has been passed to the fragment.
        if (!recipe.getTitle().equals("")) {
            listener.recipeSelected(recipe);
            // Set EditText text to current values of recipe attributes.
            binding.recipeNameEdit.setText(recipe.getTitle());
            binding.servingsEdit.setText(String.valueOf(recipe.getServings()));
            binding.preparationTimeEdit.setText(String.valueOf(recipe.getPrepTime()));
            binding.categoryEdit.setText(recipe.getCategory());
            binding.commentsEdit.setText(recipe.getComments());

            if (recipe.getPhoto() != null) {
                // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
                try {
                    byte[] encodeByte = Base64.decode(recipe.getPhoto(), Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    if (bmp != null) {
                        setRecipeImage(bmp);
                    }
                }
                catch (IllegalArgumentException e) {

                }
            }


            // OnClickListener for adding ingredient to a recipe.
            binding.addIngredientToRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this)
                            .navigate(R.id.action_add_ingredient_to_recipe);
                }
            });

            // OnClickListener for deleting a selected ingredient.
            binding.listOfIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    binding.deleteIngredientFromRecipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                ingredientsAdapter.remove(recipe.getIngredients().get(i).getName());
                                IngredientItem ingredientItem = recipe.getIngredients().get(i);
                                ingredientsAdapter.notifyDataSetChanged();
                                listener.onDeletePressed(ingredientItem);
                            } catch (IndexOutOfBoundsException e) {
                            }
                        }
                    });
                }
            });

            int totalHeight = 0;
            for (int i = 0; i < ingredientsAdapter.getCount(); i++) {
                View listItem = ingredientsAdapter.getView(i, null, binding.listOfIngredients);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = binding.listOfIngredients.getLayoutParams();
            params.height = totalHeight + (binding.listOfIngredients.getDividerHeight() * (ingredientsAdapter.getCount() - 1));
            binding.listOfIngredients.setLayoutParams(params);
            binding.listOfIngredients.requestLayout();

            // onClickListener for the save button.
            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = createNewRecipe();
                    listener.onAddDonePressed(newRecipe);
                    NavHostFragment.findNavController(AddEditViewRecipeFragment.this).navigateUp();

                }
            });
        }
        else {
            listener.recipeSelected(recipe);
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
                            .navigate(R.id.action_add_ingredient_to_recipe);
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
        if (selectedImage != null) {
            ByteArrayOutputStream baos = new  ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG,100, baos);
            recipe.setPhoto(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        }

        return newRecipe;
    }

    /**
     * Sets the Recipe Image.
     * @param image Of type {@link Bitmap}
     */
    private void setRecipeImage(Bitmap image) {
        selectedImage = image;
        binding.recipeImageView.setImageBitmap(selectedImage);
    }



    /**
     * Interface of the listener for {@link RecipeItem} changes.
     */
    public interface OnRecipeItemChangedListener {
        void onAddDonePressed(RecipeItem recipe);
        void onDeletePressed(RecipeItem recipe);
        void onDeletePressed(IngredientItem ingredientItem);
        void changeRecipe(RecipeItem oldRecipe, RecipeItem newRecipe);
        void onDonePressed(IngredientItem ingredientItem);
        void recipeSelected(RecipeItem recipe);
    }



}
