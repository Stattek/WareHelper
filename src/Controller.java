import database.*;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    public Controller() throws RuntimeException {
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
     * Creates an Item from dictionaries.
     * 
     * @param itemData          The Item data.
     * @param innerCategoryData The inner Category object data for the Item.
     * @return True if the Item could be created, false otherwise.
     */
    public boolean createItem(Map<String, String> itemData, Map<String, String> innerCategoryData) {
        Item item = ObjectService.createItem(itemData, innerCategoryData);
        return storageCrud.createItem(item);
    }

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

    public List<Item> readItemByName(String name) {
        return storageCrud.readItemByName(name);
    }
}
