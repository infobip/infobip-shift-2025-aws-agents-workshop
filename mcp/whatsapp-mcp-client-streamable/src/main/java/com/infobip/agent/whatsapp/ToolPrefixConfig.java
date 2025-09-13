package com.infobip.agent.whatsapp;

import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.McpConnectionInfo;
import org.springframework.ai.mcp.McpToolNamePrefixGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolPrefixConfig {

    @Bean
    public McpToolNamePrefixGenerator mcpToolNamePrefixGenerator() {
        return new CustomPrefixGenerator();
    }

    static class CustomPrefixGenerator implements McpToolNamePrefixGenerator {
        @Override
        public String prefixedToolName(McpConnectionInfo mcpConnInfo, McpSchema.Tool tool) {
            return tool.name();
        }
    }
}
