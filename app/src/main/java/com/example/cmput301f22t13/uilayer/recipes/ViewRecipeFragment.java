package com.example.cmput301f22t13.uilayer.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cmput301f22t13.MainActivity;
import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.AddEditViewIngredientFragment;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ViewRecipeFragment extends DialogFragment {
    View view;

    EditText recipeName;
    EditText preparationTime;
    EditText servings;
    Spinner category;
    EditText comments;

    FloatingActionButton editButton;
    FloatingActionButton deleteButton;
    Button backButton;
    ArrayAdapter<String> adapter;

    RecipeItem recipe;

    Context context;
    OnFragmentInteractionListener listener;
    String mode;

    ActivityResultLauncher imageSelectLaunch;

    public interface OnFragmentInteractionListener {
        void updateRecipe(RecipeItem oldRecipe, RecipeItem newRecipe);
        void addRecipe(RecipeItem newRecipe);
        void deleteRecipe(RecipeItem recipe);
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
    }

    static public ViewRecipeFragment newInstance() {
        ViewRecipeFragment fragment = new ViewRecipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        recipe = (RecipeItem) intent.getSerializableExtra("recipePassed");
        mode = (String) intent.getSerializableExtra("mode");
        view = inflater.inflate(R.layout.fragment_view_edit_add_recipe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        TextView editRecipeHeading = view.findViewById(R.id.edit_recipe_heading);
        TextView viewRecipeHeading = view.findViewById(R.id.view_recipe_heading);
        TextView addRecipeHeading = view.findViewById(R.id.add_recipe_heading);
        Button createButton = view.findViewById(R.id.create_new);
        Button makeChanges = view.findViewById(R.id.make_changes);

        //FloatingActionButton addIngredientButton = view.findViewById(R.id.recipe_add_ingredient);

        editRecipeHeading.setVisibility(View.GONE);
        addRecipeHeading.setVisibility(View.GONE);
        viewRecipeHeading.setVisibility(View.VISIBLE);

        recipeName = view.findViewById(R.id.recipe_name_edit);
        preparationTime = view.findViewById(R.id.preparation_time_edit);
        servings = view.findViewById(R.id.servings_edit);
        category = view.findViewById(R.id.spinner);
        comments = view.findViewById(R.id.comments_edit);
        backButton = view.findViewById(R.id.back_button_edit);
        editButton = view.findViewById(R.id.edit_button_edit);
        deleteButton = view.findViewById(R.id.delete_button_view);

        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        category = view.findViewById(R.id.spinner);
        String[] spinner_values = {"Breakfast", "Lunch", "Dinner", "Dessert"};

        // Close Fragment when back button is clicked.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Fragment was opened in view/edit mode.
        if (!mode.equals("add_mode")) {
            viewRecipeHeading.setVisibility(View.GONE);
            editRecipeHeading.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.GONE);
            makeChanges.setVisibility(View.GONE);

            // Setting views to hold recipe's attribute values.
            recipeName.setText(recipe.getTitle());
            servings.setText(String.valueOf(recipe.getServings()));
            preparationTime.setText(String.valueOf(recipe.getPrepTime()));
            String categoryString = recipe.getCategory();
            try {
                int spinnerPosition = adapter.getPosition(categoryString);
                category.setSelection(spinnerPosition);
            } catch (NullPointerException y) {
                category.setSelection(1);
            }
            comments.setText(recipe.getComments());

            recipeName.setEnabled(false);
            servings.setEnabled(false);
            preparationTime.setEnabled(false);
            category.setEnabled(false);
            comments.setEnabled(false);

            // Recipe will be deleted if delete button is clicked.
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.deleteRecipe(recipe);
                    dismiss();
                }
            });

            // Enter editing mode for selected recipe.
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    makeChanges.setVisibility(View.VISIBLE);
                    viewRecipeHeading.setVisibility(View.GONE);
                    editRecipeHeading.setVisibility(View.VISIBLE);

                    adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinner_values);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    category.setAdapter(adapter);

                    recipeName.setEnabled(true);
                    servings.setEnabled(true);
                    category.setEnabled(true);
                    comments.setEnabled(true);
                    preparationTime.setEnabled(true);
                    IngredientItem ingredient;
                    /*addIngredientButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            return builder
                                    .setView(R.id.recipe_add_ingredient)
                                    .setTitle("Quick Ingredient Setup")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {*/
  /*                                      @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int cost = (int)(Math.ceil((Double.parseDouble(foodCost.getText().toString()))))
                                            if (toUpdate) {
                                            EditText recipeName = view.findViewById(R.id.new_ingredient_edit);
                                            EditText recipeDescription = view.findViewById(R.id.new_ingredient_description_edit);
                                            ingredient = new IngredientItem(String.valueOf(recipeName.getText()), String.valueOf(recipeDescription.getText()), null, null, null);

                                        }
                                    }
                                    }).create();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(AddEditViewIngredientFragment.ARG_INGREDIENT, ingredient);
                        }
                    });*/

                    // User is done making changes and clicks the DONE button.
                    makeChanges.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create new recipe. Recipe will have new values typed in by user.
                            RecipeItem newRecipe = new RecipeItem();

                            // Set title typed in by user.
                            newRecipe.setTitle(recipeName.getText().toString());

                            // Empty title will default to Untitled Recipe.
                            if (newRecipe.getTitle().equals("")) {
                                newRecipe.setTitle("Untitled Recipe");
                            }

                            // Setting servings attribute.
                            // Illegal value will set servings to 0.
                            try {
                                newRecipe.setServings(Integer.parseInt(String.valueOf(servings.getText())));
                            } catch (NumberFormatException e) {
                                newRecipe.setServings(0);
                            }

                            // Set Category attribute.
                            newRecipe.setCategory(category.getSelectedItem().toString());

                            // Set Comments attribute.
                            newRecipe.setComments(comments.getText().toString());

                            // Set Preparation Time attribute.
                            // Illegal value will set preparation time to 0.
                            try {
                                newRecipe.setPrepTime(Integer.parseInt(String.valueOf(preparationTime.getText())));
                            } catch (NumberFormatException x) {
                                newRecipe.setPrepTime(0);
                            }

                            // Replace old recipe with new recipe.
                            listener.updateRecipe(recipe, newRecipe);

                            // Close fragment.
                            dismiss();
                        }
                    });
                }
            });
        }

        // Fragment was opened in add recipe mode.
        else {
            backButton = view.findViewById(R.id.back_button_edit);
            editRecipeHeading.setVisibility(View.GONE);
            viewRecipeHeading.setVisibility(View.GONE);
            addRecipeHeading.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            makeChanges.setVisibility(View.GONE);
            Button createNewButton = view.findViewById(R.id.create_new);
            createNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeItem newRecipe = new RecipeItem();
                    newRecipe.setTitle(recipeName.getText().toString());
                    try {
                        newRecipe.setServings(Integer.parseInt(String.valueOf(servings.getText())));
                    } catch (NumberFormatException e) {
                        newRecipe.setServings(0);
                    }
                    try {
                        newRecipe.setPrepTime(Integer.parseInt(String.valueOf(preparationTime.getText())));
                    } catch (NumberFormatException x) {
                        newRecipe.setPrepTime(0);
                    }

                    try {
                        String selection = String.valueOf(category.getSelectedItem());
                        newRecipe.setCategory(selection);
                    } catch (NullPointerException y) {
                        category.setSelection(1);
                    }
                    newRecipe.setComments(comments.getText().toString());
                    listener.addRecipe(newRecipe);
                    dismiss();
                }
            });

        }
    }

}