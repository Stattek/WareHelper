package database.items;

import java.util.List;

/**
 * Class to represent an object that can be converted into storage data.
 */
public interface ConvertableObject {

    /**
     * Gets all the attribute keys for this object.
     * 
     * @return The list of keys.
     */
    public List<String> getAttributeKeys();

    /**
     * Gets the keys for this object, except for its own ID.
     * 
     * @return The list of keys.
     */
    public List<String> getAttributeKeysNoId();

    /**
     * Gets all the attribute data for this object as strings.
     * 
     * @return The list of data.
     */
    public List<String> getAllAttributes();

    /**
     * Gets all the attribute data for this object as strings, except for its own
     * ID.
     * 
     * @return The list of data.
     */
    public List<String> getAllAttributesNoId();

    /**
     * Gets the datatypes of each attribute in this object.
     * 
     * @return The list of datatypes.
     */
    public List<DataType> getAttributeDataTypes();

    /**
     * Gets the datatypes of each attribute in this object, except for its own ID.
     * 
     * @return The list of datatypes.
     */
    public List<DataType> getAttributeDataTypesNoId();

    /**
     * Gets the keys required to create this object.
     * 
     * @return The list of keys.
     */
    public List<String> getAttributeKeysRequired();

}
