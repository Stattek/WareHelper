package database.items;

import java.util.ArrayList;
import java.util.List;

public class Preference implements ConvertableObject {
    private int sellWithinNumDays; // number of days to sell the item in
    private int lowInventoryThreshold; // number of items before it is considered low inventory
    private double promotionPercentOff; // percent off promotion

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
        keys.add("SellWithinNumDays");
        keys.add("LowInventoryThreshold");
        keys.add("PromotionPercentOff");
        return keys;
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
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // for sellWithinNumDays
        dataTypes.add(DataType.INTEGER); // for lowInventoryThreshold
        dataTypes.add(DataType.DOUBLE); // for promotionPercentOff
        return dataTypes;
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

}
