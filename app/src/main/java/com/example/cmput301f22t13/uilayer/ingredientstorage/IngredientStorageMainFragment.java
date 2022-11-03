package com.example.cmput301f22t13.uilayer.ingredientstorage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.ActionBar;
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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentIngredientStorageMainBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.storage.IngredientStorage;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import java.util.ArrayList;
import java.util.Comparator;

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

        /*Bundle bundle = getArguments();
        if (bundle != null) {
            ingredients = (ArrayList<IngredientItem>) bundle.getSerializable("arraylist");
        }
        else {
            Log.d("IngredientStorageMain", "bundle null");
            try {
                ingredients = ((IngredientStorageActivity)getActivity()).getIngredients();
            }
            catch (NullPointerException e) {
                Log.d("IngredientStorageMain", "could not get ingredients from ingredient storage activity");
            }

        }*/
        Log.d("IngredientStorageMain", "onCreateView()");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientListAdapter = new IngredientListAdapter(getActivity(), IngredientStorage.getInstance().getIngredients());
        binding.ingredientListview.setAdapter(ingredientListAdapter);
        binding.ingredientListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IngredientItem item = (IngredientItem) adapterView.getItemAtPosition(i);
                if (getActivity() instanceof RecipeStorageActivity) {
                    ((RecipeStorageActivity)getActivity()).onDonePressed(item);
                    NavHostFragment.findNavController(IngredientStorageMainFragment.this).navigateUp();
                }
                else if (getActivity() instanceof IngredientStorageActivity) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AddEditViewIngredientFragment.ARG_INGREDIENT, item);
                    NavHostFragment.findNavController(IngredientStorageMainFragment.this)
                            .navigate(R.id.action_MainIngredient_to_AddEditView, bundle);

                }
            }
        });


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
                TextView description = popupView.findViewById(R.id.ingredient_sort_description);
                description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView bestBeforeDate = popupView.findViewById(R.id.ingredient_sort_bbf);
                bestBeforeDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView location = popupView.findViewById(R.id.ingredient_sort_location);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: add call to adapter sort
                        popupWindow.dismiss();
                    }
                });

                TextView category = popupView.findViewById(R.id.ingredient_sort_category);
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