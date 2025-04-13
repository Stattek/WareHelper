package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.List;

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
        return storageService.create("Item", data, keys);
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
        List<String> data = new ArrayList<>();
        data.add(category.getName());
        return storageService.create("Category", data, keys);
    }

    @Override
    public Item readItem(int itemId) {
        Item output = new Item();
        // keys should be PascalCase
        List<String> keys = output.getAttributeKeys();

        Map<String, String> data = this.storageService.read("Item", itemId, keys);
        output = ObjectService.createItem(data);

        return output;
    }

    @Override
    public List<Item> readAllItems() {
        List<Item> items = new ArrayList<>();

        Item temp = new Item();

        List<String> keys = temp.getAttributeKeys();

        List<Map<String, String>> dataMaps = this.storageService.readAll("Item", keys);

        for (int i = 0; i < dataMaps.size(); i++) {
            items.add(ObjectService.createItem(dataMaps.get(i)));
        }

        return items;
    }

    @Override
    public Bundle readBundle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readBundle'");
    }

    @Override
    public Category readCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readCategory'");
    }

    @Override
    public boolean updateItem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateItem'");
    }

    @Override
    public boolean updateBundle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBundle'");
    }

    @Override
    public boolean updateCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategory'");
    }

    @Override
    public boolean deleteItem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteItem'");
    }

    @Override
    public boolean deleteBundle(int bundleId) {
        // TODO Auto-generated method stub
        return storageService.delete("Bundle", "BundleId", bundleId);

    }

    @Override
    public boolean deleteCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategory'");
    }

}
