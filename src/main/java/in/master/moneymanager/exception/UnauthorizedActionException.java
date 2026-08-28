package in.master.moneymanager.exception;

/**
 * Thrown when the current user attempts to act on a resource that belongs
 * to a different profile (e.g. deleting someone else's income/expense).
 * Mapped to HTTP 403 by GlobalExceptionHandler.
 */
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
