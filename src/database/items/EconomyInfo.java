package database.items;

public class EconomyInfo {
    private double price; // price of item
    private int numItems; // number of items in stock

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
