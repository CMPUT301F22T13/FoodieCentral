package com.example.cmput301f22t13.uilayer.ingredientstorage;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301f22t13.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IngredientStorageActivityTest {

    @Rule
    public ActivityScenarioRule<IngredientStorageActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(IngredientStorageActivity.class);

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

    @Test
    public void testEditingIngredient() {
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.ingredient_listview),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.ingredient_name_edittext), withText("Apple"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                11),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.edit_ingredient_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                4),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.ingredient_name_edittext), withText("Apple"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                11),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Orange"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.ingredient_name_edittext), withText("Orange"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                11),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.ingredient_description_edittext), withText("This is an apple"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                12),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.ingredient_description_edittext), withText("This is an apple"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                12),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("This is an orange"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.ingredient_description_edittext), withText("This is an orange"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                12),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        /*ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.ingredient_bbd_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                13),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton.perform(scrollTo(), click());*/
        onView(withId(R.id.ingredient_bbd_edittext)).perform(click()).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(2022, 11, 24));
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.ingredient_bbd_edittext)).check(matches(withText("2022/11/24")));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.ingredient_location_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                14),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("Pantry"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.ingredient_amount_edittext), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                15),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("2"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.ingredient_amount_edittext), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                15),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.ingredient_unit_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                16),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("kg"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.ingredient_category_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                17),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("Fruit"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.done_ingredient_button), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_content_ingredient_storage),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredient_name_textview), withText("Orange"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Orange")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.ingredient_description_textview), withText("This is an orange"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("This is an orange")));

        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.ingredient_listview),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.ingredient_name_edittext), withText("Orange"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText.check(matches(withText("Orange")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.ingredient_description_edittext), withText("This is an orange"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText2.check(matches(withText("This is an orange")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.ingredient_bbd_edittext), withText("2022/11/24"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText3.check(matches(withText("2022/11/24")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.ingredient_location_edittext), withText("Pantry"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText4.check(matches(withText("Pantry")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.ingredient_amount_edittext), withText("2"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText5.check(matches(withText("2")));

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.ingredient_unit_edittext), withText("kg"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText6.check(matches(withText("kg")));

        ViewInteraction editText7 = onView(
                allOf(withId(R.id.ingredient_category_edittext), withText("Fruit"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText7.check(matches(withText("Fruit")));

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.ingredient_category_edittext), withText("Fruit"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_ingredient_storage))),
                        isDisplayed()));
        editText8.check(matches(withText("Fruit")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
