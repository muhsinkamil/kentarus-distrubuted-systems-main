package com.kentarus.distributed_systems.services;

import java.util.ArrayList;

import com.kentarus.distributed_systems.constants.InstancesUrl;
import com.kentarus.distributed_systems.constants.ResponseConstants;
import com.kentarus.distributed_systems.structures.PostWordsRequestStructure;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WordService {

    private WebClient webClient = WebClient.create();

    public ArrayList<String> getWords() {
        ArrayList<String> result = new ArrayList<>();

        for (Integer key : InstancesUrl.instances.keySet()) {
            @SuppressWarnings("unchecked")
            ArrayList<String> instanceResult = webClient.get()
                    .uri(InstancesUrl.instances.get(key) + "/words")
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            result.addAll(instanceResult);
        }

        return result;
    }

    public String deleteAllWords() {
        String result = ResponseConstants.OK;

        for (Integer key : InstancesUrl.instances.keySet()) {
            result = webClient
                    .delete()
                    .uri(InstancesUrl.instances.get(key) + "/words")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        return result;
    }

    public String deleteSingleWord(String delWord) {
        String result = ResponseConstants.NOK;

        for (Integer key : InstancesUrl.instances.keySet()) {
            result = webClient
                    .delete()
                    .uri(InstancesUrl.instances.get(key) + "/words/" + delWord)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // If the result is OK, then the word is deleted in node, we can break and avoid
            // further calls
            if (result.equals(ResponseConstants.OK)) {
                break;
            }
        }
        return result;
    }

    public String postMultipleWords(ArrayList<String> words) {
        String result = ResponseConstants.OK;
        int startIndex = 0;

        for (Integer key : InstancesUrl.instances.keySet()) {
            startIndex = webClient
                    .post()
                    .uri(InstancesUrl.instances.get(key) + "/words")
                    .body(BodyInserters.fromValue(new PostWordsRequestStructure(words, startIndex)))
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();

            if (startIndex >= words.size()) {
                break;
            }
        }

        return result;
    }
}
