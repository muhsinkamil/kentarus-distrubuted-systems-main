package com.kentarus.distributed_systems.controllers;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.ResponseConstants;
import com.kentarus.distributed_systems.services.WordService;
import com.kentarus.distributed_systems.structures.GetWordsResponseStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/words")
public class WordController {

    @Autowired
    WordService wordService;

    @GetMapping()
    public ResponseEntity<ArrayList<String>> getWords() {
        GetWordsResponseStructure result = wordService.getWords();

        if (result.getStatus().equals(ResponseConstants.NOK)) {
            return ResponseEntity.status(404).body(result.getWords());
        }
        return ResponseEntity.ok().body(result.getWords());
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteWords() {
        // TODO: Add error handling
        return ResponseEntity.ok(wordService.deleteAllWords());
    }

    @DeleteMapping(value = "{wordValue}")
    public ResponseEntity<String> deleteWord(@PathVariable String wordValue) {
        String result = wordService.deleteSingleWord(wordValue);
        if (result.equals(ResponseConstants.OK)) {
            return ResponseEntity.ok().body(ResponseConstants.OK);
        }

        return ResponseEntity.status(404).body(ResponseConstants.NOK);
    }

    @PostMapping()
    public ResponseEntity<String> postWords(@RequestBody ArrayList<String> words) {
        // TODO: Add error handling
        if (words.size() < 1) {
            return ResponseEntity.ok(ResponseConstants.OK);
        }
        return ResponseEntity.ok(wordService.postMultipleWords(words));
    }
}
