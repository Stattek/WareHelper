import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import user.Controller;
import user.Pair;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    /**
     * Retrieves the entire inventory.
     * 
     * @param keyboard User input scanner.
     */
    private static void retrieveInventory(Scanner keyboard) {

        boolean continueChoice = true;
        while (continueChoice) {
            System.out.println("Select a Sort Option");
            String sortOptions[] = { "Unsorted", "Sort by Name", "Sort by Date", "Sort by Item Cost",
                    "Group By Category" };
            promptUser(sortOptions);

            int sortChoice = 0;
            try {
                sortChoice = keyboard.nextInt();
            } catch (Exception e) {
            } finally {
                // get rid of garbage data
                keyboard.nextLine();
            }

            boolean ascending = true;
            // check if the user wants to sort by asc or desc
            if (sortChoice > 0 && sortChoice != 1 && sortChoice < 6) {
                System.out.println("Choose sorting order:");
                String orderOptions[] = { "Ascending", "Descending" };
                promptUser(orderOptions);
                int orderChoice = 0;
                try {
                    orderChoice = keyboard.nextInt();
                } catch (Exception e) {
                } finally {
                    // get rid of garbage data
                    keyboard.nextLine();
                }

                if (orderChoice == 2) {
                    ascending = false;
                } else if (orderChoice != 1) {
                    System.err.println("\nInvalid order choice. Defaulting to Ascending.");
                }
            }

            switch (sortChoice) {
                case 1:
                    // read all items
                    System.out.println(Controller.readAllItems());
                    continueChoice = false;
                    break;
                case 2:
                    System.out
                            .println(Controller.readAllItemsSortByName(ascending));
                    continueChoice = false;
                    break;
                case 3:
                    System.out.println(Controller.readAllItemsSortByDate(ascending));
                    continueChoice = false;
                    break;
                case 4:
                    System.out.println(Controller.readAllItemsSortByCost(ascending));
                    continueChoice = false;
                    break;
                case 5:
                    System.out.println(Controller.readAllItemsGroupByCategory(ascending));
                    continueChoice = false;
                    break;
                default:
                    System.err.println("\nInvalid sort choice.");
                    break;
            }

        }
    }

    /**
     * Searches for an item by name.
     * 
     * @param keyboard User input scanner.
     */
    private static void searchByName(Scanner keyboard) {
        boolean continueChoice = true;
        while (continueChoice) {
            System.out.print("Enter name to search by > ");
            String name = "";
            try {
                name = keyboard.nextLine().trim();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                break;
            }

            // check that the name is valid
            if (Controller.validateString(name)) {
                System.out.println("name: " + name);
                System.out.println(Controller.readItemByName(name));
                continueChoice = false;
            } else {
                System.err.println("\nInvalid name, enter only letters, numbers, and spaces");
                break;
            }
        }
    }

    /**
     * Retrieves all bundles.
     * 
     * @param keyboard User input scanner.
     */
    private static void retrieveAllBundles(Scanner keyboard) {
        System.out.println(Controller.readAllBundles());
    }

    /**
     * Searches for an item by SKU.
     * 
     * @param keyboard User input scanner.
     */
    private static void searchBySku(Scanner keyboard) {
        // Search by SKU
        System.out.print("Enter SKU to search by > ");
        String sku = "";
        try {
            sku = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }
        if (sku.isEmpty()) {
            System.err.println("ERROR: SKU cannot be empty.");
            return;
        }

        if (Controller.validateSKU(sku)) {
            List<String> item = Controller.readItemBySKU(sku);
            if (item != null && !item.isEmpty()) {

                List<String> itemKeys = Controller.getItemKeys();
                System.out.println("Item details-");
                for (int i = 0; i < item.size(); i++) {
                    // print out all the keys and their values
                    if (i < itemKeys.size()) {
                        System.out.println(itemKeys.get(i) + ": " + item.get(i));
                    } else {
                        // if for some reason we have more attributes, just print them
                        System.out.println("Attribute " + (i + 1) + ": " + item.get(i));
                    }
                }
            } else {
                System.err.println("Item not found for SKU: " + sku);
            }
        } else {
            System.err.println("\nInvalid SKU format. Example of valid SKU: CategoryName1");
        }
    }

    /**
     * Creates a new category.
     * 
     * @param keyboard User input scanner.
     */
    private static void createCategory(Scanner keyboard) {
        Map<String, String> categoryData = new HashMap<>();
        List<String> categoryKeys = Controller.getCategoryKeysNoId();
        for (String key : categoryKeys) {
            System.out.print("Enter value for the Category \"" + key + "\" field > ");
            String inputField = "";
            try {
                inputField = keyboard.nextLine().trim();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }

            // check that the field is valid
            if (!Controller.validateString(inputField)) {
                System.err.println("ERROR: Invalid input for Category object");
                return;
            }

            // add this validated key, value pair into the dictionary
            categoryData.put(key, inputField);
        }

        boolean success = Controller.createCategory(categoryData);
        if (success) {
            System.out.println("Category '" + categoryData.get(categoryKeys.get(0)) + "' created successfully.");
        } else {
            System.err.println(
                    "ERROR: Could not create category. Please only enter letters, numbers and spaces. It may also already exist.");
        }
    }

    /**
     * Creates a new category.
     * 
     * @param keyboard User input scanner.
     */
    private static void createBundle(Scanner keyboard) {

        List<String> bundleKeys = Controller.getBundleKeysNoId();

        Map<String, String> bundleMap = new HashMap<>();
        for (String key : bundleKeys) {
            System.out.print("Enter value for the Bundle \"" + key + "\" field > ");
            String inputField = "";
            try {
                inputField = keyboard.nextLine().trim();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }

            // check that the field is valid
            if (!Controller.validateString(inputField)) {
                System.err.println("ERROR: Invalid input for Bundle object");
                return;
            }

            // add this validated key, value pair into the dictionary
            bundleMap.put(key, inputField);
        }

        List<Integer> itemIds = new ArrayList<>();
        int id = 0;
        do {
            System.out.print("Enter ID of Item to add to Bundle or -1 to stop (must have at least one Item) > ");
            try {
                id = keyboard.nextInt();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }

            // add new ID
            if (id != -1 && Controller.validateId(id)) {
                itemIds.add(id);
            }
        } while (id != -1 || itemIds.isEmpty());

        // get rid of garbage data
        keyboard.nextLine();

        // create the bundle
        if (!Controller.createBundle(bundleMap, itemIds)) {
            System.err.println("ERROR: Could not create a new Bundle");
        }
    }

    /**
     * Retrieves all categories.
     * 
     * @param keyboard User input scanner.
     */
    private static void retrieveAllCategories(Scanner keyboard) {
        System.out.println("Do you want to perform a search by name?");
        String options[] = { "Yes", "No" };
        promptUser(options);

        int choice = 0;
        try {
            choice = keyboard.nextInt();
        } catch (Exception e) {
        } finally {
            // get rid of garbage data
            keyboard.nextLine();
        }

        switch (choice) {
            case 1:
                System.out.print("Enter name to search by > ");
                String name = "";

                try {
                    name = keyboard.nextLine().trim();
                } catch (Exception e) {
                    System.err.println("ERROR: Could not read user input");
                    return;
                }

                if (Controller.validateString(name)) {
                    System.out.println("name: " + name);
                    System.out.println(Controller.readCategoryByName(name));
                } else {
                    System.err.println("\nInvalid name, enter only letters, numbers, and spaces");
                }
                break;
            case 2:
                String categories = Controller.readAllCategories();
                System.out.println(categories);
                break;
            default:
                System.err.println("\nInvalid choice.");
                break;
        }
    }

    /**
     * Deletes a category.
     * 
     * @param keyboard User input scanner.
     */
    private static void deleteCategory(Scanner keyboard) {
        System.out.print("Enter the ID of the category to delete > ");
        String categoryId = "";
        try {
            categoryId = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }

        // validate that the category ID is a valid integer
        if (Controller.validateStringToId(categoryId)) {
            int categoryIdInt = Integer.parseInt(categoryId);
            boolean success = Controller.deleteCategory(categoryIdInt);
            if (success) {
                System.out.println("Category with ID '" + categoryIdInt + "' deleted successfully.");
            } else {
                System.err.println("ERROR: Could not delete category. It may not exist.");
            }
        } else {
            System.err.println("\nInvalid category ID, enter a non-negative integer.");
        }
    }

    /**
     * Deletes a bundle.
     * 
     * @param keyboard User input scanner.
     */
    private static void deleteBundle(Scanner keyboard) {
        System.out.print("Enter the ID of the bundle to delete > ");
        String bundleIdStr = "";
        try {
            bundleIdStr = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }

        // validate that the bundle ID is a valid integer
        if (Controller.validateStringToId(bundleIdStr)) {
            int bundleId = Integer.parseInt(bundleIdStr);
            boolean success = Controller.deleteBundle(bundleId);
            if (success) {
                System.out.println("Bundle with ID '" + bundleId + "' deleted successfully.");
            } else {
                System.err.println("ERROR: Could not delete bundle. It may not exist.");
            }
        } else {
            System.err.println("\nInvalid bundle ID, enter a non-negative integer.");
        }
    }

    /**
     * Creates an item by prompting the user for input.
     * 
     * @param keyboard User input scanner.
     */
    private static void createItem(Scanner keyboard) {
        System.out.println("Creating a new item");
        Map<String, String> itemData = new HashMap<>();
        List<String> itemKeys = Controller.getItemKeysRequiredInput();
        List<String> optionalKeys = Controller.getPreferenceKeys();
        Map<String, String> optionalDefaults = Controller.getPreferenceDefaults();

        for (String key : itemKeys) {

            System.out.print("Enter value for the Item \"" + key + "\" field > ");
            String inputField = "";
            try {
                inputField = keyboard.nextLine().trim();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }

            // Validate the input
            if (!Controller.validateString(inputField)) {
                System.err.println("ERROR: Invalid input for Item object");
                return;
            }
            if (Controller.getNumericItemKeys().contains(key) && !Controller.validateNumericInput(inputField)) {
                System.err.println("ERROR: Invalid input for " + key + ". Please enter a valid numeric value.");
                return;
            }

            if (inputField.isEmpty()) {
                System.err.println("ERROR: This field cannot be empty.");
                return;
            }

            // Add the validated key-value pair to the map
            itemData.put(key, inputField);
        }
        System.out.println("Would you like to provide optional data?");
        String yesNoOptions[] = { "Yes", "No" };
        promptUser(yesNoOptions);

        int choice = 0;
        String choiceString = "";
        while (true) {
            try {
                choiceString = keyboard.nextLine().trim();
                if (choiceString.equalsIgnoreCase("Yes") || choiceString.equals("1")) {
                    choice = 1;
                    break;
                } else if (choiceString.equalsIgnoreCase("No") || choiceString.equals("2")) {
                    choice = 2;
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'Yes' or 'No' (or 1 or 2).");
                }
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }
        }

        // get rid of garbage data
        switch (choice) {
            case 1:
                for (String key : optionalKeys) {
                    System.out.print("Enter value for the optional Item \"" + key
                            + "\" field (leave blank if you would like to not set this value) > ");
                    String inputField = "";
                    try {
                        inputField = keyboard.nextLine().trim();
                    } catch (Exception e) {
                        System.err.println("ERROR: Could not read user input");
                        return;
                    }

                    // If the user leaves the field blank, use the default value
                    if (inputField.isEmpty()) {
                        inputField = optionalDefaults.getOrDefault(key, "");
                    }
                    // validate input
                    else if (!Controller.validateString(inputField)) {
                        System.err.println("ERROR: Invalid input for optional Item object");
                        return;
                    }
                    if (!Controller.validateNumericInput(inputField)) {
                        System.err.println("ERROR: Invalid input for " + key + ". Please enter a valid numeric value.");
                        return;
                    }
                    // Check if the key is a numeric field (e.g., price, quantity)
                    if (Controller.getNumericItemKeys().contains(key) && !Controller.validateNumericInput(inputField)) {
                        System.err.println("ERROR: Invalid input for " + key + ". Please enter a valid numeric value.");
                        return;
                    }

                    // Add the validated key-value pair to the map
                    itemData.put(key, inputField);
                }
                break;
            case 2:
                System.out.println("Skipping optional data.");
                for (String key : optionalKeys) {
                    String defaultValue = optionalDefaults.getOrDefault(key, "");
                    itemData.put(key, defaultValue);
                }
        }

        // Prompt for category
        String categoryName;
        do {
            System.out.print("Enter Category Name > ");

            try {
                categoryName = keyboard.nextLine().trim();
            } catch (Exception e) {
                System.err.println("ERROR: Could not read user input");
                return;
            }

            if (!Controller.validateString(categoryName)) {
                System.err.println("ERROR: Invalid Category Name");
            } else {
                // valid
                break;
            }
        } while (true);

        // Check for empty input
        if (categoryName.isEmpty()) {
            System.err.println("ERROR: Category name cannot be empty.");
            return;
        }

        Map<String, String> innerCategory = new HashMap<>();
        List<String> categoryKeys = Controller.getCategoryKeysNoId();
        List<String> categoryValues = List.of(categoryName);

        if (categoryValues.size() != categoryKeys.size()) {
            System.err.println("ERROR: category values and keys are not the same size");
            return;
        }

        for (int i = 0; i < categoryValues.size(); i++) {
            innerCategory.put(categoryKeys.get(i), categoryValues.get(i));
        }

        // Create the item
        Pair<Boolean, String> result = Controller.createItem(itemData, innerCategory);
        boolean success = result.getFirst();

        if (success) {
            String sku = result.getSecond();
            System.out.println("Item created successfully-\nSku: " + sku);
        } else {
            System.err.println("ERROR: Failed to create item");
        }
    }

    /**
     * Deletes an item.
     * 
     * @param keyboard User input scanner.
     */
    private static void deleteItem(Scanner keyboard) {
        System.out.print("Enter the ID of the item to delete > ");
        String itemId = "";
        try {
            itemId = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }

        // validate that the item ID is a valid integer
        if (Controller.validateStringToId(itemId)) {
            int itemIdInt = Integer.parseInt(itemId);
            boolean success = Controller.deleteItem(itemIdInt);
            if (success) {
                System.out.println("Item with ID '" + itemIdInt + "' deleted successfully.");
            } else {
                System.err.println("ERROR: Could not delete item. It may not exist.");
            }
        } else {
            System.err.println("Invalid item ID, enter a non-negative integer.");
        }
    }

    /**
     * Generate a preference report
     * 
     * @param keyboard User input scanner.
     */
    private static void generateReport(Scanner keyboard) {
        System.out.println("Choose the type of report to generate:");
        String reportOptions[] = {
                "Low Inventory Report",
                "Unsold Inventory Report",
                "Inventory Volume Report"
        };
        promptUser(reportOptions);

        int reportChoice = 0;
        try {
            reportChoice = keyboard.nextInt();
        } catch (Exception e) {
        } finally {
            // get rid of garbage data
            keyboard.nextLine();
        }

        switch (reportChoice) {
            case 1:
                generateLowInventoryReport(keyboard);
                break;
            case 2:
                generateUnsoldInventoryReport(keyboard);
                break;
            case 3:
                generateInventoryVolumeReport(keyboard);
                break;
            default:
                System.err.println("\nInvalid report choice.");
                break;
        }
    }

    /**
     * Import data from CSV.
     * 
     * @param keyboard User input scanner.
     */
    private static void importFromCSV(Scanner keyboard) {
        System.out.print("Enter file path to import items from > ");
        String filePath = "";
        try {
            filePath = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }
        try {
            if (!Controller.importItems(filePath)) {
                throw new Exception(); // fail
            } else {
                System.out.println("Items imported successfully from file: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not import items from file");
        }
    }

    /**
     * Update a category.
     * 
     * @param keyboard User input scanner.
     */
    private static void updateCategory(Scanner keyboard) {
        System.out.print("Enter the ID of the category to update > ");
        String categoryIdStr = "";
        try {
            categoryIdStr = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }

        // validate that the category ID is a valid integer
        if (!Controller.validateStringToId(categoryIdStr)) {
            System.err.println("\nInvalid category ID, enter a non-negative integer.");
            return;
        }

        int categoryId = Integer.parseInt(categoryIdStr);
        System.out.println("Enter new values for the fields (leave blank to keep current value):");
        List<String> updatedCategoryData = new ArrayList<>();
        List<String> updatedCategoryKeys = new ArrayList<>();
        List<String> categoryKeys = Controller.getCategoryKeysNoId();
        updatedCategoryData.add(Integer.toString(categoryId));
        updatedCategoryKeys.add(Controller.getCategoryIdKey());

        for (String key : categoryKeys) {
            System.out.print("Enter value for the Category \"" + key + "\" field > ");
            boolean isValid = false;
            while (!isValid) {
                String inputField = "";
                try {
                    inputField = keyboard.nextLine().trim();
                } catch (Exception e) {
                    System.err.println("ERROR: Could not read user input");
                    return;
                }

                // If the user provides input, validate and add it to the map
                if (!inputField.isEmpty()) {
                    if (Controller.validateString(inputField)) {
                        updatedCategoryData.add(inputField);
                        updatedCategoryKeys.add(key);
                        isValid = true;
                    } else {
                        System.err.println("ERROR: Invalid input for Category object. Please enter again:");
                    }
                } else {
                    isValid = true; // Allow empty input to keep the current value
                }
            }
        }

        boolean success = Controller.updateCategory(updatedCategoryData, updatedCategoryKeys);
        if (success) {
            System.out.println("Category with ID '" + categoryId + "' updated successfully.");
        } else {
            System.err.println("ERROR: Could not update category. Please check your inputs.");
        }

    }

    /**
     * Update an item.
     * 
     * @param keyboard User input scanner.
     */
    private static void updateItem(Scanner keyboard) {
        System.out.print("Enter the ID of the item to update > ");
        String itemIdStr = "";
        try {
            itemIdStr = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            return;
        }

        // Validate that the item ID is a valid integer
        if (!Controller.validateStringToId(itemIdStr)) {
            System.err.println("\nInvalid item ID, enter a non-negative integer.");
            return;
        }

        int itemId = Integer.parseInt(itemIdStr);
        System.out.println("Enter new values for the fields (leave blank to keep current value):");
        List<String> updatedItemData = new ArrayList<>();
        List<String> updatedItemKeys = new ArrayList<>();
        List<String> itemKeys = Controller.getItemKeysToUpdate();
        updatedItemData.add(Integer.toString(itemId));
        updatedItemKeys.add(Controller.getItemIdKey());

        for (String key : itemKeys) {
            System.out.print("Enter value for the Item \"" + key + "\" field > ");
            boolean isValid = false;
            while (!isValid) {
                String inputField = "";
                try {
                    inputField = keyboard.nextLine().trim();
                } catch (Exception e) {
                    System.err.println("ERROR: Could not read user input");
                    return;
                }
                // If the user provides input, validate and add it to the map
                if (!inputField.isEmpty()) {
                    if (Controller.validateString(inputField)) {
                        updatedItemData.add(inputField);
                        updatedItemKeys.add(key);
                        isValid = true;
                    } else {
                        System.err.println("ERROR: Invalid input for Item object. Please enter again:");
                    }
                } else {
                    isValid = true; // Allow empty input to keep the current value
                }
            }
        }

        boolean success = Controller.updateItem(updatedItemData, updatedItemKeys);
        if (success) {
            System.out.println("Item with ID '" + itemId + "' updated successfully.");
        } else {
            System.err.println("ERROR: Could not update item. Please check your inputs.");
        }
    }

    /**
     * Generates a low inventory report.
     * 
     * @param keyboard User input scanner.
     */
    private static void generateLowInventoryReport(Scanner keyboard) {
        System.out.println("Generating Low Inventory Report");
        boolean success = Controller.lowInventoryReport();
        if (success) {
            System.out.println("Low Inventory Report generated successfully.");
        } else {
            System.err.println("ERROR: Failed to generate Low Inventory Report.");
        }

    }

    /**
     * Generates an unsold inventory report.
     * 
     * @param keyboard User input scanner.
     */
    private static void generateUnsoldInventoryReport(Scanner keyboard) {
        System.out.println("Generating Unsold Inventory Report...");
        boolean success = Controller.unsoldInventoryReport();

        if (success) {
            System.out.println("Unsold Inventory generated successfully.");
        } else {
            System.err.println("ERROR: Failed to generate Unsold Inventory Report.");
        }
    }

    /**
     * Generates an inventory volume report.
     * 
     * @param keyboard User input scanner.
     */
    private static void generateInventoryVolumeReport(Scanner keyboard) {
        System.out.println("Generating Inventory Volume Report...");
        boolean success = Controller.inventoryVolumeReport();

        if (success) {
            System.out.println("Inventory Volume Report generated successfully.");
        } else {
            System.err.println("ERROR: Failed to generate Inventory Volume Report.");
        }
    }

    /**
     * Prints options as a numbered list.
     * 
     * @param options The options to print.
     */
    private static void printOptions(String options[]) {
        for (int i = 0; i < options.length; i++) {
            System.out.println("    " + (i + 1) + ". " + options[i]);
        }
    }

    /**
     * Prompts the user for input to select one of the options.
     * 
     * @param options The options for the user to choose from.
     */
    private static void promptUser(String options[]) {
        System.out.println("\n\nChoose an option:");

        printOptions(options);

        System.out.print("Select an option > ");

    }

    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception e) {
            System.err.println("ERROR: could not set up required dependencies");
            System.exit(1);
        }

        System.out.println("Welcome to WareHelper!");

        Scanner keyboard = new Scanner(System.in);

        boolean continueProgram = true;

        String options[] = {
                "Retrieve Inventory",
                "Retrieve Category",
                "Retrieve Bundles",
                "Create Category",
                "Delete Category",
                "Update Category",
                "Create Item",
                "Delete Item",
                "Update Item",
                "Create Bundle",
                "Delete Bundle",
                "Search Item by Name",
                "Search Item by SKU",
                "Generate Report",
                "Import From CSV",
                "Exit", // THIS SHOULD ALWAYS BE LAST
        };

        while (continueProgram) {
            promptUser(options);

            int choice = 0;
            try {
                choice = keyboard.nextInt();
            } catch (Exception e) {
            } finally {
                // get rid of garbage data
                keyboard.nextLine();
            }

            switch (choice) {
                case 1:
                    // retrieve inventory
                    retrieveInventory(keyboard);
                    break;
                case 2:
                    retrieveAllCategories(keyboard);
                    break;
                case 3:
                    retrieveAllBundles(keyboard);
                    break;
                case 4:
                    createCategory(keyboard);
                    break;
                case 5:
                    deleteCategory(keyboard);
                    break;
                case 6:
                    updateCategory(keyboard);
                    break;
                case 7:
                    createItem(keyboard);
                    break;
                case 8:
                    deleteItem(keyboard);
                    break;
                case 9:
                    updateItem(keyboard);
                    break;
                case 10:
                    createBundle(keyboard);
                    break;
                case 11:
                    deleteBundle(keyboard);
                    break;
                case 12:
                    searchByName(keyboard);
                    break;
                case 13:
                    searchBySku(keyboard);
                    break;
                case 14:
                    generateReport(keyboard);
                    break;
                case 15:
                    importFromCSV(keyboard);
                    break;

                case 16: // EXITING SHOULD ALWAYS BE THE LAST CHOICE
                    // exit program
                    continueProgram = false;
                    break;
                default:
                    // invalid input
                    System.err.println("\nInvalid choice.");
                    break;
            }
        }

        keyboard.close();
    }
}
