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
import com.example.cmput301f22t13.databinding.FragmentMealPlanViewBinding;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MealPlanViewFragment extends Fragment {

    public static final String ARG_MEAL_PLAN_ITEM = "ARG_MEAL_PLAN_ITEM";

    private FragmentMealPlanViewBinding binding;

    private MealPlan mealPlanItem;
    private ArrayAdapter<GregorianCalendar> mealPlanDateAdapter;
    private ArrayList<GregorianCalendar> dates;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMealPlanViewBinding.inflate(inflater, container, false);
        mealPlanItem = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN_ITEM);
        dates = new ArrayList<>(mealPlanItem.getMealPlanItems().keySet());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealPlanDateAdapter = new MealPlanDateArrayAdapter(getActivity(), dates);
        binding.mealPlanViewDateListview.setAdapter(mealPlanDateAdapter);
        binding.mealPlanViewDateListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GregorianCalendar date = (GregorianCalendar) adapterView.getItemAtPosition(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanAddEditViewDateFragment.ARG_MEAL_PLAN_DATE_ITEMS, mealPlanItem.getItemsForDay(date));
                NavHostFragment.findNavController(MealPlanViewFragment.this)
                        .navigate(R.id.action_mealPlanViewFragment_to_mealPlanAddEditViewDateFragment, bundle);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        binding.mealPlanViewDateRangeLabel.setText(formatter.format(mealPlanItem.getStartDate().getTime()) + " - " +
                formatter.format(mealPlanItem.getEndDate().getTime()));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}