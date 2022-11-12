package com.example.cmput301f22t13.domainlayer.item;

/**
 * Base class for items that stores a name and a photo
 */
public class Item {

    // name of the item
    private String name;

    // photo that is stored as a String representation of the Uri
    private String photo;

    /**
     * Creates an item with no name or photo
     */
    public Item() {
        this.name = "";
        this.photo = "";
    }

    /**
     * Creates an item with a specified name and photo
     *
     * @param name of the ingredient
     * @param photo {@code String} representation of the Uri of the photo
     */
    public Item(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
