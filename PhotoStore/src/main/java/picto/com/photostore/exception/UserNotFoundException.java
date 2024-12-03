package picto.com.photostore.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message);
    }
}