package database;

import database.items.*;

public abstract class StorageCrud {
    protected Storage storageService;
    protected ObjectFactory objectFactory;

    public abstract boolean createItem();

    public abstract boolean createBundle();

    public abstract boolean createCategory();

    public abstract Item readItem(int itemId);

    public abstract Bundle readBundle();

    public abstract Category readCategory();

    public abstract boolean updateItem();

    public abstract boolean updateBundle();

    public abstract boolean updateCategory();

    public abstract boolean deleteItem();

    public abstract boolean deleteBundle();

    public abstract boolean deleteCategory();
}
