package database.items;

import java.sql.Date;
import java.util.Map;

public class ObjectService {

    public static Bundle createBundle(Map<String, String> bundleData) {
        return null; // TODO: implement
    }

    public static Category createCategory(Map<String, String> categoryData) {
        return null; // TODO: implement
    }

    public static Item createItem(Map<String, String> itemData) throws RuntimeException {
        Item output = new Item();

        try {
            int itemId = Integer.parseInt(itemData.get("ItemId"));
            String sku = itemData.get("Sku");
            String name = itemData.get("Name");
            Category category = new Category();
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
