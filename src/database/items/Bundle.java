package database.items;

import java.util.ArrayList;
import java.util.List;

public class Bundle implements ConvertableObject, OuterObject {
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

    /**
     * Creates a new Bundle.
     * 
     * @param bundleId       The bundle ID.
     * @param bundleDiscount The bundle discount.
     * @param items          The list of Item objects in this Bundle.
     */
    public Bundle(int bundleId, double bundleDiscount, List<Item> items) {
        this.bundleId = bundleId;
        this.bundleDiscount = bundleDiscount;
        this.items = items;
    }

    /**
     * Creates a new Bundle with no ID.
     * 
     * @param bundleDiscount The bundle discount.
     * @param items          The list of Item objects in this Bundle.
     */
    public Bundle(double bundleDiscount, List<Item> items) {
        this(0, bundleDiscount, items);
    }

    /**
     * Creates a new Bundle with no ID or discount.
     * 
     * @param items The list of Item objects in this Bundle.
     */
    public Bundle(List<Item> items) {
        this(0, 0.0, items);
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
        return data;
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // For BundleId
        dataTypes.add(DataType.DOUBLE); // For BundleDiscount
        for (Item item : items) {
            dataTypes.add(DataType.INTEGER); // For Items
        }

        return dataTypes;
    }

    @Override
    public List<String> getInnerObjectIds() {
        ArrayList<String> innerIds = new ArrayList<>();
        // add all ItemIds to the list
        for (Item item : this.items) {
            innerIds.add(Integer.toString(item.getItemId()));
        }

        return innerIds;
    }

    /**
     * Adds an item to the item list for this bundle.
     * 
     * @param newItem The new item.
     * @return True on successful add, false otherwise.
     */
    public boolean addItem(Item newItem) {
        return this.items.add(newItem);
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
