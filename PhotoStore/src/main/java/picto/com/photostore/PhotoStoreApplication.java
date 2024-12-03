package picto.com.photostore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude={R2dbcAutoConfiguration.class})
@EnableJpaAuditing
public class PhotoStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoStoreApplication.class, args);
    }

}