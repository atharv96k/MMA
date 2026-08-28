package in.master.moneymanager.exception;

/**
 * Thrown when authentication fails due to an incorrect email or password.
 * Mapped to HTTP 401 (Unauthorized) by GlobalExceptionHandler.
 *
 * Deliberately uses a generic message ("Invalid email or password") rather
 * than distinguishing "email not found" vs "wrong password" - this avoids
 * leaking which emails are registered to an attacker probing the login endpoint.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
