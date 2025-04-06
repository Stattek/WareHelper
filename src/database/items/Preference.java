package database.items;

public class Preference {
    private int sellWithinNumDays; // number of days to sell the item in
    private int lowInventoryThreshold; // number of items before it is considered low inventory
    private double promotionPercentOff; // percent off promotion

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
