package com.example.cmput301f22t13.domainlayer.item;

import com.example.cmput301f22t13.domainlayer.utils.Utils;

/**
 * Base class for items that stores a name and a photo
 */
public class Item {

    // name of the item
    private String name;

    // photo that is stored as a String representation of the Uri
    private String photo;

    // unique hash id for the object
    private String hashId;

    /**
     * Creates an item with no name or photo
     */
    public Item() {
        this.name = "";
        this.photo = "";
        this.hashId = Utils.getUniqueHash();
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
        this.hashId = Utils.getUniqueHash();
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
