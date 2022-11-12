package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanAddEditViewDateBinding;
import com.example.cmput301f22t13.databinding.FragmentMealPlanMainBinding;


public class MealPlanAddEditViewDateFragment extends Fragment {

    public static final String ARG_MEAL_PLAN_DATE_ITEMS = "ARG_MEAL_PLAN_DATE_ITEMS";

    private FragmentMealPlanAddEditViewDateBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealPlanAddEditViewDateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}