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
    public static final List<String> tableQueries = new ArrayList<>();

    // create table queries
    static {
        tableQueries.add(
                "create table Category(CategoryId int not null auto_increment, CategoryName varchar(255), primary key (CategoryId), unique (CategoryName))");
        tableQueries.add(
                "create table Item(ItemId int not null auto_increment, Sku varchar(255), ItemName varchar(255), Description varchar(1024), CategoryId int, Price double(20, 2), NumItems int, Created Date, LastModified Date, SellWithinNumDays int, LowInventoryThreshold int, PromotionPercentOff double(20,2), primary key (ItemId), foreign key (CategoryId) references Category(CategoryId))");
        tableQueries.add(
                "create table Bundle(BundleId int not null auto_increment, BundleDiscount double(20,2), primary key (BundleId))");
        tableQueries.add(
                "create table ItemBundle(BundleID int not null, ItemId int not null, primary key (BundleId, ItemId), foreign key (BundleId) references Bundle(BundleId) on delete cascade, foreign key (ItemId) references Item(ItemId) on delete cascade)");
    }

    /**
     * Creates a new MySqlCrud connected to the default database.
     */
    public MySqlCrud() throws SQLException {
        // since we have to handle the error
        this.storageService = new MySql(url, username, password, tableQueries);
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
        this.storageService = new MySql(url, username, password, tableQueries);
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
        List<String> keys = item.getAttributeKeysNoId();
        List<String> data = item.getAllAttributesNoId();
        List<DataType> types = item.getAttributeDataTypesNoId();

        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }
        // Create the item in the database
        boolean result = storageService.create(Item.TABLE_NAME, data, keys, types);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    @Override
    public boolean createBundle(Bundle bundle) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }

        List<String> bundleKeys = bundle.getAttributeKeysNoId();
        List<String> bundleData = bundle.getAllAttributesNoId();
        List<DataType> bundleTypes = bundle.getAttributeDataTypesNoId();

        // since nobody else should be writing at the same time, we can do this (single
        // threaded)
        int newBundleId = storageService.getNextIncrementedId(Bundle.TABLE_NAME);
        if (!storageService.create(Bundle.TABLE_NAME, bundleData, bundleKeys, bundleTypes)) {
            storageService.abortTransaction();
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
                storageService.abortTransaction();
                return false; // failure
            }
        }

        storageService.commitTransaction();
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
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }
        // This could be done using a hash map instead of two lists, it works for now
        // though
        List<String> keys = category.getAttributeKeysNoId();
        List<String> data = category.getAllAttributesNoId();
        List<DataType> types = category.getAttributeDataTypesNoId();
        boolean result = storageService.create(Category.TABLE_NAME, data, keys, types);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    @Override
    public Item readItem(int itemId) {
        if (!storageService.startTransaction()) {
            return null; // fail to start transaction
        }
        // keys should be PascalCase
        List<String> keys = ObjectService.getItemKeys();

        Map<String, String> itemData = this.storageService.read(Item.TABLE_NAME, itemId, keys);
        List<String> categoryKeys = ObjectService.getCategoryKeys();
        Map<String, String> innerCategoryData = readInnerCategory(itemData, categoryKeys);
        Item output = ObjectService.createItem(itemData, innerCategoryData);
        storageService.commitTransaction();
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
            categoryId = Integer.parseInt(itemMap.get(Category.CATEGORY_ID_KEY));
        } catch (Exception e) {
            throw new RuntimeException("Could not convert categoryId to int");
        }

        // read this category
        return this.storageService.read(Category.TABLE_NAME, categoryId, categoryKeys);
    }

    @Override
    public List<Item> readAllItems() throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }

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
        storageService.commitTransaction();

        return items;
    }

    @Override
    public List<Item> readAllItemsSortBy(String sortBy, boolean isAscending) throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }
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
        storageService.commitTransaction();

        return items;
    }

    @Override
    public List<Category> readCategoryByName(String name) throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }

        List<Category> categories = new ArrayList<>();

        List<String> keys = ObjectService.getCategoryKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readSearchRow(Category.TABLE_NAME, keys,
                Category.NAME_KEY,
                name,
                DataType.STRING);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }
        storageService.commitTransaction();

        return categories;
    }

    @Override
    public List<Item> readItemByName(String name) throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }

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
        storageService.commitTransaction();

        return items;
    }

    @Override
    public Item readItemBySKU(String sku) {
        if (!storageService.startTransaction()) {
            return null; // fail to start transaction
        }

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
        Item output = ObjectService.createItem(itemData, innerCategoryData);
        storageService.commitTransaction();
        return output;
    }

    @Override
    public Category readCategory(int categoryId) {
        if (!storageService.startTransaction()) {
            return null; // fail to start transaction
        }
        Map<String, String> categoryData = this.storageService.read("Category", categoryId,
                ObjectService.getItemKeys());
        Category output = ObjectService.createCategory(categoryData);
        storageService.commitTransaction();
        return output;
    }

    @Override
    public List<Category> readAllCategories() throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }

        List<Category> categories = new ArrayList<>();

        List<String> keys = ObjectService.getCategoryKeys();

        List<Map<String, String>> categoryMaps = this.storageService.readAll(Category.TABLE_NAME, keys, null);

        for (Map<String, String> categoryMap : categoryMaps) {
            categories.add(ObjectService.createCategory(categoryMap));
        }
        storageService.commitTransaction();

        return categories;
    }

    @Override
    public List<Bundle> readAllBundles() throws RuntimeException {
        if (!storageService.startTransaction()) {
            return new ArrayList<>(); // fail to start transaction
        }

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
        storageService.commitTransaction();

        return bundles;
    }

    @Override
    public boolean updateItem(List<String> data, List<String> keys, List<DataType> types) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }
        boolean result = storageService.update(Item.TABLE_NAME, data, keys, types);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    @Override
    public boolean updateCategory(List<String> categoryData, List<String> categoryKeys, List<DataType> categoryTypes) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }
        boolean result = storageService.update(Category.TABLE_NAME, categoryData, categoryKeys, categoryTypes);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    /**
     * Deletes an item by its itemId from the database.
     * 
     * @param itemId The ID of the item to delete.
     * @return True if the item was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteItem(int itemId) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }
        // Delete the item from the "Item" table where the ItemId matches the provided
        // itemId.
        boolean result = storageService.delete("Item", "ItemId", itemId);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    @Override
    public boolean deleteBundle(int bundleId) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }

        boolean result = storageService.delete(Bundle.TABLE_NAME, Bundle.BUNDLE_ID_KEY, bundleId);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        if (!storageService.startTransaction()) {
            return false; // fail to start transaction
        }

        boolean result = storageService.delete(Category.TABLE_NAME, Category.CATEGORY_ID_KEY, categoryId);
        if (result) {
            storageService.commitTransaction();
        } else {
            storageService.abortTransaction();
        }
        return result;
    }

}
