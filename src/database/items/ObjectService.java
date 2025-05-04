package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import database.InnerObject;

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
        Bundle output = null;

        try {
            int bundleId = Integer.parseInt(bundleData.get(Bundle.BUNDLE_ID_KEY));
            double bundleDiscount = Double.parseDouble(bundleData.get(Bundle.BUNDLE_DISCOUNT_KEY));
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemsData.size(); i++) {
                items.add(createItem(itemsData.get(i), itemInnerCategoriesData.get(i)));
            }

            // set values
            output = new Bundle(bundleId, bundleDiscount, items);
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
        Bundle output = null;

        try {
            // TODO: make sure to do input validation and ensure that the user does not
            // input more than one of the same ID
            double bundleDiscount = Double.parseDouble(bundleData.get(Bundle.BUNDLE_DISCOUNT_KEY));
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemIds.size(); i++) {
                Item curItem = new Item();
                curItem.setItemId(itemIds.get(i));
                items.add(curItem);
            }

            // set values
            output = new Bundle(bundleDiscount, items);
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
        Category output = null;

        try {
            int categoryId = Integer.parseInt(categoryData.get(Category.CATEGORY_ID_KEY));
            String name = categoryData.get(Category.NAME_KEY).toUpperCase(); // always upper case

            // set values
            output = new Category(categoryId, name);
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
        Item output = null;

        try {
            int itemId = Integer.parseInt(itemData.get(Item.ITEM_ID_KEY));
            String sku = itemData.get(Item.SKU_KEY);
            String name = itemData.get(Item.NAME_KEY);
            String description = itemData.get(Item.DESCRIPTION_KEY);
            Category category = createCategory(innerCategoryData);
            double price = Double.parseDouble(itemData.get(EconomyInfo.PRICE_KEY));
            int numItems = Integer.parseInt(itemData.get(EconomyInfo.NUM_ITEMS_KEY));
            Date created = Date.valueOf(itemData.get(DateInfo.CREATED_KEY));
            Date lastModified = Date.valueOf(itemData.get(DateInfo.LAST_MODIFIED_KEY));
            int sellWithinNumDays = Integer.parseInt(itemData.get(Preference.SELL_WITHIN_NUM_DAYS_KEY));
            int lowInventoryThreshold = Integer.parseInt(itemData.get(Preference.LOW_INVENTORY_THRESHOLD_KEY));
            double promotionPercentOff = Double.parseDouble(itemData.get(Preference.PROMOTION_PERCENT_OFF_KEY));

            // set values
            output = new Item(itemId, sku, name, description,
                    category, price, numItems, created, lastModified,
                    sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
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
            String sku = itemData.get(Item.SKU_KEY);
            String name = itemData.get(Item.NAME_KEY);
            String description = itemData.get(Item.DESCRIPTION_KEY);
            Category category = createCategory(innerCategoryData);
            double price = Double.parseDouble(itemData.get(EconomyInfo.PRICE_KEY));
            int numItems = Integer.parseInt(itemData.get(EconomyInfo.NUM_ITEMS_KEY));
            Date created = Date.valueOf(itemData.get(DateInfo.CREATED_KEY));
            Date lastModified = Date.valueOf(itemData.get(DateInfo.LAST_MODIFIED_KEY));
            int sellWithinNumDays = Integer.parseInt(itemData.get(Preference.SELL_WITHIN_NUM_DAYS_KEY));
            int lowInventoryThreshold = Integer.parseInt(itemData.get(Preference.LOW_INVENTORY_THRESHOLD_KEY));
            double promotionPercentOff = Double.parseDouble(itemData.get(Preference.PROMOTION_PERCENT_OFF_KEY));

            // set values
            output = new Item(sku, name, description, category, price, numItems, created, lastModified,
                    sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
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
     * Gets the keys for an Item with no ID.
     * 
     * @return A List of keys.
     */
    public static List<String> getItemKeysNoId() {
        return new Item().getAttributeKeysNoId();
    }

    /**
     * Gets the required keys for an Item.
     * 
     * @return A List of keys.
     */
    public static List<String> getItemKeysRequired() {
        return new Item().getAttributeKeysRequired();
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
     * Gets the keys for an Bundle with no ID.
     * 
     * @return A List of keys.
     */
    public static List<String> getBundleKeysNoId() {
        return new Bundle().getAttributeKeysNoId();
    }

    /**
     * Gets the required keys for an Bundle.
     * 
     * @return A List of keys.
     */
    public static List<String> getBundleKeysRequired() {
        return new Bundle().getAttributeKeysRequired();
    }

    /**
     * Gets the inner objects for a Bundle for reading.
     * 
     * @return The Bundle's inner objects.
     */
    public static List<InnerObject> getBundleInnerObjects() {
        return new Bundle().getInnerObjects();
    }

    /**
     * Gets the keys for a Category.
     * 
     * @return A List of keys.
     */
    public static List<String> getCategoryKeys() {
        return new Category().getAttributeKeys();
    }

    /**
     * Gets the keys for an Category with no ID.
     * 
     * @return A List of keys.
     */
    public static List<String> getCategoryKeysNoId() {
        return new Category().getAttributeKeysNoId();
    }

    /**
     * Gets the required keys for an Category.
     * 
     * @return A List of keys.
     */
    public static List<String> getCategoryKeysRequired() {
        return new Category().getAttributeKeysRequired();
    }

    /**
     * Gets the data types for an Item.
     * 
     * @return A List of item data types.
     */
    public static List<DataType> getItemDataTypes() {
        return new Item().getAttributeDataTypes();
    }

    /**
     * Gets the data types for a Bundle.
     * 
     * @return A List of bundle data types.
     */
    public static List<DataType> getBundleDataTypes() {
        return new Bundle().getAttributeDataTypes();
    }

    /**
     * Gets the data types for a Category.
     * 
     * @return A List of category data types
     */
    public static List<DataType> getCategoryDataTypes() {
        return new Category().getAttributeDataTypes();
    }

    /**
     * Gets just the ID key for an Item.
     * 
     * @return The item ID key.
     */
    public static String getItemIdKey() {
        return Item.ITEM_ID_KEY;
    }

    /**
     * Gets just the ID key for a Bundle.
     * 
     * @return The bundle ID key.
     */
    public static String getBundleIdKey() {
        return Bundle.BUNDLE_ID_KEY;
    }

    /**
     * Gets just the ID key for a Category.
     * 
     * @return The category ID key.
     */
    public static String getCategoryIdKey() {
        return Category.CATEGORY_ID_KEY;
    }

}
