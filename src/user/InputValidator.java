package user;

public class InputValidator {
    private static final String ACCEPT_PATTERN = "^[a-zA-Z0-9. ]*$";

    public static boolean validateString(String input) {
        if (!input.matches(ACCEPT_PATTERN)) {
            return false; // fail
        }

        return true; // success
    }

    public static boolean validateStringToInt(String input) {
        try {
            Integer.parseInt(input); // attempt to parse the input
            return true; // success, return true
        } catch (NumberFormatException e) {
            return false; // fail, return false
        }
    }

    public static boolean validateSKU(String input) {
        String skuPattern = "^[a-zA-Z]+\\d+$"; // one or more letters followed by one or more digits
        return input.matches(skuPattern);
    }
}
