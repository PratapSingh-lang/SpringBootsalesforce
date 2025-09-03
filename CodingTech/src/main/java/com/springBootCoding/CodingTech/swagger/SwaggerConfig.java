package com.springBootCoding.CodingTech.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springBootCoding.CodingTech.constants.VariableConstants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
			        .apiInfo(apiInfo())
			        .securityContexts(securityContexts())
			        .securitySchemes(Collections.singletonList(apiKey()))
			        .select()
			        .apis(RequestHandlerSelectors.any())
			        .paths(PathSelectors.any())
			        .build();
    }
    
    private List<SecurityContext> securityContexts() {
		return Arrays.asList(SecurityContext.builder().securityReferences(sf()).build());
	}
    
    private List<SecurityReference> sf() {

		AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");

		return Arrays.asList(new SecurityReference(VariableConstants.BEARER, new AuthorizationScope[] { scope }));
	}
    
    private ApiKey apiKey() {
        return new ApiKey(VariableConstants.BEARER, VariableConstants.AUTHORIZATION, VariableConstants.HEADER);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        		.title("Corptec Technology - Java/Spring Boot Code Challenge")
        		.description("API endpoints with JWT token based Login mechanism."
        				+ "\n\n Created By : [BHANU PRATAP](https://www.linkedin.com/in/bhanu-pratap-584539190/)"
        				+ "\n\n Share your reviews at : bhanupratap07899@gmail.com")
        		.version("1.0")
        	    .contact("Developer - BHANU PRATAP, email : bhanupratap07899@gmail.com , domain : https://www.linkedin.com/in/bhanu-pratap-584539190/")
        	    .license("Apache 2.0")
        	    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
        	    .termsOfServiceUrl("http://www.example.com/terms-of-service")
        	    .build();
    }
    
    /*
     * *
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API CREATED BY BHANU")
                        .description("API for USER MANAGEMENT Spring Boot application with JWT token based Login mechanism")
                        .version("1.0")
                        .setContact(new Contact()
                                .name("Developer - BHANU PRATAP")
                                .email("bhanupratap07899@gmail.com")
                                .url("https://www.linkedin.com/in/bhanu-pratap-584539190/"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .termsOfService("http://www.example.com/terms-of-service")
                );
    }
    *
    */
    
    /*
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API CREATED BY BHANU",
                "API for USER MANAGEMENT Spring Boot application with JWT token based Login mechanism",
                "1.0",
                "Terms of service",
                new Contact(
                        "Developer - BHANU PRATAP",
                        "https://www.linkedin.com/in/bhanu-pratap-584539190/",
                        "bhanupratap07899@gmail.com"
                ),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html",
                null
        );
    }
    */
}
