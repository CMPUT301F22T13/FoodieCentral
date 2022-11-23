package com.example.cmput301f22t13.uilayer.recipestorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.util.ArrayList;

/**
 * This class creates an array adapter for the list of recipes which are of type {@link RecipeItem}.
 *
 * @author Shiv Chopra
 * @version 1.0
 */

public class RecipeListArrayAdapter extends ArrayAdapter<RecipeItem> {

    /**
     * This variable stores the {@link Context} of the class.
     */
    private Context context;

    /**
     * This variable is an {@link ArrayList<RecipeItem>}.
     */

    private ArrayList<RecipeItem> recipes;

    /**
     * This variable stores the resource id of type {@link Integer} that is used for instantiating views.
     */
    private int resource;

    /**
     * This is the constructor to create the Recipe list array adapter.
     * @param context This is the current context which is of type {@link Context}
     * @param objects These are the objects that will be stored in the adapter. Objects is of type {@link ArrayAdapter<RecipeItem>}
     */
    public RecipeListArrayAdapter(Context context, ArrayList<RecipeItem> objects) {
        super(context, 0, objects);
        this.recipes = objects;
        this.context = context;
    }

    /**
     * This gets the view that will display data.
     * @param position The position of the item within the adapter's data. Position is of type {@link Integer}
     * @param convertView The view of type {@link View}
     * @param parent Of type {@link ViewGroup}
     * @return The return type is {@link View}
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_recipe_list, parent, false);
        }

        RecipeItem item = recipes.get(position);
        TextView recipeName = view.findViewById(R.id.recipe_name_for_view);
        recipeName.setText(item.getTitle());

        ImageView recipeImage = view.findViewById(R.id.recipe_image);

        try {
            // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
            byte[] encodeByte = Base64.decode(item.getPhoto(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
            if (bmp != null) {
                recipeImage.setImageBitmap(bmp);
            }
            else {
                recipeImage.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.d("IngredientListAdapter", "Could not set image bitmap: " + e.getMessage());
        }

        return view;


    }
}
