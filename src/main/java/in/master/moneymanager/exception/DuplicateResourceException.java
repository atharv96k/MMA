package in.master.moneymanager.exception;

/**
 * Thrown when attempting to create a resource that violates a uniqueness
 * constraint (e.g. a category name that already exists for this profile).
 * Mapped to HTTP 409 (Conflict) by GlobalExceptionHandler.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
