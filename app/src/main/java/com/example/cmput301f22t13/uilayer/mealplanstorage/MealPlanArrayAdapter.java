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
import com.example.cmput301f22t13.datalayer.MealPlanDL;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * {@link ArrayAdapter} to display two {@link GregorianCalendar}s as a date range
 *
 * @author Logan Thimer
 */
public class MealPlanArrayAdapter extends ArrayAdapter<MealPlan> {

    private Context context;
    private ArrayList<MealPlan> mealPlans;
    private MealPlanDL mealPlanDL = MealPlanDL.getInstance();

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
        TextView text = view.findViewById(R.id.meal_plan_list_item_start_date);
        TextView unconfigured = view.findViewById(R.id.meal_plan_list_item_unconfigured_textview);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        text.setText(formatter.format(item.getStartDate().getTime()) + " - " + formatter.format(item.getEndDate().getTime()));

        int unconfiguredCount = 0;
        for (ArrayList<Item> vals : item.getMealPlanItems().values()) {
            if (vals.isEmpty()) {
                unconfiguredCount++;
            }
        }

        if (unconfiguredCount > 0) {
            unconfigured.setText(unconfiguredCount + " empty days");
        }
        else {
            unconfigured.setVisibility(View.GONE);
        }

        return view;
    }
}
