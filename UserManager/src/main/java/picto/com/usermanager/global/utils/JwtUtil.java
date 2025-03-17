package picto.com.usermanager.global.utils;


public interface JwtUtil {
    String createToken();
    void verifyToken(String givenToken) throws Exception;
}
