package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddEditViewIngredientBinding;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddEditViewIngredientFragment extends Fragment {

    public static final String ARG_INGREDIENT = "arg_ingredient";

    private FragmentAddEditViewIngredientBinding binding;

    private OnIngredientItemChangeListener listener;
    private IngredientItem ingredient;

    private DatePickerDialog datePickerDialog;
    private GregorianCalendar selectedDate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIngredientItemChangeListener) {
            listener = (OnIngredientItemChangeListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnIngredientItemChangeListener");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddEditViewIngredientBinding.inflate(inflater, container, false);

        ingredient = (IngredientItem) getArguments().getSerializable(ARG_INGREDIENT);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ingredient != null) {
            binding.doneIngredientButton.setVisibility(View.INVISIBLE);
            if (ingredient.getName() != null) {
                binding.ingredientNameEdittext.setText(ingredient.getName());
            }

            if (ingredient.getDescription() != null) {
                binding.ingredientDescriptionEdittext.setText(ingredient.getDescription());
            }

            if (ingredient.getAmount() != null) {
                binding.ingredientAmountEdittext.setText(ingredient.getAmount().toString());
            }

            if (ingredient.getUnit() != null) {
                binding.ingredientUnitEdittext.setText(ingredient.getUnit());
            }

            if (ingredient.getCategory() != null) {
                binding.ingredientCategoryEdittext.setText(ingredient.getCategory());
            }

            // TODO: Add getLocation and getBestBeforeDate

            binding.ingredientNameEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientDescriptionEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientAmountEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientUnitEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientCategoryEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientLocationEdittext.setInputType(InputType.TYPE_NULL);
        }
        else {
            ingredient = new IngredientItem();
            binding.editIngredientButton.setVisibility(View.GONE);
            binding.deleteIngredientButton.setVisibility(View.GONE);
        }

        binding.doneIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient.setName(binding.ingredientNameEdittext.getText().toString());
                ingredient.setDescription(binding.ingredientDescriptionEdittext.getText().toString());

                // in case nothing was entered in the edit text
                try {
                    ingredient.setAmount(Integer.valueOf(binding.ingredientAmountEdittext.getText().toString()));
                }
                catch(NumberFormatException e) {
                    ingredient.setAmount(0);
                }

                ingredient.setUnit(binding.ingredientUnitEdittext.getText().toString());
                ingredient.setCategory(binding.ingredientCategoryEdittext.getText().toString());
                listener.onDonePressed(ingredient);
                if (getActivity() instanceof RecipeStorageActivity) {
                    NavHostFragment.findNavController(AddEditViewIngredientFragment.this).popBackStack(R.id.addEditViewRecipeFragment, false);
                }
                else {
                    NavHostFragment.findNavController(AddEditViewIngredientFragment.this).popBackStack();
                }

            }
        });

        binding.deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeletePressed(ingredient);
                NavHostFragment.findNavController(AddEditViewIngredientFragment.this).navigateUp();
            }
        });

        binding.editIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.doneIngredientButton.setVisibility(View.VISIBLE);
                binding.ingredientNameEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.ingredientDescriptionEdittext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                binding.ingredientAmountEdittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                binding.ingredientUnitEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.ingredientCategoryEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.ingredientLocationEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.deleteIngredientButton.setVisibility(View.GONE);
                binding.editIngredientButton.setVisibility(View.GONE);
            }
        });

        binding.ingredientBbdEdittext.setInputType(InputType.TYPE_NULL);
        binding.ingredientBbdEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar initialDate = new GregorianCalendar();
                /*if (ingredient.getBestBeforeDate() != null) {
                    initialDate = ingredient.getBestBeforeDate();
                }*/
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        binding.ingredientBbdEdittext.setText(year + "/" + (month + 1) + "/" + day);
                        selectedDate = new GregorianCalendar(year, month, day);
                    }
                }, initialDate.get(Calendar.YEAR), initialDate.get(Calendar.MONTH), initialDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    binding.ingredientImageImageview.setImageURI(result.getData().getData());
                }
                else {
                    Log.d("AddEditViewIngred", String.valueOf(result.getResultCode()));
                }
            }
        });
        binding.ingredientImageImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface OnIngredientItemChangeListener {
        void onDonePressed(IngredientItem ingredientItem);
        void onDeletePressed(IngredientItem ingredientItem);
    }

}