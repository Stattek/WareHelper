import java.sql.*;
import database.DatabaseConnection;
import database.DatabaseConnection.DatabaseQueryResult;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    private static final boolean isUsingLocalDatabase = false;

    public static void main(String[] args) {
        // connect to database
        DatabaseConnection database = null;
        try {
            // we connect to a database named "warehelper" as the user "testuser"
            // NOTE: this database has to be setup by the user before we can connect to it.
            // Is is worth it to set up ourselves?
            String url = "jdbc:mysql://localhost:3306/warehelper";
            String username = "testuser";
            String password = "password";
            if (!isUsingLocalDatabase) {
                url = "jdbc:oracle:thin:@10.110.10.90:1521:oracle";
                username = "IT326T03";
                password = "reach98";
            }
            database = new DatabaseConnection(url, username, password);
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
