import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

// CREATEITEM TODO-
// Driver- implement function for user.
// Storage- The Storage interface should have no knowledge of the objects we are reading. Use the read() method instead.
// StorageCrud- that is fine.

// Me- Search Item SKU, Set Item Preferences, Create Item, Delete Item, Update Item

import database.items.Item;

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
            System.out.println("Do you want to perform a search by name?");
            String options[] = { "Yes", "No" };
            promptUser(options);

            int choice = 0;
            try {
                choice = keyboard.nextInt();
            } catch (Exception e) {
                // get rid of garbage data
                keyboard.nextLine();
            }

            switch (choice) {
                case 1:
                    // TODO: perform sort by name
                    System.out.print("Enter name to search by > ");
                    String name = "";
                    try {
                        // get rid of garbage data from last read
                        keyboard.nextLine();

                        name = keyboard.nextLine().trim();
                    } catch (Exception e) {
                        System.err.println("ERROR: Could not read user input");
                    }

                    // check that the name is valid
                    if (InputValidator.validateString(name)) {
                        System.out.println("name: " + name);
                        System.out.println(controller.readItemByName(name));
                        continueChoice = false;
                    } else {
                        System.err.println("\nInvalid name, enter only letters, numbers, and spaces");
                    }
                    break;
                case 2:
                    // read all items
                    System.out.println(controller.readAllItems());
                    continueChoice = false;
                    break;
                default:
                    // bad input
                    System.out.println("\nInvalid choice.");
                    break;
            }
        }
    }

    /**
     * Creates a new category.
     * 
     * @param keyboard User input scanner.
     */
    private static void createCategory(Scanner keyboard) {
        System.out.print("Enter the name of the new category > ");
        String categoryName = "";
        try {

            keyboard.nextLine();

            categoryName = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
        }

        // check that the category name is valid
        if (InputValidator.validateString(categoryName)) {
            boolean success = controller.createCategory(categoryName);
            if (success) {
                System.out.println("Category '" + categoryName + "' created successfully.");
            } else {
                System.err.println("ERROR: Could not create category. It may already exist.");
            }
        } else {
            System.err.println("\nInvalid category name, enter only letters, numbers, and spaces");
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

        switch (choice) {
            case 1:
                System.out.print("Enter name to search by > ");
                String name = "";
                try {
                    keyboard.nextLine();
                    name = keyboard.nextLine().trim();
                } catch (Exception e) {
                    System.err.println("ERROR: Could not read user input");
                }

                if (InputValidator.validateString(name)) {
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
                System.out.println("\nInvalid choice.");
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
            keyboard.nextLine();
            categoryId = keyboard.nextLine().trim();
        } catch (Exception e) {
            System.err.println("ERROR: Could not read user input");
            keyboard.nextLine();
        }

        // validate that the category ID is a valid integer
        if (InputValidator.validateStringToInt(categoryId)) {
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

    private static void createNewItem(Scanner keyboard){

        System.out.println("Enter Category, Item Name, and Description separated by dashes. Example: A- Jeans- bought from walmart");

        String inputLine = keyboard.nextLine();
        String[] parts = inputLine.split("- ");


        if (parts.length != 3) {
            System.out.println("Input invalid. Returning to menu.");
            return;
        }

        String categoryGiven = parts[0].trim().toUpperCase();
        String itemName = parts[1].trim();
        String description = parts[2].trim();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        String formattedDate = currentDate.format(formatter);

        System.out.println("Add any further information you would like this item to have, or confirm.");

        double price = 0.0;
        int numItems = 0;
        int sellWithinNumDays = 0;
        int lowInventoryThreshold = 0;
        double promotionPercentOff = 0.0;

        String options[] = {
                "1- Add Price",
                "2- Add Number of Items",
                "3- Add Sell-By Date",
                "4- Add Low Inventory Warning Threshhold.",
                "5- Add Item Promotion",
                "0- Confirm"
            };
            
            

            int choice = -1;
            while (choice != 0) {
                // add promptUser function here to choose options.
                System.out.println("\n\nChoose an option:");
    
                for (int i = 0; i < options.length; i++) {
                    System.out.println((i + 1) + ". " + options[i]);
                }
    
                try {
                    choice = keyboard.nextInt();
                } catch (Exception e) {
                    // get rid of garbage data
                    keyboard.nextLine();
                }
                String input = "";
    
                switch (choice) {
                    case 1:
                        System.out.print("Enter price: ");
                        input = keyboard.nextLine();
                        try {
                            price = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number (ex: 19.99).");
                        }
                        break;
                    case 2:
                        System.out.print("Enter number of items: ");
                        input = keyboard.nextLine();
                        try {
                            numItems = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid integer (ex: 15).");
                        }
                        break;
                    case 3:
                        System.out.print("Enter number of days to sell within: ");
                        input = keyboard.nextLine();
                        try {
                            sellWithinNumDays = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid integer (ex: 15).");
                        }
                        break;
    
                    case 4:
                        System.out.print("Enter low inventory threshold: ");
                        input = keyboard.nextLine();
                        try {
                            lowInventoryThreshold = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid integer (ex: 15).");
                        }
                        break;
    
                    case 5:
                        System.out.print("Enter promotion percent off: ");
                        input = keyboard.nextLine();
                        try {
                            promotionPercentOff = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number (ex: 19.99)");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        // invalid input
                        System.out.println("\nInvalid choice.");
                        break;
                }
            }
    
    
            Map<String, String> itemData = new HashMap<>();
            itemData.put("name", itemName);
            itemData.put("description", description);
            itemData.put("category", categoryGiven);
    
            itemData.put("Created", formattedDate);
            itemData.put("LastModified", formattedDate);
            itemData.put("Price", Double.toString(price));
            itemData.put("NumItems", Integer.toString(numItems));
            itemData.put("SellWithinNumDays", Integer.toString(sellWithinNumDays));
            itemData.put("LowInventoryThreshold", Integer.toString(lowInventoryThreshold));
            itemData.put("PromotionPercentOff", Double.toString(promotionPercentOff));
    
            Map<String, String> innerCategory = new HashMap<>();
            innerCategory.put("Name", categoryGiven);
    
            //driver will talk with controller, controller will ask objectService to create object from hashmap, pass created object to the storageCrud to create whatever object it is.
    
            boolean success = controller.createItem(itemData, innerCategory);
    
            if (success) {
                // created item successfully, print out item information.
                System.out.println("Item created successfully- " + categoryGiven + " " + itemName + " " + description + " " + formattedDate); 
                // TODO: Retrieve SKU, output.
            } else {
                // failed to create item, output failure
                System.out.println("Failed to create item");
            }
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
                case 6: // EXITING SHOULD ALWAYS BE THE LAST CHOICE
                    // exit program
                    continueProgram = false;
                    break;
                default:
                    // invalid input
                    System.out.println("\nInvalid choice.");
                    break;
            }
        }

        keyboard.close();
    }
}