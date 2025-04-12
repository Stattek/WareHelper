package database.items;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DateInfo implements ConvertableObject {
    private Date created;
    private Date lastModified;

    public DateInfo(Date created, Date lastModified) {
        this.created = created;
        this.lastModified = lastModified;
    }

    @Override
    public List<String> getAttributeKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("Created");
        keys.add("LastModified");
        return keys;
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