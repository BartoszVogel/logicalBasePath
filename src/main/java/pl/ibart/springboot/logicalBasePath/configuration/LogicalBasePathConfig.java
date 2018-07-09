package pl.ibart.springboot.logicalBasePath.configuration;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Configuration
@Conditional(ExistLogicalPathCondition.class)
public class LogicalBasePathConfig {

    @Value("${logical.server.context-path:}")
    private String logicalBasePath;

    @Value("#{'${logical.server.context-path.skip}'.split(',')}")
    private List<String> toSkipList;

    @Bean
    public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {

        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {

                    private final String BASE_PATH = logicalBasePath;
                    private final Set<String> toSkip = Sets.newHashSet(toSkipList);

                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                        if (hasAnnotation(method) && isNotSkipped(mapping)) {
                            PatternsRequestCondition apiPattern = new PatternsRequestCondition(BASE_PATH)
                                    .combine(mapping.getPatternsCondition());

                            mapping = new RequestMappingInfo(mapping.getName(),
                                    apiPattern,
                                    mapping.getMethodsCondition(), mapping.getParamsCondition(),
                                    mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                                    mapping.getProducesCondition(), mapping.getCustomCondition());
                        }

                        super.registerHandlerMethod(handler, method, mapping);
                    }

                    private boolean hasAnnotation(Method method) {
                        Class<?> beanType = method.getDeclaringClass();
                        return (AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null
                                || AnnotationUtils.findAnnotation(method, RequestMapping.class) != null)
                                && AnnotationUtils.findAnnotation(beanType, Skip.class) == null;
                    }

                    private boolean isNotSkipped(RequestMappingInfo mapping) {
                        Collection mutualPath = Sets.intersection(mapping.getPatternsCondition().getPatterns(), toSkip);
                        return mutualPath.isEmpty();
                    }
                };
            }
        };
    }
}
