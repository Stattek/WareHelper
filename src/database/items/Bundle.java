package database.items;

import java.util.ArrayList;
import java.util.List;

public class Bundle implements ConvertableObject {
    private int bundleId;
    private double bundleDiscount;
    private List<Item> items;

    public Bundle(int bundleId, int bundleDiscount, List<Item> items) {
        this.bundleId = bundleId;
        this.bundleDiscount = bundleDiscount;
        this.items = items;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("BundleId");
        keys.add("BundleDiscount");
        return keys;
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
