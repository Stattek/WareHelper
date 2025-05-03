package user;

public class Pair<T, U> { // created mainly to return tuple for createItem from Controller to Driver.
    private T first; // First value (boolean)
    private U second; // Second value (SKU)

    // Constructor
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    // Getter for the first element
    public T getFirst() {
        return first;
    }

    // Getter for the second element
    public U getSecond() {
        return second;
    }
}
