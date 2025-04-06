package database;

public interface Storage extends AutoCloseable {
    public boolean write(String data);

    public String read(int id);
}
