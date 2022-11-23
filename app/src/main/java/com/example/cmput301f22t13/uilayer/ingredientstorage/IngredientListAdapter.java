package com.example.cmput301f22t13.uilayer.ingredientstorage;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * List adapter for displaying ingredient items on ingredient storage activity
 */
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

        try {
            // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
            byte[] encodeByte = Base64.decode(item.getPhoto(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
            if (bmp != null) {
                image.setImageBitmap(bmp);
            }
            else {
                image.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.d("IngredientListAdapter", "Could not set image bitmap: " + e.getMessage());
        }

        return view;
    }
}
