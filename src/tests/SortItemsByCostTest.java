package tests;

import static org.junit.Assert.assertTrue;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.*;

import database.MySqlCrud;
import database.StorageCrud;
import database.items.Category;
import database.items.EconomyInfo;
import database.items.Item;

public class SortItemsByCostTest {
    private static StorageCrud storageCrud;
    private static ReentrantLock databaseMutex = new ReentrantLock();

    @Before
    public void setup() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories();
            addTestItems();
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

    @BeforeClass
    public static void initializeStorageCrud() {
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
    }

    /**
     * Adds test items to the database.
     */
    private void addTestItems() {
        addItem("TESTCATEGORY1", "testfirstsku", "firstItem", 10.12);
        addItem("TESTCATEGORY2", "testsecondsku", "secondItem", 20.50);
    }

    /**
     * Adds a single item to the database.
     */
    private void addItem(String categoryName, String sku, String name, double price) {
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
    }

    /**
     * Retrieves the highest category ID from the database.
     */
    private int getMaxCategoryId() {
        return storageCrud.getNextId(Category.TABLE_NAME);
    }

    @Test
    public void testSortItemsByCostAsc() {
        List<Item> sortedItems = storageCrud.readAllItemsSortBy(EconomyInfo.PRICE_KEY, true);

        // Assert items are sorted in ascending order
        for (int i = 0; i < sortedItems.size() - 1; i++) {
            assertTrue("Items are not sorted in ascending order by price",
                    sortedItems.get(i).getPrice() <= sortedItems.get(i + 1).getPrice());
        }
    }

    @Test
    public void testSortItemsByCostDesc() {
        List<Item> sortedItems = storageCrud.readAllItemsSortBy(EconomyInfo.PRICE_KEY, false);

        // Assert items are sorted in descending order
        for (int i = 0; i < sortedItems.size() - 1; i++) {
            assertTrue("Items are not sorted in descending order by price",
                    sortedItems.get(i).getPrice() >= sortedItems.get(i + 1).getPrice());
        }
    }
}