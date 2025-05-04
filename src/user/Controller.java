package user;

import database.*;
import database.importers.Importer;
import database.importers.ImporterFactory;
import database.importers.ImporterTypes;
import database.items.Bundle;
import database.items.Category;
import database.items.DataType;
import database.items.DateInfo;
import database.items.EconomyInfo;
import database.items.Item;
import database.items.ObjectService;
import database.reports.ReportGeneratorFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Controller {

    private static final ReportGeneratorFactory reportGeneratorFactory = new ReportGeneratorFactory();
    private static final StorageCrud storageCrud;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /*
     * This may need to be moved to an environment file.
     */
    private static final String url = "jdbc:mysql://localhost:3306/warehelper";
    private static final String username = "testuser";
    private static final String password = "password";

    static {
        try {
            storageCrud = new MySqlCrud(url, username, password);
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
    public static boolean createCategory(Map<String, String> categoryData) {
        // Add the next ID to the category data map
        int nextCategoryId = storageCrud.getNextId(Category.TABLE_NAME);
        categoryData.put(Category.CATEGORY_ID_KEY, Integer.toString(nextCategoryId));
        Category category = ObjectService.createCategory(categoryData);
        return storageCrud.createCategory(category);
    }

    /**
     * Creates a Bundle from dictionaries.
     * 
     * @param bundleData The Item data.
     * @param itemIds    The IDs of the Items this bundle contains.
     * @return True if the Bundle could be created, false otherwise.
     */
    public static boolean createBundle(Map<String, String> bundleData, List<Integer> itemIds) {
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
    public static Pair<Boolean, String> createItem(Map<String, String> itemData,
            Map<String, String> innerCategoryData) {
        String categoryName = innerCategoryData.get(Category.NAME_KEY);

        List<Category> categories = storageCrud.readCategoryByName(categoryName);
        if (categories.isEmpty()) {
            // this category name does not exist
            return new Pair<>(false, null); // empty list
        }

        // since we know that the list is not empty
        int categoryId = categories.get(0).getCategoryId();

        // now we know the ID for the Item's inner Category object
        innerCategoryData.put(Category.CATEGORY_ID_KEY, Integer.toString(categoryId));
        itemData.put(Item.CATEGORY_ID_KEY, Integer.toString(categoryId));

        // we want to get the ID of the next item to set the SKU number
        int itemId = storageCrud.getNextId(Item.TABLE_NAME);
        String sku = categoryName + Integer.toString(itemId);
        itemData.put(Item.SKU_KEY, sku);
        Item item = ObjectService.createItemStub(itemData, innerCategoryData);
        boolean result = storageCrud.createItem(item);

        return new Pair<>(result, sku);
    }

    /**
     * Reads an item by its itemId.
     * 
     * @param itemId The item ID of the item to read.
     * @return A JSON representation of the read Item from storage.
     */
    public static String readItem(int itemId) {
        return gson.toJson(storageCrud.readItem(itemId));
    }

    /**
     * Reads all items in storage.
     * 
     * @return A JSON representation of all the Item objects read from storage.
     */
    public static String readAllItems() {
        return gson.toJson(storageCrud.readAllItems());
    }

    /**
     * Reads all Items with the name provided.
     * 
     * @param name The name to search for.
     * @return A JSON represenation of all the Item objects read from storage.
     */
    public static String readItemByName(String name) {
        return gson.toJson(storageCrud.readItemByName(name));
    }

    /**
     * Reads all Categories with the name provided.
     * 
     * @param name The name to search for.
     * @return A JSON representation of all the Category objects read from storage.
     */
    public static String readCategoryByName(String name) {
        return gson.toJson(storageCrud.readCategoryByName(name));
    }

    /**
     * Reads all categories in storage.
     * 
     * @return A JSON representation of all the Category objects read from storage.
     */
    public static String readAllCategories() {
        return gson.toJson(storageCrud.readAllCategories());
    }

    /**
     * Reads all items sorted by their name.
     * 
     * @param isAscending Sort by ascending (true) or descending (false).
     * @return A JSON representation of all the Item objects sorted by name.
     */
    public static String readAllItemsSortByName(boolean isAscending) {
        return gson.toJson(storageCrud.readAllItemsSortBy(Item.NAME_KEY, isAscending));
    }

    /**
     * Reads all items sorted by their cost.
     * 
     * @param isAscending Sort by ascending (true) or descending (false).
     * @return A JSON representation of all the Item objects sorted by cost.
     */
    public static String readAllItemsSortByCost(boolean isAscending) {
        return gson.toJson(storageCrud.readAllItemsSortBy(EconomyInfo.PRICE_KEY, isAscending));
    }

    /**
     * Reads all items grouped by their category.
     * 
     * @param isAscending Sort by ascending (true) or descending (false).
     * @return A JSON representation of all the Item objects sorted by cost.
     */
    public static String readAllItemsGroupByCategory(boolean isAscending) {
        return gson.toJson(storageCrud.readAllItemsSortBy(Item.CATEGORY_ID_KEY, isAscending));
    }

    /**
     * Reads all items sorted by their date.
     * 
     * @param isAscending Sort by ascending (true) or descending (false).
     * @return A JSON representation of all the Item objects sorted by date.
     */
    public static String readAllItemsSortByDate(boolean isAscending) {
        return gson.toJson(storageCrud.readAllItemsSortBy(DateInfo.CREATED_KEY, isAscending));
    }

    /**
     * Reads all items in storage.
     * 
     * @return A JSON representation of all the Item objects read from storage.
     */
    public static String readAllBundles() {
        return gson.toJson(storageCrud.readAllBundles());
    }

    /**
     * Deletes a category by its categoryId.
     * 
     * @param categoryId The ID of the category to delete.
     * @return {@code true} if the category was successfully deleted, {@code false}
     *         otherwise.
     */
    public static boolean deleteCategory(int categoryId) {
        return storageCrud.deleteCategory(categoryId);
    }

    /**
     * Updates a category in the database.
     * 
     * @param categoryId   The ID of the category to update.
     * @param categoryData A map containing the updated category data.
     * @return {@code true} if the category was successfully updated, {@code false}
     *         otherwise.
     */
    public static boolean updateCategory(List<String> categoryData, List<String> categoryKeys) {
        List<DataType> allTypes = ObjectService.getCategoryDataTypes();
        List<String> allKeys = ObjectService.getCategoryKeys();
        List<DataType> types = new ArrayList<>();
        for (String key : categoryKeys) {
            int index = allKeys.indexOf(key);
            if (index != -1) {
                types.add(allTypes.get(index));
            }
        }
        return storageCrud.updateCategory(categoryData, categoryKeys, types);
    }

    /**
     * Deletes a bundle by its bundleId.
     * 
     * @param bundleId The ID of the bundle to delete.
     * @return {@code true} if the bundle was successfully deleted, {@code false}
     *         otherwise.
     */
    public static boolean deleteBundle(int bundleId) {
        return storageCrud.deleteBundle(bundleId);
    }

    /**
     * Gets the keys for an Item.
     * 
     * @return A List of keys.
     */
    public static List<String> getItemKeys() {
        return ObjectService.getItemKeys();
    }

    /**
     * Gets the keys for a Bundle.
     * 
     * @return A List of keys.
     */
    public static List<String> getBundleKeys() {
        return ObjectService.getBundleKeys();
    }

    /**
     * Gets the keys for a Category.
     * 
     * @return A List of keys.
     */
    public static List<String> getCategoryKeys() {
        return ObjectService.getCategoryKeys();
    }

    /**
     * Gets the keys for an Item excluding the "Id" key.
     * 
     * @return A List of keys excluding the "Id" key.
     */
    public static List<String> getItemKeysNoId() {
        return ObjectService.getItemKeysNoId();
    }

    /**
     * Gets the keys required for an Item (excludes the "Id" and "Sku" keys).
     * 
     * @return A List of keys.
     */
    public static List<String> getItemKeysNoIdNoSku() {
        return ObjectService.getItemKeysRequired();
    }

    /**
     * Gets the keys for a Bundle excluding the "Id" key.
     * 
     * @return A List of keys excluding the "Id" key.
     */
    public static List<String> getBundleKeysNoId() {
        return ObjectService.getBundleKeysNoId();
    }

    /**
     * Gets the keys for a Category excluding the "Id" key.
     * 
     * @return A List of keys excluding the "Id" key.
     */
    public static List<String> getCategoryKeysNoId() {
        return ObjectService.getCategoryKeysNoId();
    }

    /**
     * Deletes an item by its itemId.
     * 
     * @param itemId The ID of the item to delete.
     * @return {@code true} if the item was successfully deleted, {@code false}
     *         otherwise.
     */
    public static boolean deleteItem(int itemId) {
        // Check if the item exists by querying the storage
        Item item = storageCrud.readItem(itemId);
        if (item == null) {
            // If item does not exist, return false
            return false;
        }

        // Perform the deletion of the item
        return storageCrud.deleteItem(itemId);
    }

    /**
     * Generated a low inventory report
     * 
     * @return True if report is generated
     */
    public static boolean lowInventoryReport() {
        List<Item> items = storageCrud.readAllItems();
        List<Bundle> bundles = storageCrud.readAllBundles();
        List<Category> categories = storageCrud.readAllCategories();
        return reportGeneratorFactory.generateLowInventoryReport(categories, items, bundles);
    }

    /**
     * Generate a unsold inventory report
     * 
     * @return True if report is generated
     */
    public static boolean unsoldInventoryReport() {
        List<Item> items = storageCrud.readAllItems();
        List<Bundle> bundles = storageCrud.readAllBundles();
        List<Category> categories = storageCrud.readAllCategories();
        return reportGeneratorFactory.generateUnsoldInventoryReport(categories, items, bundles);
    }

    /**
     * Generate an inventory volume Report
     * 
     * @return True if reprot is generated
     */
    public static boolean inventoryVolumeReport() {
        List<Item> items = storageCrud.readAllItems();
        List<Bundle> bundles = storageCrud.readAllBundles();
        List<Category> categories = storageCrud.readAllCategories();
        return reportGeneratorFactory.generateInventoryVolumeReport(categories, items, bundles);
    }

    /**
     * Deletes an item by its itemId.
     * 
     * @param filePath The path to the csv file.
     * @return A JSON representation of all the Item objects added to storage.
     */
    public static boolean importItems(String filePath) {
        Importer<Pair<List<Map<String, String>>, List<Map<String, String>>>> importer = ImporterFactory
                .createItemImporter(ImporterTypes.CSV);
        Pair<List<Map<String, String>>, List<Map<String, String>>> data = importer.importData(filePath);
        List<Map<String, String>> items = data.getFirst();
        List<Map<String, String>> categories = data.getSecond();

        if (items.size() != categories.size()) {
            return false; // fail
        }

        for (int i = 0; i < items.size(); i++) {
            Pair<Boolean, String> result = createItem(items.get(i), categories.get(i));
            if (!result.getFirst()) {
                return false; // could not create item
            }
        }

        return true; // success
    }

    /**
     * Validates a string input is a valid string
     * 
     * @param input User inputed string
     * @return
     */
    public static boolean validateString(String input) {
        return InputValidator.validateString(input);
    }

    /**
     * Validates an inputed string can be parsed as an int
     * 
     * @param input User inputed string
     * @return
     */
    public static boolean validateStringToInt(String input) {
        return InputValidator.validateStringToInt(input);
    }

    /**
     * Gets just the ID key for an Item.
     * 
     * @return The item ID key.
     */
    public static String getItemIdKey() {
        return ObjectService.getItemIdKey();
    }

    /**
     * Gets just the ID key for a Bundle.
     * 
     * @return The bundle ID key.
     */
    public static String getBundleIdKey() {
        return ObjectService.getBundleIdKey();
    }

    /**
     * Gets just the ID key for a Category.
     * 
     * @return The category ID key.
     */
    public static String getCategoryIdKey() {
        return ObjectService.getCategoryIdKey();
    }
}
