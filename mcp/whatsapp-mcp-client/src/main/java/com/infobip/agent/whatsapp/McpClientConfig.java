package com.infobip.agent.whatsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.mcp.client.autoconfigure.NamedClientMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

@Configuration
public class McpClientConfig {

    @Bean
    List<NamedClientMcpTransport> customMcpTransports(@Value("${infobip.api.key}") String apiKey) {
        var transport = HttpClientSseClientTransport.builder("https://mcp.infobip.com")
                .sseEndpoint("/whatsapp/sse")
//        var transport = HttpClientSseClientTransport.builder("http://localhost:8082")
//                .sseEndpoint("/sse")
                .customizeRequest(requestBuilder ->
                    requestBuilder.header("Authorization", "App %s".formatted(apiKey))
                )
                .build();

        return List.of(new NamedClientMcpTransport("server1", transport));
    }
}
