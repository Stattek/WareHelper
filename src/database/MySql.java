package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySql implements Storage {
    private Connection connection = null;

    /**
     * Creates a Database object with a connection to a database.
     * 
     * @param url      The URL to the database.
     * @param username The username for the user of the database.
     * @param password The password for the user of the database.
     */
    public MySql(String url, String username, String password) throws SQLException {
        // we don't want to handle this exception ourselves, so the user can decide what
        // to do if this fails
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Closes the connection to the database.
     * 
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        connection.close(); // close database connection
    }

    /**
     * Sends a query to the database and validates the specified query.
     * 
     * @param query The query to send to the database.
     * 
     * @return The resulting set of data from the query. NOTE: The user of this
     *         function is responsible for closing this ResultSet object.
     * 
     * @throws SQLException
     */
    public DatabaseQueryResult performQuery(String query) throws SQLException {
        Statement statement;
        statement = connection.createStatement();

        // TODO: Validate that the query (possibly from user input) does not attempt SQL
        // injection by calling function to do this check before the statement below.
        ResultSet resultSet = statement.executeQuery(query);

        return new DatabaseQueryResult(statement, resultSet);
    }

    /**
     * Represents the result from a database query, as we must not close the
     * statement before getting the data from the resultSet.
     */
    public class DatabaseQueryResult implements AutoCloseable {
        private Statement statement = null;
        private ResultSet resultSet = null;

        /**
         * Closes the resources for the DatabaseQueryResult.
         * 
         * @throws Exception
         */
        @Override
        public void close() throws SQLException {
            statement.close();
            resultSet.close();
        }

        /**
         * Creates a new DatabaseQueryResult object.
         * 
         * @param statement
         * @param resultSet
         */
        public DatabaseQueryResult(Statement statement, ResultSet resultSet) {
            this.statement = statement;
            this.resultSet = resultSet;
        }

        /**
         * Returns the result set from this database query.
         * <br>
         * <br>
         * NOTE: This resource must not be closed by the user of this function.
         * 
         * @return The result set from this database query.
         */
        public ResultSet getResultSet() {
            return this.resultSet;
        }
    }

    @Override
    public boolean write(String data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'write'");
    }

    @Override
    public Map<String, String> read(String tableName, int id, List<String> keys) {
        HashMap<String, String> output = new HashMap<>();
        try {
            DatabaseQueryResult queryResult = performQuery(
                    "select * from " + tableName + " where " + tableName + "Id = " + id);
            ResultSet resultSet = queryResult.getResultSet();

            // add keys to the output hashmap
            while (resultSet.next()) {
                for (String key : keys) {
                    output.put(key, resultSet.getString(key));
                }
            }

            queryResult.close();
        } catch (Exception e) {
            // TODO: should we just throw an exception?
            e.printStackTrace();
        }

        return output;
    }
}
