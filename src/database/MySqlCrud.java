package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class MySqlCrud extends StorageCrud {

    public MySqlCrud(String url, String username, String password) throws SQLException {
        this.storageService = new MySql(url, username, password);
        // TODO: check if the MySql database has the tables for the programs and if not,
        // create them
    }

    private void setup() {

    }

    @Override
    public boolean createItem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createItem'");
    }

    @Override
    public boolean createBundle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBundle'");
    }

    @Override
    public boolean createCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCategory'");
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
    public boolean deleteBundle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBundle'");
    }

    @Override
    public boolean deleteCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategory'");
    }

}
