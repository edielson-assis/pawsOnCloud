package br.com.pawsoncloud.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Classe de configuração do Swagger.
 * 
 * @author Edielson Assis
 */
@Configuration
public class SpringDocConfiguration {

    /** 
     * Bean do tipo OpenAPI. Contém os dados da documentação gerada pelo Swagger.
     * 
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("PawsOnCloud API")
                        .version("0.0.1")
                        .description("API Rest da aplicação PawsOnCloud. A API permite aos usuários doar ou adotar animais de estimação.")
                        .contact(new Contact()
                                .name("Documentação")
                                .url("https://edielson-assis.github.io/documentacao-pawsoncloud/"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://github.com/edielson-assis/pawsOnCloud/blob/main/LICENSE")));
    }
}