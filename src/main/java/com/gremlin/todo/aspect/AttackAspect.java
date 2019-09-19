package com.gremlin.todo.aspect;

import com.gremlin.TrafficCoordinates;
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
    private List<TrafficCoordinates> trafficCoordinatesList = new ArrayList<>();

    @Autowired
    public AttackAspect(GremlinConfig gremlinConfig) {
        this.gremlinConfig = gremlinConfig;
    }

    @Before("@annotation(attack)")
    public void attack(Attack attack) throws Throwable {
        TrafficCoordinates trafficCoordinates = setupAttack(attack.type(), attack.fields());
        executeAttack(trafficCoordinates);
    }

    TrafficCoordinates setupAttack(String type, String[] fields) {
        Map<String, String> map = toMap(fields);
        TrafficCoordinates trafficCoordinates = new TrafficCoordinates.Builder()
                .withType(type)
                .build();
        map.forEach((key, value) -> {
            trafficCoordinates.putField(key, value);
        });
        if (!trafficCoordinatesList.contains(trafficCoordinates)) {
            trafficCoordinatesList.add(trafficCoordinates);
        }
        return trafficCoordinates;
    }

    void executeAttack(TrafficCoordinates trafficCoordinates) {
        gremlinConfig.gremlinService().applyImpact(trafficCoordinates);
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
