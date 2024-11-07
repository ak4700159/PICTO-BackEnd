package picto.com.photomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// 데이터베이스 autoConfiguration 제외
@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class PhotoManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoManagerApplication.class, args);
    }
}
