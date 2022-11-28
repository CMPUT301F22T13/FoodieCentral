package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanEditBinding;
import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.example.cmput301f22t13.datalayer.MealPlanDL;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Fragment} that allows the user to view details of a meal plan. User can also add ingredients
 * & recipes to a certain day in the meal plan. Recipes and Ingredients can also be removed from
 * each day.
 */
public class MealPlanEditFragment extends Fragment {

    // argument key for the meal plan item passed through the bundle
    public static final String ARG_MEAL_PLAN_ITEM = "ARG_MEAL_PLAN_ITEM";

    private FragmentMealPlanEditBinding binding;

    // meal plan item that is passed in through the bundle
    private MealPlan mealPlanItem;

    // array adapter for the drop down list of dates to select
    private ArrayAdapter<GregorianCalendar> mealPlanDateAdapter;

    // all dates of the meal plan passed into the fragment
    private ArrayList<GregorianCalendar> dates;

    /**
     * We need an array adapter for every day of the meal plan. Key of the map will be the day of
     * the meal plan and the value is the array adapter for that day
     */
    private Map<GregorianCalendar, ArrayAdapter<Item>> itemsArrayAdapters;

    // item selected from the list of ingredients and recipes
    private Item selectedItem;

    // date selected from the dropdown
    private GregorianCalendar selectedDate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMealPlanEditBinding.inflate(inflater, container, false);
        mealPlanItem = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN_ITEM);
        dates = new ArrayList<>(mealPlanItem.getMealPlanItems().keySet());

        itemsArrayAdapters = new HashMap<>();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize the adapters for every date
        for (GregorianCalendar date : dates) {
            itemsArrayAdapters.put(date, new ItemArrayAdapter(getActivity(), mealPlanItem.getItemsForDay(date)));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        binding.mealPlanViewDateRangeLabel.setText(formatter.format(mealPlanItem.getStartDate().getTime()) + " - " +
                formatter.format(mealPlanItem.getEndDate().getTime()));

        mealPlanDateAdapter = new MealPlanDateArrayAdapter(getActivity(), dates);
        binding.selectDateMealPlanSpinnerText.setAdapter(mealPlanDateAdapter);
        binding.selectDateMealPlanSpinnerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
                selectedDate = (GregorianCalendar) adapterView.getItemAtPosition(i);
                binding.mealPlanViewDateListview.setAdapter(itemsArrayAdapters.get(selectedDate));
                itemsArrayAdapters.get(selectedDate).notifyDataSetChanged();
                binding.addRecipeIngredientMealPlanButton.show();
            }
        });

        // don't set these if there is no date selected from the dropdown menu
        if (selectedDate != null) {
            binding.mealPlanViewDateListview.setAdapter(itemsArrayAdapters.get(selectedDate));
            itemsArrayAdapters.get(selectedDate).notifyDataSetChanged();
        }
        else {
            // don't show add button if there is no date selected
            binding.addRecipeIngredientMealPlanButton.setVisibility(View.GONE);
        }

        binding.mealPlanViewDateListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                binding.deleteItemButton.show();
            }
        });

        // only show delete button if an item is selected
        binding.deleteItemButton.setVisibility(View.GONE);
        binding.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mealPlanItem.removeItemForDay(selectedDate, selectedItem);
                selectedItem = null;
                binding.deleteItemButton.hide();
                itemsArrayAdapters.get(selectedDate).notifyDataSetChanged();
            }
        });

        binding.addIngredientMealPlanItemButton.setVisibility(View.GONE);
        binding.addRecipeMealPlanItemButton.setVisibility(View.GONE);
        binding.addIngredientTextview.setVisibility(View.GONE);
        binding.addRecipeTextview.setVisibility(View.GONE);
        binding.addRecipeIngredientMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addIngredientTextview.getVisibility() == View.GONE) {
                    binding.addIngredientMealPlanItemButton.show();
                    binding.addRecipeMealPlanItemButton.show();
                    binding.addIngredientTextview.setVisibility(View.VISIBLE);
                    binding.addRecipeTextview.setVisibility(View.VISIBLE);
                }
                else {
                    binding.addIngredientMealPlanItemButton.hide();
                    binding.addRecipeMealPlanItemButton.hide();
                    binding.addIngredientTextview.setVisibility(View.GONE);
                    binding.addRecipeTextview.setVisibility(View.GONE);
                }
            }
        });

        binding.addIngredientMealPlanItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanAddIngredientFragment.ARG_ITEM_LIST, mealPlanItem.getItemsForDay(selectedDate));
                NavHostFragment.findNavController(MealPlanEditFragment.this)
                        .navigate(R.id.action_mealPlanEditFragment_to_mealPlanAddIngredientFragment, bundle);
            }
        });

        binding.addRecipeMealPlanItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanAddRecipeFragment.ARG_ITEM_LIST, mealPlanItem.getItemsForDay(selectedDate));
                NavHostFragment.findNavController(MealPlanEditFragment.this)
                        .navigate(R.id.action_mealPlanEditFragment_to_mealPlanAddRecipeFragment, bundle);
            }
        });

        binding.doneMealplanEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanDL.getInstance().firebaseAddEdit(mealPlanItem);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}