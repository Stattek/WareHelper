import java.sql.*;
import java.util.Scanner;

import database.items.Category;

/**
 * Driver class for running WareHelper.
 */
public class Driver {

    private static final boolean isUsingLocalDatabase = true;

    private static void retrieveInventory(Controller controller) {

    }

    public static void main(String[] args) {
        Controller controller = new Controller();

        System.out.println("Welcome to WareHelper!");

        Scanner keyboard = new Scanner(System.in);

        boolean continueProgram = true;

        while (continueProgram) {
            System.out.println("\n\nChoose an option:");

            String options[] = {
                    "Retrieve Inventory",
                    "Exit", // THIS SHOULD ALWAYS BE LAST
            };

            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }

            System.out.print("Select an option > ");
            int choice = keyboard.nextInt();

            switch (choice) {
                case 1:
                    // retrieve inventory
                    retrieveInventory(controller);
                    break;
                case 2: // SHOULD ALWAYS BE THE LAST CHOICE
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
