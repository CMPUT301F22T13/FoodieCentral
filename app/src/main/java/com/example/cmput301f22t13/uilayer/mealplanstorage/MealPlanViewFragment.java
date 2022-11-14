package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanViewBinding;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MealPlanViewFragment extends Fragment {

    public static final String ARG_MEAL_PLAN_ITEM = "ARG_MEAL_PLAN_ITEM";

    private FragmentMealPlanViewBinding binding;

    private MealPlan mealPlanItem;
    private ArrayAdapter<GregorianCalendar> mealPlanDateAdapter;
    private ArrayList<GregorianCalendar> dates;

    private Map<GregorianCalendar, ArrayAdapter<Item>> itemsArrayAdapters;
    private Item selectedItem;
    private GregorianCalendar selectedDate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMealPlanViewBinding.inflate(inflater, container, false);
        mealPlanItem = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN_ITEM);
        dates = new ArrayList<>(mealPlanItem.getMealPlanItems().keySet());

        // choose the first date by default if none selected
        if (selectedDate == null) {
            selectedDate = dates.get(0);
        }

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
            }
        });

        binding.mealPlanViewDateListview.setAdapter(itemsArrayAdapters.get(selectedDate));
        binding.mealPlanViewDateListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                binding.deleteItemButton.show();
            }
        });

        itemsArrayAdapters.get(selectedDate).notifyDataSetChanged();

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
                NavHostFragment.findNavController(MealPlanViewFragment.this)
                        .navigate(R.id.action_mealPlanViewFragment_to_mealPlanAddIngredientFragment, bundle);
            }
        });

        binding.addRecipeMealPlanItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanAddRecipeFragment.ARG_ITEM_LIST, mealPlanItem.getItemsForDay(selectedDate));
                NavHostFragment.findNavController(MealPlanViewFragment.this)
                        .navigate(R.id.action_mealPlanViewFragment_to_mealPlanAddRecipeFragment, bundle);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}