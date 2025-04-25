package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import database.items.DataType;

public class MySqlCrud extends StorageCrud {

    /**
     * Creates a new MySqlCrud, establishing a connection to the database through
     * the creation of a MySql object.
     * 
     * @param url      The URL of the database to connect to.
     * @param username The username of the user to use for the database. Must have
     *                 sufficient permissions.
     * @param password The password of the user to use for the database.
     * @throws SQLException
     */
    public MySqlCrud(String url, String username, String password) throws SQLException {
        this.storageService = new MySql(url, username, password);
        // TODO: check if the MySql database has the tables for the programs and if not,
        // create them
    }

    private void setup() {

    }


    /**
     * Update the SKU of an item given the item's ID and the new SKU for it.
     * 
     * @param itemId the ID of the item.
     * @param newSku the new SKU we want to give the item.
     * @return True if successful in updating, False otherwise.
     */
    @Override
    public boolean updateItemSku(int itemId, String newSku) {
        return storageService.updateSKU(itemId, newSku); // TODO: Temporary until the update function is made.
    }
    
    /**
     * Find the next incremented ID from the provided table (Item, Category).
     * 
     * @param tableName The table name to search for in Storage.
     * @return The table's next incremented ID.
     */
    @Override
    public int getNextId(String tableName){
        return storageService.getNextIncrementedId(tableName);
    }

    /**
     * Reads a Category in Storage from the provided name.
     * 
     * @param categoryName The object name to search for in Storage.
     * @return The category ID.
     */
    @Override
    public int readCategory(String categoryName) {
        List<String> keys = Arrays.asList("CategoryId"); // only fetch the ID
        List<Map<String, String>> result = this.storageService.readSearchRow(
            "Category", keys, "Name", categoryName, DataType.STRING
        );

        if (result.isEmpty()) {
            throw new RuntimeException("Category not found: " + categoryName);
        }

        return Integer.parseInt(result.get(0).get("CategoryId"));
    }

    /**
     * Creates an Item in Storage from the provided item.
     * 
     * @param item The object to store in the Storage.
     * @return True upon success, false upon failure.
     */
    @Override
    public boolean createItem(Item item) {
        // remove ID keys as they're auto generated.
        List<String> keys = item.getAttributeKeys();
        keys.remove(0);
        List<String> data = item.getAllAttributes();
        data.remove(0);
        List<DataType> types = item.getAttributeDataTypes();
        types.remove(0);
        boolean created = storageService.create("Item", data, keys, types);

        if (created) {
            // Retrieve the item we just inserted using its temporary Sku, and fix it.
            List<String> idKey = new ArrayList<>();
            idKey.add("ItemId");

            List<Map<String, String>> result = ((MySql) storageService).readSearchRow("Item", idKey, "Sku", item.getSku(), DataType.STRING);

            if (!result.isEmpty()) {
                int generatedId = Integer.parseInt(result.get(0).get("ItemId"));
                item.setItemId(generatedId);
            }
        }


        return created;
    }

    @Override
    public boolean createBundle(List<Item> items) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle(-1, 0, items);
        List<String> keys = bundle.getAttributeKeys();

