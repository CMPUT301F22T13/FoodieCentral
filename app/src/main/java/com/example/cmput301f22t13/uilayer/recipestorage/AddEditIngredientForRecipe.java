package com.example.cmput301f22t13.uilayer.recipestorage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddEditIngredientForRecipeBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

/**
 * This is the class for the fragment that allows for adding/editing an ingredient that will be added to a recipe. It is a subclass of the {@link Fragment} class.
 * Class is responsible for deciding what will be displayed on the fragment and calling methods responsible for Recipe changes.
 *
 * @author Shiv Chopra
 * @version 1.0
 */

public class AddEditIngredientForRecipe extends Fragment {

    /**
     * This variable is responsible for data binding.
     */
    private FragmentAddEditIngredientForRecipeBinding binding;

    /**
     * This variable holds the IngredientItem that's being added or edited.
     */
    private IngredientItem ingredient;

    public static final String INGREDIENT_PASSED = "recipe_passed";


    /**
     * Called to have the fragment instantiate its UI view. Gets ingredient passed to fragment.
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
     * This method is responsible for filling in the name of the ingredient if an existing one is chosen.
     * This method sets up onClickListener for the save button, which updates the ingredient and sends it to the recipe.
     * Calls methods from {@link RecipeStorageActivity} responsible for manipulation of Recipe ingredients.
     *
     * @param view               Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Condition when ingredient is picked from storage.
        if (ingredient != null) {
            // Set EditText text to the current name of the ingredient.
            binding.editIngredientNameForRecipe.setText(ingredient.getName());
        }

        // OnClickListener for the save button creates an ingredient with the user typed attributes and sends it to the recipe.
        // User is navigated back to editing the recipe.
        binding.saveIngredientForRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientItem newIngredient = new IngredientItem();
                if (binding.editIngredientNameForRecipe.getText().toString().equals("")) {
                    newIngredient.setName("Untitled Ingredient");
                }
                else {
                    newIngredient.setName(binding.editIngredientNameForRecipe.getText().toString());
                }
                newIngredient.setDescription(binding.editIngredientDescriptionForRecipe.getText().toString());

                try {
                    newIngredient.setAmount(Double.parseDouble(binding.editIngredientQuantityForRecipe.getText().toString()));
                } catch (NumberFormatException x) {
                    newIngredient.setAmount(0.0);
                }
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