package database.items;

import java.util.ArrayList;
import java.util.List;

public class EconomyInfo implements ConvertableObject {
    private double price; // price of item
    private int numItems; // number of items in stock

    public final static String PRICE_KEY = "Price";
    public final static String NUM_ITEMS_KEY = "NumItems";

    /**
     * Default Constructor for EconomyInfo
     */
    public EconomyInfo() {

    }

    public EconomyInfo(double price, int numItems) {
        this.price = price;
        this.numItems = numItems;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(PRICE_KEY);
        keys.add(NUM_ITEMS_KEY);
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
        data.add(String.valueOf(price));
        data.add(String.valueOf(numItems));
        return data;
    }

    @Override
    public List<String> getAllAttributesNoId() {
        return this.getAllAttributes();
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.DOUBLE);
        dataTypes.add(DataType.INTEGER);
        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        return this.getAttributeDataTypes();
    }

    /* Getters and Setters */

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    /**
     * Method to return the numeric attribute keys for EconomyInfo
     * 
     * @return A list of all names for numeric keys
     */
    public static List<String> getNumericAttributeKeys() {
        List<String> numericKeys = new ArrayList<>();
        numericKeys.add(PRICE_KEY); // Add price key
        numericKeys.add(NUM_ITEMS_KEY); // Add numItems key
        return numericKeys;
    }
}
