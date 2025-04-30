package io.github.jayachandragoteti.taskmanagement.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TaskHealthAndHelpController {
    
    public TaskHealthAndHelpController(){

    }

    @GetMapping("/")
    public String TaskRoot() {
        return "Workng";
    }

    @RestController
public class MyController {

    @GetMapping("/health")
    public String checkHealth(@RequestParam(required = false, defaultValue = "ok") String param) {
        return "Health Check: " + param.toUpperCase();
    }

    @GetMapping("/help")
    public String getHelp(@RequestParam(required = false, defaultValue = "general") String param) {
        return "Help section for: " + param;
    }
}

    
    
    
}
