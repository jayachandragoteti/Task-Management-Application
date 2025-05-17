package io.github.jayachandragoteti.taskmanagement.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class ErrorMessagesConfig {

    @Bean
    public JsonNode errorMessages() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/error-messages.json")) {
            return mapper.readTree(is);
        }
    }
}
