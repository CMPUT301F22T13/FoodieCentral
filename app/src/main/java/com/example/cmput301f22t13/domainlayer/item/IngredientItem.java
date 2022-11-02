package com.example.cmput301f22t13.domainlayer.item;

import java.io.Serializable;
import java.util.Comparator;

public class IngredientItem implements Serializable {
    private String name;
    private String description;
    private Integer amount;
    private String unit;
    private String category;

    /**
     * Constructor that initializes all ingredient fields to default values
     */
    public IngredientItem () {
        this.name = "";
        this.description = "";
        this.amount = 0;
        this.unit = "";
        this.category = "";
    }

    /**
     * Constructor that initializes all ingredient fields to values that are passed in as arguments
     * @param name name of ingredient
     * @param description description of ingredient
     * @param amount number of units of this ingredient
     * @param unit unit type for the ingredient (eg. kg, lb)
     * @param category the category that this ingredient belongs to
     */
    public IngredientItem (String name, String description, Integer amount, String unit, String category) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Gets the name of the ingredient
     * @return name of ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the ingredient
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the ingredient
     * @return description of ingredient
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the ingredient
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the amount/number of units there are of this ingredient
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Sets the amount/number of units there are of this ingredient
     * @param amount new amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Gets the unit of measure for the ingredient
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measure for the ingredient
     * @param unit new unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the category that this ingredient belongs to
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category that this ingredient belongs to
     * @param category new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

}


/**
 * Comparator class to sort ingredient item by description
 */
class SortIngredientByDescription implements Comparator<IngredientItem> {
    @Override
    /**
     * Override compare method for description sorting
     */
    public int compare(IngredientItem t1, IngredientItem t2) {
        return t1.getDescription().compareTo(t2.getDescription());
    }
}

/**
 * Comparator class to sort ingredient item by best before date
 */
//    TODO Implment once ii has been updated to have bbfd
class SortIngredientByBbfd implements Comparator<IngredientItem> {
    @Override
    /**
     * Override compare method for best before date
     */
    public int compare(IngredientItem t1, IngredientItem t2) {
        return 0;
    }
}

/**
 * Comparator class to sort ingredient item by category
 */
//    TODO Implment once ii has been updated to have location
class SortIngredientByLocation implements Comparator<IngredientItem> {
    @Override
    /**
     * Override compare method for category
     */
    public int compare(IngredientItem t1, IngredientItem t2) {
        return 0;
    }
}

/**
 * Comparator class to sort ingredient item by category
 */
class SortIngredientByCategory implements Comparator<IngredientItem> {
    @Override
    /**
     * Override compare method for category
     */

    public int compare(IngredientItem t1, IngredientItem t2) {
        return t1.getCategory().compareTo(t2.getCategory());
    }
}
