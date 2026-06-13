package bcc.group.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bccOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Building Creators And Consulting API")
                        .description("REST API documentation for BCC backend services")
                        .version("1.0.0")
                        .license(new License()
                                .name("BCC Private")
                                .url("https://bcc.net.in")));
    }
}
