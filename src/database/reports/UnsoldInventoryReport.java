package database.reports;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import database.items.Bundle;
import database.items.Category;
import database.items.DataType;
import database.items.Item;

/**
 * Generates a report of inventory items that have been in stock longer than
 * their designated sell-within period.
 */
public class UnsoldInventoryReport extends ReportGenerator {

    private List<Item> unsoldItems;
    private String reportFilePath;

    /**
     * Constructor that initializes the report with a timestamped filename.
     */
    public UnsoldInventoryReport() {
        this.unsoldItems = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        this.reportFilePath = "unsold_inventory_report_" + dateFormat.format(new java.util.Date()) + ".csv";
    }

    @Override
    public boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList) {
        // Clear previous results
        this.unsoldItems.clear();

        // Current date for comparison
        LocalDate currentDate = LocalDate.now();

        // Identify items that have been in inventory longer than their sell-within
        // period
        for (Item item : itemList) {
            Date createdDate = item.getLastModified();
            int sellWithinDays = item.getSellWithinNumDays();

            // Convert SQL Date to LocalDate
            LocalDate created = createdDate.toLocalDate();

            // Calculate days since creation
            long daysSinceCreation = ChronoUnit.DAYS.between(created, currentDate);

            // If item has exceeded its sell-by timeframe
            if (daysSinceCreation > sellWithinDays) {
                unsoldItems.add(item);
            }
        }

        if (unsoldItems.isEmpty()) {
            System.out.println("No unsold items found for report");
            return true; // No items to report but not an error condition
        }

        try (FileWriter writer = new FileWriter(reportFilePath)) {
            // Get a sample item to access attribute keys and data types
            Item sampleItem = unsoldItems.get(0);
            List<String> keys = sampleItem.getAttributeKeys();
            List<DataType> dataTypes = sampleItem.getAttributeDataTypes();

            // Add custom calculated fields for the report
            List<String> reportHeaders = new ArrayList<>(keys);
            reportHeaders.add("ExpectedSellByDate");
            reportHeaders.add("DaysOverdue");

            // Find the index of the category ID column
            int categoryIdIndex = reportHeaders.indexOf(Item.CATEGORY_ID_KEY);

            // Write headers - replace CategoryId with CategoryName
            for (int i = 0; i < reportHeaders.size(); i++) {
                if (i == categoryIdIndex) {
                    writer.append(Category.NAME_KEY);
                } else {
                    writer.append(reportHeaders.get(i));
                }

                if (i < reportHeaders.size() - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // Date formatter for CSV output
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

            // Write data rows
            for (Item item : unsoldItems) {
                List<String> attributes = item.getAllAttributes();

                // Write standard attributes based on data types
                for (int i = 0; i < attributes.size(); i++) {
                    String value = attributes.get(i);
                    DataType type = dataTypes.get(i);
                    // If this is the category ID column, write the category name instead
                    if (i == categoryIdIndex) {
                        writer.append(item.getCategory().getName());
                    } else {
                        switch (type) {
                            case DOUBLE:
                                // Format doubles with two decimal places
                                try {
                                    double doubleVal = Double.parseDouble(value);
                                    writer.append(String.format("%.2f", doubleVal));
                                } catch (NumberFormatException e) {
                                    writer.append(value);
                                }
                                break;
                            case DATE:
                                // Format dates nicely
                                try {
                                    Date date = Date.valueOf(value);
                                    writer.append(dateFormatter.format(date));
                                } catch (IllegalArgumentException e) {
                                    writer.append(value);
                                }
                                break;
                            default:
                                // For all other types, use the value as is
                                writer.append(value);
                        }
                    }
                    writer.append(",");
                }

                // Calculate and write the custom fields
                LocalDate createdLocalDate = item.getCreated().toLocalDate();
                LocalDate expectedSellByDate = createdLocalDate.plusDays(item.getSellWithinNumDays());
                long daysOverdue = ChronoUnit.DAYS.between(expectedSellByDate, currentDate);

                writer.append(expectedSellByDate.toString()).append(",");
                writer.append(String.valueOf(daysOverdue));
                writer.append("\n");
            }

            writer.flush();
            System.out.println("Unsold inventory report generated: " + reportFilePath);
            System.out.println("Found " + unsoldItems.size() + " items that have exceeded their sell-within period");
        } catch (IOException e) {
            System.err.println("Error writing unsold inventory report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Gets the file path of the generated report
     *
     * @return The file path of the report
     */
    @Override
    public String getReportFilePath() {
        return reportFilePath;
    }

    /**
     * Sets a custom file path for the report
     *
     * @param filePath The file path to use for the report
     */
    @Override
    public void setReportFilePath(String filePath) {
        this.reportFilePath = filePath;
    }

    /**
     * Gets the list of unsold items identified during report generation
     *
     * @return List of items that have exceeded their sell-within period
     */
    public List<Item> getUnsoldItems() {
        return new ArrayList<>(unsoldItems);
    }
}