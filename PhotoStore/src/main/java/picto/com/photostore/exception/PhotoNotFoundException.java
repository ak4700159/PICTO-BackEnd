package picto.com.photostore.exception;

public class PhotoNotFoundException extends CustomException {
    public PhotoNotFoundException(String message) {
        super(message);
    }
}