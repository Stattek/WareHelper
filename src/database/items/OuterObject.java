package database.items;

import java.util.List;

import database.InnerObject;

/**
 * If an object has inner objects that are also tables, it is considered an
 * OuterObject.
 */
public interface OuterObject {

    /**
     * Gets the inner object Ids for each inner object.
     * 
     * @return The list of Ids.
     */
    public List<String> getInnerObjectIds();

    /**
     * Gets the inner objects for this object.
     * 
     * @return The list of InnerObjects.
     */
    public List<InnerObject> getInnerObjects();

}