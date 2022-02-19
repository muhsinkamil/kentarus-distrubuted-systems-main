package com.kentarus.distributed_systems.controllers;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.ResponseConstants;
import com.kentarus.distributed_systems.services.WordService;
import com.kentarus.distributed_systems.structures.PostWordsRequestStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static com.kentarus.distributed_systems.constants.InstancesUrl.*;

@RestController
@RequestMapping(value = "/words")
public class WordController {

    @Autowired
    WordService wordService;

    private WebClient webClient = WebClient.create();

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
        String result = ResponseConstants.OK;
        for (String key : instances.keySet()) {
            result = webClient
                    .delete()
                    .uri(instances.get(key) + "/words")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "{wordValue}")
    public ResponseEntity<String> deleteWord(@PathVariable String wordValue) {
        String result = ResponseConstants.OK;
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

    @PostMapping()
    public ResponseEntity<String> postWords(@RequestBody ArrayList<String> words) {
        String result = ResponseConstants.OK;
        int startIndex = 0;
        if (words.size() < 1) {
            return ResponseEntity.ok(result);
        }
        for (String key : instances.keySet()) {
            startIndex = webClient
                    .post()
                    .uri(instances.get(key) + "/words")
                    .body(BodyInserters.fromValue(new PostWordsRequestStructure(words, startIndex)))
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();

            System.out.println(startIndex);
            if (startIndex >= words.size()) {
                break;
            }
        }
        return ResponseEntity.ok(result);
    }
}
