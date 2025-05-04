package database.importers;

/**
 * Abstract base class for data importers.
 * 
 * @param <T> the type of data being imported
 */
public abstract class Importer<T> {

    /**
     * Performs the import operation.
     * 
     * @return List of imported items
     */
    public abstract T importData(String filePath);
}