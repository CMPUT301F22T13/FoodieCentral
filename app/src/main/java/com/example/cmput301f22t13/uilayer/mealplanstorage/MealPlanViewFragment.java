package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanEditBinding;
import com.example.cmput301f22t13.databinding.FragmentMealPlanViewBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MealPlanViewFragment extends Fragment {

    // argument key for the meal plan item passed through the bundle
    public static final String ARG_MEAL_PLAN_ITEM = "ARG_MEAL_PLAN_ITEM";

    private FragmentMealPlanViewBinding binding;

    // meal plan item that is passed in through the bundle
    private MealPlan mealPlanItem;

    private ViewGroup linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealPlanViewBinding.inflate(inflater, container, false);
        mealPlanItem = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN_ITEM);
        linearLayout = (ViewGroup) binding.mealPlanViewLinearLayout;

        addItemsToLayout();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        binding.mealPlanViewDaterangeTextview.setText(formatter.format(mealPlanItem.getStartDate().getTime()) + " - " + formatter.format(mealPlanItem.getEndDate().getTime()));

        binding.mealPlanEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanEditFragment.ARG_MEAL_PLAN_ITEM, mealPlanItem);
                NavHostFragment.findNavController(MealPlanViewFragment.this)
                        .navigate(R.id.action_mealPlanViewFragment_to_mealPlanEditFragment, bundle);
            }
        });


    }

    private void addItemsToLayout() {
        for (Map.Entry<GregorianCalendar, ArrayList<Item>> entry : mealPlanItem.getMealPlanItems().entrySet()) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_meal_plan_view_list_item, binding.mealPlanViewLinearLayout, false);

            TextView date = (TextView) view.findViewById(R.id.mpview_date_title_textview);

            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
            date.setText(formatter.format(entry.getKey().getTime()));

            LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.meal_plan_view_item_linearlayout);
            for (Item item : entry.getValue()) {
                View view2 = LayoutInflater.from(getContext()).inflate(R.layout.content_meal_plan_view_list_item_compact, linearLayout2, false);
                TextView itemText = (TextView) view2.findViewById(R.id.mp_view_compact_item_textview);

                if (item instanceof IngredientItem) {
                    itemText.setText(item.getName() + " - " + ((IngredientItem) item).getAmount() + " " + ((IngredientItem) item).getUnit());
                }
                else {
                    String text = item.getName() + " - " + ((RecipeItem) item).getServings() + " serv";
                    itemText.setText(text);
                }

                linearLayout2.addView(view2);
            }

            binding.mealPlanViewLinearLayout.addView(view);
        }


    }
}