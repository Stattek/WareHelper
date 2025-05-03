package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import user.Controller;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.MySqlCrud;
import database.StorageCrud;
import database.items.Bundle;
import database.items.Category;
import database.items.Item;

public class RetrieveInventoryTest {
    private Controller controller;
    private StorageCrud storageCrud; // for direct calls
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Item> expectedItems;

    private int tempItemId = 0;
    private int tempBundleId = 0;

    @Before
    public void setup() {
        this.controller = new Controller();
        try {
            this.storageCrud = new MySqlCrud();
        } catch (SQLException sqle) {
            fail("Could not initialize storageCrud");
        }
        expectedItems = new ArrayList<>(); // no expected items yet
    }

    @Before
    public void deleteAllItems() {
        List<Item> readItems = storageCrud.readAllItems();
        for (Item item : readItems) {
            // expect to delete every item
            assertEquals(true, storageCrud.deleteItem(item.getItemId()));
        }
    }

    // @Before
    // public void controllerDeleteBundleCreateBundle() {

    // // create a new category for our item
    // Category category = new Category("TestCategory");
    // assertEquals(storageCrud.createCategory(category), true);
    // List<Category> categories = storageCrud.readAllCategories();
    // int maxCategoryId = 0;
    // int curIdx = 0;

    // // find the new category
    // for (Category curCategory : categories) {
    // if (maxCategoryId < curCategory.getCategoryId()) {
    // maxCategoryId = curCategory.getCategoryId();
    // }
    // }

    // LocalDate currentDate = LocalDate.now();
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // String formattedDate = currentDate.format(formatter);

    // Item item = new Item("testsku", "testItem", "", categories.get(curIdx), 0.0,
    // 2, Date.valueOf(formattedDate),
    // Date.valueOf(formattedDate), 2, 3, 0.0);
    // List<Item> items = storageCrud.readAllItems();
    // if (items) {
    // }
    // storageCrud.createBundle(null);
    // }

    @Test
    public void testControllerReadAllItems() {
        String output = controller.readAllItems();
        // compare gson output for test with that from the controller
        assertEquals(gson.toJson(expectedItems), output);
    }

    @Test
    public void testMySqlCrud() {
        List<Item> items = storageCrud.readAllItems();
        // we should have no items
        assertEquals(expectedItems, items);
    }
}
