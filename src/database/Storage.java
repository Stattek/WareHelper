package database;

import java.util.List;

public interface Storage extends AutoCloseable {
    // TODO: Might be best to just have functions for read, write, update, and delete
    public boolean write(String data);

    public String read(String tableName, int id, List<String> keys);
}
