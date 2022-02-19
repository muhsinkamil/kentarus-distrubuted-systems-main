package com.kentarus.distributed_systems.controllers;

import java.util.ArrayList;

import com.kentarus.distributed_systems.services.WordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static com.kentarus.distributed_systems.constants.InstancesUrl.*;

@RestController
@RequestMapping(value = "/words")
public class WordController {

    @Autowired
    WordService wordService;

    private WebClient webClient = WebClient.create();

    String OKStatus = "OK";
    String NOKStatus = "NOK";

    @GetMapping()
    public ArrayList<String> getWords() {

        ArrayList<String> result = new ArrayList<>();

        for (String key : instances.keySet()) {
            @SuppressWarnings("unchecked")
            ArrayList<String> instanceResult = webClient.get()
                    .uri(instances.get(key) + "/words")
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            result.addAll(instanceResult);
        }

        return result;
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteWords() {
        String result = "OK";
        for (String key : instances.keySet()) {
            webClient
                    .delete()
                    .uri(instances.get(key) + "/words")
                    .retrieve()
                    .bodyToMono(String.class);
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "{wordValue}")
    public ResponseEntity<String> deleteWord(@PathVariable String wordValue) {
        String result = "OK";
        for (String key : instances.keySet()) {
            result = webClient
                    .delete()
                    .uri(instances.get(key) + "/words/" + wordValue)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        return ResponseEntity.ok(result);
    }
}
