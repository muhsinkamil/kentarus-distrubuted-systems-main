package com.kentarus.distributed_systems.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import com.kentarus.distributed_systems.constants.InstancesUrl;
import com.kentarus.distributed_systems.constants.ResponseConstants;
import com.kentarus.distributed_systems.structures.GetWordsResponseStructure;
import com.kentarus.distributed_systems.structures.PostWordsRequestStructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WordService {

    @Autowired
    NodeService nodeService;

    private static HashMap<Integer, Integer> capacityOfInstances;
    static {
        capacityOfInstances = new HashMap<>();
        for (Integer key : InstancesUrl.instances.keySet()) {
            capacityOfInstances.put(key, 5);
        }
    }

    private TreeMap<Integer, ArrayList<Integer>> metadataOfInstances = new TreeMap<>();

    private WebClient webClient = WebClient.create();

    public GetWordsResponseStructure getWords() {
        if (nodeService.disabledNodes.size() > 0) {
            return new GetWordsResponseStructure(ResponseConstants.UNAVAILABLE, new ArrayList<String>());
        }

        ArrayList<String> result = new ArrayList<>();

        // If any request fails, send 500 error
        // ( TODO: consider making this boolean )
        String isAnyFailure = ResponseConstants.OK;

        for (Integer key : InstancesUrl.instances.keySet()) {
            GetWordsResponseStructure instanceResult = getWordsHelper(InstancesUrl.instances.get(key));

            result.addAll(instanceResult.getWords());
        }

        return new GetWordsResponseStructure(isAnyFailure, result);
    }

    public String deleteAllWords() {
        if (nodeService.disabledNodes.size() > 0) {
            return ResponseConstants.UNAVAILABLE;
        }

        String result = ResponseConstants.OK;

        deleteWordsLoop: for (Integer key : InstancesUrl.instances.keySet()) {
            String instanceUrl = InstancesUrl.instances.get(key);
            String instanceResult = webClient
                    .delete()
                    .uri(instanceUrl + "/words")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Inactive nodes will return NOK, but that is okay as the delete operation in
            // active nodes is successful. TODO: If the real DB is used, will have to change
            // the structure of response
            if (instanceResult.equals(ResponseConstants.UNAVAILABLE)) {
                result = ResponseConstants.UNAVAILABLE;

                // Consider rollback
                // for(int instance : successInstances){
                // String instanceUrl = InstancesUrl.instances.get(instance);
                // ArrayList<String> instanceWords = getWordsHelper(instanceUrl).getWords();
                // postWordsHelper(instanceWords, instanceUrl);
                // }

                break deleteWordsLoop;
            }
        }

        if (result.equals(ResponseConstants.OK)) {
            // Restore the spaces
            for (Integer key : InstancesUrl.instances.keySet()) {
                capacityOfInstances.put(key, 5);
            }
        }

        return result;
    }

    public String deleteSingleWord(String delWord) {
        String result = ResponseConstants.NOK;

        int hashedWord = delWord.hashCode();
        ArrayList<Integer> instanceIdArray = metadataOfInstances.get(hashedWord);
        int instanceIdOfOldestSave = instanceIdArray.get(0);
        String instanceUrlToHit = InstancesUrl.instances.get(instanceIdOfOldestSave);

        // for (Integer key : InstancesUrl.instances.keySet()) {
        result = webClient
                .delete()
                .uri(instanceUrlToHit + "/words/" + delWord)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (result.equals(ResponseConstants.OK)) {
            instanceIdArray.remove(0);
            metadataOfInstances.put(hashedWord, instanceIdArray);
            capacityOfInstances.put(instanceIdOfOldestSave, capacityOfInstances.get(instanceIdOfOldestSave) + 1);
        }
        // // If the result is OK, then the word is deleted in node, we can break and
        // avoid
        // // further calls
        // if (result.equals(ResponseConstants.OK)) {
        // break;
        // }
        // }
        return result;
    }

    public String postMultipleWords(ArrayList<String> words) {
        if (nodeService.disabledNodes.size() > 0) {
            return ResponseConstants.UNAVAILABLE;
        }

        String result = ResponseConstants.OK;

        // Prepare empty list of words to post to instances
        HashMap<Integer, ArrayList<String>> wordsForInstances = new HashMap<>();
        for (Integer key : InstancesUrl.instances.keySet()) {
            wordsForInstances.put(key, new ArrayList<>());
        }

        for (String word : words) {
            boolean spaceLeftAtInstances = false;

            instancesLoop: for (int key : InstancesUrl.instances.keySet()) {
                if (capacityOfInstances.get(key) > 0) {
                    int hashedWord = word.hashCode();
                    if (metadataOfInstances.containsKey(hashedWord)) {
                        ArrayList<Integer> nodesList = metadataOfInstances.get(hashedWord);
                        nodesList.add(key);
                        metadataOfInstances.put(hashedWord, nodesList);
                    } else {
                        metadataOfInstances.put(hashedWord, new ArrayList<Integer>(Arrays.asList(key)));
                    }

                    spaceLeftAtInstances = true;

                    ArrayList<String> wordsListAtCurrInstance = wordsForInstances.get(key);
                    wordsListAtCurrInstance.add(word);
                    wordsForInstances.put(key, wordsListAtCurrInstance);

                    capacityOfInstances.put(key, capacityOfInstances.get(key) - 1);
                    // Since the word already goes in one instance, break out of instances loop
                    break instancesLoop;
                }
            }
            if (!spaceLeftAtInstances)
                break;
        }

        int startIndex = 0;

        for (Integer key : InstancesUrl.instances.keySet()) {
            startIndex = postWordsHelper(wordsForInstances.get(key), InstancesUrl.instances.get(key));

            if (startIndex < 0) {
                result = ResponseConstants.UNAVAILABLE;
                break;
            }

            // Implementation 1:
            // All the words are posted
            // if (startIndex >= words.size()) {
            // break;
            // }

            // Implementation 2: Since all the words should be posted, we don't need the
            // above check
        }

        // Implementation 1: If any of the posted words is saved, ( atleast one node
        // accepted connection
        // and had space to save )
        // if (startIndex > 0) {
        // result = ResponseConstants.OK;
        // }

        // Implementation 2: All words should accept connection => If any fails, it will
        // send UNAVAILABLE

        return result;
    }

    private int postWordsHelper(ArrayList<String> words, String instanceUrl) {
        int result = webClient
                .post()
                .uri(instanceUrl + "/words")
                .body(BodyInserters
                        .fromValue(new PostWordsRequestStructure(words, 0)))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return result;
    }

    private GetWordsResponseStructure getWordsHelper(String instanceUrl) {
        return webClient.get()
                .uri(instanceUrl + "/words")
                .retrieve()
                .bodyToMono(GetWordsResponseStructure.class)
                .block();
    }
}