package database.importers;

import database.items.Item;

/**
 * Factory to create different types of importers
 * 
 * Only returns CSVImporter for now
 */
public class ImporterFactory {

    /**
     * Creates a new Importer object based on the given type.
     * 
     * @return An Importer object
     */
    public Importer<Item> createItemImporter(ImporterTypes importerType) {
        if (importerType == ImporterTypes.CSV) {
            return new CsvImporter();
        }
        throw new IllegalArgumentException("Invalid importer type: " + importerType);
    }
}