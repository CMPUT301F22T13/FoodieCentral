package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
        this.context = context;
        this.items = items;
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
        TextView unit = view.findViewById(R.id.unit_item_list_textview);
        EditText amount = view.findViewById(R.id.amount_item_list_edittext);
        ImageView image = view.findViewById(R.id.picture_item_list_imageview);

        name.setText(item.getName());

        if (item.getPhoto() != null) {
            try {
                byte[] encodedByte = Base64.decode(item.getPhoto(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
                if (bmp != null) {
                    image.setImageBitmap(bmp);
                }
            }
            catch (IllegalArgumentException e) {

            }
        }

        if (item instanceof IngredientItem) {
            unit.setText(((IngredientItem)item).getUnit());
            amount.setText(((IngredientItem)item).getAmount().toString());
        }
        else if (item instanceof RecipeItem) {
            amount.setText(String.valueOf(((RecipeItem)item).getServings()));
            unit.setText("serv");
        }

        // using text watcher to update the amount fields when there is a change in text
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("ItemArrayAdapter", String.valueOf(i2));
                if (charSequence.length() > 0 && i2 > 0) {
                    if (item instanceof IngredientItem) {
                        ((IngredientItem)item).setAmount(Integer.valueOf(charSequence.toString()));
                    }
                    else if (item instanceof RecipeItem) {
                        double oldServings = ((RecipeItem)item).getServings();
                        double newServings = Integer.parseInt(charSequence.toString());
                        Log.d("ItemArrayAdapter", String.valueOf(oldServings));
                        Log.d("ItemArrayAdapter", String.valueOf(newServings));

                        if (oldServings == newServings) {
                            return;
                        }

                        double scalingFactor = newServings / oldServings;
                        ((RecipeItem)item).setServings((int) newServings);

                        for (IngredientItem ingredient : ((RecipeItem)item).getIngredients()) {
                            ingredient.setAmount((int) (ingredient.getAmount() * scalingFactor));
                            Log.d("ItemArrayAdapter", ingredient.getName() + " " + ingredient.getAmount());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}
