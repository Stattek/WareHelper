package database.items;

/**
 * Class that represents an Item in the inventory.
 */
public class Item {
    private int itemId;
    private String sku;
    private String name;
    private Category category;
    private EconomyInfo economyInfo;
    private DateInfo dateInfo;
    private Preference preference;

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

}
