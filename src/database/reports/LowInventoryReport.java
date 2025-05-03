package database.reports;

import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import database.items.*;

public class LowInventoryReport extends ReportGenerator {

	private List<Item> lowInventoryItems;
	private String reportFilePath;

	public LowInventoryReport() {
		this.lowInventoryItems = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		this.reportFilePath = "low_inventory_report_" + dateFormat.format(new Date()) + ".csv";
	}

	@Override
	public boolean generateReport(List<Item> itemList, List<Category> categoryList, List<Bundle> bundleList) {
		// Clear previous results
		this.lowInventoryItems.clear();

		// Identify items with low inventory
		for (Item item : itemList) {
			if (item.getNumItems() <= item.getLowInventoryThreshold()) {
				lowInventoryItems.add(item);
			}
		}

		if (lowInventoryItems.isEmpty()) {
			System.out.println("No low inventory items found for report");
			return true; // No items to report but not an error condition
		}

		try (FileWriter writer = new FileWriter(reportFilePath)) {
			// Get a sample item to access attribute keys and data types
			Item sampleItem = lowInventoryItems.get(0);
			List<String> keys = sampleItem.getAttributeKeys();
			List<DataType> dataTypes = sampleItem.getAttributeDataTypes();

			// Find the index of the category ID column
			int categoryIdIndex = keys.indexOf(Item.CATEGORY_ID_KEY);

			// Write headers - replace CategoryId with CategoryName
			for (int i = 0; i < keys.size(); i++) {
				if (i == categoryIdIndex) {
					writer.append(Category.NAME_KEY);
				} else {
					writer.append(keys.get(i));
				}

				if (i < keys.size() - 1) {
					writer.append(",");
				}
			}
			writer.append("\n");

			// Date formatter for CSV output
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

			// Write data rows
			for (Item item : lowInventoryItems) {
				List<String> attributes = item.getAllAttributes();

				// Write each attribute according to its data type
				for (int i = 0; i < attributes.size(); i++) {
					String value = attributes.get(i);
					DataType type = dataTypes.get(i);

					// If this is the category ID column, write the category name instead
					if (i == categoryIdIndex) {
						writer.append(item.getCategory().getName());
					} else {
						// Process other columns as before
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
									Date date = java.sql.Date.valueOf(value);
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

					if (i < attributes.size() - 1) {
						writer.append(",");
					}
				}
				writer.append("\n");
			}

			writer.flush();
			System.out.println("Low inventory report generated: " + reportFilePath);
			System.out.println("Found " + lowInventoryItems.size() + " items below their inventory threshold");
		} catch (IOException e) {
			System.err.println("Error writing low inventory report: " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// Getter for the report file path
	public String getReportFilePath() {
		return reportFilePath;
	}

	// Setter to specify a custom file path
	public void setReportFilePath(String filePath) {
		this.reportFilePath = filePath;
	}
}