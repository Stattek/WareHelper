package database.items;

/**
 * Class that represents a Category for an Item.
 */
public class Category {
    private int categoryId;
    private String name;

    /**
     * Creates a new category with a name.
     * 
     * @param name The name of the Category.
     * @return The new Category.
     */
    public Category(String name) {
        this.name = name;
    }
}
