package picto.com.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;


//Failed to determine a suitable R2DBC Connection URL 에러 방지
@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class GeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }
}
