package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Context;
import android.net.Uri;
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
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;

    public ItemArrayAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_item_list, parent, false);
        }

        Item item = items.get(position);

        TextView name = view.findViewById(R.id.name_item_list_textview);
        TextView amount = view.findViewById(R.id.amount_item_list_textview);
        ImageView image = view.findViewById(R.id.picture_item_list_imageview);

        name.setText(item.getName());
        if (item instanceof IngredientItem) {
            amount.setText(((IngredientItem)item).getAmount());
        }
        else if (item instanceof RecipeItem) {
            amount.setText(((RecipeItem)item).getServings());
        }

        image.setImageURI(Uri.parse(item.getPhoto()));

        return view;
    }
}
