package com.example.cmput301f22t13.uilayer.ingredientstorage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.example.cmput301f22t13.databinding.FragmentIngredientStorageMainBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

import java.util.ArrayList;

public class IngredientStorageMainFragment extends Fragment {

    private FragmentIngredientStorageMainBinding binding;

    private ArrayAdapter<IngredientItem> ingredientListAdapter;
    private ListView ingredientListView;
    private ArrayList<IngredientItem> ingredients;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentIngredientStorageMainBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            ingredients = (ArrayList<IngredientItem>) bundle.getSerializable("arraylist");
        }
        else {
            Log.d("IngredientStorageMain", "bundle null");
        }

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ingredients != null) {
            ingredientListAdapter = new IngredientListAdapter(getActivity(), ingredients);
            binding.ingredientListview.setAdapter(ingredientListAdapter);
            binding.ingredientListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IngredientItem item = (IngredientItem) adapterView.getItemAtPosition(i);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AddEditViewIngredientFragment.ARG_INGREDIENT, item);
                    NavHostFragment.findNavController(IngredientStorageMainFragment.this)
                            .navigate(R.id.action_MainIngredient_to_AddEditView, bundle);
                }
            });

            ingredientListAdapter.notifyDataSetChanged();
        }

        binding.addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IngredientStorageMainFragment.this)
                        .navigate(R.id.action_MainIngredient_to_AddEditView);
            }
        });
        Context context = getContext();
        View fragView = view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.content_ingredient_sort_popup, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        binding.sortIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView description = popupView.findViewById(R.id.recipe_sort_title);
                description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView bestBeforeDate = popupView.findViewById(R.id.recipe_sort_preparation_time);
                bestBeforeDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView location = popupView.findViewById(R.id.recipe_sort_servings);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView category = popupView.findViewById(R.id.recipe_sort_category);
                category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                if (!popupWindow.isShowing()) {
                    popupWindow.showAtLocation(fragView, Gravity.BOTTOM, 0, 0);
                }
                else {
                    popupWindow.dismiss(); // close popup if sort button is pressed and the popup is open
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}