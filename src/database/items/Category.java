package database.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Category for an Item.
 */
public class Category implements ConvertableObject {
    private int categoryId;
    private String name;

    public final static String TABLE_NAME = "Category";

    public final static String CATEGORY_ID_KEY = "CategoryId";
    public final static String NAME_KEY = "CategoryName"; // different to avoid name conflicts

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

    /**
     * Default Constructor
     */
    public Category() {
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(CATEGORY_ID_KEY); // SHOULD ALWAYS BE FIRST
        keys.add(NAME_KEY);
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
        data.add(String.valueOf(categoryId));// SHOULD ALWAYS BE FIRST
        data.add(name);
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
        dataTypes.add(DataType.INTEGER);// SHOULD ALWAYS BE FIRST
        dataTypes.add(DataType.STRING);
        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        List<DataType> dataTypes = this.getAttributeDataTypes();
        dataTypes.remove(0);
        return dataTypes;
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
