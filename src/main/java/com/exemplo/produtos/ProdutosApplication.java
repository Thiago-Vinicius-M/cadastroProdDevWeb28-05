package com.exemplo.produtos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 *
 * @SpringBootApplication combina três anotações:
 *   - @Configuration: esta classe é uma fonte de configuração Spring
 *   - @EnableAutoConfiguration: configura o Spring Boot automaticamente
 *     com base nas dependências presentes no classpath
 *   - @ComponentScan: varre o pacote atual e subpacotes procurando
 *     componentes (@Controller, @Service, @Repository, etc.)
 */
@SpringBootApplication
public class ProdutosApplication {

    public static void main(String[] args) {
        // Inicia o servidor Tomcat embutido e sobe toda a aplicação Spring
        SpringApplication.run(ProdutosApplication.class, args);
    }
}
