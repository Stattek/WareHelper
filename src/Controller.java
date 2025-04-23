import database.*;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Controller {
    private final StorageCrud storageCrud;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
     * Creates a Bundle from dictionaries.
     * 
     * @param bundleData The Item data.
     * @param itemIds    The IDs of the Items this bundle contains.
     * @return True if the Bundle could be created, false otherwise.
     */
    public boolean createBundle(Map<String, String> bundleData, List<Integer> itemIds) {
        // create a bundle stub since we do not know the Item information, only each
        // Item's ID
        Bundle bundle = ObjectService.createBundleStub(bundleData, itemIds);
        return storageCrud.createBundle(bundle);
    }

    /**
     * Creates an Item from dictionaries.
     * 
     * @param itemData          The Item data.
     * @param innerCategoryData The inner Category object data for the Item.
     * @return True if the Item could be created, false otherwise.
     */
    public boolean createItem(Map<String, String> itemData, Map<String, String> innerCategoryData) {
        // ask storageCrud for next itemID?
        int nextItemId = storageCrud.getNextId("Item");
        if (nextItemId == -1) {
            System.out.println("Could not find a valid Item ID for SKU.");
            return false;
        }
        String category = itemData.get("category");
        int categoryId = storageCrud.getCategoryId(category);

        if (categoryId == -1) {
            System.out.println("Could not find a valid Category ID.");
            return false;
        }
        String sku = category + Integer.toString(nextItemId);
        itemData.put("Sku", sku);
        itemData.put("ItemId", Integer.toString(nextItemId));

        innerCategoryData.put("CategoryId", Integer.toString(categoryId));
        itemData.put("CategoryId", Integer.toString(categoryId));

        Item item = ObjectService.createItem(itemData, innerCategoryData);
        return storageCrud.createItem(item);

        // TODO: Make different maps from ItemData and call different ObjectService
        // methods with those?
    }

    /**
     * Reads an item by its itemId.
     * 
     * @param itemId The item ID of the item to read.
     * @return A JSON representation of the read Item from storage.
     */
    public String readItem(int itemId) {
        return gson.toJson(storageCrud.readItem(itemId));
    }

    /**
     * Reads all items in storage.
     * 
     * @return A JSON representation of all the Item objects read from storage.
     */
    public String readAllItems() {
        return gson.toJson(storageCrud.readAllItems());
    }

    /**
     * Reads all Items with the name provided.
     * 
     * @param name The name to search for.
     * @return A JSON represenation of all the Item objects read from storage.
     */
    public String readItemByName(String name) {
        return gson.toJson(storageCrud.readItemByName(name));
    }

    /**
     * Reads all Categories with the name provided.
     * 
     * @param name The name to search for.
     * @return A JSON representation of all the Category objects read from storage.
     */
    public String readCategoryByName(String name) {
        return gson.toJson(storageCrud.readCategoryByName(name));
    }

    /**
     * Reads all categories in storage.
     * 
     * @return A JSON representation of all the Category objects read from storage.
     */
    public String readAllCategories() {
        return gson.toJson(storageCrud.readAllCategories());
    }

    /**
     * Deletes a category by its categoryId.
     * 
     * @param categoryId The ID of the category to delete.
     * @return {@code true} if the category was successfully deleted, {@code false}
     *         otherwise.
     */
    public boolean deleteCategory(int categoryId) {
        return storageCrud.deleteCategory(categoryId);
    }

    /**
     * Gets the keys for an Item.
     * 
     * @return A List of keys.
     */
    public List<String> getItemKeys() {
        return ObjectService.getItemKeys();
    }

    /**
     * Gets the keys for a Bundle.
     * 
     * @return A List of keys.
     */
    public List<String> getBundleKeys() {
        return ObjectService.getBundleKeys();
    }

    /**
     * Gets the keys for a Category.
     * 
     * @return A List of keys.
     */
    public List<String> getCategoryKeys() {
        return ObjectService.getCategoryKeys();
    }
}
