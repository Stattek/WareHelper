package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import user.Controller;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.*;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.MySqlCrud;
import database.StorageCrud;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

@OrderWith(Alphanumeric.class)
public class RetrieveInventoryTest {
    private static StorageCrud storageCrud; // for direct calls
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Item> expectedItems = new ArrayList<>();

    static {
        try {
            storageCrud = new MySqlCrud();
        } catch (SQLException sqle) {
            throw new RuntimeException("Could not initialize MySqlCrud");
        }
    }

    // we want to prevent multiple tests from accessing the database at the same
    // time
    private ReentrantLock databaseMutex = new ReentrantLock();

    /**
     * Deletes all Items and Categories in the database.
     */
    public void deleteAllItemsAndCategories() {
        // delete items
        List<Item> items = storageCrud.readAllItems();
        for (Item item : items) {
            // expect to delete every item
            assertEquals(true, storageCrud.deleteItem(item.getItemId()));
        }

        // delete categories too
        List<Category> categories = storageCrud.readAllCategories();
        for (Category category : categories) {
            // expect to delete every item
            assertEquals(true, storageCrud.deleteCategory(category.getCategoryId()));
        }

        // clear expected items
        expectedItems.clear();
    }

    /**
     * Tests reading all Items from Controller with no Items in the list.
     */
    @Test
    public void test1ControllerReadAllItemsEmpty() {
        databaseMutex.lock();
        deleteAllItemsAndCategories();

        String output = Controller.readAllItems();

        // compare gson output for test with that from the controller
        assertEquals(gson.toJson(expectedItems), output);

        databaseMutex.unlock();
    }

    /**
     * Test reading from MySqlCrud with no Items.
     */
    @Test
    public void test2MySqlCrud() {
        databaseMutex.lock();
        deleteAllItemsAndCategories();

        List<Item> items = storageCrud.readAllItems();

        // we should have no items
        assertEquals(gson.toJson(expectedItems), gson.toJson(items));

        databaseMutex.unlock();
    }

    /**
     * Creates a first item in the database.
     */
    public void addFirstItem() {
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

        // create the item
        assertEquals(true, storageCrud.createItem(firstItem));
    }

    /**
     * Creates a second item in the database.
     */
    public void addSecondItem() {
        // create a new category for our item
        Category category = new Category("TESTCATEGORY2");

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

        Item secondItem = new Item("testsecondsku", "secondItem", "this is the second item", category, 10.12, 24,
                Date.valueOf(formattedDate), Date.valueOf(formattedDate), 10, 23, 0.0);
        expectedItems.add(secondItem);

        // create the item
        assertEquals(true, storageCrud.createItem(secondItem));
    }

    /**
     * Tests ObjectService to create an Item.
     */
    @Test
    public void test3ObjectServiceCreateItem() {
        databaseMutex.lock();
        addFirstItem();

        Item theItem = expectedItems.get(0);
        List<String> itemKeys = theItem.getAttributeKeys();
        List<String> itemValues = theItem.getAllAttributes();
        Map<String, String> itemData = new HashMap<>();
        assertEquals(itemKeys.size(), itemValues.size());

        // add the item data
        for (int i = 0; i < itemKeys.size(); i++) {
            itemData.put(itemKeys.get(i), itemValues.get(i));
        }

        Category theCategory = theItem.getCategory();
        List<String> categoryKeys = theCategory.getAttributeKeys();
        List<String> categoryValues = theCategory.getAllAttributes();
        Map<String, String> categoryData = new HashMap<>();
        assertEquals(categoryKeys.size(), categoryValues.size());

        // add the category data
        for (int i = 0; i < categoryKeys.size(); i++) {
            categoryData.put(categoryKeys.get(i), categoryValues.get(i));
        }

        assertEquals(gson.toJson(theCategory), gson.toJson(ObjectService.createCategory(categoryData)));
        assertEquals(gson.toJson(theItem), gson.toJson(ObjectService.createItem(itemData, categoryData)));

        deleteAllItemsAndCategories();
        databaseMutex.unlock();
    }

    /**
     * Tests reading a single item in the database from MySqlCrud.
     */
    @Test
    public void test4MySqlCrud() {
        databaseMutex.lock();
        addFirstItem();

        List<Item> items = storageCrud.readAllItems();

        // we should have no items
        assertEquals(gson.toJson(expectedItems), gson.toJson(items));

        deleteAllItemsAndCategories();
        databaseMutex.unlock();
    }

    @After
    public void cleanup() {
        databaseMutex.lock();
        deleteAllItemsAndCategories();
        databaseMutex.unlock();
    }
}
