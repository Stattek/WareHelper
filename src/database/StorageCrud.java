package database;

import database.items.*;
import java.util.List;

public abstract class StorageCrud {
    protected Storage storageService;

    /**
     * Find the next incremented ID from the provided table (Item, Category).
     * 
     * @param tableName The table name to search for in Storage.
     * @return The table's next incremented ID.
     */
    public abstract int getNextId(String tableName);

    /**
     * Creates an Item in Storage from the provided item.
     * 
     * @param item The object to store in the Storage.
     * @return True upon success, false upon failure.
     */
    public abstract boolean createItem(Item item);

    /**
     * Creates a Bundle in Storage from the provided item.
     * 
     * @param bundle The object to store in the Storage.
     * @return True upon success, false upon failure.
     */
    public abstract boolean createBundle(Bundle bundle);

    /**
     * Creates a Category in Storage from the provided item.
     * 
     * @param category The object to store in the Storage.
     * @return True upon success, false upon failure.
     */
    public abstract boolean createCategory(Category category);

    /**
     * Reads an Item in Storage from the provided ID.
     * 
     * @param itemId The object ID to search for in Storage.
     * @return The read Item object, or null upon error.
     */
    public abstract Item readItem(int itemId);

    /**
     * Reads all Item objects in storage.
     * 
     * @return The read Items from storage, or an empty list, if none were found.
     */
    public abstract List<Item> readAllItems();

    /**
     * Reads all items and sorts by specified data
     * 
     * @param key         the data to sort by
     * @param isAscending sort by ascending (true) or decending (false)
     * 
     * @return The read Items from storage sorted by the key, or an empty list, if
     *         none were found
     */
    public abstract List<Item> readAllItemsSortBy(String key, boolean isAscending);

    /**
     * Reads all Bundle objects in storage.
     * 
     * @return The read Bundles from storage, or an empty list, if none were
     *         found.
     */
    public abstract List<Bundle> readAllBundles();

    /**
     * Reads an Item by name.
     * 
     * @param name The name of the Item.
     * 
     * @return The read Items from storage, or an empty list, if none were found.
     */
    public abstract List<Item> readItemByName(String name);

    /**
     * Reads an Item by sku.
     * 
     * @param sku The Item's sku.
     * 
     * @return The read Item from storage.
     */
    public abstract Item readItemBySKU(String sku);

    /**
     * Reads a Category by name.
     * 
     * @param name The name of the Category.
     * 
     * @return The read Categories from storage, or an empty list, if none were
     *         found.
     */
    public abstract List<Category> readCategoryByName(String name);

    /**
     * Reads all Category objects in storage.
     * 
     * @return The read Categories from storage, or an empty list, if none were
     *         found.
     */
    public abstract List<Category> readAllCategories();

    /**
     * Reads a Category in Storage from the provided ID.
     * 
     * @param categoryId The object ID to search for in Storage.
     * @return The read Category object, or null upon error.
     */
    public abstract Category readCategory(int categoryId);

    /**
     * Updates an Item in Storage from the provided object.
     * 
     * @param itemData  The updated data
     * @param itemKeys  The equivalent keys
     * @param itemTypes The equivalent types
     * @return True upon success, false upon failure.
     */
    public abstract boolean updateItem(List<String> itemData, List<String> itemKeys, List<DataType> itemTypes);

    /**
     * Updates a Category in storage from the provided object
     * 
     * @param categoryData The updated data
     * @param categoryKeys The equivalent keys
     * @return
     */
    public abstract boolean updateCategory(List<String> categoryData, List<String> categoryKeys,
            List<DataType> categoryTypes);

    /**
     * Deletes an Item in Storage from the provided object ID.
     * 
     * @param itemId The ID of the object to delete.
     * @return True upon success, false upon failure.
     */
    public abstract boolean deleteItem(int itemId);

    /**
     * Deletes a Bundle in Storage from the provided object ID.
     * 
     * @param bundleId The ID of the object to delete.
     * @return True upon success, false upon failure.
     */
    public abstract boolean deleteBundle(int bundleId);

    /**
     * Deletes a Category in Storage from the provided object ID.
     * 
     * @param categoryId The ID of the object to delete.
     * @return True upon success, false upon failure.
     */
    public abstract boolean deleteCategory(int categoryId);
}
