package com.example.cmput301f22t13.uilayer.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates an array adapter for the list of recipes which are of type {@link RecipeItem}.
 * @author Shiv Chopra
 * @version 1.0
 */

public class RecipeListArrayAdapter extends ArrayAdapter<RecipeItem> {

    /**
     * This variable stores the {@link Context} of the class.
     */
    private Context context;

    /**
     * This variable stores the resource id of type {@link Integer} that is used for instantiating views.
     */
    private int resource;


    /**
     * This is the constructor to create the Recipe list array adapter.
     * @param context This is the current context which is of type {@link Context}
     * @param resource This is the resource id of type {@link Integer}
     * @param objects These are the objects that will be stored in the adapter. Objects is of type {@link ArrayAdapter<RecipeItem>}
     */
    public RecipeListArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RecipeItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    /**
     * This gets the view that will display data.
     * @param position The position of the item within the adapter's data. Position is of type {@link Integer}
     * @param convertView The view of type {@link View}
     * @param parent Of type {@link ViewGroup}
     * @return The return type is {@link View}
     */
   public View getView(int position, View convertView, ViewGroup parent) {
        String recipeTitle = getItem(position).getTitle();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView viewRecipeTitle = (TextView) convertView.findViewById(R.id.recipe_title);
        viewRecipeTitle.setText(recipeTitle);

       return convertView;


   }




}
