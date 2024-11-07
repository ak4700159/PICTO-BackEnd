package picto.com.sessionscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class SessionSchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SessionSchedulerApplication.class, args);
	}

}
