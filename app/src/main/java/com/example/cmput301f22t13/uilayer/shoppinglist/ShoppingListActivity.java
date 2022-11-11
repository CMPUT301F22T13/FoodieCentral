package com.example.cmput301f22t13.uilayer.shoppinglist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.ActivityShoppingListBinding;

public class ShoppingListActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityShoppingListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShoppingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


    }
}