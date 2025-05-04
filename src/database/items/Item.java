package database.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Date;

/**
 * Class that represents an Item in the inventory.
 */
public class Item implements ConvertableObject {
    private int itemId;
    private String sku;
    private String name;
    private String description;
    private Category category;
    private EconomyInfo economyInfo;
    private DateInfo dateInfo;
    private Preference preference;

    public final static String TABLE_NAME = "Item";

    public final static String ITEM_ID_KEY = "ItemId";
    public final static String SKU_KEY = "Sku";
    public final static String NAME_KEY = "ItemName"; // different to avoid name conflicts
    public final static String DESCRIPTION_KEY = "Description";
    public final static String CATEGORY_ID_KEY = "CategoryId";

    /**
     * Creates a default Item object.
     * 
     * @return The new Item.
     */
    public Item() {
        this.category = new Category();
        this.economyInfo = new EconomyInfo();
        this.dateInfo = new DateInfo();
        this.preference = new Preference();
    }

    /**
     * Constructor to create an Item.
     * 
     * @param itemId      The item ID.
     * @param sku         The SKU of the item.
     * @param name        The name of the Item.
     * @param category    The category this item belongs to.
     * @param economyInfo The economy info for this item.
     * @param dateInfo    The date info for this item.
     * 
     * @return The new Item.
     */
    public Item(int itemId, String sku, String name, String description, Category category, double price,
            int numItems, Date created, Date lastModified, int sellWithinNumDays, int lowInventoryThreshold,
            double promotionPercentOff) {
        this.itemId = itemId;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.economyInfo = new EconomyInfo(price, numItems);
        this.dateInfo = new DateInfo(created, lastModified);
        this.preference = new Preference(sellWithinNumDays, lowInventoryThreshold, promotionPercentOff);
    }

    /**
     * Constructor to create an Item with no set ID.
     * 
     * @param sku         The SKU of the item.
     * @param name        The name of the Item.
     * @param category    The category this item belongs to.
     * @param economyInfo The economy info for this item.
     * @param dateInfo    The date info for this item.
     * 
     * @return The new Item.
     */
    public Item(String sku, String name, String description, Category category, double price,
            int numItems, Date created, Date lastModified, int sellWithinNumDays, int lowInventoryThreshold,
            double promotionPercentOff) {
        this(0, sku, name, description, category, price, numItems, created, lastModified, sellWithinNumDays,
                lowInventoryThreshold, promotionPercentOff);
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(ITEM_ID_KEY); // SHOULD ALWAYS BE FIRST
        keys.add(SKU_KEY); // SHOULD ALWAYS BE SECOND
        keys.add(NAME_KEY);
        keys.add(DESCRIPTION_KEY);
        keys.add(CATEGORY_ID_KEY);
        // Price and Number of Items
        keys.addAll(economyInfo.getAttributeKeys());
        // Created and Last Modified
        keys.addAll(dateInfo.getAttributeKeys());
        // Sell Within, low inventory, precentage off
        keys.addAll(preference.getAttributeKeys());
        return keys;
    }

    @Override
    public List<String> getAttributeKeysNoId() {
        List<String> keys = this.getAttributeKeys();
        keys.remove(0);
        return keys;
    }

