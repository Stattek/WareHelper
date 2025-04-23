package database;

public class InnerObject {
    private String parentObject;
    private String objectName;
    private String thisId;

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

    public String getParentObject() {
        return parentObject;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getThisId() {
        return thisId;
    }

}
