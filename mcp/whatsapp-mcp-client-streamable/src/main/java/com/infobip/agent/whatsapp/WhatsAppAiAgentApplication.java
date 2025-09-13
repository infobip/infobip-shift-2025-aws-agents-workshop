package com.infobip.agent.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WhatsAppAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsAppAiAgentApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
//        return args -> {
//            var chatClient = chatClientBuilder
//                    .defaultSystem("You are a Unicorn Rentals Agent, expert in all sorts of things related to Unicorns and renting them.")
//                    .build();
//
//            System.out.println("\nI am your Unicorn Rentals assistant.\n");
//            try (Scanner scanner = new Scanner(System.in)) {
//                while (true) {
//                    System.out.print("\nUSER: ");
//                    System.out.println("\nASSISTANT: " +
//                            chatClient.prompt(scanner.nextLine()) // Get the user input
//                                    .call()
//                                    .content());
//                }
//            }
//        };
//    }

}
