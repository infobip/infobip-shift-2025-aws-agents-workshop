# Infobip WhatsApp MCP

In this part of the workshop, we will expand our agent's capabilities to interact with the [Infobip WhatsApp MCP server](https://github.com/infobip/mcp).
The agent will be capable of exploring [WhatsApp message templates](https://www.infobip.com/docs/whatsapp/message-types-and-templates/message-templates) and sending WhatsApp template messages.

Why message templates?
Sending initial business-initiated WhatsApp messages requires using a pre-approved template.

## Prerequisites

If you are a workshop participant, **all the prerequisites are already set up for you** and will be provided during the workshop.
You will have an API key with the required scopes and a WhatsApp sender with pre-approved templates, including the one that will be used during the workshop.

Here is the detailed list of prerequisites if you want to set up your own environment:
1. An Infobip account. You can [sign up](https://www.infobip.com/signup) for a free trial account.
2. An Infobip [API key](https://www.infobip.com/docs/essentials/api-essentials/api-authentication#api-key-header) with the `whatsapp:manage` scope.
3. A WhatsApp sender with pre-approved message templates. You can follow the [Infobip WhatsApp Getting Started Guide](https://www.infobip.com/docs/whatsapp) to set up a WhatsApp sender and get your message templates approved.

## Connecting to the Infobip WhatsApp MCP Server

To connect to the Infobip WhatsApp remote MCP server, configure your MCP client to use the following URL:
- `https://mcp.infobip.com/whatsapp` for Streamable HTTP transport
- `https://mcp.infobip.com/whatsapp/sse` for SSE transport

For this workshop we will use the API key authentication method.
You will need to configure your MCP client to use the API key for authentication.
The API key should be included in the `Authorization` header.
For more details on how to configure your MCP client, refer to the [Infobip MCP documentation](https://github.com/infobip/mcp).

For this workshop, we will use the [Java MCP client](https://modelcontextprotocol.io/sdk/java/mcp-client) through [Spring AI MCP support](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html).
The code samples can be found in Appendix A: Spring AI code samples section below.

## Scenario

After connecting your agent to the Infobip WhatsApp MCP server, you will be able to interact with it using natural language.

1. First, we can prompt the configured model to find an appropriate WhatsApp template for following up with attendees after the Infobip Shift workshop.

    Example prompt:

    > Hi! Please explore my approved WhatsApp templates for sender number <PUT_YOUR_SENDER_HERE> and recommend the most suitable template for following up with my audience after an Infobip Shift conference workshop.

    The MCP tool for fetching WhatsApp templates should be invoked, and the model should interpret the response and suggest the most appropriate template. A pre-approved template named `workshop_followup` should be suggested.
    The model should be able to explain the template details and immediately provide example values for placeholders. If not, we can prompt it again for more details.

2. As a final step, we can prompt the model to send the WhatsApp message.

    Example prompt:

    > Send a WhatsApp template message with the following specifications: sender number <PUT_YOUR_SENDER_HERE>, recipient number <PUT_THE_NUMBER_HERE>, template name 'workshop_followup', placeholder {{1}} = '<PUT_THE_NAME_HERE>', placeholder {{2}} = 'https://shift.infobip.com/schedule'.

    If our chat agent has memory, it should also be able to send the correct message with a much less structured prompt. For example:

    > Please send the workshop follow-up template message to the attendee named <PUT_THE_NAME_HERE> at phone number <PUT_THE_NUMBER_HERE>, using the agenda link https://shift.infobip.com/schedule.


## Appendix A: Spring AI code samples

The code snippets will be shared prior to the workshop.
