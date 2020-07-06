package movieland.constants.entities;

import java.math.BigDecimal;

public class ProjectionConstants {

    // Fields name
    public static final String STARTING_DATE_FILED = "startingTime";
    public static final String MOVIE_FILED = "movieId";
    public static final String CINEMA_FIELD = "cinemaId";
    public static final String HALL_FIELD = "hallId";

    // Validation constants
    public static final String START_DATE_NOT_NULL = "Start date should not be null.";
    public static final String MOVIE_NOT_NULL = "Movie should not be null.";
    public static final String CINEMA_NOT_NULL = "Cinema should not be null.";
    public static final String HALL_NOT_NULL = "Hall should not be null.";

    public static final String MOVIE_DOES_NOT_EXIST = "Movie does not exist.";
    public static final String CINEMA_DOES_NOT_EXIST = "Cinema does not exist.";
    public static final String HALL_DOES_NOT_EXIST = "Hall does not exist.";

    public static final String STARTING_TIME_NOT_IN_CINEMA_WORKING_HOURS = "Starting time should be in range of cinema's working hours.";
    public static final String HALL_NOT_AVAILABLE_DURING_GIVEN_TIME = "The hall is not available during the given time.";
    public static final String STARTING_DATE_CAN_BE_IN_PAST = "The starting date should be after %s";
    public static final String NO_AVAILABLE_PROGRAMME_AT_THIS_TIME = "There is no available programme at the given time.";
    public static final String PROJECTION_WITH_THIS_MOVIE_EXISTS_AT_THIS_TIME = "There is such projection in the cinema at the given time.";

    // Constraints constants
    public static final int MAX_MINUTES_DIFFERENCE = 30;

    public static final BigDecimal START_PRICE = BigDecimal.ONE;

    // Error handling constants
    public static final String PROJECTION_MODEL_NOT_VALID = "Projection model is not valid";
    public static final String PROJECTION_NOT_FOUND = "Projection was not found";
    public static final String PROJECTION_SHOULD_START_IN_CINEMA_WORKING_HOURS_RANGE = "Projection should start in cinema's working hours range";
    public static final String HALL_IS_NOT_FREE_IN_THE_GIVEN_PERIOD = "The hall is not free during the given period of time, try another hours... ;(";
    public static final String SUCH_MOVIE_PROJECTION_ALREADY_EXISTS = "In the given cinema there is already such movie projection at the given time";
    public static final String MOVIE_AND_HALL_ARE_NOT_UPDATABLE = "The movie and hall can not be updated";
}
