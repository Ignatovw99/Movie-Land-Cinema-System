package movieland.constants.entities;

public class GenreConstants {

    public static final String NAME_FIELD = "name";
    public static final String AGE_RESTRICTION_FIELD = "ageRestriction";
    public static final String CLASSIFICATION_FIELD = "classification";

    public static final String NAME_NOT_EMPTY = "Name should not be empty";
    public static final String NAME_CHARACTERS_LENGTH = "Name should contain between 3 and 25 characters";
    public static final String ALREADY_EXISTS_WITH_SUCH_NAME = "Genre with such name already exists";

    public static final String AGE_RESTRICTION_RANGE = "Age restriction should be between 6 and 21 years";

    public static final String CLASSIFICATION_NOT_NULL = "Classification should not be null";
}
