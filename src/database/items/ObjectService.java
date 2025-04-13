package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectService {

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

    public static Item createItem(Map<String, String> itemData, Map<String, String> innerCategoryData)
            throws RuntimeException {
        Item output = new Item();

        try {
            int itemId = Integer.parseInt(itemData.get("ItemId"));
            String sku = itemData.get("Sku");
            String name = itemData.get("Name");
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

}
