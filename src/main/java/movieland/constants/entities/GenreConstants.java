package movieland.constants.entities;

public class GenreConstants {

    // Validation constants
    public static final String NAME_FIELD = "name";
    public static final String IS_AGE_RESTRICTION_REQUIRED_FIELD = "isAgeRestrictionRequired";
    public static final String CLASSIFICATION_FIELD = "classification";

    public static final String NAME_NOT_EMPTY = "Name should not be empty";
    public static final String NAME_CHARACTERS_LENGTH = "Name should contain between 3 and 25 characters";
    public static final String ALREADY_EXISTS_WITH_SUCH_NAME = "Genre with such name already exists";

    public static final String IS_AGE_RESTRICTION_REQUIRED_NOT_NULL = "Age restriction requirement should not be null";

    public static final String CLASSIFICATION_NOT_NULL = "Classification should not be null";

    // Constraints constants
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 25;

    // Invalid constants
    public static final String INVALID_GENRE_MODEL = "Invalid Genre Model";
    public static final String GENRE_WITH_SUCH_NAME_EXISTS = "A genre with such name already exists";
    public static final String GENRE_WITH_SUCH_ID_DOES_NOT_EXIST = "Genre with such id does not exist";

    // Error handling constants
    public static final String GENRE_ALREADY_EXISTS = "Genre already exists";
    public static final String GENRE_NOT_FOUND = "Genre was not found";

}
