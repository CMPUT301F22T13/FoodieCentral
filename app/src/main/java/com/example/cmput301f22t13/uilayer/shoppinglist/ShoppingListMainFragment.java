package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301f22t13.databinding.FragmentShoppingListMainBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

public class ShoppingListMainFragment extends Fragment {
    private FragmentShoppingListMainBinding binding;
    private ArrayAdapter<IngredientItem> ingredientListAdapter;

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

    }

}
