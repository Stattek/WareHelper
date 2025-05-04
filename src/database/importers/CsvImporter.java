package database.importers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import database.items.Category;
import database.items.Item;
import database.items.ObjectService;
import user.Pair;
import user.Controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

/**
 * Imports item data from CSV files into a list of {@link Item} objects.
 * 
 * The CSV file must contain specific columns (case-insensitive):
 * itemid - Unique identifier for the item
 * sku - Stock keeping unit identifier
 * name - Name of the item
 * category - Category of the item
 * price - Price of the item (numeric)
 * quantity - Available quantity (integer)
 * 
 * 
 * Example CSV format:
 * 
 * itemid,sku,name,category,price,quantity
 * 1001,SKU123,Widget A,Hardware,19.99,50
 * 1002,SKU456,Widget B,Hardware,29.99,30
 * 
 */
public class CsvImporter extends Importer<Pair<List<Map<String, String>>, List<Map<String, String>>>> {

    /**
     * Finds if a value is in a list.
     * 
     * @param haystack The list to search through.
     * @param needle   The value to find.
     * @return True if the value is in the list, false otherwise.
     */
    private boolean isInList(final List<String> haystack, final String needle) {
        boolean output = false;
        for (int i = 0; i < haystack.size(); i++) {
            if (haystack.get(i) == needle) {
                output = true;
                break;
            }
        }

        return output;
    }

    /**
     * Imports item data from a CSV file.
     *
     * @param filePath Path to the CSV file to import
     * @return List of imported {@link Item} objects
     * @throws RuntimeException         If the file is not found or cannot be read
     * @throws IllegalArgumentException If required columns are missing in the CSV
     *                                  header
     */
    @Override
    public Pair<List<Map<String, String>>, List<Map<String, String>>> importData(String filePath) {
        List<Map<String, String>> items = new ArrayList<>();
        List<Map<String, String>> categories = new ArrayList<>();

        // Required column names (case-sensitive)
        List<String> validKeys = ObjectService.getItemKeysRequired();
        // we want to have all the category values as well, without the ID, since Item
        // should have its ID
        validKeys.addAll(ObjectService.getCategoryKeysRequired());

        // remove the category ID
        for (int i = 0; i < validKeys.size(); i++) {
            if (validKeys.get(i) == Category.CATEGORY_ID_KEY) {
                validKeys.remove(i);
                break;
            }
        }

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Check for empty file
            if (!scanner.hasNextLine()) {
                throw new FileNotFoundException("CSV file is empty");
            }

            // --- HEADER PROCESSING ---
            // Read and parse the header line
            String headerLine = scanner.nextLine();
            String[] headers = headerLine.split(",");

            // This is used to handle the case where the client has the right headers but in
            // teh wrong order
            Map<String, Integer> columnIndexMap = new HashMap<>();

            // Create mapping of column names to their indices
            for (int i = 0; i < headers.length; i++) {
                String normalizedHeader = headers[i].trim();
                columnIndexMap.put(normalizedHeader, i);
            }

            // Validate that all required columns exist
            for (String key : validKeys) {
                if (!columnIndexMap.containsKey(key)) {
                    throw new IllegalArgumentException(
                            String.format("Missing required column: '%s' in CSV header", key));
                }
            }

            // --- DATA PROCESSING ---
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Actual values to process
                String[] fields = line.split(",");

                final List<String> itemKeys = ObjectService.getItemKeys();
                final List<String> categoryKeys = ObjectService.getCategoryKeys();

                try {
                    Map<String, String> itemData = new HashMap<>();
                    Map<String, String> categoryData = new HashMap<>();

                    // since the fields and validKeys have been checked to be the same
                    for (int i = 0; i < fields.length; i++) {
                        // set the key with its value pulled out of the CSV
                        String curKey = validKeys.get(i);

                        // set if in item's keys
                        if (isInList(itemKeys, curKey)) {
                            itemData.put(curKey, fields[columnIndexMap.get(curKey)].trim());
                        }

                        // set if in category's keys
                        // NOTE: a key can be in both sets
                        if (isInList(categoryKeys, curKey)) {
                            categoryData.put(curKey, fields[columnIndexMap.get(curKey)].trim());
                        }
                    }

                    // add the category and item data
                    items.add(itemData);
                    categories.add(categoryData);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    // Skip malformed rows but continue processing
                    // Consider logging this to a proper logger in production
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    String.format("Failed to read CSV file at path: %s", filePath),
                    e);
        }

        return new Pair<List<Map<String, String>>, List<Map<String, String>>>(items, categories);
    }
}