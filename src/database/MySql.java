package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import database.items.DataType;

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
     * Retrieves the next auto-increment ID value for a given table.
     * 
     * @param tableName The name of the table.
     * @return The next auto-increment ID, or -1 if the query fails.
     */
    public int getNextIncrementedId(String tableName) {
        String query = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'warehelper' AND TABLE_NAME = '"
                + tableName + "'";

        try (DatabaseQueryResult result = performQuery(query)) {
            ResultSet rs = result.getResultSet();
            if (rs.next()) {
                return rs.getInt("AUTO_INCREMENT");
            } else {
                throw new SQLException("Could not retrieve next auto-increment ID."); // could not find the table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
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
    public boolean update(String tableName, List<String> data, List<String> keys) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * Reads a single row from a table in the database.
     * 
     * @param query The query.
     * @param keys  The keys for the query.
     * @return The Map of data for the row.
     */
    private Map<String, String> readSingle(String query, List<String> keys) {
        HashMap<String, String> output = new HashMap<>();
        try {
            DatabaseQueryResult queryResult = performQuery(query);
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

    @Override
    public Map<String, String> read(String tableName, int id, List<String> keys) {
        return readSingle(
                "select * from " + tableName + " where " + tableName + "Id = " + id, keys);
    }

    /**
     * Reads all values that are equal to the needle value.
     * 
     * @param tableName   The table name.
     * @param keys        The keys to read from the object.
     * @param haystackKey The name of the "haystack" to search through. The column
     *                    name of what we are looking for.
     * @param needleValue The "needle" value that we are searching through the
     *                    haystack for.
     * @return A List of Maps containing the keys, along with the values pulled from
     *         the Storage.
     */
    public List<Map<String, String>> readSearchRow(String tableName, List<String> keys, String haystackKey,
            String needleValue, DataType needleType) {
        return readList(
                "select * from " + tableName + " where " + haystackKey + " = "
                        + formatData(needleValue, needleType),
                keys);
    }

    /**
     * Reads all rows from a table in the database from the given query.
     * 
     * @param query The query.
     * @param keys  The keys for the query.
     * @return The List of Map of data from the query.
     */
    private List<Map<String, String>> readList(String query, List<String> keys) {
        List<Map<String, String>> output = new ArrayList<>();

        try {
            DatabaseQueryResult queryResult = performQuery(query);
            ResultSet resultSet = queryResult.getResultSet();

            while (resultSet.next()) {
                // get all of the elements for each hash map
                HashMap<String, String> curItem = new HashMap<>();

                for (String key : keys) {
                    curItem.put(key, resultSet.getString(key));
                }

                output.add(curItem);
            }

        } catch (Exception e) {
            // TODO: should we just throw an exception?
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public List<Map<String, String>> readAll(String tableName, List<String> keys, List<InnerObject> innerObjects) {
        String query = "select * from " + tableName;
        if (innerObjects != null) {
            for (InnerObject innerObject : innerObjects) {
                query += " join " + innerObject.getObjectName() + " on " + innerObject.getParentObject() + "."
                        + innerObject.getThisId() + "=" + innerObject.getObjectName() + "." + innerObject.getThisId();
            }
        }
        return readList(query, keys);
    }

    /**
     * Inserts a new row into the specified table in the database.
     * 
     * @param tableName The name of the table where the data will be inserted.
     * @param tableData A list of values to be inserted into the table.
     * @param keys      A list of column names corresponding to the values in
     *                  {@code tableData}.
     * 
     * @return {@code true} if the row was successfully inserted, {@code false}
     *         otherwise.
     *         Returns {@code false} if the size of {@code tableData} and
     *         {@code keys} do not match
     *         or if an exception occurs during the insertion process.
     */
    @Override
    public boolean create(String tableName, List<String> tableData, List<String> keys, List<DataType> dataTypes) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<String> formattedData = formatDataList(tableData, dataTypes);
        if (formattedData.size() != keys.size()) {
            return false;
        }
        for (int i = 0; i < formattedData.size() - 1; i++) {
            values.append(formattedData.get(i)).append(",");
        }
        values.append(formattedData.get(formattedData.size() - 1));

        for (int i = 0; i < keys.size() - 1; i++) {
            columns.append(keys.get(i)).append(",");
        }
        columns.append(keys.get(formattedData.size() - 1));

        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        try {
            performPreparedStatement(query);
        } catch (Exception e) {
            // TODO: should we just throw an exception?
            e.printStackTrace();

            return false;
        }

        return true;

    }

    /**
     * Deletes a row from the specified table in the database.
     * 
     * @param tableName The name of the table where the data will be inserted.
     * @param key       The name of the record's unique identifier
     * @param value     The value of the record's unique identifier
     * 
     * @return True on success, false on failure.
     */
    @Override
    public boolean delete(String tableName, String key, int value) {
        String query = "DELETE FROM " + tableName + " WHERE " + key + "=" + value;
        try {
            performPreparedStatement(query);
        } catch (Exception e) {
            // TODO: should we just throw an exception?
            e.printStackTrace();

            return false;
        }

        return true;

    }

    /**
     * Executes a prepared SQL statement on the database.
     * 
     * @param query The SQL query to be executed. This query should be properly
     *              formatted to prevent SQL injection.
     * 
     * @throws SQLException If an error occurs while preparing or executing the
     *                      statement.
     */
    private void performPreparedStatement(String query) throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement(query);

        statement.execute();
    }

    public int getNextId(String tableName, String idColumn) {
        int nextId = -1;
        // TODO get next ID.
        return nextId;
    }

    /**
     * Formats a list of data.
     * 
     * @param data  The data list.
     * @param types The types for each piece of data.
     * @return The formatted data list for use with the database.
     */
    private List<String> formatDataList(List<String> data, List<DataType> types) {

        List<String> formattedData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            // add all formatted data
            formattedData.add(formatData(data.get(i), types.get(i)));
        }
        return formattedData;
    }

    /**
     * Formats a piece of data.
     * 
     * @param data  The data.
     * @param types The type for the piece of data.
     * @return The formatted data for use with the database.
     */
    private String formatData(String data, DataType type) {

        String formattedData = "";

        if (type == DataType.STRING) {
            formattedData = "\"" + data + "\"";
        } else if (type == DataType.DATE) {
            // keeping this here incase we need to adjust it later
            formattedData = "\"" + data + "\""; // Assuming value is already in a valid MySQL date format
        } else {
            formattedData = data; // For other types, keep as is
        }
        return formattedData;
    }

}
