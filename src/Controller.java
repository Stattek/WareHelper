import database.*;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;

import java.sql.Date;
import java.sql.SQLException;

public class Controller {
    private final MySqlCrud storageCrud;

    /*
     * This may need to the moved to an eviorment file.
     */
    String url = "jdbc:mysql://localhost:3306/warehelper";
    String username = "testuser";
    String password = "password";

    public Controller() {
        try {
            this.storageCrud = new MySqlCrud(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize Database", e);
        }
    }

    /*
     * Sends a request to the StorageCrud to create a category
     */
    public boolean createCategory(String categoryName) {
        //TODO: IMPLEMENT
        return false;
    }

    public Item readItem(int itemId) {
        return storageCrud.readItem(itemId);
    }
}
