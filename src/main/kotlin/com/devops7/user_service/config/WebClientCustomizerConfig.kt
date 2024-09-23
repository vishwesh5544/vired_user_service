package com.devops7.user_service.config

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class WebClientCustomizerConfig : WebClientCustomizer {
    /**
     * Callback to customize a
     * [ WebClient.Builder][org.springframework.web.reactive.function.client.WebClient.Builder] instance.
     * @param webClientBuilder the client builder to customize
     */
    override fun customize(webClientBuilder: WebClient.Builder) {
        webClientBuilder
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .filter { request, next ->
                // Add logging for each request
                println("Requesting URL: ${request.url()}")
                next.exchange(request)
            }
    }
}