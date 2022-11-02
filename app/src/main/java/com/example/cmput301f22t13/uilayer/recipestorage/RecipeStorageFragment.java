package com.example.cmput301f22t13.uilayer.recipestorage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentRecipeStorageBinding;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.util.ArrayList;

public class RecipeStorageFragment extends Fragment {

    private FragmentRecipeStorageBinding binding;

    private ArrayAdapter<RecipeItem> recipeAdapter;
    private ListView recipeList;
    private ArrayList<RecipeItem> recipeDataList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRecipeStorageBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();

        if (bundle != null) {
            recipeDataList = (ArrayList<RecipeItem>) bundle.getSerializable("init_recipes");
        } else {
            Log.d("RecipeStorageFragment", "Binding was null!");
        }

        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (recipeDataList != null) {
            recipeAdapter = new RecipeListArrayAdapter(getActivity(), recipeDataList);
            binding.recipelistview.setAdapter(recipeAdapter);
            binding.recipelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RecipeItem recipe = (RecipeItem) adapterView.getItemAtPosition(i);
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

            //recipeAdapter.notifyDataSetChanged();
        }
    }

/*        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RecipeStorageFragment.this)
                        .navigate(R.id.action);
            }
        });
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