    @Override
    public List<String> getAttributeKeysRequired() {
        List<String> keys = this.getAttributeKeysNoId();
        keys.remove(0); // remove the SKU
        return keys;
    }

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(itemId));
        data.add(sku);
        data.add(name);
        data.add(description);
        data.add(String.valueOf(category.getCategoryId()));
        data.addAll(economyInfo.getAllAttributes());
        data.addAll(dateInfo.getAllAttributes());
        data.addAll(preference.getAllAttributes());
        return data;
    }

    @Override
    public List<String> getAllAttributesNoId() {
        List<String> attributes = this.getAllAttributes();
        attributes.remove(0);
        return attributes;
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // ItemId
        dataTypes.add(DataType.STRING); // Sku
        dataTypes.add(DataType.STRING); // Name
        dataTypes.add(DataType.STRING); // Description
        dataTypes.add(DataType.INTEGER); // CategoryId
        // Price and Number of Items
        dataTypes.addAll(economyInfo.getAttributeDataTypes());
        // Created and Last Modified
        dataTypes.addAll(dateInfo.getAttributeDataTypes());
        // Sell Within, low inventory, percentage off
        dataTypes.addAll(preference.getAttributeDataTypes());
        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        List<DataType> dataTypes = this.getAttributeDataTypes();
        dataTypes.remove(0);
        return dataTypes;
    }
    /**
     * Get the attribute keys related to preference information.
     * 
     * @return A list of preference-related attribute keys.
     */
    public List<String> getPreferenceKeys() {
        return preference.getAttributeKeys();
    }

    /**
     * Get the attribute keys related to date information.
     * 
     * @return A list of date-related attribute keys.
     */
    public List<String> getDateKeys() {
        return dateInfo.getAttributeKeys();
    }
    
    /**
     * get the default preference values for this Item.
     */
    public Map<String, String> getDefaultPreferences() {
        return this.preference.getDefaultValues();
    }
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Item item &&
                this.category.getCategoryId() == item.category.getCategoryId() &&
                this.category.getName().equals(item.category.getName()) &&
                this.dateInfo.getCreated().toString().equals(item.dateInfo.getCreated().toString()) &&
                this.dateInfo.getLastModified().toString().equals(item.dateInfo.getLastModified().toString()) &&
                this.description.equals(item.description) &&
                this.economyInfo.getNumItems() == item.economyInfo.getNumItems() &&
                Double.compare(this.economyInfo.getPrice(), item.economyInfo.getPrice()) == 0 &&
                this.itemId == item.itemId &&
                this.name.equals(item.name) &&
                this.preference.getLowInventoryThreshold() == item.preference.getLowInventoryThreshold() &&
                Double.compare(this.preference.getPromotionPercentOff(), item.preference.getPromotionPercentOff()) == 0
                &&
                this.preference.getSellWithinNumDays() == item.preference.getSellWithinNumDays() &&
                this.sku.equals(item.sku));
    }
    /**
     * Get the attribute keys related to preference information.
     * 
     * @return A list of preference-related attribute keys.
     */
    public List<String> getPreferenceKeys() {
        return preference.getAttributeKeys();
    }

    /**
     * Get the attribute keys related to date information.
     * 
     * @return A list of date-related attribute keys.
     */
    public List<String> getDateKeys() {
        return dateInfo.getAttributeKeys();
    }
    
    /**
     * get the default preference values for this Item.
     */
    public Map<String, String> getDefaultPreferences() {
        return this.preference.getDefaultValues();
    }
    /* Getters and Setters */

    /**
     * Get the number of days to sell this Item within.
     * 
     * @return The number of days to sell this Item within.
     */
    public int getSellWithinNumDays() {
        return this.preference.getSellWithinNumDays();
    }

    /**
     * Get the number of days to sell this Item within.
     * 
     * @param sellWithinNumDays The number of days to sell this Item within.
     */
    public void setSellWithinNumDays(int sellWithinNumDays) {
        this.preference.setSellWithinNumDays(sellWithinNumDays);
    }

    /**
     * Get the low inventory threshold for this Item.
     * 
     * @return The low inventory threshold for this Item.
     */
    public int getLowInventoryThreshold() {
        return this.preference.getLowInventoryThreshold();
    }

    /**
     * Get the promotion percent off for this Item.
     * 
     * @return The promotion percent off for this Item.
     */
    public double getPromotionPercentOff() {
        return preference.getPromotionPercentOff();
    }

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

    /**
     * Get the date this Item was created.
     * 
     * @return The date this Item was created.
     */
    public Date getCreated() {
        return this.dateInfo.getCreated();
    }

    /**
     * Set the date this Item was created.
     * 
     * @param created The date this Item was created.
     */
    public void setCreated(Date created) {
        this.dateInfo.setCreated(created);
    }

    /**
     * Get the date this Item was last modified.
     * 
     * @return The date this Item was last modified.
     */
    public Date getLastModified() {
        return this.dateInfo.getLastModified();
    }

    /**
     * Set the date this Item was last modified.
     * 
     * @param created The date this Item was last modified.
     */
    public void setLastModified(Date lastModified) {
        this.dateInfo.setLastModified(lastModified);
    }

    /**
     * Get the price of this Item.
     * 
     * @return The price of this Item.
     */
    public double getPrice() {
        return this.economyInfo.getPrice();
    }

    /**
     * Set the price of this Item.
     * 
     * @param price The price of this Item.
     */
    public void setPrice(double price) {
        this.economyInfo.setPrice(price);
    }

    /**
     * Get the number of this Item in stock.
     * 
     * @return The number of this Item in stock.
     */
    public int getNumItems() {
        return this.economyInfo.getNumItems();
    }

    /**
     * Set the number of this Item in stock.
     * 
     * @param numItems The number of this Item in stock.
     */
    public void setNumItems(int numItems) {
        this.economyInfo.setNumItems(numItems);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
