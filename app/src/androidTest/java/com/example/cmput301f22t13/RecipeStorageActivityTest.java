package com.example.cmput301f22t13;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.example.cmput301f22t13.uilayer.recipestorage.RecipeStorageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

@LargeTest
public class RecipeStorageActivityTest {
    @Rule
    public ActivityScenarioRule<RecipeStorageActivity> activityScenarioRule = new ActivityScenarioRule<RecipeStorageActivity>(RecipeStorageActivity.class);

    @Test
    public void viewIngredient() {
        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0).perform(click());
        onView(withId(R.id.recipe_name_edit)).check(matches(withText("Sample Recipe")));
    }

    /**
     * Test checks that a recipe has been updated properly.
     * First it checks the current values of the Sample Recipe.
     * Then it changes the values, including deleting an ingredient from the recipe.
     * Finally, it checks whether the recipe updated to its correct values.
     */
    @Test
    public void editRecipe() {
        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0).perform(click());
        onView(withId(R.id.recipe_name_edit)).check(matches(withText("Sample Recipe")));
        onView(withId(R.id.preparation_time_edit)).check(matches(withText(String.valueOf(120))));
        onView(withId(R.id.servings_edit)).check(matches(withText(String.valueOf(3))));
        onView(withId(R.id.comments_edit)).check(matches(withText("This is a sample recipe")));
        onData(anything()).inAdapterView(withId(R.id.list_of_ingredients)).atPosition(0).check(matches(withText("Eggs")));
        onData(anything()).inAdapterView(withId(R.id.list_of_ingredients)).atPosition(1).check(matches(withText("Batter")));

        onView(withId(R.id.edit_button)).perform(click());

        onView(withId(R.id.recipe_name_edit)).perform(replaceText("Apple Pie"));
        onView(withId(R.id.preparation_time_edit)).perform(replaceText(String.valueOf("100")));
        onView(withId(R.id.servings_edit)).perform(replaceText(String.valueOf("6")));
        onView(withId(R.id.category_edit)).perform(replaceText("Dessert"));
        onView(withId(R.id.comments_edit)).perform(replaceText("Recipe for Apple Pie."));
        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.recipe_name_edit)).check(matches(withText("Apple Pie")));
        onView(withId(R.id.preparation_time_edit)).check(matches(withText(String.valueOf(100))));
        onView(withId(R.id.servings_edit)).check(matches(withText(String.valueOf(6))));
        onView(withId(R.id.comments_edit)).check(matches(withText("Recipe for Apple Pie.")));}

    @Test
    public void deleteRecipe() {
        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0)
                .onChildView(withId(R.id.recipe_name_for_view))
                .check(matches(withText("Sample Recipe")));

        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0).perform(click());

        onView(withId(R.id.delete_button)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0)
                .onChildView(withId(R.id.recipe_name_for_view))
                .check(matches(not(withText("Sample Recipe"))));
    }

    @Test
    public void addMultipleIngredientToExisting() {
        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0).perform(click());
        onView(withId(R.id.edit_button)).perform(click());
        onView(withId(R.id.add_ingredient_to_recipe));
        onView(withId(R.id.add_ingredient_button));
        onView(withId(R.id.ingredient_name_edittext)).perform(typeText("Hot Sauce"));
        onView(withId(R.id.ingredient_description_edittext)).perform(typeText("Frank's Hot Sauce"));
        onView(withId(R.id.done_ingredient_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.recipelistview)).atPosition(0).check(matches(withText("Hot Sauce")));

    }
}
