package database.reports;

import database.items.*;

import java.util.List;

public class ReportGeneratorFactory {
    public boolean generateLowInventoryReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {
        LowInventoryReport lowInventoryReport = new LowInventoryReport();
        return lowInventoryReport.generateReport(items, categories, bundles);
    }

    
    public boolean generateUnsoldInventoryReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {
        UnsoldInventoryReport unsoldInventoryReport = new UnsoldInventoryReport();
        return unsoldInventoryReport.generateReport(items, categories, bundles);
    }

    public boolean generateInventoryVolumeReport(List<Category> categories, List<Item> items, List<Bundle> bundles) {
        InventoryVolumeReport inventoryVolumeReport = new InventoryVolumeReport();
        return inventoryVolumeReport.generateReport(items, categories, bundles);
    }
}
