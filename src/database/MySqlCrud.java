package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

import java.sql.SQLException;
import java.util.ArrayList;
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

    // remember, if you do not document an overridden method, it inherits the
    // superclass javadoc
    @Override
    public boolean createItem(Item item) {
        List<String> keys = item.getAttributeKeys();
        keys.remove(0);
        List<String> data = item.getAllAttributes();
        data.remove(0);
        List<DataType> types = item.getAttributeDataTypes();
        types.remove(0);
        return storageService.create("Item", data, keys, types);
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
