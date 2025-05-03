package database.reports;

import java.util.List;
import database.items.*; // Adjust the package path if necessary

public abstract class ReportGenerator {
    public abstract boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList);
}
