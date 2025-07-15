package com.mohit.SpringAICode.controller;

import com.mohit.SpringAICode.Movie;
import org.springframework.ai.chat.client.ChatClient;


import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MovieConverter {

    private ChatClient chatClient;

    public MovieConverter(OpenAiChatModel chatModel)
    {
        this.chatClient=ChatClient.create(chatModel);
    }


    @GetMapping("movies")
    public List<String> getMovies(@RequestParam String name) {



        List<String> movies = chatClient.prompt()
                .user(u -> u.text("List top 10 movies of {name} based of imdb").param("name", name))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));


        return movies;


    }
    @GetMapping("movie")
    public Movie getaMovie(@RequestParam String name) {

        Movie movie = chatClient.prompt()
                .user(u -> u.text("Best movie of {name} based of imdb").param("name", name))
                .call()
                .entity(new BeanOutputConverter<Movie>(Movie.class));


        return movie;
    }
    @GetMapping("movie/list")
    public List<Movie> getMovieList(@RequestParam String name) {

        List<Movie> movies = chatClient.prompt()
                .user(u -> u.text("Top 3 movie of {name} based of imdb").param("name", name))
                .call()
                .entity(new BeanOutputConverter<>(new ParameterizedTypeReference<List<Movie>>() {}));


        return movies;
    }

}
