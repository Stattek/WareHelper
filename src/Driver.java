import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    private static Controller controller; // controller to communicate with

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
                    "Group By Category", "Search By SKU" };
            promptUser(sortOptions);

            int sortChoice = 0;
            try {
                sortChoice = keyboard.nextInt();
            } catch (Exception e) {
                keyboard.nextLine();
            }

            // get rid of garbage data
            keyboard.nextLine();

            switch (sortChoice) {
                case 1:
                    // read all items
                    System.out.println(controller.readAllItems());
                    continueChoice = false;
                    break;
                case 2:
                    // Sort by name
                    System.out.println("Choose sorting order:");
                    String orderOptions[] = { "Ascending", "Descending" };
                    promptUser(orderOptions);

                    int orderChoice = 0;
                    try {
                        orderChoice = keyboard.nextInt();
                    } catch (Exception e) {
                        keyboard.nextLine();
                    }

                    // get rid of garbage data
                    keyboard.nextLine();

                    boolean ascending = true;
                    if (orderChoice == 2) {
                        ascending = false;
                    } else if (orderChoice != 1) {
                        System.err.println("\nInvalid order choice. Defaulting to Ascending.");
                    }
                    System.out
                            .println(controller.readAllItemsSortByName(ascending));
                    continueChoice = false;
                    break;
                case 3:
                    // TODO: by date
                    System.err.println("ERROR: Sort By Date is not implemented yet");
                    continueChoice = false;
                    break;
                case 4:
                    // TODO: by Item Cost
                    System.err.println("ERROR: Sort By item Cost is not implemented yet");
                    continueChoice = false;
                    break;
                case 5:
                    // TODO: group by category
                    System.err.println("ERROR: Group by category is not implemented yet");
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
            if (controller.validateString(name)) {
                System.out.println("name: " + name);
                System.out.println(controller.readItemByName(name));
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
        System.out.println(controller.readAllBundles());
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
        }

        if (InputValidator.validateSKU(sku)) { 
            System.out.println(controller.readItemBySKU(sku));
        } 
        else {
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
        List<String> categoryKeys = controller.getCategoryKeysNoId();
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
            if (!controller.validateString(inputField)) {
                System.err.println("ERROR: Invalid input for Category object");
                return;
            }

            // add this validated key, value pair into the dictionary
            categoryData.put(key, inputField);
        }

        boolean success = controller.createCategory(categoryData);
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

        List<String> bundleKeys = controller.getBundleKeysNoId();

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
            if (!controller.validateString(inputField)) {
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
            id = keyboard.nextInt();

            // add new ID
            if (id != -1) {
                itemIds.add(id);
            }
        } while (id != -1 || itemIds.isEmpty());

        // get rid of garbage data
        keyboard.nextLine();

        // create the bundle
        if (!controller.createBundle(bundleMap, itemIds)) {
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
            keyboard.nextLine();
        }

        // get rid of garbage data
        keyboard.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter name to search by > ");
                String name = "";

                try {
                    name = keyboard.nextLine().trim();
                } catch (Exception e) {
                    System.err.println("ERROR: Could not read user input");
                }

                if (controller.validateString(name)) {
                    System.out.println("name: " + name);
                    System.out.println(controller.readCategoryByName(name));
                } else {
                    System.err.println("\nInvalid name, enter only letters, numbers, and spaces");
                }
                break;
            case 2:
                String categories = controller.readAllCategories();
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
            keyboard.nextLine();
        }

        // validate that the category ID is a valid integer
        if (controller.validateStringToInt(categoryId)) {
            int categoryIdInt = Integer.parseInt(categoryId);
            boolean success = controller.deleteCategory(categoryIdInt);
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
            keyboard.nextLine();
        }

        // validate that the bundle ID is a valid integer
        if (controller.validateStringToInt(bundleIdStr)) {
            int bundleId = Integer.parseInt(bundleIdStr);
            boolean success = controller.deleteBundle(bundleId);
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
     * Creates an item.
     * 
     * @param keyboard User input scanner.
     */
    private static void createNewItem(Scanner keyboard) {

        System.out.println("Enter Category, Item Name, and Description.");

        System.out.print("Enter Category: (ex: A) \n> ");
        String categoryGiven = keyboard.nextLine().trim().toUpperCase();
        System.out.print("Enter Item Name: (ex: Jeans) \n> ");
        String itemName = keyboard.nextLine().trim();
        System.out.print("Enter Description: (ex: Bought from Walmart) \n> ");
        String description = keyboard.nextLine().trim();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        System.out.println("Add any further information you would like this item to have, or confirm:");

        double price = 0.0;
        int numItems = 0;
        int sellWithinNumDays = 0;
        int lowInventoryThreshold = 0;
        double promotionPercentOff = 0.0;

        String options[] = {
                "Add Price",
                "Add Number of Items",
                "Add Sell-By Date",
                "Add Low Inventory Warning Threshhold.",
                "Add Item Promotion",
                "Confirm"
        };

        int choice = 0;
        boolean isDone = false;
        while (!isDone) {
            // add promptUser function here to choose options.
            promptUser(options);

            try {
                choice = keyboard.nextInt();
            } catch (Exception e) {

            }

            String input = "";
            keyboard.nextLine(); // get rid of garbage data

            switch (choice) {

                case 1:
                    System.out.print("Enter price: ");
                    input = keyboard.nextLine();
                    try {
                        price = Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid input. Please enter a number (ex: 19.99).");
                    }
                    break;
                case 2:
                    System.out.print("Enter number of items: ");
                    input = keyboard.nextLine();
                    try {
                        numItems = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.err.println("Please enter a valid integer (ex: 15).");
                    }
                    break;
                case 3:
                    System.out.print("Enter number of days to sell within: ");
                    input = keyboard.nextLine();
                    try {
                        sellWithinNumDays = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.err.println("Please enter a valid integer (ex: 15).");
                    }
                    break;

                case 4:
                    System.out.print("Enter low inventory threshold: ");
                    input = keyboard.nextLine();
                    try {
                        lowInventoryThreshold = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.err.println("Please enter a valid integer (ex: 15).");
                    }
                    break;

                case 5:
                    System.out.print("Enter promotion percent off: ");
                    input = keyboard.nextLine();
                    try {
                        promotionPercentOff = Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        System.err.println("Please enter a valid number (ex: 0.15)");
                    }
                    break;
                case 6:
                    isDone = true;
                    break;
                default:
                    // invalid input
                    System.err.println("\nInvalid choice.");
                    break;
            }
        }

        // NOTE: not the best solution, as this values list has to be in the same order
        // as the list that the controller returns back
        List<String> itemValues = List.of(
                itemName,
                description,
                "0", // NOTE: we do not know the category ID yet
                Double.toString(price),
                Integer.toString(numItems),
                formattedDate,
                formattedDate,
                Integer.toString(sellWithinNumDays),
                Integer.toString(lowInventoryThreshold),
                Double.toString(promotionPercentOff));
        List<String> itemKeys = controller.getItemKeysNoIdNoSku();

        if (itemValues.size() != itemKeys.size()) {
            System.err.println("ERROR: item values and keys are not the same size");
            return;
        }

        Map<String, String> itemData = new HashMap<>();
        for (int i = 0; i < itemValues.size(); i++) {
            itemData.put(itemKeys.get(i), itemValues.get(i));
        }

        Map<String, String> innerCategory = new HashMap<>();
        List<String> categoryValues = List.of(categoryGiven);
        List<String> categoryKeys = controller.getCategoryKeysNoId();

        if (categoryValues.size() != categoryKeys.size()) {
            System.err.println("ERROR: category values and keys are not the same size");
            return;
        }

        for (int i = 0; i < categoryValues.size(); i++) {
            innerCategory.put(categoryKeys.get(i), categoryValues.get(i));
        }

        // driver will talk with controller, controller will ask objectService to create
        // object from hashmap, pass created object to the storageCrud to create
        // whatever object it is.

        Pair<Boolean, String> result = controller.createItem(itemData, innerCategory);
        boolean success = result.getFirst();

        if (success) {
            // created item successfully, print out item information.
            String sku = result.getSecond();
            System.out.println("Item created successfully-\nSku: " + sku + "\nName: " + itemName + "\nDescription: "
                    + description + "\nDate: " + formattedDate);
        } else {
            // failed to create item, output failure
            System.out.println("Failed to create item");
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
            System.err.println("Could not read input");
            keyboard.nextLine();
        }

        // validate that the item ID is a valid integer
        if (controller.validateStringToInt(itemId)) {
            int itemIdInt = Integer.parseInt(itemId);
            boolean success = controller.deleteItem(itemIdInt);
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
        System.err.println("Generate Report is not implemented yet");
        // TODO: Implement generateReport functionality
    }

    /**
     * Import data from CSV
     * 
     * @param keyboard User input scanner.
     */
    private static void importFromCSV(Scanner keyboard) {
        System.err.println("Import from csv is not implemented yet");
        // TODO: Implement generateReport functionality
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
        if (!controller.validateStringToInt(categoryIdStr)) {
            System.err.println("\nInvalid category ID, enter a non-negative integer.");
            return;
        }

        int categoryId = Integer.parseInt(categoryIdStr);
        System.out.println("Enter new values for the fields (leave blank to keep current value):");
        List<String> updatedCategoryData = new ArrayList<>();
        List<String> updatedCategoryKeys = new ArrayList<>();             
        List<String> categoryKeys = controller.getCategoryKeysNoId();
        updatedCategoryData.add(Integer.toString(categoryId));
        updatedCategoryKeys.add(controller.getCategoryIdKey());
        
        for (String key : categoryKeys) {
            System.out.print("Enter value for the Category \"" + key + "\" field > ");
            boolean isValid = false;
            while (!isValid) {
                String inputField = keyboard.nextLine().trim();

                // If the user provides input, validate and add it to the map
                if (!inputField.isEmpty()) {
                    if (controller.validateString(inputField)) {
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

        boolean success = controller.updateCategory(updatedCategoryData, updatedCategoryKeys);
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
        System.err.println("Update Item is not implemented yet");
        // TODO: Implement updateItem functionality
    }

    /**
     * Performs a one-time setup for running the program.
     */
    private static void setup() {
        // TODO: we will eventually want to create the controller with some data (for
        // the database)
        controller = new Controller();
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

        // perform one-time setup
        setup();

        System.out.println("Welcome to WareHelper!");

        Scanner keyboard = new Scanner(System.in);

        boolean continueProgram = true;

        String options[] = {
                "Retrieve Inventory",
                "Create Category",
                "Retrieve Category",
                "Delete Category",
                "Create Item",
                "Create Bundle",
                "Retrieve Bundles",
                "Delete Item",
                "Search Item by Name",
                "Search Item by SKU",
                "Generate Report",
                "Import From CSV",
                "Update Category",
                "Update Item",
                "Delete Bundle",
                "Exit", // THIS SHOULD ALWAYS BE LAST
        };

        while (continueProgram) {
            promptUser(options);

            int choice = 0;
            try {
                choice = keyboard.nextInt();
            } catch (Exception e) {
                // get rid of garbage data
                keyboard.nextLine();
            }

            // get rid of garbage data
            keyboard.nextLine();

            switch (choice) {
                case 1:
                    // retrieve inventory
                    retrieveInventory(keyboard);
                    break;
                case 2:
                    createCategory(keyboard);
                    break;
                case 3:
                    retrieveAllCategories(keyboard);
                    break;
                case 4:
                    deleteCategory(keyboard);
                    break;
                case 5:
                    createNewItem(keyboard);
                    break;
                case 6:
                    createBundle(keyboard);
                    break;
                case 7:
                    retrieveAllBundles(keyboard);
                    break;
                case 8:
                    deleteItem(keyboard);
                    break;
                case 9:
                    searchByName(keyboard);
                    break;
                case 10:
                    searchBySku(keyboard);
                    break;
                case 11:
                    generateReport(keyboard);
                    break;
                case 12:
                    importFromCSV(keyboard);
                    break;
                case 13:
                    updateCategory(keyboard);
                    break;
                case 14:
                    updateItem(keyboard);
                    break;
                case 15:
                    deleteBundle(keyboard);
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
