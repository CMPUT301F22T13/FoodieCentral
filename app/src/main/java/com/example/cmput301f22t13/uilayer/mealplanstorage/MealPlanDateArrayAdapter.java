package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * {@link ArrayAdapter} to display {@link GregorianCalendar}s
 * The getDropdownView method is overridden so this adapter can be used for dropdown menus
 *
 * @author Logan Thimer
 */
public class MealPlanDateArrayAdapter extends ArrayAdapter<GregorianCalendar> {

    private Context context;
    private ArrayList<GregorianCalendar> dates;

    public MealPlanDateArrayAdapter(Context context, ArrayList<GregorianCalendar> dates) {
        super(context, 0, dates);
        this.context = context;
        this.dates = dates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_meal_plan_date_list, parent, false);
        }

        GregorianCalendar date = dates.get(position);
        TextView start = view.findViewById(R.id.meal_plan_date_list_item_textview);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        start.setText(formatter.format(date.getTime()));

        return view;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_meal_plan_date_list, parent, false);
        }

        GregorianCalendar date = dates.get(position);
        TextView start = view.findViewById(R.id.meal_plan_date_list_item_textview);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        start.setText(formatter.format(date.getTime()));

        return view;
    }

    @Override
    public Filter getFilter() {
        // had to do this because the toString method on GregorianCalendar doesn't display relevant info
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
                return formatter.format(((GregorianCalendar) resultValue).getTime());
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }
}
