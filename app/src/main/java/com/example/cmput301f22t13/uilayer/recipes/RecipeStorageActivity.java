package com.example.cmput301f22t13.uilayer.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.cmput301f22t13.MainActivity;
import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeStorageActivity extends AppCompatActivity implements ViewRecipeFragment.OnFragmentInteractionListener {

    ListView recipeList;
    ArrayList<RecipeItem> recipeDataList;
    RecipeListArrayAdapter recipeAdapter;
    FloatingActionButton addButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);
        recipeList = findViewById(R.id.recipes_list);

        RecipeItem sampleRecipe = new RecipeItem();
        sampleRecipe.setTitle("Sample Recipe");
        sampleRecipe.setPrepTime(120);
        sampleRecipe.setServings(3);
        sampleRecipe.setComments("This is a sample recipe");
        sampleRecipe.setCategory("Dessert");
        recipeDataList = new ArrayList<>();
        addButton = findViewById(R.id.add_button);


        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getIntent().putExtra("recipePassed", recipeAdapter.getItem(i));
                getIntent().putExtra("mode", "view_mode");
                DialogFragment dialog = ViewRecipeFragment.newInstance();
                dialog.show(getSupportFragmentManager(), "tag");
            }
        }
        );

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntent().putExtra("mode", "add_mode");
                DialogFragment dialog = ViewRecipeFragment.newInstance();
                dialog.show(getSupportFragmentManager(), "add");
            }
        });

        FloatingActionButton sortButton = findViewById(R.id.sort_button);
        Context context = getApplicationContext();
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, sortButton);
                popup.getMenuInflater();
                        //.inflate(R.menu.sort_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                    ///    if (menuItem.getItemId() == byName)
                        return false;
                    }
                });
                popup.show();

            }
        });
        recipeAdapter = new RecipeListArrayAdapter(this, R.layout.recipe_list_content, recipeDataList);
        recipeAdapter.add(sampleRecipe);
        recipeList.setAdapter(recipeAdapter);
    }

    public void updateRecipe(RecipeItem oldRecipe, RecipeItem newRecipe) {
        oldRecipe.setTitle(newRecipe.getTitle());
        oldRecipe.setPrepTime(newRecipe.getPrepTime());
        oldRecipe.setServings(newRecipe.getServings());
        oldRecipe.setComments(newRecipe.getComments());
        oldRecipe.setCategory(newRecipe.getCategory());

        recipeAdapter.notifyDataSetChanged();
    }

    public void sortRecipes() {
        //recipeAdapter.sort()
    }

    public void addRecipe(RecipeItem recipe) {
        recipeAdapter.add(recipe);
        recipeAdapter.notifyDataSetChanged();
        getIntent().removeExtra("mode");

    }

    public void deleteRecipe(RecipeItem recipe) {
        recipeAdapter.remove(recipe);
        recipeAdapter.notifyDataSetChanged();
    }
}