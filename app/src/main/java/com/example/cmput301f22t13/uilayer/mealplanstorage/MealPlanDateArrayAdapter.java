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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        start.setText(formatter.format(date.getTime()));

        return view;
    }
}
