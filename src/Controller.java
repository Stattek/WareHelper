import database.*;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;

import java.sql.Date;
import java.sql.SQLException;

public class Controller {
    private final MySqlCrud storageCrud;

    /*
     * This may need to be moved to an environment file.
     */
    String url = "jdbc:mysql://localhost:3306/warehelper";
    String username = "testuser";
    String password = "password";

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
     * 
     * @param itemId
     * @param sku
     * @param name
     * @param category
     * @param price
     * @param numItems
     * @param created
     * @param lastModified
     * @param sellWithinNumDays
     * @param lowInventoryThreshold
     * @param promotionPercentOff
     * @return true or false
     * THIS ENTIRE CLASS AND THIS FILE IS GONNA BE CHANGED
     */
    public boolean createItem(int itemId, String sku, String name, Category category, double price, int numItems,
            Date created,
            Date lastModified, int sellWithinNumDays, int lowInventoryThreshold, double promotionPercentOff) {
        Item item = new Item(itemId, sku, name, category, price, numItems, created,
                lastModified, sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
        return storageCrud.createItem(item);
    }

    public Item readItem(int itemId) {
        return storageCrud.readItem(itemId);
    }
}
