package database.importers;

import java.io.IOException;
import java.util.zip.DataFormatException;

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
    public Importer<T> createImporter(ImporterTypes importerType) {
        if (importerType == ImporterTypes.CSV) {
            return new CsvImporter();
        }
        return new CsvImporter();
    }
}