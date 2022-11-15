package com.example.cmput301f22t13.domainlayer.item;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import junit.framework.TestCase;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class MealPlanTest extends TestCase {

    @Test
    public void testDefaultConstructor() {
        MealPlan mp = new MealPlan();
        assertNotNull(mp.getStartDate());
        assertNotNull(mp.getEndDate());
        assertNotNull(mp.getMealPlanItems());
    }

    @Test
    public void testStartEndDateConstructor() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 25);
        MealPlan mp = new MealPlan(start, end);
        assertEquals(start, mp.getStartDate());
        assertEquals(end, mp.getEndDate());
    }

    @Test
    public void testFullConstructor() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 21);
        ArrayList<Item> list1 = new ArrayList<>();
        ArrayList<Item> list2 = new ArrayList<>();
        TreeMap<GregorianCalendar, ArrayList<Item>> treeMap = new TreeMap<>();
        treeMap.put(start, list1);
        treeMap.put(end, list2);
        MealPlan mp = new MealPlan(start, end, treeMap);
        assertEquals(start, mp.getStartDate());
        assertEquals(end, mp.getEndDate());
        assertEquals(treeMap, mp.getMealPlanItems());
    }

    @Test
    public void testSetStartDate() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 25);
        MealPlan mp = new MealPlan(start, end);

        GregorianCalendar newStart = new GregorianCalendar(2022, 11, 21);
        mp.setStartDate(newStart);
        assertEquals(newStart, mp.getStartDate());
    }

    @Test
    public void testSetEndDate() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 25);
        MealPlan mp = new MealPlan(start, end);

        GregorianCalendar newEnd = new GregorianCalendar(2022, 11, 23);
        mp.setEndDate(newEnd);
        assertEquals(newEnd, mp.getEndDate());
    }

    @Test
    public void testSetMealPlanItems() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 22);
        MealPlan mp = new MealPlan(start, end);

        TreeMap<GregorianCalendar, ArrayList<Item>> treeMap = new TreeMap<>();
        treeMap.put(start, new ArrayList<>());
        treeMap.put(new GregorianCalendar(2022, 11, 21), new ArrayList<>());
        treeMap.put(end, new ArrayList<>());
        mp.setMealPlanItems(treeMap);
        assertEquals(treeMap, mp.getMealPlanItems());
    }

    @Test
    public void testKeysCreatedForDates() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 20);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 25);
        MealPlan mp = new MealPlan(start, end);

        ArrayList<GregorianCalendar> keys = new ArrayList<>();
        keys.add(new GregorianCalendar(2022, 11, 20));
        keys.add(new GregorianCalendar(2022, 11, 21));
        keys.add(new GregorianCalendar(2022, 11, 22));
        keys.add(new GregorianCalendar(2022, 11, 23));
        keys.add(new GregorianCalendar(2022, 11, 24));
        keys.add(new GregorianCalendar(2022, 11, 25));

        ArrayList<GregorianCalendar> mpkeys = new ArrayList<>(mp.getMealPlanItems().keySet());
        assertArrayEquals(keys.toArray(), mpkeys.toArray());
    }

    @Test
    public void testSetItemsForDay() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 15);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 19);
        MealPlan mp = new MealPlan(start, end);

        Item item = new IngredientItem();
        item.setName("Apple");

        Item item2 = new IngredientItem();
        item.setName("Pear");

        Item item3 = new IngredientItem();
        item.setName("Carrot");

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);
        mp.setItemsForDay(start, items);
        assertEquals(items, mp.getItemsForDay(start));
        assertEquals(mp.getItemsForDay(start).size(), 3);
        assertArrayEquals(items.toArray(), mp.getItemsForDay(start).toArray());
    }

    @Test
    public void testAddingItemForDay() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 15);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 19);
        MealPlan mp = new MealPlan(start, end);

        Item item = new IngredientItem();
        item.setName("Apple");
        mp.addItemForDay(start, item);
        assertTrue(mp.getItemsForDay(start).contains(item));
        assertEquals(mp.getItemsForDay(start).size(), 1);
        assertEquals(mp.getItemsForDay(end).size(), 0);

        Item item2 = new IngredientItem();
        item.setName("Pear");
        mp.addItemForDay(start, item2);
        assertTrue(mp.getItemsForDay(start).contains(item2));
        assertEquals(mp.getItemsForDay(start).size(), 2);
        assertEquals(mp.getItemsForDay(end).size(), 0);

        Item item3 = new IngredientItem();
        item.setName("Carrot");
        mp.addItemForDay(end, item3);
        assertTrue(mp.getItemsForDay(end).contains(item3));
        assertEquals(mp.getItemsForDay(start).size(), 2);
        assertEquals(mp.getItemsForDay(end).size(), 1);
    }

    @Test
    public void testRemovingItemForDay() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 15);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 19);
        MealPlan mp = new MealPlan(start, end);

        Item item = new IngredientItem();
        item.setName("Apple");
        mp.addItemForDay(start, item);
        assertTrue(mp.getItemsForDay(start).contains(item));

        Item item2 = new IngredientItem();
        item.setName("Pear");
        mp.addItemForDay(start, item2);
        assertTrue(mp.getItemsForDay(start).contains(item2));

        Item item3 = new IngredientItem();
        item.setName("Carrot");
        mp.addItemForDay(end, item3);
        assertTrue(mp.getItemsForDay(end).contains(item3));

        mp.removeItemForDay(start, item);
        assertFalse(mp.getItemsForDay(start).contains(item));
        assertEquals(mp.getItemsForDay(start).size(), 1);
        assertEquals(mp.getItemsForDay(end).size(), 1);

        mp.removeItemForDay(end, item3);
        assertFalse(mp.getItemsForDay(start).contains(item3));
        assertEquals(mp.getItemsForDay(start).size(), 1);
        assertEquals(mp.getItemsForDay(end).size(), 0);

        mp.removeItemForDay(start, item2);
        assertFalse(mp.getItemsForDay(start).contains(item2));
        assertEquals(mp.getItemsForDay(start).size(), 0);
        assertEquals(mp.getItemsForDay(end).size(), 0);
    }

    public void testInvalidDay() {
        GregorianCalendar start = new GregorianCalendar(2022, 11, 15);
        GregorianCalendar end = new GregorianCalendar(2022, 11, 19);
        MealPlan mp = new MealPlan(start, end);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // test a date lower than start date
        GregorianCalendar dateBefore = new GregorianCalendar(2022, 11, 21);
        String exceptionMessage = "Date must be between " + formatter.format(start.getTime()) +
                " and " + formatter.format(end.getTime()) + ". " + formatter.format(dateBefore.getTime()) + " is not";
        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.getItemsForDay(dateBefore);
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.setItemsForDay(dateBefore, new ArrayList<>());
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.addItemForDay(dateBefore, new IngredientItem());
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.removeItemForDay(dateBefore, new IngredientItem());
        });

        // test a date higher than end date
        GregorianCalendar dateAfter = new GregorianCalendar(2022, 11, 21);
        exceptionMessage = "Date must be between " + formatter.format(start.getTime()) +
                " and " + formatter.format(end.getTime()) + ". " + formatter.format(dateAfter.getTime()) + " is not";
        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.getItemsForDay(dateAfter);
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.setItemsForDay(dateAfter, new ArrayList<>());
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.addItemForDay(dateAfter, new IngredientItem());
        });

        assertThrows(exceptionMessage, IllegalArgumentException.class, () -> {
            mp.removeItemForDay(dateAfter, new IngredientItem());
        });
    }

}
