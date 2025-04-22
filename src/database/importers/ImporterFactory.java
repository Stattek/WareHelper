package database.importers;

/**
 * Factory to create different types of importers
 * 
 * Only returns CSVImporter for now
 */
public class ImporterFactory<T> {

    /**
     * Creates a new Importer object based on the given type.
     * 
     * @return An Importer object
     */
    public Importer createImporter(ImporterTypes importerType) {
        if (importerType == ImporterTypes.CSV) {
            return new CsvImporter();
        }
        return new CsvImporter();
    }
}