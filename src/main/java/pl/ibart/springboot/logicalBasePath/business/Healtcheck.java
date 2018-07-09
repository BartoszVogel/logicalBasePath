package pl.ibart.springboot.logicalBasePath.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ibart.springboot.logicalBasePath.configuration.ExistLogicalPathCondition;
import pl.ibart.springboot.logicalBasePath.configuration.Skip;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Skip
@Conditional(ExistLogicalPathCondition.class)
public class Healtcheck {

    private static final Logger logger = LoggerFactory.getLogger(Healtcheck.class);

    private final Map<String, HealthIndicator> indicators;
    private final HealthAggregator healthAggregator;

    public Healtcheck(Map<String, HealthIndicator> indicators, HealthAggregator healthAggregator) {
        this.indicators = indicators;
        this.healthAggregator = healthAggregator;
    }

    @GetMapping("/")
    public Health health() {
        Map<String, Health> healths = new LinkedHashMap<>();
        indicators.keySet().forEach(stringHealthIndicatorEntry -> {
            Health health = indicators.get(stringHealthIndicatorEntry).health();
            logger.info("{} -> {}", stringHealthIndicatorEntry, health);
            healths.put(stringHealthIndicatorEntry, health);
        });

        return this.healthAggregator.aggregate(healths);
    }
}
