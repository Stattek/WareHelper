package database.reports;

import java.util.List;
import database.items.*; 

/**
 * Generate a report based on inventory data
 */
public abstract class ReportGenerator {
    public abstract boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList);
    public abstract String getReportFilePath();
    public abstract void setReportFilePath(String filePath);
}
