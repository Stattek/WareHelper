package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import user.Controller;

import java.sql.SQLException;
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
import database.items.*;

@OrderWith(Alphanumeric.class)
public class CreateCategoryTest {
    private static Controller controller = new Controller();
    private static StorageCrud storageCrud;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Category> expectedCategories = new ArrayList<>();

    static {
        try {
            storageCrud = new MySqlCrud();
        } catch (SQLException sqle) {
            throw new RuntimeException("Could not initialize MySqlCrud", sqle);
        }
    }

    // Mutex to prevent concurrent database access during tests
    private final ReentrantLock databaseMutex = new ReentrantLock();

    /**
     * Deletes all Categories and Items in the database.
     */
    private void deleteAllCategoriesAndItems() {
        // Delete items
        List<Item> items = storageCrud.readAllItems();
        for (Item item : items) {
            assertTrue("Failed to delete item: " + item.getItemId(), storageCrud.deleteItem(item.getItemId()));
        }

        // Delete categories
        List<Category> categories = storageCrud.readAllCategories();
        for (Category category : categories) {
            assertTrue("Failed to delete category: " + category.getCategoryId(),
                    storageCrud.deleteCategory(category.getCategoryId()));
        }

        // Clear expected categories
        expectedCategories.clear();
    }

    /**
     * Deletes all Bundles in the database.
     */
    private void deleteAllBundles() {
        List<Bundle> bundles = storageCrud.readAllBundles();
        for (Bundle bundle : bundles) {
            assertTrue("Failed to delete bundle: " + bundle.getBundleId(),
                    storageCrud.deleteBundle(bundle.getBundleId()));
        }
    }

    /**
     * Tests reading all Categories from Controller with no Categories in the list.
     */
    @Test
    public void test1_ControllerReadAllCategories() {
        databaseMutex.lock();
        try {
            deleteAllBundles();
            deleteAllCategoriesAndItems();

            String output = controller.readAllCategories();

            // Compare Gson output for test with that from the controller
            assertEquals(gson.toJson(expectedCategories), output);
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Test reading from MySqlCrud with no Categories.
     */
    @Test
    public void test2_MySqlCrudReadAllCategories() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            List<Category> categories = storageCrud.readAllCategories();

            // We should have no categories
            assertEquals(gson.toJson(expectedCategories), gson.toJson(categories));
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests creating a single category using the Controller.
     */
    @Test
    public void test3_ControllerCreateCategory() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            String categoryName = "TESTCATEGORY";
            Category newCategory = new Category(categoryName);
            expectedCategories.add(newCategory);

            // Create category through controller with a Map
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put(Category.NAME_KEY, categoryName);
            boolean result = controller.createCategory(categoryData);
            assertTrue("Category creation should return success", result);

            // Verify the category was created
            String categoriesJson = controller.readAllCategories();
            List<Category> retrievedCategories = storageCrud.readAllCategories();

            // The retrieved category should have an ID assigned
            assertFalse("Category ID should be assigned", retrievedCategories.isEmpty());
            assertTrue("Category ID should be greater than 0", retrievedCategories.get(0).getCategoryId() > 0);

            // Update our expected category with the assigned ID
            expectedCategories.get(0).setCategoryId(retrievedCategories.get(0).getCategoryId());

            // Compare with expected
            assertEquals(gson.toJson(expectedCategories), categoriesJson);
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests creating a duplicate category (should fail).
     */
    @Test
    public void test4_ControllerCreateDuplicateCategory() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            String categoryName = "TestCategory";
            Category newCategory = new Category(categoryName);

            // Create first category
            assertTrue("Failed to create category", storageCrud.createCategory(newCategory));
            expectedCategories.add(newCategory);

            // Update with assigned ID
            List<Category> retrievedCategories = storageCrud.readAllCategories();
            assertFalse("Retrieved categories should not be empty", retrievedCategories.isEmpty());
            expectedCategories.get(0).setCategoryId(retrievedCategories.get(0).getCategoryId());

            // Try creating duplicate category with a Map
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put(Category.NAME_KEY, categoryName);
            boolean result = controller.createCategory(categoryData);
            assertFalse("Duplicate category creation should return error", result);

            // Verify no additional category was created
            assertEquals(1, storageCrud.readAllCategories().size());
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests creating a category using the object service.
     */
    @Test
    public void test5_CreateCategoryFromObjectService() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            // Prepare category data as a Map (how ObjectService expects it)
            String categoryName = "OBJECTSERVICECATEGORY";
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put(Category.NAME_KEY, categoryName);
            categoryData.put(Category.CATEGORY_ID_KEY, Integer.toString(storageCrud.getNextId(Category.TABLE_NAME)));

            // Create the category using ObjectService
            Category category = ObjectService.createCategory(categoryData);

            // Save the created category
            boolean result = storageCrud.createCategory(category);
            assertTrue("Category creation using object service should return success", result);

            // Retrieve the created category
            List<Category> categories = storageCrud.readAllCategories();
            assertEquals(1, categories.size());
            Category retrievedCategory = categories.get(0);

            // Verify the category details
            assertEquals(categoryName.toUpperCase(), retrievedCategory.getName());
            assertTrue("Category ID should be greater than 0", retrievedCategory.getCategoryId() > 0);

            // Update expected categories
            expectedCategories.add(category);

            // Compare with expected
            assertEquals(gson.toJson(expectedCategories), gson.toJson(categories));
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests deleting a category.
     */
    @Test
    public void test6_DeleteCategory() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            // Create a category
            Category category = new Category("CategoryToDelete");
            assertTrue("Failed to create category", storageCrud.createCategory(category));

            // Get the created category ID
            List<Category> categories = storageCrud.readAllCategories();
            assertEquals(1, categories.size());
            int categoryId = categories.get(0).getCategoryId();

            // Delete the category using controller
            boolean result = controller.deleteCategory(categoryId);
            assertTrue("Category deletion should return success", result);

            // Verify deletion
            categories = storageCrud.readAllCategories();
            assertEquals(0, categories.size());
        } finally {
            databaseMutex.unlock();
        }
    }

    @After
    public void cleanup() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();
        } finally {
            databaseMutex.unlock();
        }
    }
}