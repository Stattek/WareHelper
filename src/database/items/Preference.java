package database.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preference implements ConvertableObject {
    private int sellWithinNumDays; // number of days to sell the item in
    private int lowInventoryThreshold; // number of items before it is considered low inventory
    private double promotionPercentOff; // percent off promotion

    public final static String SELL_WITHIN_NUM_DAYS_KEY = "SellWithinNumDays";
    public final static String LOW_INVENTORY_THRESHOLD_KEY = "LowInventoryThreshold";
    public final static String PROMOTION_PERCENT_OFF_KEY = "PromotionPercentOff";

    /**
     * Default Constructor for Preference
     */
    public Preference() {

    }

    public Preference(int sellWithinNumDays, int lowInventoryThreshold, double promotionPercentOff) {
        this.sellWithinNumDays = sellWithinNumDays;
        this.lowInventoryThreshold = lowInventoryThreshold;
        this.promotionPercentOff = promotionPercentOff;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(SELL_WITHIN_NUM_DAYS_KEY);
        keys.add(LOW_INVENTORY_THRESHOLD_KEY);
        keys.add(PROMOTION_PERCENT_OFF_KEY);
        return keys;
    }

    @Override
    public List<String> getAttributeKeysNoId() {
        return this.getAttributeKeys();
    }

    @Override
    public List<String> getAttributeKeysRequired() {
        return this.getAttributeKeysNoId();
    }

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(sellWithinNumDays));
        data.add(String.valueOf(lowInventoryThreshold));
        data.add(String.valueOf(promotionPercentOff));
        return data;
    }

    @Override
    public List<String> getAllAttributesNoId() {
        return this.getAllAttributes();
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // for sellWithinNumDays
        dataTypes.add(DataType.INTEGER); // for lowInventoryThreshold
        dataTypes.add(DataType.DOUBLE); // for promotionPercentOff
        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        return this.getAttributeDataTypes();
    }

    /* Getters and Setters */

    public int getSellWithinNumDays() {
        return sellWithinNumDays;
    }

    public void setSellWithinNumDays(int sellWithinNumDays) {
        this.sellWithinNumDays = sellWithinNumDays;
    }

    public int getLowInventoryThreshold() {
        return lowInventoryThreshold;
    }

    public void setLowInventoryThreshold(int lowInventoryThreshold) {
        this.lowInventoryThreshold = lowInventoryThreshold;
    }

    public double getPromotionPercentOff() {
        return promotionPercentOff;
    }

    public void setPromotionPercentOff(double promotionPercentOff) {
        this.promotionPercentOff = promotionPercentOff;
    }

    /**
     * Returns the default values for the attributes of Preference as a map.
     * 
     * @return A map of attribute keys to their default values.
     */
    public Map<String, String> getDefaultValues() {
        Map<String, String> defaultValues = new HashMap<>();
        defaultValues.put(SELL_WITHIN_NUM_DAYS_KEY, "-1"); // Default for sellWithinNumDays
        defaultValues.put(LOW_INVENTORY_THRESHOLD_KEY, "-1"); // Default for lowInventoryThreshold
        defaultValues.put(PROMOTION_PERCENT_OFF_KEY, "0.0"); // Default for promotionPercentOff
        return defaultValues;
    }

    /**
     * Method to return the numeric attribute keys for Preference
     * 
     * @return A list of all names for numeric keys
     */
    public static List<String> getNumericAttributeKeys() {
        List<String> numericKeys = new ArrayList<>();
        numericKeys.add(SELL_WITHIN_NUM_DAYS_KEY);
        numericKeys.add(LOW_INVENTORY_THRESHOLD_KEY);
        numericKeys.add(PROMOTION_PERCENT_OFF_KEY);
        return numericKeys;
    }

}
