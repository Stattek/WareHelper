package database.items;

import java.util.ArrayList;
import java.util.List;

public class Preference extends ConvertableObject {
    private int preferenceId;
    private int sellWithinNumDays; // number of days to sell the item in
    private int lowInventoryThreshold; // number of items before it is considered low inventory
    private double promotionPercentOff; // percent off promotion

    public Preference(int preferenceId, int sellWithinNumDays, int lowInventoryThreshold, double promotionPercentOff) {
        this.preferenceId = preferenceId;
        this.sellWithinNumDays = sellWithinNumDays;
        this.lowInventoryThreshold = lowInventoryThreshold;
        this.promotionPercentOff = promotionPercentOff;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("PreferenceId");
        keys.add("SellWithinNumDays");
        keys.add("LowInventoryThreshold");
        keys.add("PromotionPercentOff");
        return keys;
    }

    @Override
    public List<String> getSubObjects() {
        ArrayList<String> output = new ArrayList<>();
        return output;
    }

    /* Getters and Setters */

    public int getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }

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
