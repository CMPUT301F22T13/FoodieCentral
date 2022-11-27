package com.example.cmput301f22t13.uilayer.mealplanstorage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentMealPlanEditBinding;
import com.example.cmput301f22t13.databinding.FragmentMealPlanViewBinding;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 *
 */
public class MealPlanViewFragment extends Fragment {

    // argument key for the meal plan item passed through the bundle
    public static final String ARG_MEAL_PLAN_ITEM = "ARG_MEAL_PLAN_ITEM";

    private FragmentMealPlanViewBinding binding;

    // meal plan item that is passed in through the bundle
    private MealPlan mealPlanItem;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealPlanViewBinding.inflate(inflater, container, false);
        mealPlanItem = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN_ITEM);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        binding.mealPlanEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MealPlanEditFragment.ARG_MEAL_PLAN_ITEM, mealPlanItem);
                NavHostFragment.findNavController(MealPlanViewFragment.this)
                        .navigate(R.id.action_mealPlanViewFragment_to_mealPlanEditFragment, bundle);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.menuLogout){
            logoutUser();
            return true;

        }
        return false;
    }

    private void logoutUser() {

        //Normal user logout
        Log.d("TAG", "logoutUser: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

    }
}