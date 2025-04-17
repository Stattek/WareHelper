public class InputValidator {
    private static final String ACCEPT_PATTERN = "^[a-zA-Z0-9 ]*$";

    public static boolean validateString(String input) {
        if (!input.matches(ACCEPT_PATTERN)) {
            return false; // fail
        }

        return true; // success
    }
}
