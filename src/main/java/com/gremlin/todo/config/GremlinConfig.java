package com.gremlin.todo.config;

import com.gremlin.*;
import com.gremlin.todo.ToDoApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class GremlinConfig {

    @Bean
    public GremlinCoordinatesProvider gremlinCoordinatesProvider() {
        return new GremlinCoordinatesProvider() {
            @Override
            public ApplicationCoordinates initializeApplicationCoordinates() {
                return new ApplicationCoordinates.Builder()
                        .withType(String.format("%s%s", ToDoApplication.class.getSimpleName(), ApplicationCoordinates.class.getSimpleName()))
                        .withField("service", "to-do")
                        .build();
            }
        };
    }

    @Bean
    public GremlinServiceFactory gremlinServiceFactory() {
        return new GremlinServiceFactory(gremlinCoordinatesProvider());
    }

    @Bean
    public GremlinService gremlinService() {
        return gremlinServiceFactory().getGremlinService();
    }


    /**
     * Used just to initilaize ALFI with the control plane
     * @return
     */
    @Bean
    public TrafficCoordinates trafficCoordinates() {
        return new TrafficCoordinates.Builder()
                .withType(String.format("%s%s", getClass().getSimpleName(), TrafficCoordinates.class.getSimpleName()))
                .build();
    }

    @PostConstruct
    public void init() {
        // register gremlin with the control plane when the application loads
        gremlinService().applyImpact(trafficCoordinates());
    }
}
