package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold date info for an Item.
 */
public class DateInfo implements ConvertableObject {
    private Date created;
    private Date lastModified;

    public final static String CREATED_KEY = "Created";
    public final static String LAST_MODIFIED_KEY = "LastModified";

    /**
     * Default Constructor for DateInfo
     */
    public DateInfo() {

    }

    public DateInfo(Date created, Date lastModified) {
        this.created = created;
        this.lastModified = lastModified;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(CREATED_KEY);
        keys.add(LAST_MODIFIED_KEY);
        return keys;
    }

    @Override
    public List<String> getAttributeKeysNoId() {
        return this.getAttributeKeys();
    }

    @Override
    public List<String> getAttributeKeysRequired() {
        return this.getAttributeKeysNoId();
    }

    @Override
    public List<String> getAllAttributes() {
        ArrayList<String> data = new ArrayList<>();
        data.add(created.toString());
        data.add(lastModified.toString());
        return data;
    }

    @Override
    public List<String> getAllAttributesNoId() {
        return this.getAllAttributes();
    }

    @Override
    public List<DataType> getAttributeDataTypes() {
        ArrayList<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.DATE);
        dataTypes.add(DataType.DATE);
        return dataTypes;
    }

    @Override
    public List<DataType> getAttributeDataTypesNoId() {
        return this.getAttributeDataTypes();
    }

    /* Getters and Setters */

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

}