package database;

/**
 * Interface that represents the any record that can be uniquely identified in the database.
 */
public interface Identifiable {
    
    // Returns the unique key associated with the record
    public abstract int getId();
}
