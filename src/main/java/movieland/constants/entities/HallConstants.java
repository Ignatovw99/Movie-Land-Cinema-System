package movieland.constants.entities;

public class HallConstants {

    // Fields name
    public static final String NAME_FIELD = "name";
    public static final String ROWS_FIELD = "rows";
    public static final String COLUMNS_FIELD = "columns";
    public static final String FILM_TECHNOLOGY_FIELD = "filmTechnology";
    public static final String SOUND_SYSTEM_FIELD = "soundSystem";
    public static final String CINEMA_ID_FIELD = "cinemaId";

    // Validation constants
    public static final String NAME_NOT_EMPTY = "Name should not be empty";
    public static final String NAME_CHARACTERS_LENGTH = "Name should contain at least 4 characters";
    public static final String ALREADY_EXISTS_WITH_SUCH_NAME = "Hall with such name already exists in this cinema";

    public static final String ROWS_NOT_NULL = "Rows should not be null";
    public static final String ROWS_NOT_NEGATIVE_OR_ZERO = "Rows should be positive number";

    public static final String COLUMNS_NOT_NULL = "Columns should not be null";
    public static final String COLUMNS_NOT_NEGATIVE_OR_ZERO = "Columns should be positive number";

    public static final String FILM_TECHNOLOGY_NOT_NULL = "Film technology should not be null";

    public static final String SOUND_SYSTEM_NOT_NULL = "Sound system should not be null";

    public static final String CINEMA_NOT_NULL = "Cinema should not be null";

    // Constraints constants
    public static final int NAME_LENGTH_MIN_VALUE = 4;

    // Invalid constants
    public static final String INVALID_HALL_MODEL = "Invalid Hall Model";

    // Error handling constants
    public static final String HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA = "Hall with such name already exists in the given cinema";
    public static final String HALL_NOT_FOUND = "Hall was not found";
    public static final String HALL_CAN_NOT_CHANGE_ITS_CINEMA = "The cinema of the hall can not be changed";
}
