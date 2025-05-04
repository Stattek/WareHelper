package database;

import database.items.Bundle;
import database.items.Category;
import database.items.DataType;
import database.items.Item;
import database.items.ObjectService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlCrud extends StorageCrud {

    // default database
    public static final String url = "jdbc:mysql://localhost:3306/warehelper";
    public static final String username = "testuser";
    public static final String password = "password";

    /**
     * Creates a new MySqlCrud connected to the default database.
     */
    public MySqlCrud() throws SQLException {
        // since we have to handle the error
        this.storageService = new MySql(url, username, password);
    }

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
        // set global information_schema_stats_expiry=0;
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
        // Ensure the category exists or create it if it doesn't
        String categoryName = item.getCategory().getName();
        List<Category> existingCategories = readCategoryByName(categoryName);

        int categoryId;
        if (existingCategories.isEmpty()) {
            // Category does not exist, create it
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            boolean categoryCreated = createCategory(newCategory);

            if (!categoryCreated) {
                return false;
            }

            // Retrieve the newly created category's ID
            List<Category> createdCategories = readCategoryByName(categoryName);
            if (createdCategories.isEmpty()) {
                return false;
            }

            categoryId = createdCategories.get(0).getCategoryId();
        } else {
            // Use the existing category's ID
            categoryId = existingCategories.get(0).getCategoryId();
        }

        // Set the category ID in the item
        item.getCategory().setCategoryId(categoryId);

        // Remove ID keys as they're auto-generated
        List<String> keys = item.getAttributeKeys();
        keys.remove(0);
        List<String> data = item.getAllAttributes();
        data.remove(0);
        List<DataType> types = item.getAttributeDataTypes();
        types.remove(0);
        // Create the item in the database
        boolean created = storageService.create(Item.TABLE_NAME, data, keys, types);

        if (created) {
            // Retrieve the item we just inserted using its temporary SKU and fix it
            List<String> idKey = new ArrayList<>();
            idKey.add(Item.ITEM_ID_KEY);

            List<Map<String, String>> result = storageService.readSearchRow(Item.TABLE_NAME, idKey, Item.SKU_KEY,
                    item.getSku(), DataType.STRING);

            if (!result.isEmpty()) {
                int generatedId = Integer.parseInt(result.get(0).get(Item.ITEM_ID_KEY));
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
        int newBundleId = storageService.getNextIncrementedId(Bundle.TABLE_NAME);
        if (!storageService.create(Bundle.TABLE_NAME, bundleData, bundleKeys, bundleTypes)) {
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
            if (!storageService.create(Bundle.ASSOCIATION_TABLE_NAME, itemBundleData, itemBundleKeys,
                    itemBundleTypes)) {
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
        return storageService.create(Category.TABLE_NAME, data, keys, types);
    }

    @Override
    public Item readItem(int itemId) {
        // keys should be PascalCase
        List<String> keys = ObjectService.getItemKeys();

        Map<String, String> itemData = this.storageService.read(Item.TABLE_NAME, itemId, keys);
        List<String> categoryKeys = ObjectService.getCategoryKeys();
        Map<String, String> innerCategoryData = readInnerCategory(itemData, categoryKeys);
        return ObjectService.createItem(itemData, innerCategoryData);
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
            categoryId = Integer.parseInt(itemMap.get(Category.CATEGORY_ID_KEY));
        } catch (Exception e) {
            throw new RuntimeException("Could not convert categoryId to int");
        }

        // read this category
        return this.storageService.read(Category.TABLE_NAME, categoryId, categoryKeys);
    }

    @Override
    public List<Item> readAllItems() throws RuntimeException {
        List<Item> items = new ArrayList<>();

        List<String> keys = ObjectService.getItemKeys();

        List<Map<String, String>> itemMaps = this.storageService.readAll(Item.TABLE_NAME, keys, null);
        List<Map<String, String>> categoryMaps = new ArrayList<>();

        List<String> categoryKeys = ObjectService.getCategoryKeys();
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
    public List<Item> readAllItemsSortBy(String sortBy, boolean isAscending) throws RuntimeException {
        List<Item> items = new ArrayList<>();

        List<String> keys = ObjectService.getItemKeys();

        // Read all items sorted by the specified column
        List<Map<String, String>> itemMaps = this.storageService.readAllSortBy(Item.TABLE_NAME, keys, sortBy,
                isAscending);
        List<Map<String, String>> categoryMaps = new ArrayList<>();

        List<String> categoryKeys = ObjectService.getCategoryKeys();
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

        List<String> keys = ObjectService.getCategoryKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readSearchRow(Category.TABLE_NAME, keys,
                Category.NAME_KEY,
                name,
                DataType.STRING);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }

        return categories;
    }

    @Override
    public List<Item> readItemByName(String name) throws RuntimeException {
        List<Item> items = new ArrayList<>();

        List<String> keys = ObjectService.getItemKeys();

        List<Map<String, String>> itemMaps = this.storageService.readSearchRow(Item.TABLE_NAME, keys,
                Item.NAME_KEY,
                name,
                DataType.STRING);
        List<Map<String, String>> categoryMaps = new ArrayList<>();

        List<String> categoryKeys = ObjectService.getCategoryKeys();
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
    public Item readItemBySKU(String sku) {

        List<String> keys = ObjectService.getItemKeys();

        List<Map<String, String>> itemDataMaps = this.storageService.readSearchRow(Item.TABLE_NAME, keys,
                Item.SKU_KEY,
                sku,
                DataType.STRING);

        if (itemDataMaps.isEmpty()) {
            throw new RuntimeException("ERROR: Item with SKU " + sku + " not found.");
        }
        Map<String, String> itemData = itemDataMaps.get(0);

        List<String> categoryKeys = ObjectService.getCategoryKeys();
        Map<String, String> innerCategoryData = readInnerCategory(itemData, categoryKeys);

        // Create and return the Item object using the retrieved
        return ObjectService.createItem(itemData, innerCategoryData);
    }

    @Override
    public Bundle readBundle(int bundleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readBundle'");
    }

    @Override
    public Category readCategory(int categoryId) {
        Map<String, String> categoryData = this.storageService.read("Category", categoryId,
                ObjectService.getItemKeys());
        return ObjectService.createCategory(categoryData);
    }

    @Override
    public List<Category> readAllCategories() throws RuntimeException {
        List<Category> categories = new ArrayList<>();

        List<String> keys = ObjectService.getCategoryKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readAll(Category.TABLE_NAME, keys, null);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }

        return categories;
    }

    @Override
    public List<Bundle> readAllBundles() throws RuntimeException {
        List<Bundle> bundles = new ArrayList<>();

        List<String> keys = ObjectService.getBundleKeys();
        keys.addAll(ObjectService.getItemKeys());
        keys.addAll(ObjectService.getCategoryKeys());

        List<InnerObject> innerObjects = ObjectService.getBundleInnerObjects();

        // we are reading the bundles, items, and categories
        List<Map<String, String>> bundleItemCategoryMaps = this.storageService.readAll(Bundle.TABLE_NAME, keys,
                innerObjects);

        // we want to save bundle IDs and the index into the bundles list so we can
        // modify them if they already exist
        Map<Integer, Integer> bundleIdToIdx = new HashMap<>();

        for (Map<String, String> bundleItemCategoryMap : bundleItemCategoryMaps) {
            int currentBundleId = Integer.parseInt(bundleItemCategoryMap.get(Bundle.BUNDLE_ID_KEY));

            if (bundleIdToIdx.containsKey(currentBundleId)) {
                // this bundle has already been created, modify it
                int bundleIdx = bundleIdToIdx.get(currentBundleId);

                // we only need to create an Item and put the data into the Bundle
                Item curItem = ObjectService.createItem(bundleItemCategoryMap, bundleItemCategoryMap);
                bundles.get(bundleIdx).addItem(curItem);
            } else {
                List<Map<String, String>> itemList = new ArrayList<>(); // since we need to create bundle
                itemList.add(bundleItemCategoryMap);
                Bundle curBundle = ObjectService.createBundle(bundleItemCategoryMap, itemList, itemList);
                bundles.add(curBundle);
                // we added a bundle to the list, keep track of its ID and index
                bundleIdToIdx.put(curBundle.getBundleId(), bundles.size() - 1);
            }
        }

        return bundles;
    }

    @Override
    public boolean updateItem(Item item) {
        List<String> keys = item.getAttributeKeys();
        List<String> data = item.getAllAttributes();
        List<DataType> types = item.getAttributeDataTypes();
        return storageService.update(Item.TABLE_NAME, data, keys, types);
    }

    @Override
    public boolean updateBundle(Bundle bundle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBundle'");
    }

    @Override
    public boolean updateCategory(List<String> categoryData, List<String> categoryKeys, List<DataType> categoryTypes) {

        return storageService.update(Category.TABLE_NAME, categoryData, categoryKeys, categoryTypes);
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
        return storageService.delete(Bundle.TABLE_NAME, Bundle.BUNDLE_ID_KEY, bundleId);
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        return storageService.delete(Category.TABLE_NAME, Category.CATEGORY_ID_KEY, categoryId);
    }

}
