package database.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents an Item in the inventory.
 */
public class Item extends ConvertableObject {
    private int bundleId;
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
    public Item(int itemId, String sku, String name, Category category,
            EconomyInfo economyInfo, DateInfo dateInfo, Preference preference) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.economyInfo = economyInfo;
        this.dateInfo = dateInfo;
        this.preference = preference;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("ItemId");
        keys.add("Sku");
        keys.add("Name");
        keys.add("CategoryId");
        keys.add("EconomyInfoId");
        keys.add("DateInfoId");
        keys.add("PreferenceId");
        keys.add("BundleId");
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

    /* Getters and Setters */

    /**
     * Adds promotion to item.
     * 
     * @param percentOffPromotion The percent off of the item.
     */
    public void setPromotion(double percentOffPromotion) {
        this.preference.setPromotionPercentOff(percentOffPromotion);
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

    public EconomyInfo getEconomyInfo() {
        return economyInfo;
    }

    public void setEconomyInfo(EconomyInfo economyInfo) {
        this.economyInfo = economyInfo;
    }

    public DateInfo getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(DateInfo dateInfo) {
        this.dateInfo = dateInfo;
    }

    public void setBundleId(int id){
        bundleId = id;
    }

    public int getBundleId(int id){
        return bundleId;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

}
