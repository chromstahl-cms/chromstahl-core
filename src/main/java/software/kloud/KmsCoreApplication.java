package software.kloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@ComponentScan("software.kloud")
public class KmsCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmsCoreApplication.class, args);
    }
}