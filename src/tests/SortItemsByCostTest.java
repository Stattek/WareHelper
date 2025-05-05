package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import database.MySql;
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
import database.Storage;
import database.StorageCrud;
import database.items.Category;
import database.items.Item;
import user.Controller;

public class SortItemsByCostTest {
    private static StorageCrud storageCrud;
    private static Storage storage;
    private static ReentrantLock databaseMutex = new ReentrantLock();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Item> expectedItems = new ArrayList<>();

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
            storage = new MySql(MySqlCrud.url, MySqlCrud.username, MySqlCrud.password, MySqlCrud.tableQueries);
        } catch (SQLException sqle) {
            throw new RuntimeException("Could not initialize MySqlCrud or MySql");
        }
    }

    /**
     * Deletes all Items and Categories in the database.
     */
    public static void deleteAllItemsAndCategories() {
        // delete items
        List<Item> items = storageCrud.readAllItems();
        for (Item item : items) {
            // expect to delete every item
            assertTrue(storageCrud.deleteItem(item.getItemId()));
        }

        // delete categories too
        List<Category> categories = storageCrud.readAllCategories();
        for (Category category : categories) {
            // expect to delete every item
            assertTrue(storageCrud.deleteCategory(category.getCategoryId()));
        }
        expectedItems.clear();
        storage.commitTransaction();
    }

    /**
     * Creates a first item in the database.
     */
    public static void addFirstItem() {
        // create a new category for our item
        Category category = new Category("TESTCATEGORY");

        assertEquals(storageCrud.createCategory(category), true);
        List<Category> categories = storageCrud.readAllCategories();
        int maxCategoryId = 0;
        int maxIdx = 0;

        // find the new category
        for (int i = 0; i < categories.size(); i++) {
            if (maxCategoryId < categories.get(i).getCategoryId()) {
                // found the new highest ID
                maxCategoryId = categories.get(i).getCategoryId();
                maxIdx = i;
            }
        }
        // set the ID of the newest category
        category.setCategoryId(categories.get(maxIdx).getCategoryId());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Item firstItem = new Item("testfirstsku", "firstItem", "this is the first item", category, 10.12, 24,
                Date.valueOf(formattedDate), Date.valueOf(formattedDate), 10, 23, 0.0);
        expectedItems.add(firstItem);

        // get the ID
        int itemId = storageCrud.getNextId(Item.TABLE_NAME);
        firstItem.setItemId(itemId);

        // create the item
        assertTrue(storageCrud.createItem(firstItem));
        // commit to storage, since it did not commit these changes yet
        storage.commitTransaction();
    }

    /**
     * Creates a first item in the database.
     */
    public static void addSecondItem() {
        // create a new category for our item
        Category category = new Category("TESTCATEGORY1");

        assertEquals(storageCrud.createCategory(category), true);
        List<Category> categories = storageCrud.readAllCategories();
        int maxCategoryId = 0;
        int maxIdx = 0;

        // find the new category
        for (int i = 0; i < categories.size(); i++) {
            if (maxCategoryId < categories.get(i).getCategoryId()) {
                // found the new highest ID
                maxCategoryId = categories.get(i).getCategoryId();
                maxIdx = i;
            }
        }
        // set the ID of the newest category
        category.setCategoryId(categories.get(maxIdx).getCategoryId());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Item firstItem = new Item("testfirstsku1", "first2Item", "this is the first item", category, 20.12, 24,
                Date.valueOf(formattedDate), Date.valueOf(formattedDate), 10, 23, 0.0);
        expectedItems.add(firstItem);

        // get the ID
        int itemId = storageCrud.getNextId(Item.TABLE_NAME);
        firstItem.setItemId(itemId);

        // create the item
        assertTrue(storageCrud.createItem(firstItem));
        // commit to storage, since it did not commit these changes yet
        storage.commitTransaction();
    }
   
    /**
     * Creates a first item in the database.
     */
    public static void addItem(String categoryName, String sku, String name, double price,
            List<Item> expectedItems) {
        // create a new category for our item
        Category category = new Category(categoryName);

        assertEquals(storageCrud.createCategory(category), true);
        List<Category> categories = storageCrud.readAllCategories();
        int maxCategoryId = 0;
        int maxIdx = 0;

        // find the new category
        for (int i = 0; i < categories.size(); i++) {
            if (maxCategoryId < categories.get(i).getCategoryId()) {
                // found the new highest ID
                maxCategoryId = categories.get(i).getCategoryId();
                maxIdx = i;
            }
        }
        // set the ID of the newest category
        category.setCategoryId(categories.get(maxIdx).getCategoryId());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Item firstItem = new Item(sku, name, "this is the first item", category, price, 24,
                Date.valueOf(formattedDate), Date.valueOf(formattedDate), 10, 23, 0.0);
        expectedItems.add(firstItem);

        // get the ID
        int itemId = storageCrud.getNextId(Item.TABLE_NAME);
        firstItem.setItemId(itemId);

        // create the item
        assertTrue(storageCrud.createItem(firstItem));
        // commit to storage, since it did not commit these changes yet
        storage.commitTransaction();
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
        try {
            deleteAllItemsAndCategories();
            addFirstItem();
            addSecondItem();

            String result = Controller.readAllItemsSortByCost(true);
            expectedItems.sort((item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));
            assertEquals(gson.toJson(expectedItems), result);
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
        try {
            deleteAllItemsAndCategories();
            addFirstItem();
            addSecondItem();

            String result = Controller.readAllItemsSortByCost(false);
            System.out.println(result);
            expectedItems.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));
            assertEquals(gson.toJson(expectedItems), result);
        } finally {
            databaseMutex.unlock();
        }
    }

}