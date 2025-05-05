package database.reports;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import database.items.*;

/**
 * Generates a report summarizing inventory volumes and values by category.
 */
public class InventoryVolumeReport extends ReportGenerator {

    private Map<Category, CategorySummary> categorySummaries;
    private String reportFilePath;

    /**
     * Constructor that initializes the report with a timestamped filename.
     */
    public InventoryVolumeReport() {
        this.categorySummaries = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        this.reportFilePath = "inventory_volume_report_" + dateFormat.format(new Date()) + ".csv";
    }

    @Override
    public boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList) {
        // Clear previous results
        this.categorySummaries.clear();

        // Create a map from category ID to CategorySummary
        Map<Integer, CategorySummary> categoryIdMap = new HashMap<>();
        Map<Integer, Category> idToCategoryMap = new HashMap<>();

        // Initialize summary for each category
        for (Category category : categoryList) {
            CategorySummary summary = new CategorySummary(category.getName());
            categorySummaries.put(category, summary);
            categoryIdMap.put(category.getCategoryId(), summary);
            idToCategoryMap.put(category.getCategoryId(), category);
        }

        // Count items and calculate values by category
        for (Item item : itemList) {
            Category category = item.getCategory();
            if (category != null) {
                int categoryId = category.getCategoryId();
                if (categoryIdMap.containsKey(categoryId)) {
                    CategorySummary summary = categoryIdMap.get(categoryId);
                    summary.addItem(item.getNumItems(), item.getPrice(),
                            item.getPrice() - (item.getPrice() * item.getPromotionPercentOff()));

                    // Also update our main map if we haven't already
                    if (!categorySummaries.containsKey(category)) {
                        categorySummaries.put(idToCategoryMap.get(categoryId), summary);
                    }
                }
            }
        }

        // Rest of the method remains the same
        if (categorySummaries.isEmpty()) {
            return true;
        }

        try (FileWriter writer = new FileWriter(reportFilePath)) {
            // Write headers
            writer.append(
                    "CategoryName,UniqueItems,TotalUnits,TotalValue,TotalDiscountedValue,AverageUnitPrice,AverageDiscountedUnitPrice\n");

            // Track totals across all categories
            int totalItems = 0;
            int totalUnqiueItems = 0;
            double totalValue = 0.0;
            double totalDiscountedValue = 0.0;

            // Write data rows for each category
            for (CategorySummary summary : categorySummaries.values()) {
                if (summary.getItemCount() > 0) {
                    writer.append(summary.getCategoryName()).append(",");
                    writer.append(String.valueOf(summary.getUniqueItemCount())).append(",");
                    writer.append(String.valueOf(summary.getItemCount())).append(",");
                    writer.append(String.format("%.2f", summary.getTotalValue())).append(",");
                    writer.append(String.format("%.2f", summary.getTotalDiscountedValue())).append(",");
                    writer.append(String.format("%.2f", summary.getAverageItemPrice())).append(",");
                    writer.append(String.format("%.2f", summary.getAverageDiscountedPrice())).append("\n");

                    // Add to overall totals
                    totalUnqiueItems += summary.getUniqueItemCount();
                    totalItems += summary.getItemCount();
                    totalValue += summary.getTotalValue();
                    totalDiscountedValue += summary.getTotalDiscountedValue();
                }
            }

            // Write overall totals row
            writer.append("ALL CATEGORIES,");
            writer.append(String.valueOf(totalUnqiueItems)).append(",");
            writer.append(String.valueOf(totalItems)).append(",");
            writer.append(String.format("%.2f", totalValue)).append(",");
            writer.append(String.format("%.2f", totalDiscountedValue)).append(",");

            // Calculate overall average price
            double overallAverage = totalItems > 0 ? totalValue / totalItems : 0.0;
            writer.append(String.format("%.2f", overallAverage)).append(",");

            // Calculate overall discounted average price
            double overallDiscountedAverage = totalItems > 0 ? totalDiscountedValue / totalItems : 0.0;
            writer.append(String.format("%.2f", overallDiscountedAverage)).append("\n");

            writer.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Helper class to track summary data for each category
     */
    private static class CategorySummary {
        private String categoryName;
        private int itemCount;
        private double totalValue;
        private double totalDiscountedValue;
        private int uniqueItemCount;

        /**
         * Initializes all values to 0 and sets the categoryName
         * 
         * @param categoryName the name of the category
         */
        public CategorySummary(String categoryName) {
            this.categoryName = categoryName;
            this.itemCount = 0;
            this.totalValue = 0.0;
        }

        /**
         * increment the count values
         * 
         * @param quantity Quantity of that item in the inventory
         * @param price    Price of item (induvidually)
         */
        public void addItem(int quantity, double price, double discountedPrice) {
            this.uniqueItemCount += 1;
            this.itemCount += quantity;
            this.totalValue += quantity * price;
            this.totalDiscountedValue += quantity * discountedPrice;
        }

        public int getUniqueItemCount() {
            return uniqueItemCount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public int getItemCount() {
            return itemCount;
        }

        public double getTotalValue() {
            return totalValue;
        }

        public double getAverageItemPrice() {
            return itemCount > 0 ? totalValue / itemCount : 0.0;
        }

        public double getTotalDiscountedValue() {
            return totalDiscountedValue;
        }

        public double getAverageDiscountedPrice() {
            return itemCount > 0 ? totalDiscountedValue / itemCount : 0.0;
        }
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
     * Gets the category summaries generated during report creation
     * 
     * @return Map of categories to their inventory summaries
     */
    public Map<Category, CategorySummary> getCategorySummaries() {
        return new HashMap<>(categorySummaries);
    }
}