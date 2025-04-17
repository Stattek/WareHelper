public class InputValidator {
    private static final String ACCEPT_PATTERN = "^[a-zA-Z0-9]*$";

    public static String validateString(String input) throws IllegalArgumentException {
        if (!input.matches(ACCEPT_PATTERN)) {
            throw new IllegalArgumentException("Invalid input");
        }

        return input;
    }
}
