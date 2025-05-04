package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.MySqlCrud;
import database.StorageCrud;
import database.items.Category;
import database.items.Item;
import user.Controller;

public class SortItemsByCostTest {
    private static StorageCrud storageCrud;
    private static ReentrantLock databaseMutex = new ReentrantLock();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void setup() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories();
        } finally {
            databaseMutex.unlock();
        }
    }

    @After
    public void cleanup() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories();
        } finally {
            databaseMutex.unlock();
        }
    }

    static {
        try {
            storageCrud = new MySqlCrud();
        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize MySqlCrud", e);
        }
    }

    /**
     * Deletes all Items and Categories in the database.
     */
    private void deleteAllItemsAndCategories() {
        // Delete all items
        databaseMutex.lock();
        List<Item> items = storageCrud.readAllItems();
        for (Item item : items) {
            assertTrue("Failed to delete item with ID: " + item.getItemId(), storageCrud.deleteItem(item.getItemId()));
        }

        // Delete all categories
        List<Category> categories = storageCrud.readAllCategories();
        for (Category category : categories) {
            assertTrue("Failed to delete category with ID: " + category.getCategoryId(),
                    storageCrud.deleteCategory(category.getCategoryId()));
        }
        databaseMutex.unlock();
    }

    

    /**
     * Adds a single item to the database.
     */
    private void addItem(String categoryName, String sku, String name, double price, List<Item> items) {
        Category category = new Category(categoryName);
        assertTrue("Failed to create category: " + categoryName, storageCrud.createCategory(category));

        // Retrieve the newly created category
        category.setCategoryId(getMaxCategoryId());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Item item = new Item(sku, name, "description", category, price, 24, Date.valueOf(formattedDate),
                Date.valueOf(formattedDate), 10, 23, 0.0);

        assertTrue("Failed to create item: " + name, storageCrud.createItem(item));
        items.add(item);
    }

    /**
     * Retrieves the highest category ID from the database.
     */
    private int getMaxCategoryId() {
        return storageCrud.getNextId(Category.TABLE_NAME);
    }

    /*
     * Test reading all items sorted by cost in ascending order. On empty database,
     * expect an empty JSON array.
     */
    @Test
    public void testReadAllItemsSortByCost_Ascending_EmptyDatabase() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories(); // Ensure database is empty
            String result = Controller.readAllItemsSortByCost(true);
            assertEquals("[]", result); // Expect an empty JSON array
        } finally {
            databaseMutex.unlock();
        }
    }

    /*
     * Test reading all items sorted by cost in descending order. On empty database,
     * expect an empty JSON array.
     */
    @Test
    public void testReadAllItemsSortByCost_Descending_EmptyDatabase() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories(); // Ensure database is empty
            String result = Controller.readAllItemsSortByCost(false);
            assertEquals("[]", result); // Expect an empty JSON array
        } finally {
            databaseMutex.unlock();
        }
    }

    /*
     * Test reading all items sorted by cost in ascending order. On database with
     * items, expect a JSON array of items sorted by cost in ascending order.
     */
    @Test
    public void testReadAllItemsSortByCost_Ascending_WithItems() {
        databaseMutex.lock();
        List<Item> items = new ArrayList<>();
        try {
            deleteAllItemsAndCategories();
            addItem("CATEGORY1", "sku1", "item1", 10.00, items);
            addItem("CATEGORY2", "sku2", "item2", 5.00, items);
            addItem("CATEGORY3", "sku3", "item3", 20.00, items);

            String result = Controller.readAllItemsSortByCost(true);
            items.sort((item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));
            assertEquals(gson.toJson(items), result);
        } finally {
            databaseMutex.unlock();
        }
    }

    /*
     * Test reading all items sorted by cost in descending order. On database with
     * items, expect a JSON array of items sorted by cost in descending order.
     */
    @Test
    public void testReadAllItemsSortByCost_Descending_WithItems() {
        databaseMutex.lock();
        List<Item> items = new ArrayList<>();
        try {
            deleteAllItemsAndCategories();
            addItem("CATEGORY1", "sku1", "item1", 10.00, items);
            addItem("CATEGORY2", "sku2", "item2", 5.00, items);
            addItem("CATEGORY3", "sku3", "item3", 20.00, items);

            String result = Controller.readAllItemsSortByCost(false);
            items.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
            assertEquals(gson.toJson(items), result);
        } finally {
            databaseMutex.unlock();
        }
    }

}