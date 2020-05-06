package movieland.constants.entities;

public class HallConstants {

    // Fields name
    public static final String NAME_FIELD = "name";
    public static final String ROWS_FIELD = "rows";
    public static final String COLUMNS_FIELD = "columns";
    public static final String FILM_TECHNOLOGY_FIELD = "filmTechnology";
    public static final String SOUND_SYSTEM_FIELD = "soundSystem";

    // Validation constants
    public static final String NAME_NOT_EMPTY = "Name should not be empty";
    public static final String NAME_CHARACTERS_LENGTH = "Name should contain at least 4 characters";
    public static final String ALREADY_EXISTS_WITH_SUCH_NAME = "Cinema with such name already exists";

    public static final String ROWS_NOT_NULL = "Rows should not be null";

    public static final String COLUMNS_NOT_NULL = "Columns should not be null";

    public static final String FILM_TECHNOLOGY_NOT_NULL = "Film technology should not be null";

    public static final String SOUND_SYSTEM_NOT_NULL = "Sound system should not be null";

    public static final String CINEMA_NOT_NULL = "Cinema should not be null";

    // Constraints constants
    public static final int NAME_LENGTH_MIN_VALUE = 4;

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]{2,4}$";

    public static final String PHONE_NUMBER_PATTERN = "^(\\+)*[0-9]{11}$";

    public static final String TIME_12_HOUR_FORMAT_PATTERN = "hh:mm a";

    public static final String TIME_24_HOUR_FORMAT_PATTERN = "HH:mm";

    // Invalid constants
    public static final String INVALID_HALL_MODEL = "Invalid Hall Model";
    public static final String CINEMA_WITH_SUCH_NAME_EXISTS = "A cinema with such name already exists";
    public static final String CINEMA_NOT_FOUND = "Cinema was not found";

    // Error handling constants
    public static final String HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA = "Hall with such name already exists in the given cinema";
}
