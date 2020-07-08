package movieland.constants.entities;

public class ProgrammeConstants {

    // Fields name
    public static final String START_DATE_FILED = "startDate";
    public static final String END_DATE_FILED = "endDate";
    public static final String CINEMA_ID_FIELD = "cinemaId";

    // Validation constants
    public static final String START_DATE_NOT_NULL = "Start date should not be null.";
    public static final String START_DATE_NOT_VALID = "Start date should be %s.";

    public static final String END_DATE_NOT_NULL = "End date should not be null.";
    public static final String END_DATE_AFTER_START_DATE = "End date should after start date.";
    public static final String PROGRAMME_LENGTH_NOT_VALID = "Programme should be between one and two weeks long.";

    public static final String CINEMA_NOT_NULL = "Cinema should not be null.";

    // Constraints constants
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    // Error handling constants
    public static final String PROGRAMME_MODEL_NOT_VALID = "Programme model is not valid";
    public static final String START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME = "Start date should be tomorrow if there is not any active programmes for the given cinema";
    public static final String START_DATE_SHOULD_BE_THE_NEXT_DAY_AFTER_CURRENT_ACTIVE_PROGRAMME = "Start date should be the next day after the current active programme";
    public static final String CINEMA_DOES_NOT_HAVE_PROGRAMMES = "The cinema does mot have any programmes";
    public static final String CINEMA_DOES_NOT_HAVE_ACTIVE_PROGRAMMES = "The cinema does not have an active programme";
    public static final String PROGRAMME_NOT_FOUND = "Programme was not found.";
}
