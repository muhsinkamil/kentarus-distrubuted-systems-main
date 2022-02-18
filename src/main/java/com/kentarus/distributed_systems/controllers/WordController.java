package com.kentarus.distributed_systems.controllers;

import com.kentarus.distributed_systems.services.WordService;
import com.kentarus.distributed_systems.structures.DeleteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/words")
public class WordController {

    @Autowired
    WordService wordService;
    String OKStatus = "OK";
    String NOKStatus = "NOK";

    @GetMapping()
    public String[] getWords() {
        String[] words = new String[5];
        return words;
    }

    @DeleteMapping()
    public ResponseEntity<DeleteResponse> deleteWords() {
        return ResponseEntity.ok().body(new DeleteResponse(OKStatus));
    }

    @DeleteMapping(value = "{word}")
    public ResponseEntity<DeleteResponse> deleteWord(@PathVariable String word) {
        return ResponseEntity.ok().body(new DeleteResponse(word));
    }
}
