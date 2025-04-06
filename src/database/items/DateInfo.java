package database.items;

import java.sql.Date;

public class DateInfo {
    private Date created;
    private Date lastModified;

    public DateInfo(Date created, Date lastModified) {
        this.created = created;
        this.lastModified = lastModified;
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