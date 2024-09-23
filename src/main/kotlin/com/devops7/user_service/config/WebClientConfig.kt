package com.devops7.user_service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun roleServiceWebClient(
        builder: WebClient.Builder,
        @Value("\${role-service.url}") roleServiceUrl: String  // Load from env
    ): WebClient {
        return builder
            .baseUrl(roleServiceUrl)
            .build()
    }
}