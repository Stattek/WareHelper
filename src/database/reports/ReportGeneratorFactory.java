package database.reports;

import database.items.*;

import java.util.List;

/**
 * Factory class responsible for generating various inventory reports
 */
public class ReportGeneratorFactory {
    LowInventoryReport lowInventoryReport = new LowInventoryReport();
    UnsoldInventoryReport unsoldInventoryReport = new UnsoldInventoryReport();
    InventoryVolumeReport inventoryVolumeReport = new InventoryVolumeReport();

    /**
     * Generates a  low inventory report based on the provided categories, items, and bundles.
     *
     * @param categories the list of categories to include in the report
     * @param items      the list of items to include in the report
     * @param bundles    the list of bundles to include in the report
     * @return true if the report generation was successful, false otherwise
     */
    public boolean generateLowInventoryReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {

        return lowInventoryReport.generateReport(items, categories, bundles);
    }

    /**
     * Generates an unsold inventory report based on the provided categories, items,
     * and bundles.
     *
     * @param categories the list of categories to include in the report
     * @param items      the list of items to include in the report
     * @param bundles    the list of bundles to include in the report
     * @return true if the report generation was successful, false otherwise
     */
    public boolean generateUnsoldInventoryReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {

        return unsoldInventoryReport.generateReport(items, categories, bundles);
    }

    /**
     * Generates a inventory volume report based on the provided categories, items, and bundles.
     *
     * @param categories the list of categories to include in the report
     * @param items      the list of items to include in the report
     * @param bundles    the list of bundles to include in the report
     * @return true if the report generation was successful, false otherwise
     */
    public boolean generateInventoryVolumeReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {

        return inventoryVolumeReport.generateReport(items, categories, bundles);
    }
}
