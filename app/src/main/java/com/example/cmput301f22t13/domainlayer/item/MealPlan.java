package com.example.cmput301f22t13.domainlayer.item;

import com.example.cmput301f22t13.domainlayer.utils.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class will store information relevant to a meal plan. It contains a start and end date, and
 * each date within this date range will have its own list of ingredients and recipes stored as
 * {@link Item}
 */
public class MealPlan implements Serializable {

    /**
     * Each day from startDate to endDate will have its own list of ingredients/recipes with the
     * key as the date and the value equal to the list of ingredients/recipes
     */
    private TreeMap<GregorianCalendar, ArrayList<Item>> mealPlanItems;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private String hashId;

    /**
     * Default constructor. Start date will be set to the current date and end date will be set to
     * one day after the start date. Tree map for the items are initialized to empty TreeMap
     */
    public MealPlan() {
        this.startDate = new GregorianCalendar();
        this.endDate = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH) + 1);
        this.mealPlanItems = new TreeMap<GregorianCalendar, ArrayList<Item>>();
        this.hashId = Utils.getUniqueHash();
        validateTreeMap(this.mealPlanItems);
    }

    /**
     * Default constructor. Start date will be set to the current date and end date will be set to
     * one day after the start date. Tree map for the items are initialized to empty TreeMap
     */
    public MealPlan(GregorianCalendar startDate, GregorianCalendar endDate, String hashId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.mealPlanItems = new TreeMap<GregorianCalendar, ArrayList<Item>>();
        this.hashId = hashId;
        validateTreeMap(this.mealPlanItems);

//        MealPlanDL.getInstance().firebaseAddEdit(this);
    }


    /**
     * Constructor that takes in a start and end date. TreeMap of items is created and a key for each
     * day between the start and end date is created
     *
     * @param startDate start date of the meal plan as a {@link GregorianCalendar}
     * @param endDate end date of the meal plan as a {@link GregorianCalendar}
     */
    public MealPlan(GregorianCalendar startDate, GregorianCalendar endDate) {
        this.mealPlanItems = new TreeMap<GregorianCalendar, ArrayList<Item>>();
        this.startDate = startDate;
        this.endDate = endDate;

        // fill the meal plan items with all dates from start to end with empty lists
        int difference = endDate.get(Calendar.DATE) - startDate.get(Calendar.DATE);
        int year = startDate.get(Calendar.YEAR);
        int month = startDate.get(Calendar.MONTH);
        int day = startDate.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i <= difference; i++) {
            GregorianCalendar date = new GregorianCalendar(year, month, day + i);
            mealPlanItems.put(date, new ArrayList<Item>());
        }

        this.hashId = Utils.getUniqueHash();
