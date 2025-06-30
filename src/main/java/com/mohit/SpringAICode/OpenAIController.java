package com.mohit.SpringAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.Map;

@RestController
public class OpenAIController {

    private ChatClient chatClient;

//    public OpenAIController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }


    public OpenAIController(ChatClient.Builder builder) {
       ChatMemory chatMemory= MessageWindowChatMemory.builder().build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }

    @GetMapping("/api/{message}")
    public org.springframework.http.ResponseEntity<String> getAnswer(@PathVariable String message){

        ChatResponse chatResponse=chatClient
                .prompt(message)
                .call()
                .chatResponse();
        System.out.println(chatResponse.getMetadata().getModel());
        String response =chatResponse
                .getResult()
                .getOutput()
                .getText();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/recommend")
    public String suggest(@RequestParam String type, @RequestParam String year, @RequestParam String lang)
    {
        String template= """
            Suggest a movie with{type} which was released around {year}
            and is in language{lang}. Also provide the imdb rating
            Suggest specific movie
            
            The output format should be 
            Movie Name:
            Movie Type:
            Language:
            IMDB Rating:
            """;
        PromptTemplate promptTemplate=new PromptTemplate(template);
        Prompt prom=promptTemplate.create(Map.of("type",type,"year", year,"lang", lang));

        String response=chatClient
                .prompt(prom)
                .call()
                .content();
        return response;
    }


}
