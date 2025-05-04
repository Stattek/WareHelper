package user;

/**
 * Class to handle input validation.
 */
public class InputValidator {
    private static final String STRING_ACCEPT_PATTERN = "^[a-zA-Z0-9. ]*$";
    private static final String SKU_ACCEPT_PATTERN = "^[a-zA-Z]+\\d+$"; // one or more letters followed by one or more
                                                                        // digits

    /**
     * Validates a string to be used.
     * 
     * @param input The input string.
     * @return True upon successful validation, false otherwise.
     */
    public static boolean validateString(String input) {
        return input.matches(STRING_ACCEPT_PATTERN);
    }

    /**
     * Validates a string to be an integer.
     * 
     * @param input The input string.
     * @return True upon successful validation, false otherwise.
     */
    public static boolean validateStringToId(String input) {
        try {
            int id = Integer.parseInt(input); // attempt to parse the input
            return validateId(id); // return whether it is valid
        } catch (NumberFormatException e) {
            return false; // fail, return false
        }
    }

    /**
     * Validates an ID to be valid.
     * 
     * @param id The input ID.
     * @return True upon successful validation, false otherwise.
     */
    public static boolean validateId(int id) {
        return (id > 0);
    }

    /**
     * Validates an item SKU
     * 
     * @param input The sku to validate
     * @return True upon successful validation, false otherwise.
     */
    public static boolean validateSKU(String input) {
        return input.matches(SKU_ACCEPT_PATTERN);
    }

    public static boolean validateIntOrDouble(String input) {
        // First, check if it's an integer
        try {
            Integer.parseInt(input); // Tries to parse the input as an integer
            return true; // It's a valid integer
        } catch (NumberFormatException e) {
            // If it fails, try to parse it as a double
            try {
                Double.parseDouble(input); // Tries to parse the input as a double
                return true; // It's a valid double
            } catch (NumberFormatException ex) {
                return false; // It's neither an integer nor a double
            }
        }
    }

}
