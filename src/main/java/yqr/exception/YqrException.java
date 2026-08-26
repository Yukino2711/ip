package yqr.exception;

/**
 * Represents an error caused by an invalid command entered in yqr.
 */
public class YqrException extends Exception {
    /**
     * Creates an exception containing an explanation for the user.
     *
     * @param message explanation of the invalid input.
     */
    public YqrException(String message) {
        super(message);
    }
}
