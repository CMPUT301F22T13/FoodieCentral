package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentIngredientViewBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

/**
 * {@link Fragment} to view all details of an ingredient
 *
 * @author Logan Thimer
 */
public class IngredientViewFragment extends Fragment {

    // argument id for passing ingredient through bundle
    public static final String ARG_INGREDIENT = "ARG_INGREDIENT";

    private FragmentIngredientViewBinding binding;

    // the ingredient item to view
    private IngredientItem ingredient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentIngredientViewBinding.inflate(inflater, container, false);

        ingredient = (IngredientItem) getArguments().getSerializable(ARG_INGREDIENT);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (ingredient.getPhoto() != null) {
            // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
            try {
                byte[] encodeByte = Base64.decode(ingredient.getPhoto(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                if (bmp != null) {
                    binding.viewIngredientPictureImageView.setImageBitmap(bmp);
                }
            }
            catch (IllegalArgumentException e) {

            }
        }

        if (ingredient.getName() != null) {
            if (ingredient.getAmount() != null && ingredient.getAmount() != 0.0 && ingredient.getUnit() != null && !ingredient.getUnit().equals("")) {
                binding.viewIngredientHeaderTextview.setText(ingredient.getName() + " - " + ingredient.getAmount() + " " + ingredient.getUnit());
            }
            else {
                binding.viewIngredientHeaderTextview.setText(ingredient.getName());
            }
        }

        if (ingredient.getDescription() != null) {
            binding.viewIngredientDescriptionTextview.setText(ingredient.getDescription());
        }

        if (ingredient.getCategory() != null) {
            binding.viewIngredientCategoryTextview.setText(ingredient.getCategory());
        }

        if (ingredient.getLocation() != null) {
            binding.viewIngredientLocationTextview.setText(ingredient.getLocation());
        }

        if (ingredient.getBbd() != null) {
            binding.viewIngredientBbdTextview.setText(
                    ingredient.getBbd().get(Calendar.YEAR) + "/" +
                            (ingredient.getBbd().get(Calendar.MONTH) + 1) + "/"+
                            ingredient.getBbd().get(Calendar.DAY_OF_MONTH));
        }

        binding.editIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddEditViewIngredientFragment.ARG_INGREDIENT, ingredient);
                NavHostFragment.findNavController(IngredientViewFragment.this)
                        .navigate(R.id.action_ingredientViewFragment_to_AddEditViewIngredientFragment, bundle);
            }
        });
    }


    /** onCreateOptionsMenu - inflates the menu xml file into the action bar
     *
     * */


}

