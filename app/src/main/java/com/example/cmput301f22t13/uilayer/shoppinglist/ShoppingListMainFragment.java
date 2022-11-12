package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301f22t13.databinding.FragmentShoppingListMainBinding;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import java.util.ArrayList;

public class ShoppingListMainFragment extends Fragment {
    private FragmentShoppingListMainBinding binding;
    private ArrayAdapter<CountedIngredient> countedIngredientListAdapter;
    public static final String ARG_COUNTED_INGREDIENT_LIST = "ARG_COUNTED_INGREDIENT_LIST";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentShoppingListMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IngredientItem item1 = new IngredientItem();
        item1.setName("Apple");
        IngredientItem item2 = new IngredientItem();
        item2.setName("Pear");
        CountedIngredient countedIngredient1 = new CountedIngredient(item1, 6);
        CountedIngredient countedIngredient2 = new CountedIngredient(item2, 12);

        ArrayList<CountedIngredient> countedIngredients = new ArrayList<>();
        countedIngredients.add(countedIngredient1);
        countedIngredients.add(countedIngredient2);

        countedIngredientListAdapter = new ShoppingListAdapter(getActivity(), countedIngredients);
        binding.shoppinglistListview.setAdapter(countedIngredientListAdapter);
    }

}