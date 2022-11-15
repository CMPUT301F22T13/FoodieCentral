package com.example.cmput301f22t13.uilayer.recipestorage;

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
import com.example.cmput301f22t13.databinding.FragmentRecipeStorageBinding;
import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This is the fragment class for Recipe Storage. It is a subclass of {@link Fragment}
 * It is responsible for the initializing the list adapter and setting OnClickListener for the Add Recipe button.
 * @version 1.0
 */
public class RecipeStorageFragment extends Fragment {

    /**
     * This variable is for data binding.
     */
    private FragmentRecipeStorageBinding binding;

    /**
     * This variable holds the array adapter for recipes.
     */
    private ArrayAdapter<RecipeItem> recipeAdapter;

    /**
     * This variable is a ListView for the recipes.
     */
    private ListView recipeList;

    /**
     * This variable is an array of {@link RecipeItem} objects
     */
    private ArrayList<RecipeItem> recipeDataList;

    private RecipeDL recipeDL = RecipeDL.getInstance();

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
        recipeDL.listener = new ResultListener() {
            @Override
            public void onSuccess() {
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        binding = FragmentRecipeStorageBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();

        recipeDataList = recipeDL.getRecipes();

        return binding.getRoot();

    }

    /**
     * This function is called after onCreateView
     * @param view Of type {@link View}
     * @param savedInstanceState Of type {@link Bundle}
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (recipeDataList != null) {
            recipeAdapter = new RecipeListArrayAdapter(getActivity(), recipeDataList);
            binding.recipelistview.setAdapter(recipeAdapter);
            binding.recipelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RecipeItem recipe = (RecipeItem) adapterView.getItemAtPosition(i);
                    ((RecipeStorageActivity) getActivity()).recipeSelected(recipe);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AddEditViewRecipeFragment.RECIPE_PASSED, recipe);
                    NavHostFragment.findNavController(RecipeStorageFragment.this)
                            .navigate(R.id.recipe_action_storage_to_view, bundle);
                }
            });

            binding.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AddEditViewRecipeFragment.RECIPE_PASSED, new RecipeItem());
                    NavHostFragment.findNavController(RecipeStorageFragment.this)
                            .navigate(R.id.recipe_action_storage_to_view);

                }
            });
            recipeAdapter.notifyDataSetChanged();

            Context context = getContext();
            View fragView = view;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.content_recipe_sort_popup, null);
            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            binding.sortButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView title = popupView.findViewById(R.id.recipe_sort_title);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recipeAdapter.sort(new Comparator<RecipeItem>() {
                                @Override
                                public int compare(RecipeItem r1, RecipeItem r2) {
                                    return r1.getTitle().compareTo(r2.getTitle());
                                }
                            });
                            recipeAdapter.notifyDataSetChanged();
                            popupWindow.dismiss();
                        }
                    });

                    TextView preparationTime = popupView.findViewById(R.id.recipe_sort_preparation_time);
                    preparationTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recipeAdapter.sort(new Comparator<RecipeItem>() {
                                @Override
                                public int compare(RecipeItem r1, RecipeItem r2) {
                                    if (r1.getPrepTime() > r2.getPrepTime())
                                        return 1;
                                    else if (r1.getPrepTime() < r2.getPrepTime())
                                        return -1;
                                    else
                                        return 0;
                                }
                            });
                            recipeAdapter.notifyDataSetChanged();
                            popupWindow.dismiss();
                        }
                    });

                    TextView servings = popupView.findViewById(R.id.recipe_sort_servings);
                    servings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recipeAdapter.sort(new Comparator<RecipeItem>() {
                                @Override
                                public int compare(RecipeItem r1, RecipeItem r2) {
                                    if (r1.getServings() > r2.getServings())
                                        return 1;
                                    else if (r1.getServings() < r2.getServings())
                                        return -1;
                                    else
                                        return 0;
                                }
                            });
                            recipeAdapter.notifyDataSetChanged();
                            popupWindow.dismiss();
                        }
                    });

                    TextView category = popupView.findViewById(R.id.recipe_sort_category);
                    category.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recipeAdapter.sort(new Comparator<RecipeItem>() {
                                @Override
                                public int compare(RecipeItem r1, RecipeItem r2) {
                                    return r1.getCategory().compareTo(r2.getCategory());
                                }
                            });
                            recipeAdapter.notifyDataSetChanged();
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
    }



    /**
     * This function is called when the view created has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

