package movieland.constants.entities;

public class CinemaConstants {

    // Fields name
    public static final String NAME_FIELD = "name";
    public static final String ADDRESS_FIELD = "address";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String EMAIL_FIELD = "email";
    public static final String OPENING_TIME_FIELD = "openingTime";
    public static final String CLOSING_TIME_FIELD = "closingTime";

    // Validation constants
    public static final String NAME_NOT_EMPTY = "Name should not be empty";
    public static final String NAME_CHARACTERS_LENGTH = "Name should contain at least 4 characters";
    public static final String ALREADY_EXISTS_WITH_SUCH_NAME = "Cinema with such name already exists";

    public static final String ADDRESS_NOT_NULL = "Address should not be null";

    public static final String PHONE_NUMBER_NOT_NULL = "Phone number should not be null";
    public static final String PHONE_NUMBER_INVALID_VALUE = "Phone number should contain only 11 digit. \"+\" in the beginning is optional";

    public static final String EMAIL_NOT_NULL = "Email should not be null";
    public static final String EMAIL_INVALID_VALUE = "Invalid email";

    public static final String OPENING_TIME_NOT_NULL = "Opening time should not be null";

    public static final String CLOSING_TIME_NOT_NULL = "Closing time should not be null";

    public static final String CLOSING_TIME_SHOULD_BE_AFTER_OPENING_TIME = "Closing time should be after %s";

    // Constraints constants
    public static final int NAME_LENGTH_MIN_VALUE = 4;

    public static final int PHONE_NUMBER_LENGTH = 11;

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]{2,4}$";

    public static final String PHONE_NUMBER_PATTERN = "^(\\+)*[0-9]{11}$";

    public static final String TIME_12_HOUR_FORMAT_PATTERN = "hh:mm a";

    public static final String TIME_24_HOUR_FORMAT_PATTERN = "HH:mm";

    // Invalid constants
    public static final String INVALID_CINEMA_MODEL = "Invalid Cinema Model";
    public static final String CINEMA_WITH_SUCH_NAME_EXISTS = "A cinema with such name already exists";
    public static final String CINEMA_NOT_FOUND = "Cinema was not found";

    // Error handling constants
    public static final String CINEMA_ALREADY_EXISTS = "Cinema already exists";
}
