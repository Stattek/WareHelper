package database;

import java.util.List;
import java.util.Map;
import database.items.DataType;

public interface Storage extends AutoCloseable {
    // TODO: Might be best to just have functions for read, create, update, and
    // delete

    /**
     * Retrieves the next auto-increment ID value for a given table.
     * 
     * @param tableName The name of the table.
     * @return The next auto-increment ID, or -1 if the query fails.
     */
    public int getNextIncrementedId(String tableName);

    /**
     * Updates a row of the storage.
     * 
     * @param tableName The table name.
     * @param data      The data to update.
     * @param keys      The keys for the data to update.
     * @param dataTypes The datatypes for the data to update
     * @return True upon successful update, false otherwise.
     */
    public boolean update(String tableName, List<String> data, List<String> keys, List<DataType> dataTypes);

    /**
     * Deletes a row of the storage.
     * 
     * @param tableName The table name.
     * @param key       The key to delete on.
     * @param value     The value to delete on.
     * @return True upon successful update, false otherwise.
     */
    public boolean delete(String tableName, String key, int value);

    /**
     * Reads a single value from the storage device, including
     * any sub-objects.
     * 
     * @param tableName The table name.
     * @param id        The ID of the object.
     * @param keys      The keys to read from the object.
     * @return A Map containing the keys, along with the values pulled from the
     *         Storage.
     */
    public Map<String, String> read(String tableName, int id, List<String> keys);

    /**
     * Reads all objects from a table.
     * 
     * @param tableName    The table name.
     * @param keys         The keys to read.
     * @param innerObjects The inner objects to read, or null if there are no inner
     *                     objects in this table (for joins with MySQL, for
     *                     example).
     * @return A list of map data for each object read.
     */
    public List<Map<String, String>> readAll(String tableName, List<String> keys, List<InnerObject> innerObjects);

    /**
     * Reads all values from the storage device and sorts them by a specified key.
     * 
     * @param tableName The table name.
     * @param keys      The keys to read from the object.
     * @param sortByKey The key to sort the results by.
     * @param ascending Whether to sort in ascending (true) or descending (false)
     *                  order.
     * @return A List of Maps containing the keys, along with the values pulled from
     *         the Storage, sorted by the specified key.
     */
    public List<Map<String, String>> readAllSortBy(String tableName, List<String> keys, String sortByKey,
            boolean ascending);

    /**
     * Reads all values that are equal to the
     * 
     * @param tableName   The table name.
     * @param keys        The keys to read from the object.
     * @param haystackKey The name of the "haystack" to search through. The column
     *                    name of what we are looking for.
     * @param needleValue The "needle" value that we are searching through the
     *                    haystack for.
     * @param needleType  The datatype of the needle.
     * @return A List of Maps containing the keys, along with the values pulled from
     *         the Storage.
     */
    public List<Map<String, String>> readSearchRow(String tableName, List<String> keys, String haystackKey,
            String needleValue, DataType needleType);

    /**
     * Creates an entry to a table.
     * 
     * @param tableName The table name.
     * @param data      The data to put into the database.
     * @param keys      The keys of the data to put into the database. NOTE: should
     *                  have the same size and have the same data in the same order
     *                  as data.
     * @param dataTypes The datatypes of the data to put into the database. NOTE:
     *                  should have the same size and have the same data in the same
     *                  order as data.
     * 
     * @return True if successful, false otherwise.
     */
    public boolean create(String tableName, List<String> data, List<String> keys, List<DataType> dataTypes);

    /**
     * Start database transaction.
     * 
     * @return True if successful, false otherwise.
     */
    public boolean startTransaction();

    /**
     * Commit database transaction.
     * 
     * @return True if successful, false otherwise.
     */
    public boolean commitTransaction();

    /**
     * Abort database transaction.
     * 
     * @return True if successful, false otherwise.
     */
    public boolean abortTransaction();

}
