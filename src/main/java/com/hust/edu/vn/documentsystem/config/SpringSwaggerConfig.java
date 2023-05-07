package com.hust.edu.vn.documentsystem.config;

import autovalue.shaded.com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringSwaggerConfig {
    @Bean
    public OpenAPI baseOpenAPI(){
        Server serverForDevelop = new Server().url("http://localhost:8080");
        Server serverForProduction = new Server().url("https://hust-document-system.as.r.appspot.com");

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        SecurityScheme basic = new SecurityScheme()
                .name("basicAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");
        return new OpenAPI()
                .servers(Lists.newArrayList(
                        serverForDevelop,
                        serverForProduction
                ))
                .info(
                        new Info()
                                .title("HUST document system")
                                .description("HUST document system API DOC")
                                .version("1.0.0")
                                .contact(new Contact()
                                        .name("Nguyễn Ngô Cao Cường")
                                        .email("cuong.nnc184055@sis.hust.edu.vn")
                                        .url("https://github.com/nguyengocaocuong/hust-document-system"))
                                .license(new License()
                                        .name("Apache 2.0")
                                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                                .version("1.0.0.")
                )
                .components(new Components()
                        .addSecuritySchemes("Authorization", securityScheme)
                        .addSecuritySchemes("basicAuth", basic)
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}
