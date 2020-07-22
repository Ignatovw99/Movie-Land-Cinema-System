package movieland.errors.invalid;

public class SeatNotFreeException extends RuntimeException {

    public SeatNotFreeException(String message) {
        super(message);
    }
}
