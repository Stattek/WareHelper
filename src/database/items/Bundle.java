package database.items;

import java.util.List;

public class Bundle {
    private int bundleId;
    private double bundleDiscount;
    private List<Item> items;

    Bundle(int bundleId, int bundleDiscount, List<Item> items) {
        this.bundleId = bundleId;
        this.bundleDiscount = bundleDiscount;
        this.items = items;
    }

    /* Getters and Setters */

    public int getBundleId() {
        return bundleId;
    }

    public void setBundleId(int bundleId) {
        this.bundleId = bundleId;
    }

    public double getBundleDiscount() {
        return bundleDiscount;
    }

    public void setBundleDiscount(double bundleDiscount) {
        this.bundleDiscount = bundleDiscount;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
