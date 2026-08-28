package in.master.moneymanager.exception;

/**
 * Thrown when a requested entity (Income, Expense, Category, Profile, etc.)
 * cannot be found. Mapped to HTTP 404 by GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
