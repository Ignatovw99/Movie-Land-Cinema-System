package movieland.constants.entities;

public class ProjectionConstants {

    // Fields name
    public static final String START_DATE_FILED = "startDate";

    // Validation constants
    public static final String START_DATE_NOT_NULL = "Start date should not be null.";

    // Constraints constants
    public static final int MAX_MINUTES_DIFFERENCE = 30;

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    // Error handling constants
    public static final String PROJECTION_MODEL_NOT_VALID = "Projection model is not valid";
    public static final String PROJECTION_SHOULD_START_IN_CINEMA_WORKING_HOURS_RANGE = "Projection should start in cinema's working hours range";
    public static final String HALL_IS_NOT_FREE_IN_THE_GIVEN_PERIOD = "The hall is not free during the given period of time, try another hours... ;(";
    public static final String SUCH_MOVIE_PROJECTION_ALREADY_EXISTS = "In the given cinema there is already such movie projection at the given time";
}
