package com.infobip.agent.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WhatsAppAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsAppAgentApplication.class, args);
    }

//    @Bean
//    public WebClient.Builder webClientBuilder() {
//        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
//                .keepAlive(true) // TCP keep-alive
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
//
//        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient));
//    }

//    @Bean
//    @Primary
//    public McpSyncClient customMcpClient(ObjectMapper objectMapper, @Value("${infobip.api.key}") String apiKey) {
////        String url = "https://mcp.mxschell.people.aws.dev/mcp";
//        String url = "http://localhost:8082";
//
//        var transport = HttpClientSseClientTransport.builder(url)
////                .sseEndpoint(url)  // only the path
//                // you can still customizeRequest if you want more tweaks
////                .customizeRequest(builder ->
////                                builder.header("Authorization", "Bearer XXX")
//                .customizeRequest(builder ->
//                        builder.header(HttpHeaders.AUTHORIZATION, "App %s".formatted(apiKey))
//                )
//                .build();
//
//        McpSyncClient client = McpClient.sync(transport)
//                .build();
//
//        client.initialize();
//
//        return client;
//    }
}
