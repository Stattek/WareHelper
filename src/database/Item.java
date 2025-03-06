package database;

import java.sql.Date;
import database.Category;

/**
 * Class that represents an Item in the inventory.
 */
public class Item {
    private String sku;
    private String name;
    private double price;
    private Date created;
    private Date lastModified;
    private int sellWithinDays;
    private int lowInventoryThreshold; // number of items before it is considered low inventory
    private Category category; // TODO: implement category
    private int numItems;
    private double percentOffPromotion;

    /**
     * Constructor to create an Item.
     * 
     * @param name           The name of the Item.
     * @param price          The price of the Item.
     * @param sellWithinDays The number of days to sell the Item within.
     * @return The new Item.
     */
    public Item(String name, double price, int sellWithinDays) {
        this.name = name;
        this.price = price;
        this.sellWithinDays = sellWithinDays;
        // TODO: add SKU and rest of variables to this constructor
    }

    /**
     * Adds promotion to item.
     * 
     * @param percentOffPromotion The percent off of the item.
     */
    public void addPromotion(double percentOffPromotion) {
        this.percentOffPromotion = percentOffPromotion;
    }

    /**
     * Sets the low inventory threshold.
     * 
     * @param lowInventoryThreshold The number of items at which the inventory is
     *                              considered low.
     */
    public void setLowInventoryThreshold(int lowInventoryThreshold) {
        this.lowInventoryThreshold = lowInventoryThreshold;
    }

    /**
     * Sets the days an item should be sold within.
     * 
     * @param sellWithinDays The number of days to sell the item within.
     */
    public void setSellWIthinDays(int sellWithinDays) {
        this.sellWithinDays = sellWithinDays;
    }

}
