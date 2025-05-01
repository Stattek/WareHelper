package database;

import database.items.Bundle;
import database.items.Category;
import database.items.DataType;
import database.items.Item;
import database.items.ObjectService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        // set autocommit = 0;
        // set information_schema_stats_expiry=0;
    }

    /**
     * Find the next incremented ID from the provided table (Item, Category).
     * 
     * @param tableName The table name to search for in Storage.
     * @return The table's next incremented ID.
     */
    @Override
    public int getNextId(String tableName) {
        return storageService.getNextIncrementedId(tableName);
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

            List<Map<String, String>> result = storageService.readSearchRow("Item", idKey, "Sku",
                    item.getSku(), DataType.STRING);

            if (!result.isEmpty()) {
                int generatedId = Integer.parseInt(result.get(0).get("ItemId"));
                item.setItemId(generatedId);
            }
        }

        return created;
    }

    @Override
    public boolean createBundle(Bundle bundle) {
        List<String> bundleKeys = bundle.getAttributeKeys();
        List<String> bundleData = bundle.getAllAttributes();
        List<DataType> bundleTypes = bundle.getAttributeDataTypes();

        // remove the bundleId, since we do not need that to create a row
        bundleKeys.remove(0);
        bundleData.remove(0);
        bundleTypes.remove(0);

        // since nobody else should be writing at the same time, we can do this (single
        // threaded)
        int newBundleId = storageService.getNextIncrementedId("Bundle");
        if (!storageService.create("Bundle", bundleData, bundleKeys, bundleTypes)) {
            return false; // failure
        }

        // create the entries for the association class
        Item item = new Item();

        List<String> itemBundleKeys = new ArrayList<>();
        // get the IDs of both the Item and Bundle
        itemBundleKeys.add(item.getAttributeKeys().get(0));
        itemBundleKeys.add(bundle.getAttributeKeys().get(0));

        List<DataType> itemBundleTypes = new ArrayList<>();
        // get types for IDs
        itemBundleTypes.add(item.getAttributeDataTypes().get(0));
        itemBundleTypes.add(bundle.getAttributeDataTypes().get(0));

        // add each item
        for (String itemId : bundle.getInnerObjectIds()) {
            List<String> itemBundleData = new ArrayList<>();
            itemBundleData.add(itemId);
            itemBundleData.add(Integer.toString(newBundleId));

            // create the entry for this item, bundle pair
            if (!storageService.create("ItemBundle", itemBundleData, itemBundleKeys, itemBundleTypes)) {
                return false; // failure
            }
        }

        return true; // success
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

        List<Map<String, String>> itemMaps = this.storageService.readAll("Item", keys, null);
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

        List<Map<String, String>> categoryMaps = this.storageService.readAll("Category", keys, null);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }

        return categories;
    }

    @Override
    public List<Bundle> readAllBundles() throws RuntimeException {
        List<Bundle> bundles = new ArrayList<>();

        Bundle temp = new Bundle();

        List<String> keys = temp.getAttributeKeys();

        // select * from ItemBundle inner join Item on ItemBundle.ItemId = Item.ItemId
        // where BundleId=2;

        // or

        // select * from Bundle join ItemBundle on Bundle.BundleId=ItemBundle.BundleId
        // join Item on Item.ItemId = ItemBundle.ItemId;

        List<Map<String, String>> bundleMaps = this.storageService.readAll("Bundle", keys, null);
        for (Map<String, String> bundleMap : bundleMaps) {
            this.storageService.readSearchRow("ItemBundle", keys, "BundleId", null, null);
            bundles.add(ObjectService.createBundle(bundleMap, null, null));
        }

        return bundles;
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

    /**
     * Deletes an item by its itemId from the database.
     * 
     * @param itemId The ID of the item to delete.
     * @return True if the item was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteItem(int itemId) {
        // Delete the item from the "Item" table where the ItemId matches the provided
        // itemId.
        return storageService.delete("Item", "ItemId", itemId);
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
