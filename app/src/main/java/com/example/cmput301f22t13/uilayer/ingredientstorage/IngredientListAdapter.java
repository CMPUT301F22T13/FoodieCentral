package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

import java.util.ArrayList;
import java.util.Comparator;

public class IngredientListAdapter extends ArrayAdapter<IngredientItem> {

    private ArrayList<IngredientItem> ingredients;
    private Context context;

    public IngredientListAdapter(Context context, ArrayList<IngredientItem> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredient_list, parent, false);
        }

        IngredientItem item = ingredients.get(position);

        TextView ingredientName = view.findViewById(R.id.ingredient_name_textview);
        TextView ingredientDescription = view.findViewById(R.id.ingredient_description_textview);
        ImageView image = view.findViewById(R.id.ingredient_picture_imageview);

        ingredientName.setText(item.getName());
        ingredientDescription.setText(item.getDescription());

        image.setImageURI(Uri.parse(item.getPhoto()));

        return view;
    }
}
