package picto.com.foldermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "picto.com.foldermanager.repository")
@EntityScan(basePackages = "picto.com.foldermanager.domain")
public class FolderManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FolderManagerApplication.class, args);
	}

}
