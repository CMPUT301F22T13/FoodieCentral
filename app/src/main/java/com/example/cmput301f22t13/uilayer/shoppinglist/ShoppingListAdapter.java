package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.datalayer.ShoppingListDL;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;

import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<CountedIngredient> {
    private ArrayList<CountedIngredient> countedIngredients;
    private Context context;

    public ShoppingListAdapter(Context context, ArrayList<CountedIngredient> countedIngredients) {
        super(context, 0, countedIngredients);
        this.countedIngredients = countedIngredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_shopping_list_list, parent, false);
        }

        CountedIngredient item = countedIngredients.get(position);

        TextView ingredientName = view.findViewById(R.id.counted_ingredient_name_textview);
        TextView ingredientDescription = view.findViewById(R.id.ingredient_count);

        ingredientName.setText(item.getIngredient().getName());
        ingredientDescription.setText(Integer.toString(item.getCount()));

        return view;
    }
}
