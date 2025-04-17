import java.util.List;
import java.util.Scanner;

import database.items.Item;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    private static Controller controller; // controller to communicate with

    /**
     * Retrieves the entire inventory.
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
                case 5: // EXITING SHOULD ALWAYS BE THE LAST CHOICE
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
