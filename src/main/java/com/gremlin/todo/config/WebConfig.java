package com.gremlin.todo.config;


import com.gremlin.todo.interceptor.GremlinInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GremlinInterceptor gremlinInterceptor;

    @Autowired
    public WebConfig(GremlinInterceptor gremlinInterceptor) {
        this.gremlinInterceptor = gremlinInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gremlinInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry
                .addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE")
        ;
    }
}