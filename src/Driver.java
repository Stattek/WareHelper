import java.sql.*;
import database.DatabaseConnection;
import database.DatabaseConnection.DatabaseQueryResult;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    public static void main(String[] args) {
        // connect to database
        DatabaseConnection database = null;
        try {
            // we connect to a database named "warehelper" as the user "testuser"
            // NOTE: this database has to be setup by the user before we can connect to it.
            // Is is worth it to set up ourselves?
            database = new DatabaseConnection("jdbc:mysql://localhost:3306/warehelper",
                    "testuser", "password");
            DatabaseQueryResult queryResult = database.performQuery("select * from ItemTest");
            ResultSet resultSet = queryResult.getResultSet();

            int itemId;
            String name;
            while (resultSet.next()) {
                itemId = resultSet.getInt("ItemId");
                name = resultSet.getString("Name").trim();
                System.out.println("ItemId : " + itemId + " Name : " + name);
            }

            queryResult.close();
            database.close();
        } catch (Exception e) {
            // TODO: stack traces should not be in production code, but are useful in dev
            e.printStackTrace();
        }
        System.out.println("Hello World!");
    }
}
