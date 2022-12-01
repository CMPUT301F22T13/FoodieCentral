package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentShoppingListMainBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.utils.Utils;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Fragment to show shopping list
 */
public class ShoppingListMainFragment extends Fragment {
    private FragmentShoppingListMainBinding binding;
    private ArrayAdapter<CountedIngredient> countedIngredientListAdapter;
    public static final String ARG_COUNTED_INGREDIENT_LIST = "ARG_COUNTED_INGREDIENT_LIST";
    ArrayList<CountedIngredient> countedIngredients;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentShoppingListMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Function to create view
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        binding.purchasedIngredientButton.setVisibility(View.GONE);

        countedIngredients = Utils.populateShoppingList();

        countedIngredientListAdapter = new ShoppingListAdapter(getActivity(), countedIngredients);
        binding.shoppinglistListview.setAdapter(countedIngredientListAdapter);

        binding.shoppinglistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.purchasedIngredientButton.setVisibility(View.VISIBLE);
                CountedIngredient countedIngredient = (CountedIngredient) adapterView.getItemAtPosition(i);

                IngredientItem ingredientToAdd = new IngredientItem();
                ingredientToAdd.setName(countedIngredient.getName());
                ingredientToAdd.setAmount(countedIngredient.getCount());
                ingredientToAdd.setHashId(countedIngredient.getHashId());
                binding.purchasedIngredientButton.setText(Integer.toString(i));

                String buttonText;
                if (ingredientToAdd.getAmount() == 1) {
                    buttonText = "Add " + ingredientToAdd.getAmount() + " " + ingredientToAdd.getName() + " to storage";
                } else {
                    buttonText = "Add " + ingredientToAdd.getAmount() + " " + ingredientToAdd.getName() + "s to storage";
                }
                binding.purchasedIngredientButton.setText(buttonText);

                binding.purchasedIngredientButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        ArrayList<IngredientItem> storedIngredientsDL = IngredientDL.getInstance().getStorage();
                        for (IngredientItem item : storedIngredientsDL) {
                            if (item.getHashId() == ingredientToAdd.getHashId()) {
                                ingredientToAdd.setAmount(ingredientToAdd.getAmount() + item.getAmount());
                            }
                        }

                        IngredientDL.getInstance().firebaseAddEdit(ingredientToAdd);
                        countedIngredients.remove(i);
                        countedIngredientListAdapter.notifyDataSetChanged();
                        binding.purchasedIngredientButton.setVisibility(View.GONE);
                    }
                });
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
    }

    /** onOptionsItemSelected - handles on click events with menu items
     *
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.menuLogout){
            logoutUser();
            return true;

        }
        return false;
    }

    /** logoutUser - signs current user out and sends user to the login page
     *
     * */
    private void logoutUser() {

        //Normal user logout
        Log.d("TAG", "logoutUser: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

    }
}