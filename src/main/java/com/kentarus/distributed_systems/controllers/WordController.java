package com.kentarus.distributed_systems.controllers;

import com.kentarus.distributed_systems.services.WordService;
import com.kentarus.distributed_systems.structures.DeleteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/words")
public class WordController {

    @Autowired
    WordService wordService;

    @Autowired
    RestTemplate restTemplate;

    String OKStatus = "OK";
    String NOKStatus = "NOK";

    @GetMapping()
    public String getWords() {
        // String[] words = new String[5];

        String result = restTemplate.getForObject("http://localhost:9000/words", String.class);
        return result;
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteWords() {
        ResponseEntity<String> result = restTemplate.exchange("http://localhost:9000/words", HttpMethod.DELETE,
                new HttpEntity<String>("L"), String.class);
        return result;
        // return ResponseEntity.ok().body(new DeleteResponse(OKStatus));
    }

    @DeleteMapping(value = "{wordValue}")
    public ResponseEntity<String> deleteWord(@PathVariable String word) {
        // return ResponseEntity.ok().body(new DeleteResponse(word));
        // HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> result = restTemplate.exchange("http://localhost:9000/words", HttpMethod.DELETE,
                new HttpEntity<String>("L"), String.class);
        return result;
    }
}
