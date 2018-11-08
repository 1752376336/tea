package com.tea.framework.document;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(Swagger2Properties.class)
@Import({Swagger2DocumentationConfiguration.class})
@ConditionalOnProperty(name = "swagger.enabled")
public class Swagger2Config {

    @Autowired
    private Swagger2Properties properties;

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> basePackage = RequestHandlerSelectors.any();
        if (!StringUtils.isEmpty(properties.getBasePackage())) {
            basePackage = RequestHandlerSelectors.basePackage(properties.getBasePackage());
        }
        if (properties.getBasePath().isEmpty()) {
            properties.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList();
        for (String path : properties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }
        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList();
        for (String path : properties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage)
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .contact(new Contact(
                        properties.getContact().getName(),
                        properties.getContact().getUrl(),
                        properties.getContact().getEmail()))
                .version(properties.getVersion())
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        //all default
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(true)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }
    //https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#swaggerdefinition
    //https://github.com/swagger-api/swagger-core/wiki/Annotations
    //https://springfox.github.io/springfox/docs/current/#springfox-samples
    //http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
}
