package movieland.constants.entities;

public class ProgrammeConstants {

    // Fields name
    public static final String TITLE_FIELD = "title";

    // Validation constants
    public static final String START_DATE_NOT_NULL = "Start date should not be null.";

    public static final String END_DATE_NOT_NULL = "End date should not be null.";

    public static final String CINEMA_NOT_NULL = "Cinema should not be null.";

    // Constraints constants
    public static final int TITLE_MIN_LENGTH = 5;

    // Error handling constants
    public static final String PROGRAMME_MODEL_NOT_VALID = "Programme model is not valid";
    public static final String START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME = "Start date should be tomorrow if there is not any active programmes for the given cinema";
    public static final String START_DATE_SHOULD_BE_THE_NEXT_DAY_AFTER_CURRENT_ACTIVE_PROGRAMME = "Start date should be the next day after the current active programme";
}
