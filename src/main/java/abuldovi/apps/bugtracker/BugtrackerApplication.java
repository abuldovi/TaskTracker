package abuldovi.apps.bugtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class BugtrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(BugtrackerApplication.class, args);
	}
}