        throw new UnsupportedOperationException("Unimplemented method 'createBundle'");
    }

    /**
     * Creates lists of all data for category along with a keys list for
     * corresponding collumn names
     * 
     * @param category A category object that is to be added to the database
     * 
     * @return {@code true} if the category was created without errors,
     *         {@code false} otherwise
     */
    @Override
    public boolean createCategory(Category category) {
        // This could be done using a hash map instead of two lists, it works for now
        // though
        List<String> keys = category.getAttributeKeys();
        keys.remove(0);
        List<String> data = category.getAllAttributes();
        data.remove(0);
        List<DataType> types = category.getAttributeDataTypes();
        types.remove(0);
        return storageService.create("Category", data, keys, types);
    }

    @Override
    public Item readItem(int itemId) {
        Item output = new Item();
        // keys should be PascalCase
        List<String> keys = output.getAttributeKeys();

        Map<String, String> itemData = this.storageService.read("Item", itemId, keys);
        List<String> categoryKeys = new Category().getAttributeKeys();
        Map<String, String> innerCategoryData = readInnerCategory(itemData, categoryKeys);
        output = ObjectService.createItem(itemData, innerCategoryData);

        return output;
    }

    /**
     * Reads an Item object's inner Category object.
     * 
     * @param itemMap      The Item data.
     * @param categoryKeys The Category keys.
     * @return The inner Category object data.
     */
    private Map<String, String> readInnerCategory(Map<String, String> itemMap, List<String> categoryKeys) {
        int categoryId = 0;
        try {
            categoryId = Integer.parseInt(itemMap.get("CategoryId"));
        } catch (Exception e) {
            throw new RuntimeException("Could not convert categoryId to int");
        }

        // read this category
        return this.storageService.read("Category", categoryId, categoryKeys);
    }

    @Override
    public List<Item> readAllItems() throws RuntimeException {
        List<Item> items = new ArrayList<>();

        Item temp = new Item();

        List<String> keys = temp.getAttributeKeys();

        List<Map<String, String>> itemMaps = this.storageService.readAll("Item", keys);
        List<Map<String, String>> categoryMaps = new ArrayList<>();

        List<String> categoryKeys = new Category().getAttributeKeys();
        // read each Category
        for (int i = 0; i < itemMaps.size(); i++) {

            // read this category
            categoryMaps.add(readInnerCategory(itemMaps.get(i), categoryKeys));
        }

        for (int i = 0; i < itemMaps.size(); i++) {
            items.add(ObjectService.createItem(itemMaps.get(i), categoryMaps.get(i)));
        }

        return items;
    }

    @Override
    public List<Category> readCategoryByName(String name) throws RuntimeException {
        List<Category> categories = new ArrayList<>();

        Category temp = new Category();

        List<String> keys = temp.getAttributeKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readSearchRow("Category", keys, "Name", name,
                DataType.STRING);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }

        return categories;
    }

    @Override
    public List<Item> readItemByName(String name) throws RuntimeException {
        List<Item> items = new ArrayList<>();

        Item temp = new Item();

        List<String> keys = temp.getAttributeKeys();

        List<Map<String, String>> itemMaps = this.storageService.readSearchRow("Item", keys, "Name", name,
                DataType.STRING);
        List<Map<String, String>> categoryMaps = new ArrayList<>();

        List<String> categoryKeys = new Category().getAttributeKeys();
        // read each Category
        for (int i = 0; i < itemMaps.size(); i++) {

            // read this category
            categoryMaps.add(readInnerCategory(itemMaps.get(i), categoryKeys));
        }

        for (int i = 0; i < itemMaps.size(); i++) {
            items.add(ObjectService.createItem(itemMaps.get(i), categoryMaps.get(i)));
        }

        return items;
    }

    @Override
    public Bundle readBundle(int bundleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readBundle'");
    }

    @Override
    public Category readCategory(int categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readCategory'");
    }

    @Override
    public List<Category> readAllCategories() throws RuntimeException {
        List<Category> categories = new ArrayList<>();

        Category temp = new Category();

        List<String> keys = temp.getAttributeKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readAll("Category", keys);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }

        return categories;
    }

    @Override
    public boolean updateItem(Item item) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateItem'");
    }

    @Override
    public boolean updateBundle(Bundle bundle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBundle'");
    }

    @Override
    public boolean updateCategory(Category category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategory'");
    }

    @Override
    public boolean deleteItem(int itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteItem'");
    }

    @Override
    public boolean deleteBundle(int bundleId) {
        // TODO Auto-generated method stub
        return storageService.delete("Bundle", "BundleId", bundleId);

    }

    @Override
    public boolean deleteCategory(int categoryId) {
        return storageService.delete("Category", "CategoryId", categoryId);
    }

}
