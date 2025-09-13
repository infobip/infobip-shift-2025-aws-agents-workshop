package com.infobip.agent.whatsapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("api")
public class ChatController {

//    private static final String DEFAULT_SYSTEM_PROMPT = """
//            You are a helpful AI assistant for Unicorn Rentals, a fictional company that rents unicorns.
//            Be friendly, helpful, and concise in your responses.
//            """;
    private static final String DEFAULT_SYSTEM_PROMPT = """
            I assistant helps to recognize image and get information from here
            """;

    private ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;
    private Logger logger = LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatClient.Builder chatClient, ToolCallbackProvider tools) {
//    public ChatController(ChatClient.Builder chatClient) {

        var chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();

        this.chatClientBuilder = chatClient
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools);

        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("chat")
    public String chat(@RequestBody PromptRequest promptRequest) {
        var chatResponse = chatClient.prompt().user(promptRequest.prompt()).call().chatResponse();
        return (chatResponse != null) ? chatResponse.getResult().getOutput().getText() : null;
    }

//    @PostMapping("image")
//    public String recognizeImage(@RequestBody PromptRequest promptRequest) throws FileNotFoundException {
//        InputStreamResource resource = new InputStreamResource(new FileInputStream("/Users/shakirin/Downloads/images/wedding-dress.jpg"));
//        Message message = UserMessage.builder()
//                .text("Please define category of this image in single word that add semicolon and put description.")
//                .media(Media.builder().data(resource).mimeType(MimeType.valueOf("image/jpeg")).build())
//                .build();
//
//        var chatResponse = chatClient.prompt().messages(message).call().chatResponse();
//
//        return (chatResponse != null) ? chatResponse.getResult().getOutput().getText() : null;
//    }

    @PostMapping("/chat/stream")
    public Flux<String> chatStream(@RequestBody PromptRequest promptRequest) {
        var conversationId = "user1"; //This should be retrieved from the Auth context
        return chatClient.prompt().user(promptRequest.prompt())
                .stream()
                .content()
                .onErrorResume(TimeoutException.class, ex -> {
                    logger.warn("Stream timed out: {}", ex.getMessage());
                    chatClient = chatClientBuilder.build();
                    return Flux.just("⚠️ LLM response timed out. Please try again.");
                });
    }

    record PromptRequest(String prompt) {
    }
}