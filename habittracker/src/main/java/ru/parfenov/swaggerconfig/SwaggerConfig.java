package ru.parfenov.swaggerconfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Habit Tracker Api")
                                .version("1.0-SNAPSHOT")
                                .contact(
                                        new Contact()
                                                .email("parfenov7233@gmail.com")
                                                .url("")
                                                .name("Parfenov Arcady")
                                )
                );
    }
}