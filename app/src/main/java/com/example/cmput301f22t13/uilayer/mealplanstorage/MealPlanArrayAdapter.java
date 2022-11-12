package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MealPlanArrayAdapter extends ArrayAdapter<MealPlan> {

    private Context context;
    private ArrayList<MealPlan> mealPlans;

    public MealPlanArrayAdapter(Context context, ArrayList<MealPlan> mealPlans) {
        super(context, 0, mealPlans);
        this.context = context;
        this.mealPlans = mealPlans;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_meal_plan_list, parent, false);
        }

        MealPlan item = mealPlans.get(position);
        TextView start = view.findViewById(R.id.meal_plan_list_item_start_date);
        TextView end = view.findViewById(R.id.meal_plan_list_item_end_date);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        start.setText(formatter.format(item.getStartDate().getTime()));
        end.setText(formatter.format(item.getEndDate().getTime()));

        return view;
    }
}
