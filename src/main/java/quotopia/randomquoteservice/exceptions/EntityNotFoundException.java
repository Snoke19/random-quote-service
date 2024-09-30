package quotopia.randomquoteservice.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
