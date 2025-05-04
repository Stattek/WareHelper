package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

import database.InnerObject;
import database.MySql;
import database.MySqlCrud;
import database.Storage;
import database.StorageCrud;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;

@OrderWith(Alphanumeric.class)
public class RetrieveInventoryTest {
    private static StorageCrud storageCrud; // for direct calls
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Item> expectedItems = new ArrayList<>();
    private static Storage storage;

    static {
        try {
            storageCrud = new MySqlCrud();
            storage = new MySql(MySqlCrud.url, MySqlCrud.username, MySqlCrud.password);
        } catch (SQLException sqle) {
            throw new RuntimeException("Could not initialize MySqlCrud or MySql");
        }
    }

    // we want to prevent multiple tests from accessing the database at the same
    // time
    private ReentrantLock databaseMutex = new ReentrantLock();

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

        // clear expected items
        expectedItems.clear();
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

        // create the item
        assertTrue(storageCrud.createItem(firstItem));
    }

    /**
     * Tests reading all Items from Controller with no Items in the list.
     */
    @Test
    public void test1ControllerReadAllItemsEmpty() {
        databaseMutex.lock();

        try {
            deleteAllItemsAndCategories();

            String output = Controller.readAllItems();

            // compare gson output for test with that from the controller
            assertEquals(gson.toJson(expectedItems), output);
        } catch (Exception e) {
            fail("Error reading empty list of items with Controller");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Test reading all Items from MySqlCrud with no Items in the database.
     */
    @Test
    public void test2MySqlCrudReadAllItemsEmpty() {
        databaseMutex.lock();
        try {
            deleteAllItemsAndCategories();

            List<Item> items = storageCrud.readAllItems();

            // we should have no items
            assertTrue(items.isEmpty());
            assertTrue(expectedItems.isEmpty());
        } catch (Exception e) {
            fail("Error reading empty list of items with MySqlCrud");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests ObjectService to create an Item.
     */
    @Test
    public void test3ObjectServiceCreateItem() {
        databaseMutex.lock();
        try {
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

            assertEquals(theCategory, ObjectService.createCategory(categoryData));
            assertEquals(theItem, ObjectService.createItem(itemData, categoryData));

            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error creating an item with ObjectService");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests ObjectService to create an Item that fails and throws an exception.
     */
    @Test
    public void test4ObjectServiceCreateItemFail() {
        databaseMutex.lock();
        try {
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

            // let's set the Item ID to a value that will cause ObjectService to fail
            itemData.put(Item.ITEM_ID_KEY, "fail");

            Category theCategory = theItem.getCategory();
            List<String> categoryKeys = theCategory.getAttributeKeys();
            List<String> categoryValues = theCategory.getAllAttributes();
            Map<String, String> categoryData = new HashMap<>();
            assertEquals(categoryKeys.size(), categoryValues.size());

            // add the category data
            for (int i = 0; i < categoryKeys.size(); i++) {
                categoryData.put(categoryKeys.get(i), categoryValues.get(i));
            }

            // this one should be successful
            assertEquals(theCategory, ObjectService.createCategory(categoryData));

            // this one should fail
            ObjectService.createItem(itemData, categoryData);

            fail("ObjectService should fail when given invalid data");
        } catch (Exception e) {
            // we should have failed
        }

        try {
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error deleting all items and categories");
        } finally {
            // don't forget to unlock mutex
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySqlCrud.
     */
    @Test
    public void test5MySqlCrudReadAllItemsSingle() {
        databaseMutex.lock();
        try {
            addFirstItem();

            List<Item> items = storageCrud.readAllItems();

            // check that they're all equal
            assertEquals(items.size(), expectedItems.size());
            for (int i = 0; i < items.size(); i++) {
                assertEquals(expectedItems.get(i), items.get(i));
            }

            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error reading single item with MySqlCrud");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySql.
     */
    @Test
    public void test6MySqlReadSingle() {
        databaseMutex.lock();
        try {
            addFirstItem();

            // create expected Map data
            List<String> keys = ObjectService.getItemKeys();

            List<Map<String, String>> realData = storage.readAll(Item.TABLE_NAME, keys, null);
            List<Map<String, String>> expectedData = getExpectedItemMap();
            // we should have the same values
            assertEquals(expectedData, realData);
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error reading a single item with MySql");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySql with a bad key.
     */
    @Test
    public void test7MySqlReadSingleWithBadKey() {
        databaseMutex.lock();
        try {
            addFirstItem();

            // create expected Map data
            List<String> keys = ObjectService.getItemKeys();

            // add a bad key to cause the method to fail
            keys.add("fail");

            List<Map<String, String>> realData = storage.readAll(Item.TABLE_NAME, keys, null);
            List<Map<String, String>> expectedData = getExpectedItemMap();
            // we should have the same values
            assertEquals(expectedData, realData);
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error reading a single item with MySql");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySqlCrud with an empty list
     * of inner objects.
     */
    @Test
    public void test8MySqlReadSingleEmptyInnerObjects() {
        databaseMutex.lock();
        try {
            addFirstItem();

            // create expected Map data
            List<String> keys = ObjectService.getItemKeys();

            // use a new arraylist, shouldn't affect the read data
            List<Map<String, String>> realData = storage.readAll(Item.TABLE_NAME, keys, new ArrayList<>());
            List<Map<String, String>> expectedData = getExpectedItemMap();
            // we should have the same values
            assertEquals(expectedData, realData);
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error reading a single item with MySql");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySqlCrud with a single
     * inner object (performs a join).
     */
    @Test
    public void test9MySqlReadSingleInnerObject() {
        databaseMutex.lock();
        try {
            addFirstItem();

            // create expected Map data
            List<String> keys = ObjectService.getItemKeys();

            // use a new arraylist, shouldn't affect the read data
            List<Map<String, String>> realData = storage.readAll(Item.TABLE_NAME, keys,
                    List.of(new InnerObject(Item.TABLE_NAME, Category.TABLE_NAME, Category.CATEGORY_ID_KEY)));
            List<Map<String, String>> expectedData = getExpectedItemMap();

            // we should have the same values since the join shouldn't affect the keys we
            // pulled
            assertEquals(expectedData, realData);
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error reading a single item with MySql");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests reading a single item in the database from MySqlCrud with a single
     * inner object (performs a join).
     */
    @Test
    public void test10ObjectServiceGetItemKeys() {
        databaseMutex.lock();
        try {
            addFirstItem();

            // since we have one item
            Item item = expectedItems.get(0);

            // get expected keys
            List<String> expectedKeys = item.getAttributeKeys();
            List<String> actualKeys = ObjectService.getItemKeys();

            // these should always be the same
            assertEquals(expectedKeys, actualKeys);
            deleteAllItemsAndCategories();
        } catch (Exception e) {
            fail("Error testing object service to get item keys");
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Performs a final cleanup.
     */
    @After
    public void cleanup() {
        databaseMutex.lock();
        deleteAllItemsAndCategories();
        databaseMutex.unlock();
    }

    /**
     * Get the expected Item data as a Map.
     * 
     * @return The expected data map.
     */
    private static List<Map<String, String>> getExpectedItemMap() {
        List<Map<String, String>> expectedData = new ArrayList<>();
        for (int i = 0; i < expectedItems.size(); i++) {
            Item theItem = expectedItems.get(i);
            Map<String, String> itemData = new HashMap<>();
            List<String> theItemKeys = theItem.getAttributeKeys();
            List<String> theItemValues = theItem.getAllAttributes();

            if (theItemKeys.size() != theItemValues.size()) {
                fail("Item keys and values are not the same size");
            }

            for (int j = 0; j < theItemKeys.size(); j++) {
                itemData.put(theItemKeys.get(j), theItemValues.get(j));
            }

            // add this value
            expectedData.add(itemData);
        }

        return expectedData;
    }
}
