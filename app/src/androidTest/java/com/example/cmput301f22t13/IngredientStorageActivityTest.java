package com.example.cmput301f22t13;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientStorageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IngredientStorageActivityTest {

    @Rule
    public ActivityScenarioRule<IngredientStorageActivity> activityScenarioRule = new ActivityScenarioRule<IngredientStorageActivity>(IngredientStorageActivity.class);

    @Test
    public void testSortPopupOpen() {
        onView(withId(R.id.sort_ingredient_button)).perform(click());
        onView(withText("Sort ingredients list by")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(withId(R.id.sort_ingredient_button)).perform(click());
    }

    @Test
    public void testAddIngredientButton() {
        onView(withId(R.id.add_ingredient_button)).perform(click());
        onView(withId(R.id.ingredient_name_label)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddingNewIngredient() {
        onView(withId(R.id.add_ingredient_button)).perform(click());
        onView(withId(R.id.ingredient_name_label)).check(matches(isDisplayed()));

        onView(withId(R.id.ingredient_name_edittext)).perform(typeText("Apple"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_name_edittext)).check(matches(withText("Apple")));
        onView(withId(R.id.ingredient_description_edittext)).perform(typeText("This is an Apple"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_description_edittext)).check(matches(withText("This is an Apple")));
        onView(withId(R.id.ingredient_bbd_edittext)).perform(click()).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 11, 1));
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.ingredient_bbd_edittext)).check(matches(withText("2022/11/1")));
        onView(withId(R.id.ingredient_location_edittext)).perform(typeText("Pantry"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_location_edittext)).check(matches(withText("Pantry")));
        onView(withId(R.id.ingredient_amount_edittext)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_amount_edittext)).check(matches(withText("2")));
        onView(withId(R.id.ingredient_unit_edittext)).perform(typeText("kg"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_unit_edittext)).check(matches(withText("kg")));
        onView(withId(R.id.ingredient_category_edittext)).perform(typeText("Fruit"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_category_edittext)).check(matches(withText("Fruit")));

        onView(withId(R.id.done_ingredient_button)).perform(click());
        onView(withId(R.id.add_ingredient_button)).check(matches(isDisplayed()));
    }


}
