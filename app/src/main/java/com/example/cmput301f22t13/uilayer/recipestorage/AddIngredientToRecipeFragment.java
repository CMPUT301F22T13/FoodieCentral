package com.example.cmput301f22t13.uilayer.recipestorage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddIngredientToRecipeBinding;
import com.example.cmput301f22t13.databinding.FragmentRecipeStorageBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientListAdapter;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This is the fragment class for add ingredient to a recipe. It is a subclass of {@link Fragment}
 * It is responsible for the initializing the list adapter and setting OnClickListeners for the list of ingredients and add new button.
 *
 * @author Shiv Chopra
 * @version 1.0
 */
public class AddIngredientToRecipeFragment extends Fragment {

    /**
     * This variable is for data binding.
     */
    private FragmentAddIngredientToRecipeBinding binding;

    /**
     * This variable holds the array adapter for ingredients.
     */
    private ArrayAdapter<IngredientItem> ingredientAdapter;


    /**
     * This variable is a ListView for the ingredients.
     */
    private ListView ingredientList;

    /**
     * This variable is an array of {@link RecipeItem} objects
     */
    private ArrayList<IngredientItem> ingredientDataList;

    private IngredientDL ingredientDL = IngredientDL.getInstance();

    /**
     * This function is called to have the fragment instantiate its UI view.
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
        ingredientDL.listener = new ResultListener() {
            @Override
            public void onSuccess() {
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        binding = FragmentAddIngredientToRecipeBinding.inflate(inflater, container, false);

        ingredientDataList = ingredientDL.getStorage();

        return binding.getRoot();

    }

    /**
     * This function is called after onCreateView.
     * Sets up ingredientAdapter.
     * Sets onClickListeners for the list of ingredients and make new ingredient button.
     * @param view Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ingredientDataList != null) {
            ingredientAdapter = new IngredientListAdapter(getActivity(), ingredientDataList);
            binding.existingIngredients.setAdapter(ingredientAdapter);
            binding.existingIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IngredientItem ingredient = (IngredientItem) adapterView.getItemAtPosition(i);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AddEditIngredientForRecipe.INGREDIENT_PASSED, ingredient);
                    NavHostFragment.findNavController(AddIngredientToRecipeFragment.this)
                            .navigate(R.id.action_addingredienttorecipe_to_editingredientforrecipe, bundle);
                }
            });

            binding.makeNewIngredientForRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(AddIngredientToRecipeFragment.this)
                            .navigate(R.id.action_addingredienttorecipe_to_editingredientforrecipe);

                }
            });
        }
    }

    /** onCreateOptionsMenu - inflates the menu xml file into the action bar
     *
     * */



    /**
     * This function is called when the view created has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}