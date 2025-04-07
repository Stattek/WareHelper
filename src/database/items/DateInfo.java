package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DateInfo extends ConvertableObject {
    private int dateInfoId;
    private Date created;
    private Date lastModified;

    public DateInfo(int dateInfoId, Date created, Date lastModified) {
        this.dateInfoId = dateInfoId;
        this.created = created;
        this.lastModified = lastModified;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("DateInfoId");
        keys.add("Created");
        keys.add("LastModified");
        return keys;
    }

    @Override
    public List<String> getSubObjects() {
        ArrayList<String> output = new ArrayList<>();
        return output;
    }

    /* Getters and Setters */

    public int getDateInfoId() {
        return dateInfoId;
    }

    public void setDateInfoId(int dateInfoId) {
        this.dateInfoId = dateInfoId;
    }

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