package database.items;

import java.util.ArrayList;
import java.util.List;

public class EconomyInfo extends ConvertableObject {
    private double price; // price of item
    private int numItems; // number of items in stock

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
        keys.add("Price");
        keys.add("NumItems");
        return keys;
    }

    /* Getters and Setters */

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(price));
        data.add(String.valueOf(numItems));
        return data;
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

}
