package com.mohit.SpringAICode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OpenAIController {

    private ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

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

    @PostMapping("api/embedding")
    public float[] getembedding(@RequestParam String text)
    {
        return embeddingModel.embed(text);
    }

    @PostMapping("api/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2)
    {
        float [] embedding1= embeddingModel.embed(text1);
        float [] embedding2= embeddingModel.embed(text2);
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += Math.pow(embedding1[i], 2);
            norm2 += Math.pow(embedding2[i], 2);
        }

        return dotProduct*100 / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @PostMapping("api/product")
    public List<Document> getProducts(@RequestParam String text)
    {
        //return vectorStore.similaritySearch(text);
        return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());
    }


}
