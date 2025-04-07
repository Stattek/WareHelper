package database.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a Category for an Item.
 */
public class Category extends ConvertableObject {
    private int categoryId;
    private String name;

    /**
     * Creates a new category with a name.
     * 
     * @param name The name of the Category.
     * @return The new Category.
     */
    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("CategoryId");
        keys.add("Name");
        return keys;
    }

    @Override
    public List<String> getSubObjects() {
        ArrayList<String> output = new ArrayList<>();
        return output;
    }

    /* Getters and Setters */

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
