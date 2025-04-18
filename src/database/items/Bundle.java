package database.items;

import java.util.ArrayList;
import java.util.List;

public class Bundle extends ConvertableObject {
    private int bundleId;
    private double bundleDiscount;
    // TODO: we may want the item to have a foreign key to bundle
    private List<Item> items;

    /**
     * Default constructor for Bundle.
     */
    public Bundle() {
        this.items = new ArrayList<>();
    }

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

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(bundleId));
        data.add(String.valueOf(bundleDiscount));
        for (Item item : items) {
            data.add(item.toString());
        }
        return data;
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // For BundleId
        dataTypes.add(DataType.DOUBLE);  // For BundleDiscount
        for (Item item : items) {
            dataTypes.add(DataType.INTEGER); // For Items
        }
        
        return dataTypes;
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
