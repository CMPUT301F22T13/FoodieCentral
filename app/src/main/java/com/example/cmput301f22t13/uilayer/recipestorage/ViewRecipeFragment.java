package com.example.cmput301f22t13.uilayer.recipestorage;

import android.annotation.SuppressLint;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is the class for View Recipe Fragment. It is a subclass of the {@link Fragment} class.
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
     * Variable is the adapter for ingredients list of recipe.
     */
    private ArrayAdapter<String> ingredientsAdapter;

    /**
     * Variable is a list of ingredients.
     */
    private ArrayList<IngredientItem> ingredients;

    public static final String RECIPE_PASSED = "recipe_passed";
    String imagePath;





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


    )

    {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false);

        //this is the recipe object
        recipe = (RecipeItem) getArguments().getSerializable(RECIPE_PASSED);

        return binding.getRoot();

    }



    /**
     * Called after onCreateView.
     * This method is responsible for assigning the views its corresponding Recipe attribute.
     * This method sets up onClickListeners for the views.
     * Calls methods from {@link RecipeStorageActivity} responsible for manipulation of Recipe list.
     * @param view Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    @SuppressLint("WrongThread")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        setHasOptionsMenu(true);
        // Initializing ingredients adapter.
        ingredientsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);;
        ingredientsOfRecipeList = new ArrayList<String>();
        ingredients = new ArrayList<IngredientItem>();
        binding.viewfragIngredientsOfRecipe.setAdapter(ingredientsAdapter);
        ingredients = recipe.getIngredients();

        ingredientsAdapter.clear();

        // Set ingredients of recipe.
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientsAdapter.add(ingredients.get(i).getAmount() + " " + ingredients.get(i).getUnit() + " " + ingredients.get(i).getName() + " " + ingredients.get(i).getDescription());
            ingredientsAdapter.notifyDataSetChanged();
        }

        int totalHeight = 0;
        for (int i = 0; i < ingredientsAdapter.getCount(); i++) {
            View listItem = ingredientsAdapter.getView(i, null, binding.viewfragIngredientsOfRecipe);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = binding.viewfragIngredientsOfRecipe.getLayoutParams();
        params.height = totalHeight + (binding.viewfragIngredientsOfRecipe.getDividerHeight() * (ingredientsAdapter.getCount() - 1));
        binding.viewfragIngredientsOfRecipe.setLayoutParams(params);
        binding.viewfragIngredientsOfRecipe.requestLayout();

        binding.viewfragEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddEditViewRecipeFragment.RECIPE_PASSED, recipe);
                NavHostFragment.findNavController(ViewRecipeFragment.this)
                        .navigate(R.id.view_frag_to_edit_frag, bundle);
            }
        });

        listener.recipeSelected(recipe);

        // Set TextView text to current values of recipe attributes.
        binding.viewfragRecipeName.setText(recipe.getTitle());
        binding.viewfragServingsAmount.setText(String.valueOf(recipe.getServings()));
        binding.viewfragPreptimeAmount.setText(String.valueOf(recipe.getPrepTime()) + " mins.");
        binding.viewfragCategoryAmount.setText(recipe.getCategory());
        binding.viewfragCommentsAmount.setText(recipe.getComments());
        if (recipe.getPhoto() != null) {
            // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
            try {
                byte[] encodeByte = Base64.decode(recipe.getPhoto(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
  //              ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                imagePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bmp, recipe.getTitle(), "description");
                if (bmp != null) {
                    binding.viewfragRecipeImage.setImageBitmap(bmp);
                }
            }
            catch (IllegalArgumentException e) {

            }
        }

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
    }

    /** onCreateOptionsMenu - inflates the menu xml file into the action bar
     *
     * */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.menuShare);
        MenuItem logout = menu.findItem(R.id.menuLogout);

        logout.setVisible(false);
        item.setVisible(true);
    }

    /** onOptionsItemSelected - handles on click events with menu items
     *
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuShare){
            shareRecipe();
            return true;
        }

        return false;
    }



    /** shareRecipe - shares recipe with other people using (gmail,bluetooth,message or any other app)
     *
     * */
    private void shareRecipe(){
        StringBuilder builder = new StringBuilder();
        builder.append("Recipe name: ");
        builder.append(recipe.getTitle());
        builder.append("\n");
        builder.append("Servings : ");
        builder.append(recipe.getServings());
        builder.append("\n");
        builder.append("Prep time: ");
        builder.append(recipe.getComments());
        builder.append("\n");
        builder.append("Category: ");
        builder.append(recipe.getCategory());
        builder.append("\n");
        builder.append("Comments: ");
        builder.append(recipe.getComments());
        builder.append("\n");
        builder.append("\n");


        builder.append("Ingredients:  ") ;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
             builder.append(ingredients.get(i).getName()) ;
             if(i <= recipe.getIngredients().size() - 2){
                 builder.append(",") ;
             }



        }


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
    //    String url = getString(R.string.deep_link_url) + "?id="+recipe.getHashId();
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Please check the recipe");
        shareIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
        startActivity(Intent.createChooser(shareIntent, "Please choose"));
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
