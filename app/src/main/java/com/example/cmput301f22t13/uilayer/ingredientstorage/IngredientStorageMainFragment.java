package com.example.cmput301f22t13.uilayer.ingredientstorage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentIngredientStorageMainBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Main fragment for ingredient storage. Shows a list of all ingredients in the storage and allows
 * the user to add/edit/delete ingredients by calling upon the {@link AddEditViewIngredientFragment}
 *
 * @author Logan Thimer
 */
public class IngredientStorageMainFragment extends Fragment {

    private FragmentIngredientStorageMainBinding binding;
    private IngredientDL ingredientDL = IngredientDL.getInstance();

    public static final String ARG_INGREDIENT_LIST = "ARG_INGREDIENT_LIST";

    private ArrayAdapter<IngredientItem> ingredientListAdapter;
    private ArrayList<IngredientItem> ingredients = ingredientDL.getStorage();

    private PopupWindow popupWindow;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentIngredientStorageMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientListAdapter = new IngredientListAdapter(getActivity(), ingredients);
        binding.ingredientListview.setAdapter(ingredientListAdapter);
        ingredientDL.listener = new ResultListener() {
            @Override
            public void onSuccess() {
                ingredientListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        binding.ingredientListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
                IngredientItem item = (IngredientItem) adapterView.getItemAtPosition(i);
                if (getActivity() instanceof RecipeStorageActivity) {
                    // deep copy so reference is not maintained
                    ((RecipeStorageActivity)getActivity()).onDonePressed(new IngredientItem(item));
                    NavHostFragment.findNavController(IngredientStorageMainFragment.this).navigateUp();
                }
                else if (getActivity() instanceof IngredientStorageActivity) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(IngredientViewFragment.ARG_INGREDIENT, item);
                    NavHostFragment.findNavController(IngredientStorageMainFragment.this)
                            .navigate(R.id.action_IngredientStorageMainFragment_to_ingredientViewFragment, bundle);

                }
            }
        });


        binding.addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                NavHostFragment.findNavController(IngredientStorageMainFragment.this)
                        .navigate(R.id.action_MainIngredient_to_AddEditView);
            }
        });
        Context context = getContext();
        View fragView = view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.content_ingredient_sort_popup, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        binding.sortIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = popupView.findViewById(R.id.ingredient_sort_name);
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingredientListAdapter.sort(new Comparator<IngredientItem>() {
                            @Override
                            public int compare(IngredientItem t1, IngredientItem t2) {
                                return t1.getName().toLowerCase().compareTo(t2.getName().toLowerCase());
                            }
                        });
                        ingredientListAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                TextView description = popupView.findViewById(R.id.ingredient_sort_description);
                description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingredientListAdapter.sort(new Comparator<IngredientItem>() {
                            @Override
                            public int compare(IngredientItem t1, IngredientItem t2) {
                                return t1.getDescription().compareTo(t2.getDescription());
                            }
                        });
                        ingredientListAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                TextView bestBeforeDate = popupView.findViewById(R.id.ingredient_sort_bbd);
                bestBeforeDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingredientListAdapter.sort(new Comparator<IngredientItem>() {
                            @Override
                            public int compare(IngredientItem t1, IngredientItem t2) {
                                if (t1.getBbd() == null && t2.getBbd() == null) {
                                    return 0;
                                }
                                else if (t1.getBbd() == null) {
                                    return 1;
                                }
                                else if (t2.getBbd() == null) {
                                    return -1;
                                }
                                return t1.getBbd().compareTo(t2.getBbd());
                            }
                        });
                        ingredientListAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                TextView location = popupView.findViewById(R.id.ingredient_sort_location);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingredientListAdapter.sort(new Comparator<IngredientItem>() {
                            @Override
                            public int compare(IngredientItem t1, IngredientItem t2) {
                                return t1.getLocation().compareTo(t2.getLocation());
                            }
                        });
                        ingredientListAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

                TextView category = popupView.findViewById(R.id.ingredient_sort_category);
                category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingredientListAdapter.sort(new Comparator<IngredientItem>() {
                            @Override
                            public int compare(IngredientItem t1, IngredientItem t2) {
                                return t1.getCategory().compareTo(t2.getCategory());
                            }
                        });
                        ingredientListAdapter.notifyDataSetChanged();
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