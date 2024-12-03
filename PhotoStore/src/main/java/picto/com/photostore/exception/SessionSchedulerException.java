package picto.com.photostore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SessionSchedulerException extends RuntimeException {
    public SessionSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}