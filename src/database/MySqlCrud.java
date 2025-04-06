package database;

import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
        ArrayList<String> keys = new ArrayList<>();
        keys.add("ItemId");
        keys.add("sku");
        keys.add("name");
        keys.add("categoryId");
        keys.add("economyInfoId");
        keys.add("dateInfoId");
        keys.add("preferenceId");

        String data = this.storageService.read("ItemTest", itemId, keys);
        // TODO: convert data to Item
        System.err.println(data);

        throw new UnsupportedOperationException();
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
