package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class MySqlCrud extends StorageCrud {

    public MySqlCrud(String url, String username, String password) throws SQLException {
        this.storageService = new MySql(url, username, password);
        // TODO: check if the MySql database has the tables for the programs and if not,
        // create them
    }

    private void setup() {

    }

    
    @Override
    public boolean createItem(Item item) {
        List<String> keys = item.getAttributeKeys();
        keys.remove(0);
        List<String> data = new ArrayList();
        data.add(Integer.toString(item.getItemId()));
        data.add(item.getSku());
        data.add(item.getName());
        data.add(Integer.toString(item.getCategory().getCategoryId()));
        data.add(Integer.toString(item.getEconomyInfo().getEconomyInfoId()));
        data.add(Integer.toString(item.getDateInfo().getDateInfoId()));
        data.add(Integer.toString(item.getPreference().getPreferenceId()));
        return storageService.create("Item", data, keys);
    }

    @Override
    public boolean createBundle(List<Item> items) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle(-1,0,items);
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
        List<String> data = new ArrayList();
        data.add(category.getName());
        return storageService.create("Category", data, keys);
    }

    @Override
    public Item readItem(int itemId) {
        // keys should be PascalCase
        ArrayList<String> keys = new ArrayList<>();

        // TODO: define these somewhere?
        keys.add("ItemId");
        keys.add("Sku");
        keys.add("Name");
        keys.add("CategoryId");
        keys.add("EconomyInfoId");
        keys.add("DateInfoId");
        keys.add("PreferenceId");

        Map<String, String> data = this.storageService.read("Item", itemId, keys);
        // TODO: convert data to Item
        System.err.println(data);

        throw new UnsupportedOperationException("Unfinished method 'readItem'");
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