//        MealPlanDL.getInstance().firebaseAddEdit(this);

    }

    /**
     * Constructor that takes in a start and end date, and the TreeMap of items for all days of the
     * meal plan
     *
     * @param startDate start date of the meal plan as a {@link GregorianCalendar}
     * @param endDate end date of the meal plan as a {@link GregorianCalendar}
     * @param items items for each day as a {@link TreeMap}
     */
    public MealPlan(GregorianCalendar startDate, GregorianCalendar endDate, TreeMap<GregorianCalendar, ArrayList<Item>> items) {
        this.mealPlanItems = items;
        this.startDate = startDate;
        this.endDate = endDate;
//        MealPlanDL.getInstance().firebaseAddEdit(this);
    }

    public MealPlan(MealPlan mealPlan) {
        this.mealPlanItems = mealPlan.getMealPlanItems();
        this.startDate = mealPlan.getStartDate();
        this.endDate = mealPlan.getEndDate();
        this.hashId = mealPlan.hashId;
    }

    /**
     * Gets the {@link TreeMap} of items for each day of the meal plan
     *
     * @return {@link TreeMap} of items for each day
     */
    public TreeMap<GregorianCalendar, ArrayList<Item>> getMealPlanItems() {
        return mealPlanItems;
    }

    /**
     * Sets the {@link TreeMap} of items for each day of the meal plan. It will only execute
     * successfully if the TreeMap is valid. A valid TreeMap is one where all date keys are
     * within the start and end date of the meal plan
     *
     * @param mealPlanItems {@link TreeMap} of items for each day of the meal plan
     */
    public void setMealPlanItems(TreeMap<GregorianCalendar, ArrayList<Item>> mealPlanItems) {
        if (isTreeMapValid(mealPlanItems)) {
            this.mealPlanItems = mealPlanItems;
        }
    }

    /**
     * Gets the start date of the meal plan
     *
     * @return start date of the meal plan
     */
    public GregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the meal plan
     *
     * @param startDate start date of the meal plan
     */
    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;

        // set end date to 1 day after the start date if start date is greater than the end date
        if (startDate.get(Calendar.DATE) > endDate.get(Calendar.DATE)) {
            endDate = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH) + 1);
        }
        validateTreeMap(this.mealPlanItems);
    }

    /**
     * Gets the end date of the meal plan
     *
     * @return end date of the meal plan
     */
    public GregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the meal plan
     *
     * @param endDate end date of the meal plan
     */
    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;

        // set start date to 1 day before the end date if end date is less than the start date
        if (startDate.get(Calendar.DATE) > endDate.get(Calendar.DATE)) {
            startDate = new GregorianCalendar(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH),
                    endDate.get(Calendar.DAY_OF_MONTH) - 1);
        }
        validateTreeMap(this.mealPlanItems);
    }

    /**
     * Gets the list of {@link Item}s for a certain day of the meal plan.
     * An {@link IllegalArgumentException} will be thrown if the date argument is not within the
     * range of start to end date.
     *
     * @param date date to get items for
     * @return {@link ArrayList} of Items for the selected day
     * @throws IllegalArgumentException if date argument is not within the range of start to end date
     */
    public ArrayList<Item> getItemsForDay(GregorianCalendar date) {
        // check that date is within specified range
        if (!isDateValid(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()) + ". " + formatter.format(date.getTime()) + " is not");
        }

        if (mealPlanItems.containsKey(date)) {
            return mealPlanItems.get(date);
        }

        return null;

    }

    /**
     * Gets the list of {@link Item}s for a certain day of the meal plan.
     * An {@link IllegalArgumentException} will be thrown if the date argument is not within the
     * range of start to end date.
     *
     * @param date date to set items for
     * @param items {@link ArrayList} of Items to set for the selected day
     * @throws IllegalArgumentException if date argument is not within the range of start to end date
     */
    public void setItemsForDay(GregorianCalendar date, ArrayList<Item> items) {
        // check that date is within specified range
        if (!isDateValid(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()) + ". " + formatter.format(date.getTime()) + " is not");
        }

        if (mealPlanItems.containsKey(date)) {
            mealPlanItems.remove(date);
            mealPlanItems.put(date, items);
        }
    }

    /**
     * Adds an item to the list of {@link Item}s for a certain day.
     * An {@link IllegalArgumentException} will be thrown if the date argument is not within the
     * range of start to end date.
     *
     * @param date date to add item to
     * @param item {@link Item} to add
     * @throws IllegalArgumentException if date argument is not within the range of start to end date
     */
    public void addItemForDay(GregorianCalendar date, Item item) {
        // check that date is within specified range
        if (!isDateValid(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime())+ ". " + formatter.format(date.getTime()) + " is not");
        }

    }

    /**
     * Removes an item to the list of {@link Item}s for a certain day.
     * An {@link IllegalArgumentException} will be thrown if the date argument is not within the
     * range of start to end date.
     *
     * @param date date to remove item from
     * @param item {@link Item} to remove
     * @throws IllegalArgumentException if date argument is not within the range of start to end date
     */
    public void removeItemForDay(GregorianCalendar date, Item item) {
        // check that date is within specified range
        if (!isDateValid(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()) + ". " + formatter.format(date.getTime()) + " is not");
        }

        if (mealPlanItems.containsKey(date)) {
            ArrayList<Item> items = mealPlanItems.get(date);
            items.remove(item);
            mealPlanItems.remove(date);
            mealPlanItems.put(date, items);
        }
    }

    private boolean isDateValid(GregorianCalendar date) {
        return (date.get(Calendar.DATE) >= startDate.get(Calendar.DATE)) && (date.get(Calendar.DATE) <= endDate.get(Calendar.DATE));
    }

    private void validateTreeMap(TreeMap<GregorianCalendar, ArrayList<Item>> treeMap) {
        ArrayList<GregorianCalendar> keysToRemove = new ArrayList<>();
        ArrayList<GregorianCalendar> keysToAdd = new ArrayList<>();

        // find keys to remove that are not within the start & end date
        for (GregorianCalendar date : treeMap.keySet()) {
            // remove key if invalid
            if (!isDateValid(date)) {
                keysToRemove.add(date);
            }
        }

        // remove the keys from the treeMap that were found in previous step
        for (GregorianCalendar date : keysToRemove) {
            treeMap.remove(date);
        }

        // only find the keys to add if the start date is less than the end date
        if (startDate.get(Calendar.DATE) < endDate.get(Calendar.DATE)) {
            // find missing days in the map between the start and end date
            int difference = endDate.get(Calendar.DATE) - startDate.get(Calendar.DATE);
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH);
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            for (int i = 0; i <= difference; i++) {
                GregorianCalendar date = new GregorianCalendar(year, month, day + i);
                if (!treeMap.containsKey(date)) {
                    keysToAdd.add(date);
                }
            }

            // add keys computed in previous step
            for (GregorianCalendar date : keysToAdd) {
                treeMap.put(date, new ArrayList<>());
            }
        }
    }

    private boolean isTreeMapValid(TreeMap<GregorianCalendar, ArrayList<Item>> treeMap) {
        for (GregorianCalendar date : treeMap.keySet()) {
            if (!isDateValid(date)) {
                return false;
            }
        }

        return true;
    }


    // Need to talk to logan - meal plan needs a unique hash - items may or may not depending
    public String getHashId() {
        return this.hashId;
    }

    public void setHashId(String id) {
        this.hashId = id;
    }
}
