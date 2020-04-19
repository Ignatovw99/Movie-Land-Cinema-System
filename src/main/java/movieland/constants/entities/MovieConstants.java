package movieland.constants.entities;

public class MovieConstants {

    // Fields name
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String DIRECTOR_FIELD = "director";
    public static final String CAST_FIELD = "cast";
    public static final String RUNNING_TIME_FIELD = "runningTime";
    public static final String COUNTRY_FIELD = "country";
    public static final String YEAR_OF_PRODUCTION_FIELD = "yearOfProduction";
    public static final String RELEASE_DATE_FIELD = "releaseDate";
    public static final String BUDGET_FIELD = "budget";
    public static final String AGE_RESTRICTION_FIELD = "ageRestriction";
    public static final String GENRE_FIELD = "genre";
    public static final String GENRE_CREATE_BINDING_MODEL_FIELD = "genreId";

    // Validation constants
    public static final String TITLE_NOT_EMPTY = "Title should not be empty";
    public static final String TITLE_CHARACTERS_LENGTH = "Title should contain between 5 and 100 characters";

    public static final String DESCRIPTION_NOT_EMPTY = "Description should not be empty";
    public static final String DESCRIPTION_CHARACTERS_LENGTH = "Description should contain between 20 and 300 characters";

    public static final String DIRECTOR_NOT_NULL = "Director should not be null";
    public static final String DIRECTOR_CHARACTERS_LENGTH = "Director should contain between 3 and 100 characters";

    public static final String CAST_NOT_NULL = "Cast should not be null";

    public static final String RUNNING_TIME_NOT_NULL = "Running time should not be null";
    public static final String RUNNING_TIME_MIN_VALUE = "Running time should not be less than 30 minutes";
    public static final String RUNNING_TIME_MAX_VALUE = "Running time should not be greater than 270 minutes";
    public static final String RUNNING_TIME_RANGE = "Running time should be between 30 and 270 minutes";

    public static final String YEAR_OF_PRODUCTION_NOT_NULL = "Year of production should not be null";
    public static final String YEAR_OF_PRODUCTION_MAX_VALUE = "Year of production should not be greater than %d year";

    public static final String RELEASE_DATE_NOT_NULL = "Release date should not be null";
    public static final String RELEASE_DATE_INVALID_VALUE = "Release date should not be before %s";

    public static final String AGE_RESTRICTION_NOT_NULL = "Age restriction should not be null";
    public static final String AGE_RESTRICTION_MIN_VALUE = "Age restriction should not be less than 12";
    public static final String AGE_RESTRICTION_MAX_VALUE = "Age restriction should not be greater than 21";
    public static final String AGE_RESTRICTION_RANGE = "Age restriction should be between 12 and 21";

    public static final String GENRE_NOT_NULL = "Genre should not be null";

    // Constraints constants
    public static final int TITLE_MIN_LENGTH = 5;
    public static final int TITLE_MAX_LENGTH = 100;

    public static final int AGE_RESTRICTION_MIN = 12;
    public static final int AGE_RESTRICTION_MAX = 21;

    public static final int DESCRIPTION_MIN_LENGTH = 20;
    public static final int DESCRIPTION_MAX_LENGTH = 300;

    public static final int DIRECTOR_MIN_LENGTH = 3;
    public static final int DIRECTOR_MAX_LENGTH = 100;

    public static final int RUNNING_TIME_MIN = 30;
    public static final int RUNNING_TIME_MAX = 270;

    public static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd";

    // Invalid constants
    public static final String INVALID_MOVIE_MODEL = "Invalid Movie Model";

    // Error handling constants
    public static final String MOVIE_ALREADY_EXISTS = "Movie already exists";
    public static final String MOVIE_NOT_FOUND = "Movie was not found";
    public static final String MOVIE_WITH_SUCH_ID_NOT_FOUND = "Movie with such id was not found";
}
