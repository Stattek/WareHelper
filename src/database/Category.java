package database;

/**
 * Class that represents a Category for an Item.
 */
public class Category {
    private static int nextId = 0;
    private int id;
    private String name;

    /**
     * Creates a new category with a name.
     * 
     * @param name The name of the Category.
     * @return The new Category.
     */
    public Category(String name) {
        this.name = name;
        this.id = nextId;
        // increment next possible ID.
        nextId++;
    }
}
