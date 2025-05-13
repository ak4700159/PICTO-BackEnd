package picto.com.generator.global.utils;


public interface JwtUtil {
    String createToken();
    void verifyToken(String givenToken);
}
