package database.importers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import database.items.Category;
import database.items.Item;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.Scanner;

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
public class CsvImporter extends Importer<Item> {

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
    public List<Item> importData(String filePath) {
        List<Item> items = new ArrayList<>();

        // Required column names (case-insensitive)
        String[] validKeys = { "name", "category", "price", "quantity" };

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
                String normalizedHeader = headers[i].trim().toLowerCase();
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

                try {
                    Item item = new Item();

                    // Set item properties from CSV columns
                    item.setName(fields[columnIndexMap.get("name")].trim());

                    // Create new Category with temporary ID (-1)
                    item.setCategory(new Category(
                            -1,
                            fields[columnIndexMap.get("category")].trim()));

                    item.setPrice(Double.parseDouble(
                            fields[columnIndexMap.get("price")].trim()));
                    item.setNumItems(Integer.parseInt(
                            fields[columnIndexMap.get("quantity")].trim()));

                    item.setCreated(new Date(0));
                    item.setLastModified(new Date(0));

                    items.add(item);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    // Skip malformed rows but continue processing
                    System.err.println("Skipping malformed row: " + line);
                    // Consider logging this to a proper logger in production
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    String.format("Failed to read CSV file at path: %s", filePath),
                    e);
        }

        return items;
    }
}