import database.*;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.List;

public class Controller {
    private final StorageCrud storageCrud;

    /*
     * This may need to be moved to an environment file.
     */
    private static final String url = "jdbc:mysql://localhost:3306/warehelper";
    private static final String username = "testuser";
    private static final String password = "password";

    /**
     * Creates a new Controller, instantiating the MySQL database in the process,
     * through the creation of the MySqlCrud object.
     */
    public Controller() {
        try {
            this.storageCrud = new MySqlCrud(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize Database", e);
        }
    }

    /**
     * Sends a request to the StorageCrud to create a new category in the database.
     * 
     * @param categoryName The name of the category to be created.
     * @return {@code true} if the category was successfully created, {@code false}
     *         otherwise.
     */
    public boolean createCategory(String categoryName) {
        /*
         * THIS IS TEMPORARY WE WILL HAVE A SPERATE FACTORY THAT HANDLES CREATING
         * OBJECTS
         * NOTE THAT THE CONTROLLER SHOULD TAKE IN USER INPUTED VALUES AND SHOULD ONLY
         * PASS OBJECTS FOR CATEGORY ETC TO THE STORAGECRUD NOT CREATE THE OBJECTS.
         */
        Category category = new Category();
        category.setName(categoryName);
        return storageCrud.createCategory(category);

    }


    /**
     * Creates a new Item object.
     * 
     * @param itemId                The Item ID.
     * @param sku                   The SKU of the Item.
     * @param name                  The name of the Item.
     * @param category              The Category of the Item.
     * @param price                 The price of the Item.
     * @param numItems              The number of items.
     * @param created               The date this Item was created.
     * @param lastModified          The date this Item was last modified.
     * @param sellWithinNumDays     The number of days to sell an Item of this type
     *                              within.
     * @param lowInventoryThreshold The number of items before this Item is
     *                              considered to be "low stock."
     * @param promotionPercentOff   The percent off this Item, as part of a
     *                              promotion.
     * 
     * @return True upon success, false upon failure.
     */
    public boolean createItem(int itemId, String sku, String name, Category category, double price, int numItems,
            Date created,
            Date lastModified, int sellWithinNumDays, int lowInventoryThreshold, double promotionPercentOff) {
        Item item = new Item(itemId, sku, name, category, price, numItems, created,
                lastModified, sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
        return storageCrud.createItem(item);
    }

    /**
     * Reads an item by its itemId.
     * 
     * @param itemId The item ID of the item ot read.
     * @return The read Item from storage.
     */
    /**
     * Reads an item by its itemId.
     * 
     * @param itemId The item ID of the item ot read.
     * @return The read Item from storage.
     */
    public Item readItem(int itemId) {
        return storageCrud.readItem(itemId);
    }

    /**
     * Reads all items in storage.
     * 
     * @return A list of all the Item objects read from storage.
     */
    public List<Item> readAllItems() {
        return storageCrud.readAllItems();
    }

    /**
     * Reads all items in storage.
     * 
     * @return A list of all the Item objects read from storage.
     */
    public List<Item> readAllItems() {
        return storageCrud.readAllItems();
    }
}
