package user;

/**
 * Class to represent a pair of values.
 */
public class Pair<T, U> {
    private T first;
    private U second;

    // Constructor
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first element.
     * 
     * @return The first element.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Gets the second element.
     * 
     * @return The second element.
     */
    public U getSecond() {
        return second;
    }
}
