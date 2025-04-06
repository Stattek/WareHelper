package database;

import java.util.List;

public interface Storage extends AutoCloseable {
    public boolean write(String data);

    public String read(String tableName, int id, List<String> keys);
}
