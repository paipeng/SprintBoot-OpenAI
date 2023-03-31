package com.paipeng.openai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ConfigurationProperties()
public class ApplicationConfig {
    @Value("${openai.apikey}")
    private String apiKey;

    @Bean
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/string");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }

    public String getApiKey() {
        return apiKey;
    }
}
