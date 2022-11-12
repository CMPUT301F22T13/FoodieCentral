package com.example.cmput301f22t13.domainlayer.item;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * This class will store information relevant to a meal plan. It contains a start and end date, and
 * each date within this date range will have its own list of ingredients and recipes
 */
public class MealPlan implements Serializable {

    /**
     * Each day from startDate to endDate will have its own list of ingredients/recipes with the
     * key as the date and the value equal to the list of ingredients/recipes
     */
    private HashMap<GregorianCalendar, ArrayList<Item>> mealPlanItems;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;

    public MealPlan() {
        this.mealPlanItems = new HashMap<GregorianCalendar, ArrayList<Item>>();
        this.startDate = new GregorianCalendar();
        this.endDate = new GregorianCalendar();
    }

    public MealPlan(GregorianCalendar startDate, GregorianCalendar endDate) {
        this.mealPlanItems = new HashMap<GregorianCalendar, ArrayList<Item>>();
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
    }

    public MealPlan(GregorianCalendar startDate, GregorianCalendar endDate, HashMap<GregorianCalendar, ArrayList<Item>> items) {
        this.mealPlanItems = items;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public HashMap<GregorianCalendar, ArrayList<Item>> getMealPlanItems() {
        return mealPlanItems;
    }

    public void setMealPlanItems(HashMap<GregorianCalendar, ArrayList<Item>> mealPlanItems) {
        this.mealPlanItems = mealPlanItems;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Item> getItemsForDay(GregorianCalendar date) {
        // check that date is within specified range
        if (date.before(startDate) || date.after(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()));
        }

        if (mealPlanItems.containsKey(date)) {
            return mealPlanItems.get(date);
        }

        return null;

    }

    public void setItemsForDay(GregorianCalendar date, ArrayList<Item> items) {
        // check that date is within specified range
        if (date.before(startDate) || date.after(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()));
        }

        if (mealPlanItems.containsKey(date)) {
            mealPlanItems.remove(date);
            mealPlanItems.put(date, items);
        }
    }

    public void addItemForDay(GregorianCalendar date, Item item) {
        // check that date is within specified range
        if (date.before(startDate) || date.after(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()));
        }

        if (mealPlanItems.containsKey(date)) {
            ArrayList<Item> items = mealPlanItems.get(date);
            items.add(item);
            mealPlanItems.remove(date);
            mealPlanItems.put(date, items);
        }
    }

    public void removeItemForDay(GregorianCalendar date, Item item) {
        // check that date is within specified range
        if (date.before(startDate) || date.after(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            throw new IllegalArgumentException("Date must be between " + formatter.format(startDate.getTime()) +
                    " and " + formatter.format(endDate.getTime()));
        }

        if (mealPlanItems.containsKey(date)) {
            ArrayList<Item> items = mealPlanItems.get(date);
            items.remove(item);
            mealPlanItems.remove(date);
            mealPlanItems.put(date, items);
        }
    }
}
