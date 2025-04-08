package database;

import java.util.List;
import java.util.Map;

public interface Storage extends AutoCloseable {
    // TODO: Might be best to just have functions for read, create, update, and
    // delete
    public boolean write(String data);

    public boolean delete(String tableName, String key, int value);

    public Map<String, String> read(String tableName, int id, List<String> keys);

    public boolean create(String tableName, List<String> data, List<String> keys);
}
