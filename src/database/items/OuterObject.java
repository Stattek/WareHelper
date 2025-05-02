package database.items;

import java.util.List;

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

}