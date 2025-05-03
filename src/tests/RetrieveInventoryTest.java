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
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.MySqlCrud;
import database.StorageCrud;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

@OrderWith(Alphanumeric.class)
public class RetrieveInventoryTest {
    private Controller controller;
    private StorageCrud storageCrud; // for direct calls
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Item> savedItems; // for adding back after tests are complete
    private List<Item> expectedItems;
    private List<Category> tempCategories;

    // we want to prevent multiple tests from accessing the database at the same
    // time
    private ReentrantLock databaseMutex = new ReentrantLock();

    @Before
    public void setup() {
        databaseMutex.lock();
        this.controller = new Controller();
        try {
            this.storageCrud = new MySqlCrud();
        } catch (SQLException sqle) {
            fail("Could not initialize storageCrud");
        }
        this.savedItems = new ArrayList<>();
        this.expectedItems = new ArrayList<>(); // no expected items yet
        this.tempCategories = new ArrayList<>();
        databaseMutex.unlock();
    }

    @Before
    public void deleteAllItems() {
        databaseMutex.lock();
        this.savedItems = storageCrud.readAllItems();
        for (Item item : savedItems) {
            // expect to delete every item
            assertEquals(true, storageCrud.deleteItem(item.getItemId()));
        }
        databaseMutex.unlock();
    }

    @Test
    public void test1ControllerReadAllItems() {
        databaseMutex.lock();
        String output = controller.readAllItems();
        databaseMutex.unlock();

        // compare gson output for test with that from the controller
        assertEquals(gson.toJson(expectedItems), output);
    }

    @Test
    public void test2MySqlCrud() {
        databaseMutex.lock();
        List<Item> items = storageCrud.readAllItems();
        databaseMutex.unlock();
        // we should have no items
        assertEquals(gson.toJson(expectedItems), gson.toJson(items));
    }

    public void addFirstItem() {
        databaseMutex.lock();

        // create a new category for our item
        Category category = new Category("TESTCATEGORY");
        tempCategories.add(category); // save this to delete later

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

        databaseMutex.unlock();
    }

    @Test
    public void test3ObjectServiceCreateItem() {
        databaseMutex.lock();

        Item theItem = this.expectedItems.get(0);
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
        databaseMutex.unlock();
    }

    @After
    public void cleanup() {
        databaseMutex.lock();
        // delete items
        for (Item item : this.expectedItems) {
            // expect to delete every item
            assertEquals(true, storageCrud.deleteItem(item.getItemId()));
        }
        // delete categories
        for (Category category : this.tempCategories) {
            assertEquals(true, storageCrud.deleteCategory(category.getCategoryId()));
        }
        databaseMutex.unlock();
    }
}
