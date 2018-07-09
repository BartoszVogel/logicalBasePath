package pl.ibart.springboot.logicalBasePath.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ibart.springboot.logicalBasePath.LogicalBasePathApplication;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(and(
                        not(withClassAnnotation(Skip.class)),
                        basePackage(LogicalBasePathApplication.class.getPackage().getName())
                ))
                .build();
    }

    @Controller
    @Skip
    @Conditional(ExistLogicalPathCondition.class)
    static class SwaggerForwarder {

        private static final String V2_API_PATH = "/v2/api-docs";

        @RequestMapping("${logical.server.context-path:}" + V2_API_PATH)
        public String forward() {
            return "forward:" + V2_API_PATH;
        }
    }
}
