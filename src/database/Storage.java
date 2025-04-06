package database;

public interface Storage {
    public void close();

    public boolean write(String data);

    public String read(int id);
}
