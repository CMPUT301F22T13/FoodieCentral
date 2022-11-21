package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageMainFragment;

import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<CountedIngredient> {
    private ArrayList<CountedIngredient> countedIngredients;
    private Context context;
    private Button purchasedIngredientButton;

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
        TextView ingredientCount = view.findViewById(R.id.ingredient_count);

        ingredientName.setText(item.getIngredient().getName());
        ingredientCount.setText(Integer.toString(item.getCount()));

//        purchasedIngredientButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                IngredientItem ingredientItem = new IngredientItem();
//                ingredientItem.setName("Created By Shopping List");
//                ingredientItem.setAmount(Integer.parseInt(ingredientCount.getText().toString()));
//
//                IngredientDL.getInstance().ingredientFirebaseAddEdit(ingredientItem);
//            }
//        });

        return view;
    }
}