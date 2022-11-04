package com.example.cmput301f22t13.domainlayer.item;

import com.example.cmput301f22t13.domainlayer.utils.Utils;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

public class IngredientItem implements Serializable {
    private String name;
    private String description;
    private Integer amount;
    private String unit;
    private String category;
    private GregorianCalendar bbd;
    private String location;
    private String photo;
    private String hashId;

    /**
     * Constructor that initializes all ingredient fields to default values
     */
    public IngredientItem () {
        this.name = "";
        this.description = "";
        this.amount = 0;
        this.unit = "";
        this.category = "";
        this.bbd = new GregorianCalendar();
        this.location = "";
        this.photo = "";
        this.hashId = Utils.getUniqueHash();
    }


    /**
     * Constructor that initializes all ingredient fields to values that are passed in as arguments
     * @param name name of ingredient
     * @param description description of ingredient
     * @param amount number of units of this ingredient
     * @param unit unit type for the ingredient (eg. kg, lb)
     * @param category the category that this ingredient belongs to
     * @param bbd the best before date that this ingredient has
     * @param photo the uri string for storing images for the ingredient
     * @param hashId the hash ID for uniquely identifying ingredients
     * @param location the location of the ingredient stored
     */
    public IngredientItem (String name, String description, Integer amount, String unit, String category, GregorianCalendar bbd, String photo, String location) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.bbd = bbd;
        this.photo = photo;
        this.location = location;

        int timestamp = new Timestamp(new Date()).getNanoseconds();
        String timeStampString = Integer.toString(timestamp);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(
                timeStampString.getBytes(StandardCharsets.UTF_8));

        this.hashId = Utils.bytesToHex(encodedhash);
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
    /**
     * Gets the best before date the ingredient
     * @return bbd
     */
    public GregorianCalendar getBbd() {
        return bbd;
    }
    /**
     * Sets the best before date of the ingredient it belongs to
     * @param bbd new bbd
     */
    public void setBbd(GregorianCalendar bbd) {
        this.bbd = bbd;
    }
    /**
     * Gets the location of this ingredient it belongs to
     * @return location
     */
    public String getLocation() {
        return location;
    }
    /**
     * Sets the location of the ingredient it belongs to
     * @param location new location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * Gets the uri image string of the ingredient it belongs to
     * @return photo
     */
    public String getPhoto() {
        return photo;
    }
    /**
     * Sets the uri string for the image of the ingredient it belongs to
     * @param photo new photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    /**
     * Gets the hash ID of this ingredient it belongs to
     * @return hashId
     */
    public String getHashId() {
        return hashId;
    }
    /**
     * Sets the hashId of the ingredient it belongs to
     * @param hashId new hashId
     */
    public void setHashId(String hashId) {
        this.hashId = hashId;
    }
}


