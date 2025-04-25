package database;

/**
 * Inner Object to help with database joins, for finding out the parent of an
 * object and on what ID the objects share.
 */
public class InnerObject {
    private String parentObject;
    private String objectName;
    private String thisId; // the parent object and this object MUST have the same name for this ID

    /**
     * Creates a new inner object.
     * 
     * @param parentObject
     * @param objectName
     * @param thisId
     */
    public InnerObject(String parentObject, String objectName, String thisId) {
        this.parentObject = parentObject;
        this.objectName = objectName;
        this.thisId = thisId;
    }

    /**
     * Gets the parent object name.
     * 
     * @return The parent object name.
     */
    public String getParentObject() {
        return parentObject;
    }

    /**
     * Gets this inner object's name.
     * 
     * @return This inner object's name
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Gets this inner object's ID column name. A shared name between the parent
     * object and
     * inner object.
     * 
     * @return The inner object's ID column name.
     */
    public String getThisId() {
        return thisId;
    }

}
