package com.gremlin.todo.aspect;

import com.gremlin.GremlinService;
import com.gremlin.TrafficCoordinates;
import com.gremlin.todo.GremlinTrafficCoordinatesBuilder;
import com.gremlin.todo.config.FeatureFlag;
import com.gremlin.todo.config.GremlinConfig;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class AttackAspect {
    private final GremlinConfig gremlinConfig;
    private final GremlinTrafficCoordinatesBuilder trafficCoordinatesBuilder;
    private final FeatureFlag featureFlag;
    private final GremlinService gremlinService;
    private Map<TrafficCoordinates, GremlinService> gremlinServiceMap = new HashMap<>();

    @Autowired
    public AttackAspect(GremlinConfig gremlinConfig, GremlinTrafficCoordinatesBuilder trafficCoordinatesBuilder, FeatureFlag featureFlag, GremlinService gremlinService) {
        this.gremlinConfig = gremlinConfig;
        this.trafficCoordinatesBuilder = trafficCoordinatesBuilder;
        this.featureFlag = featureFlag;
        this.gremlinService = gremlinService;
    }

    @Before("@annotation(attack)")
    public void attack(Attack attack) {
        executeAttack(createTrafficCoordinates(attack.type(), attack.fields()));
    }

    TrafficCoordinates createTrafficCoordinates(String type, String[] fields) {
        Map<String, String> map = toMap(fields);
        TrafficCoordinates trafficCoordinates = trafficCoordinatesBuilder
                .withType(type)
                .build();
        map.forEach(trafficCoordinates::putFieldIfAbsent);

        return trafficCoordinates;
    }

    /**
     * The GremlinService should be a singleton and can map to only one experiment (TrafficCoordinates).
     * This method will check if the trafficCoordinates passed in is already tied to an existing GremlinService, if not, create a new GremlinService
     * then apply the attack. If so, then apply the attack using the trafficCoordinates, corresponding GremlinService.
     * @param trafficCoordinates
     */
    void executeAttack(TrafficCoordinates trafficCoordinates) {
        if (featureFlag.isAlfiEnabled()) {

            gremlinService.applyImpact(trafficCoordinates);

        }
    }

    private Map<String, String> toMap(String[] in) {
        Map<String, String> map = new HashMap<>();
        Arrays.stream(in).forEach(
                s -> {
                    map.put(s.split("=")[0], s.split("=")[1]);
                }
        );
        return map;
    }
}
