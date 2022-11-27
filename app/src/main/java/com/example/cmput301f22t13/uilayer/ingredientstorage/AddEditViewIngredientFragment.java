package com.example.cmput301f22t13.uilayer.ingredientstorage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cmput301f22t13.R;
import com.example.cmput301f22t13.databinding.FragmentAddEditViewIngredientBinding;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.utils.Utils;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;
import com.example.cmput301f22t13.uilayer.userlogin.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private Bitmap selectedImage;




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
    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (ingredient != null) {
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
                // https://stackoverflow.com/questions/57476796/how-to-convert-bitmap-type-to-string-type
                try {
                    byte[] encodeByte = Base64.decode(ingredient.getPhoto(), Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    if (bmp != null) {
                        setIngredientImage(bmp);
                    }
                }
                catch (IllegalArgumentException e) {

                }
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

            // don't want recipe storage to delete ingredient
            if (getActivity() instanceof RecipeStorageActivity) {
                binding.deleteIngredientButton.setVisibility(View.GONE);
            }

            // we want the user to click the edit button first before being able to change the
            // ingredient attributes
            /*
            binding.ingredientNameEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientDescriptionEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientAmountEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientUnitEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientCategoryEdittext.setInputType(InputType.TYPE_NULL);
            binding.ingredientLocationEdittext.setInputType(InputType.TYPE_NULL);*/
        }
        else {
            ingredient = new IngredientItem();
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

                if (selectedImage != null) {
                    ByteArrayOutputStream baos = new  ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG,100, baos);
                    ingredient.setPhoto(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                }

                listener.onDonePressed(ingredient);

                if (getActivity() instanceof RecipeStorageActivity) {
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
                NavHostFragment.findNavController(AddEditViewIngredientFragment.this).popBackStack(R.id.IngredientStorageMainFragment, false);
            }
        });
        /*
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
        });*/

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
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    setIngredientImage((Bitmap) result.getData().getExtras().get("data"));
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
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    selectImageLauncher.launch(Intent.createChooser(intent, "Take photo"));
                }
            }
        });
    }

    /** onCreaterOptionsMenu - inflates the menu xml file into the action bar
     *
     * */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

//        MenuItem item = menu.findItem(R.id.menuShare);
//        item.setVisible(false);
    }

    /** onOptionsItemSelected - handles on click events with menu items
     *
     * */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.menuLogout){
            logoutUser();
            return true;

        }
        return false;
    }

    /** logoutUser - signs current user out and sends user to the login page
     *
     * */

    private void logoutUser() {

        //Normal user logout
        Log.d("TAG", "logoutUser: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** setIngredientImage -sets the ingredient item image
     *
     * */
    private void setIngredientImage(Bitmap image) {
        selectedImage = image;
        binding.ingredientImageImageview.setImageBitmap(selectedImage);
    }

    /** OnIngredientItemChangeListener - interface for button presses
     *
     * */
    public interface OnIngredientItemChangeListener {
        void onDonePressed(IngredientItem ingredientItem);
        void onDeletePressed(IngredientItem ingredientItem);
    }







}