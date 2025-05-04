package database.importers;

import java.util.List;
import java.util.Map;

import database.items.Item;
import user.Pair;

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
    public static Importer<Pair<List<Map<String, String>>, List<Map<String, String>>>> createItemImporter(
            ImporterTypes importerType) {
        if (importerType == ImporterTypes.CSV) {
            return new CsvImporter();
        }
        throw new IllegalArgumentException("Invalid importer type: " + importerType);
    }
}