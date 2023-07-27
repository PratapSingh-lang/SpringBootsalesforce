package com.springBootCoding.CodingTech.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Contact;
import io.swagger.models.auth.In;
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
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer";
	private static final String HEADER = "header";
	
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

		return Arrays.asList(new SecurityReference(BEARER, new AuthorizationScope[] { scope }));
	}
    
    private ApiKey apiKey() {
        return new ApiKey(BEARER, AUTHORIZATION, HEADER);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        		.title("API CREATED BY BHANU")
        	    .description("API for USER MANAGEMENT Spring Boot application")
        	    .version("1.0")
        	    .contact("Developer - BHANU PRATAP, email : bhanu.pratap@subex.com , domain : www.bel.com")
        	    .license("Apache 2.0")
        	    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
        	    .termsOfServiceUrl("http://www.example.com/terms-of-service")
        	    .build();
    }
}
