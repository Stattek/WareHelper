package database.items;

import java.util.ArrayList;
import java.util.List;

import database.InnerObject;

public class Bundle implements ConvertableObject, OuterObject {
    private int bundleId;
    private double bundleDiscount;
    private List<Item> items;

    public final static String TABLE_NAME = "Bundle";
    public final static String ASSOCIATION_TABLE_NAME = "ItemBundle"; // name of association table for
                                                                      // many-to-manyrelationship with held Items

    public final static String BUNDLE_ID_KEY = "BundleId";
    public final static String BUNDLE_DISCOUNT_KEY = "BundleDiscount";

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

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(BUNDLE_ID_KEY); // SHOULD ALWAYS BE FIRST
        keys.add(BUNDLE_DISCOUNT_KEY);
        return keys;
    }

    @Override
    public List<String> getAttributeKeysNoId() {
        List<String> keys = this.getAttributeKeys();
        keys.remove(0);
        return keys;
    }

    @Override
    public List<String> getAttributeKeysRequired() {
        return this.getAttributeKeysNoId();
    }

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(bundleId)); // SHOULD ALWAYS BE FIRST
        data.add(String.valueOf(bundleDiscount));
        return data;
    }

    @Override
    public List<String> getAllAttributesNoId() {
        List<String> attributes = this.getAllAttributes();
        attributes.remove(0);
        return attributes;
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.INTEGER); // SHOULD ALWAYS BE FIRST
        dataTypes.add(DataType.DOUBLE);
        for (Item item : items) {
            // add item ID as an integer
            dataTypes.add(DataType.INTEGER);
        }

        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        List<DataType> dataTypes = this.getAttributeDataTypes();
        dataTypes.remove(0);
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

    @Override
    public List<InnerObject> getInnerObjects() {
        List<InnerObject> innerObjects = new ArrayList<>();
        innerObjects.add(new InnerObject(Bundle.TABLE_NAME, Bundle.ASSOCIATION_TABLE_NAME, Bundle.BUNDLE_ID_KEY));
        innerObjects.add(new InnerObject(Bundle.ASSOCIATION_TABLE_NAME, Item.TABLE_NAME, Item.ITEM_ID_KEY));
        innerObjects.add(new InnerObject(Item.TABLE_NAME, Category.TABLE_NAME, Category.CATEGORY_ID_KEY));
        return innerObjects;
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
