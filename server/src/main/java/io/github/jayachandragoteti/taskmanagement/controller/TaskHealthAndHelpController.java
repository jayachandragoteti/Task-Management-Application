package io.github.jayachandragoteti.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TaskHealthAndHelpController {

    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public TaskHealthAndHelpController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    /**
     * Root route - List all endpoints
     */
    @GetMapping("/")
    public List<String> listAllEndpoints() {
        return handlerMapping.getHandlerMethods()
                .keySet()
                .stream()
                .map(info -> info.getMethodsCondition() + " " + info.getPatternsCondition())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String checkHealth(@RequestParam(required = false, defaultValue = "ok") String param) {
        return "Health Check: " + param.toUpperCase();
    }

    /**
     * Help endpoint
     */
    @GetMapping("/help")
    public String getHelp(@RequestParam(required = false, defaultValue = "general") String param) {
        return "Help section for: " + param;
    }
}
