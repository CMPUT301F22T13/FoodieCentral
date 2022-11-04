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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

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
        onView(withId(R.id.recipe_name_heading)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddingNewIngredient() {
        onView(withId(R.id.add_ingredient_button)).perform(click());
        onView(withId(R.id.recipe_name_heading)).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_name_edit)).perform(typeText("Apple"), closeSoftKeyboard());
        onView(withId(R.id.recipe_name_edit)).check(matches(withText("Apple")));
        onView(withId(R.id.preparation_time_edit)).perform(typeText("This is an Apple"), closeSoftKeyboard());
        onView(withId(R.id.preparation_time_edit)).check(matches(withText("This is an Apple")));
        onView(withId(R.id.servings_edit)).perform(click()).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 11, 1));
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.servings_edit)).check(matches(withText("2022/11/1")));
        onView(withId(R.id.category_edit)).perform(typeText("Pantry"), closeSoftKeyboard());
        onView(withId(R.id.category_edit)).check(matches(withText("Pantry")));
        onView(withId(R.id.comments_edit)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.comments_edit)).check(matches(withText("2")));
        onView(withId(R.id.ingredients_edit)).perform(typeText("kg"), closeSoftKeyboard());
        onView(withId(R.id.ingredients_edit)).check(matches(withText("kg")));
        onView(withId(R.id.ingredient_category_edittext)).perform(typeText("Fruit"), closeSoftKeyboard());
        onView(withId(R.id.ingredient_category_edittext)).check(matches(withText("Fruit")));

        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.add_ingredient_button)).check(matches(isDisplayed()));
    }


}
