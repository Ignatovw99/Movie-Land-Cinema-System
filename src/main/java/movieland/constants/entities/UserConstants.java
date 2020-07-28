package movieland.constants.entities;

public class UserConstants {

    // Fields name
    public static final String EMAIL_FILED = "email";
    public static final String PASSWORD_FILED = "password";
    public static final String CONFIRM_PASSWORD_FIELD = "confirmPassword";

    // Validation constants
    public static final String USER_NOT_VALID = "User is not valid.";
    public static final String EMAIL_NOT_NULL = "Email should not be null.";
    public static final String EMAIL_NOT_VALID = "Email is not valid.";
    public static final String EMAIL_EXISTS = "Email already exists.";
    public static final String PASSWORD_NOT_NULL = "Password should not be null.";
    public static final String PASSWORD_INVALID_LENGTH = "Password should be at least %d symbols.";
    public static final String CONFIRM_PASS_NOT_VALID = "Passwords do not match.";
    public static final String USER_NOT_FOUND = "User not found.";

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]{2,4}$";
    public static final int PASSWORD_MIN_SIZE = 5;
}
