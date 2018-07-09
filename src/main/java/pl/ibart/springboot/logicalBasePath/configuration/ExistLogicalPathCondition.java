package pl.ibart.springboot.logicalBasePath.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class ExistLogicalPathCondition implements Condition {


    public static final String LOGICAL_SERVER_CONTEXT_PATH = "logical.server.context-path";

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return StringUtils.hasText(conditionContext.getEnvironment().getProperty(LOGICAL_SERVER_CONTEXT_PATH));
    }
}
