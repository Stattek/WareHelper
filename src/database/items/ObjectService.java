package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectService {

    /**
     * Creates a Bundle from dictionary data.
     * 
     * @param bundleData              The bundle dictionary data.
     * @param itemsData               The list of inner Item data.
     * @param itemInnerCategoriesData The list of inner Item Category objects.
     * @return The created Bundle object.
     */
    public static Bundle createBundle(Map<String, String> bundleData, List<Map<String, String>> itemsData,
            List<Map<String, String>> itemInnerCategoriesData) {
        Bundle output = new Bundle();

        try {
            int bundleId = Integer.parseInt(bundleData.get("BundleId"));
            double bundleDiscount = Double.parseDouble(bundleData.get("BundleDiscount"));
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemsData.size(); i++) {
                items.add(createItem(itemsData.get(i), itemInnerCategoriesData.get(i)));
            }

            // set values
            output.setBundleId(bundleId);
            output.setBundleDiscount(bundleDiscount);
            output.setItems(items);
        } catch (Exception e) {
            // There was an error with converting this data to a Category, throw an error
            throw new RuntimeException("Could not create Category from read data", e);
        }

        return output;
    }

    /**
     * Creates a Bundle "stub" from dictionary data and a list of Item IDs. Creates
     * a Bundle with no inner Item data, except for each Item's ID. For use when
     * creating Bundles when the data inside the Item table is unknown, such as when
     * creating a new Bundle to write to the database.
     * 
     * @param bundleData The bundle dictionary data.
     * @param itemIds    The list of Item IDs.
     * @return The created Bundle object.
     */
    public static Bundle createBundleStub(Map<String, String> bundleData, List<Integer> itemIds) {
        Bundle output = new Bundle();

        try {
            double bundleDiscount = Double.parseDouble(bundleData.get("BundleDiscount"));
            // TODO: make sure to do input validation and ensure that the user does not
            // input more than one of the same ID
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemIds.size(); i++) {
                Item curItem = new Item();
                curItem.setItemId(itemIds.get(i));
                items.add(curItem);
            }

            // set values
            output.setBundleDiscount(bundleDiscount);
            output.setItems(items);
        } catch (Exception e) {
            // There was an error with converting this data to a Category, throw an error
            throw new RuntimeException("Could not create Category from read data", e);
        }

        return output;
    }

    /**
     * Creates a Category from dictionary data.
     * 
     * @param categoryData The Category object data.
     * @return The created Category object.
     */
    public static Category createCategory(Map<String, String> categoryData) {
        Category output = new Category();

        try {
            int categoryId = Integer.parseInt(categoryData.get("CategoryId"));
            String name = categoryData.get("Name");

            // set values
            output.setCategoryId(categoryId);
            output.setName(name);
        } catch (Exception e) {
            // There was an error with converting this data to a Category, throw an error
            throw new RuntimeException("Could not create Category from read data", e);
        }

        return output;
    }

    /**
     * Creates an Item from dictionaries.
     * 
     * @param itemData          The Item data.
     * @param innerCategoryData The inner Category object data for the Item.
     * @return The created Item.
     * @throws RuntimeException
     */
    public static Item createItem(Map<String, String> itemData, Map<String, String> innerCategoryData)
            throws RuntimeException {
        Item output = new Item();

        try {
            int itemId = Integer.parseInt(itemData.get("ItemId"));
            String sku = itemData.get("Sku");
            String name = itemData.get("Name");
            String description = itemData.get("Description");
            Category category = createCategory(innerCategoryData);
            double price = Double.parseDouble(itemData.get("Price"));
            int numItems = Integer.parseInt(itemData.get("NumItems"));
            Date created = Date.valueOf(itemData.get("Created"));
            Date lastModified = Date.valueOf(itemData.get("LastModified"));
            int sellWithinNumDays = Integer.parseInt(itemData.get("SellWithinNumDays"));
            int lowInventoryThreshold = Integer.parseInt(itemData.get("LowInventoryThreshold"));
            double promotionPercentOff = Double.parseDouble(itemData.get("PromotionPercentOff"));

            // set values
            output.setItemId(itemId);
            output.setSku(sku);
            output.setName(name);
            output.setDescription(description);
            output.setCategory(category);
            output.setPrice(price);
            output.setNumItems(numItems);
            output.setCreated(created);
            output.setLastModified(lastModified);
            output.setSellWIthinDays(sellWithinNumDays);
            output.setLowInventoryThreshold(lowInventoryThreshold);
            output.setPromotionPercentOff(promotionPercentOff);
        } catch (Exception e) {
            // There was an error with converting this data to an Item, throw an error
            throw new RuntimeException("Could not create Item from read data", e);
        }

        return output;
    }

    /**
     * Creates an Item Stub for writing to the database.
     * 
     * @param itemData          The Item data.
     * @param innerCategoryData The inner Category data.
     * @return The created Item.
     * @throws RuntimeException
     */
    public static Item createItemStub(Map<String, String> itemData, Map<String, String> innerCategoryData)
            throws RuntimeException {
        Item output = new Item();

        try {
            String sku = itemData.get("Sku");
            String name = itemData.get("Name");
            String description = itemData.get("Description");
            Category category = createCategory(innerCategoryData);
            double price = Double.parseDouble(itemData.get("Price"));
            int numItems = Integer.parseInt(itemData.get("NumItems"));
            Date created = Date.valueOf(itemData.get("Created"));
            Date lastModified = Date.valueOf(itemData.get("LastModified"));
            int sellWithinNumDays = Integer.parseInt(itemData.get("SellWithinNumDays"));
            int lowInventoryThreshold = Integer.parseInt(itemData.get("LowInventoryThreshold"));
            double promotionPercentOff = Double.parseDouble(itemData.get("PromotionPercentOff"));

            // set values=
            output.setSku(sku);
            output.setName(name);
            output.setDescription(description);
            output.setCategory(category);
            output.setPrice(price);
            output.setNumItems(numItems);
            output.setCreated(created);
            output.setLastModified(lastModified);
            output.setSellWIthinDays(sellWithinNumDays);
            output.setLowInventoryThreshold(lowInventoryThreshold);
            output.setPromotionPercentOff(promotionPercentOff);
        } catch (Exception e) {
            // There was an error with converting this data to an Item, throw an error
            throw new RuntimeException("Could not create Item from read data", e);
        }

        return output;
    }

    /**
     * Gets the keys for an Item.
     * 
     * @return A List of keys.
     */
    public static List<String> getItemKeys() {
        return new Item().getAttributeKeys();
    }

    /**
     * Gets the keys for a Bundle.
     * 
     * @return A List of keys.
     */
    public static List<String> getBundleKeys() {
        return new Bundle().getAttributeKeys();
    }

    /**
     * Gets the keys for a Category.
     * 
     * @return A List of keys.
     */
    public static List<String> getCategoryKeys() {
        return new Category().getAttributeKeys();
    }

}
