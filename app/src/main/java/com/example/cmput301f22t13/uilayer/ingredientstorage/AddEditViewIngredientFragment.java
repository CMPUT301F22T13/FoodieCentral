package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddEditViewIngredientBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.utils.Utils;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Fragment for adding/editing/deleting an ingredient.
 * If there is no ingredient passed in through the bundle, the fragment will assume that it is being
 * used to add an ingredient so a new ingredient item will be created
 *
 * @author Logan Thimer
 */
public class AddEditViewIngredientFragment extends Fragment {

    // argument id for passing ingredient through bundle
    public static final String ARG_INGREDIENT = "arg_ingredient";

    private FragmentAddEditViewIngredientBinding binding;

    private OnIngredientItemChangeListener listener;

    // the ingredient item to be modified/created
    private IngredientItem ingredient;

    private DatePickerDialog datePickerDialog; // used to select a date
    private GregorianCalendar selectedDate;
    private Uri selectedImageUri;

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

    /** Mainfunction that handles creating the fragment and taking user input
     *
     * */
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

            if (ingredient.getPhoto() != null) {
                setIngredientImage(Uri.parse(ingredient.getPhoto()));
            }

            if (ingredient.getLocation() != null) {
                binding.ingredientLocationEdittext.setText(ingredient.getLocation());
            }

            if (ingredient.getBbd() != null) {
                binding.ingredientBbdEdittext.setText(
                        ingredient.getBbd().get(Calendar.YEAR) + "/" +
                                (ingredient.getBbd().get(Calendar.MONTH) + 1) + "/"+
                                ingredient.getBbd().get(Calendar.DAY_OF_MONTH));
            }

            // we want the user to click the edit button first before being able to change the
            // ingredient attributes
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
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                ingredient.setLocation(binding.ingredientLocationEdittext.getText().toString());

                if (selectedDate != null) {
                    ingredient.setBbd(selectedDate);
                }

                if (selectedImageUri != null) {
                    ingredient.setPhoto(selectedImageUri.toString());
                }

                listener.onDonePressed(ingredient);

                if (getActivity() instanceof RecipeStorageActivity) {
                    ((RecipeStorageActivity)getActivity()).onDonePressed(ingredient);
                    NavHostFragment.findNavController(AddEditViewIngredientFragment.this).popBackStack(R.id.addEditViewRecipeFragment, false);
                }
                else if (getActivity() instanceof IngredientStorageActivity) {
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
                // this means that the edit button has been pressed
                // we only want to launch this date picker if we have clicked the edit button prior to this
                // when the edit button is clicked, the done visibility turns visible so we know if it has been pressed
                if (binding.doneIngredientButton.getVisibility() == View.VISIBLE) {
                    // initial date will be the date that appears first when the date picker dialog
                    // first opens
                    GregorianCalendar initialDate = new GregorianCalendar();
                    if (ingredient.getBbd() != null) {
                        initialDate = ingredient.getBbd();
                    }
                    datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            binding.ingredientBbdEdittext.setText(year + "/" + (month + 1) + "/" + day);
                            selectedDate = new GregorianCalendar(year, month, day);
                        }
                    }, initialDate.get(Calendar.YEAR), initialDate.get(Calendar.MONTH), initialDate.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });

        // this launcher should open up a chooser to select an image from the gallery and display it
        ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    setIngredientImage(result.getData().getData());
                }
                else {
                    Log.d("AddEditViewIngred", String.valueOf(result.getResultCode()));
                }
            }
        });
        binding.ingredientImageImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this means that the edit button has been pressed
                // we only want to launch this intent if we have clicked the edit button prior to this
                // when the edit button is clicked, the done visibility turns visible so we know if it has been pressed
                if (binding.doneIngredientButton.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** setIngredientImage -sets the ingredient item image
     *
     * */
    private void setIngredientImage(Uri imageUri) {
        // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        try {
            selectedImageUri = imageUri;
            final InputStream imageStream;
            imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            binding.ingredientImageImageview.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** OnIngredientItemChangeListener - interface for button presses
     *
     * */
    public interface OnIngredientItemChangeListener {
        void onDonePressed(IngredientItem ingredientItem);
        void onDeletePressed(IngredientItem ingredientItem);
    }

}