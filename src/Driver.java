import java.util.List;
import java.util.Scanner;

import database.items.Item;
import com.google.gson.*;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
                        List<Item> items = controller.readItemByName(name);
                        System.out.println(gson.toJson(items));
                        continueChoice = false;
                    } else {
                        System.err.println("\nInvalid name, enter only letters, numbers, and spaces");
                    }
                    break;
                case 2:
                    // read all items
                    List<Item> items = controller.readAllItems();
                    System.out.println(gson.toJson(items));
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
                case 2: // EXITING SHOULD ALWAYS BE THE LAST CHOICE
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
