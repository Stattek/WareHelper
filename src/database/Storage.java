package database;

import java.util.List;
import java.util.Map;

public interface Storage extends AutoCloseable {
    // TODO: Might be best to just have functions for read, create, update, and
    // delete
    public boolean write(String data);

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

    public boolean create(String tableName, List<String> data, List<String> keys);
}
