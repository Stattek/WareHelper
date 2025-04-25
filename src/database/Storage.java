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
     * Updates the SKU of an item by its ID.
     * 
     * @param itemId The ID of the item to update.
     * @param newSku The new SKU value.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateSKU(int itemId, String newSku); // TODO: This is just temporary until the update function is
                                                         // made.

    public boolean update(String tableName, List<String> data, List<String> keys);

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

    public List<Map<String, String>> readAll(String tableName, List<String> keys);

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

    public boolean create(String tableName, List<String> data, List<String> keys, List<DataType> dataTypes);
}
