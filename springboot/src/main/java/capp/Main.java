package capp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
            logger.info("Applicazione Spring Boot avviata con successo.");
        } catch (Exception e) {
            logger.info("Errore critico durante l'avvio dell'applicazione.");
        }
    }
}