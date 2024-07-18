package com.task.mgmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  @Bean
  public OperationCustomizer customOperationCustomizer() {
    return (operation, handlerMethod) -> {
      // Customize the operation as needed
      Parameter headers =
          new Parameter()
              .in("header")
              .name("Authorization")
              .schema(new StringSchema().name("Authorization")._default("Bearer {{token}}"))
              .description("authorization header")
              .required(false);

      operation.addParametersItem(headers);
      return operation;
    };
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
               .info(new Info().title("Task Management").version("1.0"))
               .addServersItem(new Server().url("http://localhost:8080"));
  }
}
