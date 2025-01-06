package com.visa.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigs implements WebMvcConfigurer {

    @Value("${frontend.base.url}")
    private String CORS;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow all origins during preflight (OPTIONS) requests
        registry.addMapping("/**")
                .allowedOrigins(CORS)  // Set the allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
