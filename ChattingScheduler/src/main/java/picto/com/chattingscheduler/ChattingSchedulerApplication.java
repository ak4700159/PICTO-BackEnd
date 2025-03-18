package picto.com.chattingscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = { R2dbcAutoConfiguration.class })
public class ChattingSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattingSchedulerApplication.class, args);
	}

}
