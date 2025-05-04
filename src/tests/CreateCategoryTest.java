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

            String output = Controller.readAllCategories();

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

            String categoryName = "TestCategory";
            Category newCategory = new Category(categoryName);
            expectedCategories.add(newCategory);

            // Create category through controller with a Map
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put(Category.NAME_KEY, categoryName);
            boolean result = Controller.createCategory(categoryData);
            assertTrue("Category creation should return success", result);

            // Verify the category was created
            String categoriesJson = Controller.readAllCategories();
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
            boolean result = Controller.createCategory(categoryData);
            assertFalse("Duplicate category creation should return false", result);

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
            String categoryName = "ObjectServiceCategory";
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
            assertEquals(categoryName, retrievedCategory.getName());
            assertTrue("Category ID should be greater than 0", retrievedCategory.getCategoryId() > 0);

            // Update expected categories
            expectedCategories.add(category);

            // Compare with expected
            assertEquals("Expected categories size does not match retrieved categories size", 
                         expectedCategories.size(), categories.size());
            for (int i = 0; i < expectedCategories.size(); i++) {
                assertEquals("Category at index " + i + " does not match", 
                             expectedCategories.get(i), categories.get(i));
            }
        } finally {
            databaseMutex.unlock();
        }
    }


    /**
     * Tests creating multiple items using the Controller.
     */
    @Test
    public void test6_ControllerCreateMultipleItems() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            int numberOfCategories = 5; // Number of categories to create
            String categoriesJson = null; // Declare categoriesJson outside the loop
            for (int i = 1; i <= numberOfCategories; i++) {
                String categoryName = "TestCategory" + i;
                Category newCategory = new Category(categoryName);
                expectedCategories.add(newCategory);

                // Create category through controller with a Map
                Map<String, String> categoryData = new HashMap<>();
                categoryData.put(Category.NAME_KEY, categoryName);
                boolean result = Controller.createCategory(categoryData);
                assertTrue("Category creation should return success for category " + categoryName, result);

                // Verify the category was created
                categoriesJson = Controller.readAllCategories();
                List<Category> retrievedCategories = storageCrud.readAllCategories();

                // The retrieved category should have an ID assigned
                assertFalse("Category ID should be assigned for category " + categoryName, retrievedCategories.isEmpty());
                assertTrue("Category ID should be greater than 0 for category " + categoryName, 
                           retrievedCategories.get(i - 1).getCategoryId() > 0);

                // Update our expected category with the assigned ID
                expectedCategories.get(i - 1).setCategoryId(retrievedCategories.get(i - 1).getCategoryId());
            }

            // Compare with expected
            assertEquals(gson.toJson(expectedCategories), categoriesJson);
        } finally {
            databaseMutex.unlock();
        }
    }
     /**
     * Tests creating multiple categories using StorageCrud.
     */
    @Test
    public void test7_StorageCrudCreateMultipleCategories() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            int numberOfCategories = 5; // Number of categories to create
            for (int i = 1; i <= numberOfCategories; i++) {
                String categoryName = "StorageCrudCategory" + i;
                Category newCategory = new Category(categoryName);
                boolean result = storageCrud.createCategory(newCategory);
                assertTrue("Category creation should return success for category " + categoryName, result);

                // Retrieve the created category
                List<Category> retrievedCategories = storageCrud.readAllCategories();
                assertEquals(i, retrievedCategories.size());

                // Verify the last created category
                Category createdCategory = retrievedCategories.get(i - 1);
                assertEquals(categoryName, createdCategory.getName());
                assertTrue("Category ID should be greater than 0", createdCategory.getCategoryId() > 0);

                // Add to expected categories
                newCategory.setCategoryId(createdCategory.getCategoryId());
                expectedCategories.add(newCategory);
            }

            // Compare with expected
            List<Category> allCategories = storageCrud.readAllCategories();
            assertEquals("Expected categories size does not match retrieved categories size", 
                         expectedCategories.size(), allCategories.size());
            for (int i = 0; i < expectedCategories.size(); i++) {
                assertEquals("Category at index " + i + " does not match", 
                             expectedCategories.get(i), allCategories.get(i));
            }
        } finally {
            databaseMutex.unlock();
        }
    }
    /**
     * Tests creating a single category using StorageCrud.
     */
    @Test
    public void test8_StorageCrudCreateCategory() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            String categoryName = "StorageCrudTestCategory";
            Category newCategory = new Category(categoryName);
            boolean result = storageCrud.createCategory(newCategory);
            assertTrue("Category creation should return success", result);

            // Retrieve the created category
            List<Category> retrievedCategories = storageCrud.readAllCategories();
            assertEquals(1, retrievedCategories.size());
            Category createdCategory = retrievedCategories.get(0);

            // Verify the category details
            assertEquals(categoryName, createdCategory.getName());
            assertTrue("Category ID should be greater than 0", createdCategory.getCategoryId() > 0);

            // Add to expected categories
            newCategory.setCategoryId(createdCategory.getCategoryId());
            expectedCategories.add(newCategory);

            // Compare with expected
            for (int i = 0; i < expectedCategories.size(); i++) {
                assertEquals("Category at index " + i + " does not match", 
                             expectedCategories.get(i), retrievedCategories.get(i));
            }
        } finally {
            databaseMutex.unlock();
        }
    }

    /**
     * Tests creating a duplicate category using StorageCrud (should fail).
     */
    @Test
    public void test9_StorageCrudCreateDuplicateCategory() {
        databaseMutex.lock();
        try {
            deleteAllCategoriesAndItems();

            String categoryName = "StorageCrudDuplicateCategory";
            Category newCategory = new Category(categoryName);

            // Create the first category
            boolean result = storageCrud.createCategory(newCategory);
            assertTrue("Category creation should return success", result);

            // Update with assigned ID
            List<Category> retrievedCategories = storageCrud.readAllCategories();
            assertFalse("Retrieved categories should not be empty", retrievedCategories.isEmpty());
            newCategory.setCategoryId(retrievedCategories.get(0).getCategoryId());
            expectedCategories.add(newCategory);

            // Attempt to create a duplicate category
            Category duplicateCategory = new Category(categoryName);
            boolean duplicateResult = storageCrud.createCategory(duplicateCategory);
            assertFalse("Duplicate category creation should return false", duplicateResult);

            // Verify no additional category was created
            assertEquals(1, storageCrud.readAllCategories().size());
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