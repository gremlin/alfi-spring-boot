package com.gremlin.todo.interceptor;

import com.gremlin.GremlinService;
import com.gremlin.TrafficCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GremlinInterceptor implements HandlerInterceptor {

    private final GremlinService gremlinService;

    @Autowired
    public GremlinInterceptor(final GremlinService gremlinService) {
        this.gremlinService = gremlinService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> headers = getHeadersInfo(request);
        if (headers.containsKey("X-GREMLIN")) {
            gremlinService.applyImpact(new TrafficCoordinates.Builder().withType("GremlinInterceptor").build());
//        if (headers.containsKey("gremlin") && headers.get("gremlin").equalsIgnoreCase("attack")) {
//            gremlinService.applyImpact(injectionPoint);
//        }
        }

        return true;
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
}
