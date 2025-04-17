package database.importers;

import java.util.List;

/**
 * Abstract base class for data importers.
 * @param <T> the type of data being imported
 */
public abstract class Importer<T> {
    
    /**
     * Performs the import operation.
     * @return List of imported items
     */
    public abstract List<T> importData(String filePath);
}