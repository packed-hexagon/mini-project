package com.group6.accommodation.global.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "여기죽이지 API 명세서",
                description = "MINI-6조",
                version = "v1")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                        .name(jwt)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat(jwt));

        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);

        // 로그인 엔드포인트 추가
        Operation loginOperation = new Operation()
                .summary("User Login")
                .description("Authenticate a user and receive JWT tokens")
                .requestBody(new RequestBody()
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>()
                                                .type("object")
                                                .properties(getLoginProperties())
                                        )
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("Successful login"))
                        .addApiResponse("400", new ApiResponse().description("Bad request"))
                );

        openAPI.path("/open-api/user/login", new PathItem().post(loginOperation));

        return openAPI;
    }

    private Map<String, Schema> getLoginProperties() {
        Map<String, Schema> properties = new HashMap<>();
        properties.put("email", new Schema<>().type("string"));
        properties.put("password", new Schema<>().type("string"));
        return properties;
    }
}