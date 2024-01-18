package com.example.best_travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value(value = "${api.base.url}")
    public String baseUrl;
    @Value(value = "${api.api_key}")
    public String apiKey;
    @Value(value = "${api.api_header}")
    public String apiKeyHeader;

    @Bean(name = "currency")
    public WebClient webClient(){
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }

    @Bean(name = "base")
    @Primary
    public WebClient baseWebClient(){
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }


}
