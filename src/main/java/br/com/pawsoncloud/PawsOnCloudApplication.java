package br.com.pawsoncloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe de inicialização da aplicação Spring Boot.
 * 
 * @author Edielson Assis
 */
@SpringBootApplication
public class PawsOnCloudApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot.
     * 
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        SpringApplication.run(PawsOnCloudApplication.class, args);
    }
}