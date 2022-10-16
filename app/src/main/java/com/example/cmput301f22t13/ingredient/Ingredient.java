package com.example.cmput301f22t13.ingredient;

import java.util.GregorianCalendar;

/**
 * This class encapsulates all necessary details that an ingredient has
 * @author Logan Thimer
 */
public class Ingredient {
    private String name;
    private String description;
    private GregorianCalendar bestBeforeDate;
    private String location;
    private Integer amount;
    private String unit;
    private String category;

    /**
     * Constructor that initializes all ingredient fields to default values
     */
    public Ingredient () {
        this.name = "";
        this.description = "";
        this.bestBeforeDate = new GregorianCalendar();
        this.location = "";
        this.amount = 0;
        this.unit = "";
        this.category = "";
    }

    /**
     * Constructor that initializes all ingredient fields to values that are passed in as arguments
     * @param name name of ingredient
     * @param description description of ingredient
     * @param bestBeforeDate best before date/expiry date
     * @param location location where the ingredient is stored
     * @param amount number of units of this ingredient
     * @param unit unit type for the ingredient (eg. kg, lb)
     * @param category the category that this ingredient belongs to
     */
    public Ingredient (String name, String description, GregorianCalendar bestBeforeDate, String location, Integer amount, String unit, String category) {
        this.name = name;
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
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
     * Gets the best before date of the ingredient as a {@link GregorianCalendar}
     * @return best before date
     */
    public GregorianCalendar getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * Sets the best before date of the ingredient as a {@link GregorianCalendar}
     * @param bestBeforeDate new best before date
     */
    public void setBestBeforeDate(GregorianCalendar bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * Gets the location of where the ingredient is stored
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location where the ingredient is stored
     * @param location new location
     */
    public void setLocation(String location) {
        this.location = location;
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
