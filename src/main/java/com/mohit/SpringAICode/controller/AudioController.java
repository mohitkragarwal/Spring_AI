package com.mohit.SpringAICode.controller;

import org.springframework.ai.audio.transcription.AudioTranscription;
import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechMessage;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioController {

    private OpenAiAudioTranscriptionModel audioModel;

    private OpenAiAudioSpeechModel audioSpeechModel;

    public AudioController(OpenAiAudioTranscriptionModel audioModel, OpenAiAudioSpeechModel audioSpeechModel){
        this.audioModel=audioModel;
        this.audioSpeechModel=audioSpeechModel;
    }

    @PostMapping("api/stt")
    public String speech_to_text(@RequestParam MultipartFile file)
    {

        OpenAiAudioTranscriptionOptions options= OpenAiAudioTranscriptionOptions
                .builder()
                .language("es")
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT)
                .build();

        AudioTranscriptionPrompt prompt=new AudioTranscriptionPrompt(file.getResource(), options);


        return audioModel.call(prompt).getResult().getOutput();
    }

    @PostMapping("api/tts")
    public byte[] text_to_speech(@RequestParam String text)
    {
        SpeechMessage message=new SpeechMessage(text);
        OpenAiAudioSpeechOptions options=OpenAiAudioSpeechOptions
                .builder()
                .speed(1.5F)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ASH)
                .build();
        SpeechPrompt prompt= new SpeechPrompt(message,options);

        return audioSpeechModel.call(prompt).getResult().getOutput();
    }
}
