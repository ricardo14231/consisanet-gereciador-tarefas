package com.consisanet.gerenciamento_tarefa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.consisanet.gerenciamento_tarefa"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                "Sistema para gerenciamento de tarefas - teste técnico Consisanet.",
                "API para gerenciamento de tarefas e usuários.",
                "0.0.1",
                null,
                new Contact("Ricardo Farias", "https://www.linkedin.com/in/ricardo14231/",
                        "ricardo14231@hotmail.com"),
                "License MIT",
                "https://opensource.org/licenses/MIT",
                new ArrayList<>()
        );
    }
}
