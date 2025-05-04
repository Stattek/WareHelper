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
    public static boolean validateStringToInt(String input) {
        try {
            Integer.parseInt(input); // attempt to parse the input
            return true; // success, return true
        } catch (NumberFormatException e) {
            return false; // fail, return false
        }
    }

    /**
     * Validates an ID to be valid.
     * 
     * @param input The input ID.
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
}
