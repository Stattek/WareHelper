package database.reports;

import java.util.List;
import database.items.*;

/**
 * Generate a report based on inventory data
 */
public abstract class ReportGenerator {

    /**
     * Generates a report using the provided lists of items, categories, and
     * bundles.
     *
     * @param itemList     the list of items to include in the report
     * @param categoryList the list of categories to include in the report
     * @param bundleList   the list of bundles to include in the report
     * @return true if the report generation is successful, false otherwise
     */
    public abstract boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList);

    /**
     * Retrieves the file path where the generated report is stored.
     *
     * @return the file path of the generated report
     */
    public abstract String getReportFilePath();

    /**
     * Sets the file path where the generated report should be stored.
     *
     * @param filePath the file path to set for the generated report
     */
    public abstract void setReportFilePath(String filePath);
}
