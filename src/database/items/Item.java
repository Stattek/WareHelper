package database.items;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Class that represents an Item in the inventory.
 */
public class Item extends ConvertableObject {
    private int itemId;
    private String sku;
    private String name;
    private Category category;
    private EconomyInfo economyInfo;
    private DateInfo dateInfo;
    private Preference preference;

    /**
     * Creates a default Item object.
     * 
     * @return The new Item.
     */
    public Item() {

    }

    /**
     * Constructor to create an Item.
     * 
     * @param sku         The SKU of the item.
     * @param name        The name of the Item.
     * @param category    The category this item belongs to.
     * @param economyInfo The economy info for this item.
     * @param dateInfo    The date info for this item.
     * 
     * @return The new Item.
     */
    public Item(int itemId, String sku, String name, Category category, double price, int numItems, Date created,
            Date lastModified, int sellWithinNumDays, int lowInventoryThreshold, double promotionPercentOff) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.economyInfo = new EconomyInfo(price, numItems);
        this.dateInfo = new DateInfo(created, lastModified);
        this.preference = new Preference(sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("ItemId");
        keys.add("Sku");
        keys.add("Name");
        keys.add("CategoryId");
        // Price and Number of Items
        keys.addAll(economyInfo.getAttributeKeys());
        // Created and Last Modified
        keys.addAll(dateInfo.getAttributeKeys());
        // Sell Within, low inventory, precentage off
        keys.addAll(preference.getAttributeKeys());
        return keys;
    }

    @Override
    public List<String> getSubObjects() {
        ArrayList<String> output = new ArrayList<>();
        output.add("Category");
        output.add("EconomyInfo");
        output.add("DateInfo");
        output.add("Preference");
        return output;
    }

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(itemId));
        data.add(sku);
        data.add(name);
        data.add(String.valueOf(category.getCategoryId()));
        data.addAll(economyInfo.getAllAttributes());
        data.addAll(dateInfo.getAllAttributes());
        data.addAll(preference.getAllAttributes());
        return data;
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // ItemId
        dataTypes.add(DataType.STRING); // Sku
        dataTypes.add(DataType.STRING); // Name
        dataTypes.add(DataType.INTEGER); // CategoryId
        // Price and Number of Items
        dataTypes.addAll(economyInfo.getAttributeDataTypes());
        // Created and Last Modified
        dataTypes.addAll(dateInfo.getAttributeDataTypes());
        // Sell Within, low inventory, percentage off
        dataTypes.addAll(preference.getAttributeDataTypes());
        return dataTypes;
    }

    /* Getters and Setters */

    /**
     * Adds promotion to item.
     * 
     * @param percentOffPromotion The percent off of the item.
     */
    public void setPromotionPercentOff(double promotionPercentOff) {
        this.preference.setPromotionPercentOff(promotionPercentOff);
    }

    /**
     * Sets the low inventory threshold.
     * 
     * @param lowInventoryThreshold The number of items at which the inventory is
     *                              considered low.
     */
    public void setLowInventoryThreshold(int lowInventoryThreshold) {
        this.preference.setLowInventoryThreshold(lowInventoryThreshold);
    }

    /**
     * Sets the days an item should be sold within.
     * 
     * @param sellWithinDays The number of days to sell the item within.
     */
    public void setSellWIthinDays(int sellWithinDays) {
        this.preference.setSellWithinNumDays(sellWithinDays);
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
